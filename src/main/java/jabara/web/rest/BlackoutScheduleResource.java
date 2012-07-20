/**
 * 
 */
package jabara.web.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;

/**
 * @author jabaraster
 */
@Path("/blackout/schedule")
public class BlackoutScheduleResource {

    /**
     * @return 計画停電スケジュール.
     * @throws MalformedURLException
     * @throws IOException
     */
    @SuppressWarnings("static-method")
    @Produces({ MediaType.TEXT_PLAIN })
    @GET
    public String getExpandedSchedule() throws MalformedURLException, IOException {
        final InputStream in = new URL("http://www2.kyuden.co.jp/kt_search/csv/201207.csv").openStream(); //$NON-NLS-1$
        final ByteArrayOutputStream mem = new ByteArrayOutputStream();
        IOUtils.copy(in, mem);
        return new String(mem.toByteArray(), "Shift_JIS"); //$NON-NLS-1$
    }
}
