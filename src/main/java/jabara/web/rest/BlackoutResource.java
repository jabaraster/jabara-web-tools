/**
 * 
 */
package jabara.web.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.io.IOUtils;

/**
 * @author jabaraster
 */
@Path("/blackout")
public class BlackoutResource {

    private static final String ENCODING       = "UTF-8";    //$NON-NLS-1$
    private static final String QDEN_ENCODING  = "Shift_JIS"; //$NON-NLS-1$
    private static final String LINE_SEPARATOR = "\r\n";     //$NON-NLS-1$

    /**
     * @return 計画停電スケジュール.
     * @throws IOException
     * @throws ParseException
     */
    @SuppressWarnings("static-method")
    @Path("/schedule.csv")
    @Produces({ "text/plain; charset=" + ENCODING })
    @GET
    public byte[] getExpandedSchedule() throws IOException, ParseException {
        final List<String> lines = getExpandedScheduleCore();
        final StringBuilder sb = new StringBuilder();
        for (final String line : lines) {
            sb.append(line).append(LINE_SEPARATOR);
        }
        return new String(sb).getBytes(ENCODING);
    }

    static List<String> getExpandedScheduleCore() throws IOException, ParseException {
        final List<String> ret = new ArrayList<String>();
        append(ret, "201207.csv"); //$NON-NLS-1$
        append(ret, "201208.csv"); //$NON-NLS-1$
        append(ret, "201209.csv"); //$NON-NLS-1$
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
