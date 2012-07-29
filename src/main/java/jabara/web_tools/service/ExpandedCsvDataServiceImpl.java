/**
 * 
 */
package jabara.web_tools.service;

import jabara.web_tools.entity.BlackoutSchedule;
import jabara.web_tools.entity.ExpandedCsvData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaQuery;

import org.apache.commons.io.IOUtils;

/**
 * @author jabaraster
 */
public class ExpandedCsvDataServiceImpl extends DaoBase implements IExpandedCsvDataService {
    private static final long   serialVersionUID = 4907375411981453116L;

    private static final String QDEN_ENCODING    = "Shift_JIS";         //$NON-NLS-1$

    private static final String LINE_SEPARATOR   = "\r\n";              //$NON-NLS-1$

    /**
     * @see jabara.web_tools.service.IExpandedCsvDataService#get()
     */
    @Override
    public ExpandedCsvData get() throws NotFound {
        try {
            return getFromDb();

        } catch (final NoResultException e) {
            return getFromWebAndInsert();
        }
    }

    /**
     * @see jabara.web_tools.service.IExpandedCsvDataService#refresh(java.util.List)
     */
    @Override
    public ExpandedCsvData refresh(final List<BlackoutSchedule> pSchedules) {
        final EntityManager em = this.getEntityManager();
        em.createQuery("delete from " + ExpandedCsvData.class.getSimpleName()).executeUpdate();

        final StringBuilder sb = new StringBuilder();
        for (final BlackoutSchedule s : pSchedules) {
            em.persist(s);
            sb.append("\"").append(new SimpleDateFormat("yyyy/MM/dd").format(s.getDate())).append("\"");
            sb.append(",\"").append(new SimpleDateFormat("HH:mm").format(s.getStartTime())).append("\"");
            sb.append(",\"").append(new SimpleDateFormat("HH:mm").format(s.getEndTime())).append("\"");
            sb.append(",\"").append(s.getGroup()).append("\"");
            sb.append(",\"").append(s.getPriority()).append("\"");
            sb.append(LINE_SEPARATOR);
        }

        try {
            final ExpandedCsvData data = this.getFromDb();
            data.setData(encode(sb));
            this.update(data);
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
        merged.setLoaded(pEntity.isLoaded());
    }

    private ExpandedCsvData getFromDb() throws NoResultException {
        final EntityManager em = getEntityManager();
        final CriteriaQuery<ExpandedCsvData> query = em.getCriteriaBuilder().createQuery(ExpandedCsvData.class);
        query.from(ExpandedCsvData.class);
        final ExpandedCsvData ret = em.createQuery(query).getSingleResult();
        ret.setFromWeb(false);
        return ret;
    }

    private ExpandedCsvData getFromWebAndInsert() throws NotFound {
        try {
            return getFromWebAndInsertCore();

        } catch (final IOException e) {
            throw new NotFound(e);
        } catch (final ParseException e) {
            throw new NotFound(e);
        }
    }

    private ExpandedCsvData getFromWebAndInsertCore() throws IOException, ParseException {
        try {
            final ExpandedCsvData e = getFromWeb();
            getEntityManager().persist(e);
            return e;

        } catch (final UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    static List<String> getExpandedScheduleCore() throws IOException, ParseException {
        final List<String> ret = new ArrayList<String>();
        append(ret, "201207.csv"); //$NON-NLS-1$
        sleepMilliseconds(100);
        append(ret, "201208.csv"); //$NON-NLS-1$
        sleepMilliseconds(100);
        append(ret, "201209.csv"); //$NON-NLS-1$
        sleepMilliseconds(100);
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

    private static void append(final List<String> pLines, final String pCsvFileName) throws IOException, ParseException {
        final InputStream in = new URL("http://www2.kyuden.co.jp/kt_search/csv/" + pCsvFileName).openStream(); //$NON-NLS-1$
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(in, QDEN_ENCODING));
            reader.readLine(); // ヘッダ分読み飛ばし.
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                parseLine(pLines, line);
            }
        } finally {
            IOUtils.closeQuietly(in);
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

    private static ExpandedCsvData getFromWeb() throws IOException, ParseException {
        final List<String> lines = getExpandedScheduleCore();
        final StringBuilder sb = new StringBuilder();
        for (final String line : lines) {
            sb.append(line).append(LINE_SEPARATOR);
        }

        final byte[] data = new String(sb).getBytes(ExMediaType.TEXT_ENCODING);
        final ExpandedCsvData e = new ExpandedCsvData();
        e.setData(data);
        e.setFromWeb(true);
        return e;
    }

    private static String getGroupToken(final String pLine) {
        return pLine.substring(pLine.lastIndexOf("\",\"") + 3, pLine.length() - 1); //$NON-NLS-1$
    }

    private static String getPreGroupString(final String pLine) {
        return pLine.substring(0, pLine.lastIndexOf("\",\"") + 1); //$NON-NLS-1$
    }

    private static void sleepMilliseconds(final int pTime) {
        try {
            Thread.sleep(pTime);
        } catch (final InterruptedException e) {
            //
        }
    }

}
