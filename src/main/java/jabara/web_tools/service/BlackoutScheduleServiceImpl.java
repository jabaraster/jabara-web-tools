/**
 * 
 */
package jabara.web_tools.service;

import jabara.web_tools.entity.BlackoutSchedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jabara
 */
public class BlackoutScheduleServiceImpl extends DaoBase implements IBlackoutScheduleService {
    private static final long serialVersionUID = -7313115082451833581L;

    /**
     * @see jabara.web_tools.service.IBlackoutScheduleService#parse(java.lang.String)
     */
    @Override
    public List<BlackoutSchedule> parse(final String pScheduleText) {
        try {
            return parseCore(pScheduleText);

        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

    static List<BlackoutSchedule> parseCore(final String pScheduleText) throws IOException {
        final BufferedReader reader = new BufferedReader(new StringReader(pScheduleText));
        final List<BlackoutSchedule> ret = new ArrayList<BlackoutSchedule>();
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            parseOneSection(reader, ret, line);
        }
        return ret;
    }

    private static void parseOneSection(final BufferedReader pReader, final List<BlackoutSchedule> pList, final String pHeaderLine)
            throws IOException {
        final Pattern pat = Pattern.compile("([0-9]{4})年([0-9]{1,2})月([0-9]{1,2})日（.曜日）([0-9]{1,2})時([0-9]{1,2})分～([0-9]{1,2})時([0-9]{1,2})分");
        final Matcher mat = pat.matcher(pHeaderLine);
        if (!mat.find()) {
            throw new IllegalStateException();
        }
        final int year = Integer.parseInt(mat.group(1));
        final int month = Integer.parseInt(mat.group(2));
        final int date = Integer.parseInt(mat.group(3));
        final int startTimeHour = Integer.parseInt(mat.group(4));
        final int startTimeMinutes = Integer.parseInt(mat.group(5));
        final int endTimeHour = Integer.parseInt(mat.group(6));
        final int endTimeMinutes = Integer.parseInt(mat.group(7));

        for (String line = pReader.readLine(); line != null; line = pReader.readLine()) {
            if (pat.matcher(line).find()) {
                parseOneSection(pReader, pList, line);
                continue;
            }
            final BlackoutSchedule schedule = new BlackoutSchedule();
            schedule.setDate(new GregorianCalendar(year, month - 1, date).getTime());
            schedule.setStartTime(new GregorianCalendar(year, month - 1, date, startTimeHour, startTimeMinutes, 0).getTime());
            schedule.setEndTime(new GregorianCalendar(year, month - 1, date, endTimeHour, endTimeMinutes, 0).getTime());
            setGroupAndPriority(schedule, line);
            pList.add(schedule);
        }
    }

    private static void setGroupAndPriority(final BlackoutSchedule pSchedule, final String pLine) {
        final Pattern pat = Pattern.compile("([0-9]{1,2})番目 ([A-Z][0-9]{1,2})サブグループ");
        final Matcher mat = pat.matcher(pLine);
        if (!mat.find()) {
            throw new IllegalStateException();
        }
        final byte priority = Byte.parseByte(mat.group(1));
        final String group = mat.group(2);
        pSchedule.setPriority(priority);
        pSchedule.setGroup(group);
    }
}
