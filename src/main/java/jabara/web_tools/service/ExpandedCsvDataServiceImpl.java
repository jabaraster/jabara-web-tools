/**
 * 
 */
package jabara.web_tools.service;

import jabara.web_tools.entity.BlackoutSchedule;
import jabara.web_tools.entity.ExpandedCsvData;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaQuery;

/**
 * @author jabaraster
 */
public class ExpandedCsvDataServiceImpl extends DaoBase implements IExpandedCsvDataService {
    private static final long   serialVersionUID = 4907375411981453116L;

    private static final String LINE_SEPARATOR   = "\r\n";              //$NON-NLS-1$

    /**
     * @see jabara.web_tools.service.IExpandedCsvDataService#get()
     */
    @Override
    public ExpandedCsvData get() throws NotFound {
        try {
            return getFromDb();
        } catch (final NoResultException e) {
            throw new NotFound();
        }
    }

    /**
     * @see jabara.web_tools.service.IExpandedCsvDataService#refresh(java.util.List)
     */
    @Override
    public ExpandedCsvData refresh(final List<BlackoutSchedule> pSchedules) {
        final EntityManager em = getEntityManager();
        em.createQuery("delete from " + BlackoutSchedule.class.getSimpleName()).executeUpdate(); //$NON-NLS-1$

        final StringBuilder sb = new StringBuilder();
        for (final BlackoutSchedule s : pSchedules) {
            em.persist(s);
            sb.append("\"").append(new SimpleDateFormat("yyyy/MM/dd").format(s.getDate())).append("\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            sb.append(",\"").append(new SimpleDateFormat("HH:mm").format(s.getStartTime())).append("\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            sb.append(",\"").append(new SimpleDateFormat("HH:mm").format(s.getEndTime())).append("\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            sb.append(",\"").append(s.getGroup()).append("\""); //$NON-NLS-1$ //$NON-NLS-2$
            sb.append(",\"").append(s.getPriority()).append("\""); //$NON-NLS-1$ //$NON-NLS-2$
            sb.append(LINE_SEPARATOR);
        }

        try {
            final ExpandedCsvData data = getFromDb();
            data.setData(encode(sb));
            update(data);
            return data;

        } catch (final NoResultException e) {
            final ExpandedCsvData data = new ExpandedCsvData();
            data.setData(encode(sb));
            em.persist(data);
            return data;
        }
    }

    /**
     * @see jabara.web_tools.service.IExpandedCsvDataService#update(jabara.web_tools.entity.ExpandedCsvData)
     */
    @Override
    public void update(final ExpandedCsvData pEntity) {
        final ExpandedCsvData merged = getEntityManager().merge(pEntity);
        merged.setData(pEntity.getData());
    }

    private ExpandedCsvData getFromDb() throws NoResultException {
        final EntityManager em = getEntityManager();
        final CriteriaQuery<ExpandedCsvData> query = em.getCriteriaBuilder().createQuery(ExpandedCsvData.class);
        query.from(ExpandedCsvData.class);
        final ExpandedCsvData ret = em.createQuery(query).getSingleResult();
        return ret;
    }

    static void parseLine(final List<String> pLines, final String line) throws ParseException {
        final String groupToken = getGroupToken(line);
        final List<String> expandedGroups = expandGroup(groupToken);
        final String preGroup = getPreGroupString(line); // グループより前の文字列を取得.
        for (final String group : expandedGroups) {
            pLines.add(preGroup + ",\"" + group + "\""); //$NON-NLS-1$//$NON-NLS-2$
        }
    }

    private static byte[] encode(final StringBuilder pBuilder) {
        try {
            return new String(pBuilder).getBytes(ExMediaType.TEXT_ENCODING);
        } catch (final UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    private static List<String> expandGroup(final String pGroupToken) throws ParseException {
        final Pattern pat = Pattern.compile("([A-Z])([0-9]{1,2})"); //$NON-NLS-1$
        final Matcher mat = pat.matcher(pGroupToken);

        if (!mat.find()) {
            throw new ParseException(pGroupToken, -1);
        }
        final String fromGroupAlpha = mat.group(1);
        final String fromStr = mat.group(2);

        if (!mat.find()) {
            throw new ParseException(pGroupToken, -1);
        }
        final String toGroupAlpha = mat.group(1);
        final String toStr = mat.group(2);

        if (!fromGroupAlpha.equals(toGroupAlpha)) {
            throw new UnsupportedOperationException("サブグループ範囲のアルファベット部が異なるケースは想定していませんでした. ロジックを見直す必要があります. [" + pGroupToken + "]"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        final int from = Integer.parseInt(fromStr);
        final int to = Integer.parseInt(toStr);

        final List<String> ret = new ArrayList<String>();
        for (int i = from; i <= to; i++) {
            ret.add(toGroupAlpha + String.valueOf(i));
        }
        return ret;
    }

    private static String getGroupToken(final String pLine) {
        return pLine.substring(pLine.lastIndexOf("\",\"") + 3, pLine.length() - 1); //$NON-NLS-1$
    }

    private static String getPreGroupString(final String pLine) {
        return pLine.substring(0, pLine.lastIndexOf("\",\"") + 1); //$NON-NLS-1$
    }

}
