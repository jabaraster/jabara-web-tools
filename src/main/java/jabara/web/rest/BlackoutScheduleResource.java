/**
 * 
 */
package jabara.web.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.io.IOUtils;

/**
 * @author jabaraster
 */
@Path("/blackout/schedule")
public class BlackoutScheduleResource {

    private static final String ENCODING      = "UTF-8";    //$NON-NLS-1$
    private static final String QDEN_ENCODING = "Shift_JIS"; //$NON-NLS-1$

    /**
     * @return 計画停電スケジュール.
     * @throws IOException
     */
    @SuppressWarnings("static-method")
    @Produces({ "text/plain; charset=" + ENCODING })
    @GET
    public byte[] getExpandedSchedule() throws IOException {
        final StringBuilder ret = new StringBuilder();
        append(ret, "201207.csv"); //$NON-NLS-1$
        append(ret, "201208.csv"); //$NON-NLS-1$
        append(ret, "201209.csv"); //$NON-NLS-1$
        return new String(ret).getBytes(ENCODING);
    }

    private static void append(final StringBuilder pRet, final String pCsvFileName) throws IOException {
        final InputStream in = new URL("http://www2.kyuden.co.jp/kt_search/csv/" + pCsvFileName).openStream(); //$NON-NLS-1$
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(in, QDEN_ENCODING));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

            }
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
}
