/**
 * 
 */
package jabara.web_tools.rest;

import jabara.web_tools.entity.BlackoutSchedule;
import jabara.web_tools.entity.ExpandedCsvData;
import jabara.web_tools.service.ExMediaType;
import jabara.web_tools.service.IBlackoutScheduleService;
import jabara.web_tools.service.IExpandedCsvDataService;
import jabara.web_tools.service.Injector;
import jabara.web_tools.service.NotFound;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

/**
 * @author jabaraster
 */
@Path("/blackout")
public class BlackoutResource {

    private static final String            CSV_PATH = "/schedule.csv";                       //$NON-NLS-1$

    private static final Random            _random  = new Random(System.currentTimeMillis());

    private final IExpandedCsvDataService  expandedCsvDataService;
    private final IBlackoutScheduleService blackoutScheduleService;

    /**
     * 
     */
    public BlackoutResource() {
        this.expandedCsvDataService = Injector.getInstance(IExpandedCsvDataService.class);
        this.blackoutScheduleService = Injector.getInstance(IBlackoutScheduleService.class);
    }

    /**
     * @param pData
     * @param pFileInfo
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Path("/upload")
    @Consumes({ MediaType.MULTIPART_FORM_DATA })
    @Produces({ MediaType.TEXT_HTML })
    @POST
    public void d( //
            @FormDataParam("image") final InputStream pData //
            , @FormDataParam("image") final FormDataContentDisposition pFileInfo //
    ) {
        System.out.println("♪♪♪");
        System.out.println("♪♪♪ " + pData);
        System.out.println("♪♪♪ " + pFileInfo);
        System.out.println("♪♪♪");
    }

    /**
     * @return 計画停電スケジュール.
     */
    @Path(CSV_PATH)
    @Consumes({ "text/csv", "text/plain", "text/comma-separated-values" })
    @Produces({ ExMediaType.TEXT_PLAIN, ExMediaType.TEXT_CSV })
    @GET
    public Response getExpandedSchedule() {
        try {
            final ExpandedCsvData data = this.expandedCsvDataService.get();
            return Response.ok(data.getData()) //
                    .lastModified(data.getUpdated()) //
                    .build();
        } catch (final NotFound e) {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    /**
     * @return -
     */
    @Path(CSV_PATH)
    @HEAD
    public Response getHead() {
        try {
            final ExpandedCsvData data = this.expandedCsvDataService.get();
            return Response.ok().lastModified(data.getUpdated()).build();

        } catch (final NotFound e) {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    /**
     * @param pPath
     * @return -
     * @throws IOException
     */
    @SuppressWarnings("static-method")
    @Path("scrape")
    @Produces({ MediaType.TEXT_HTML })
    @GET
    public InputStream loadFromQDenHtml(@QueryParam("path") final String pPath) throws IOException {
        try {
            // 2s-5s待つ
            final long timeout = (long) (_random.nextDouble() * 3000) + 2000;
            TimeUnit.MILLISECONDS.sleep(timeout); // 九電サイトへの負荷対策.
        } catch (final InterruptedException e) {
            // 　処理なし
        }
        return new URL("http://www2.kyuden.co.jp/kt_search/index.php" + pPath).openStream(); //$NON-NLS-1$
    }

    /**
     * @param pScheduleText
     * @return -
     */
    @Path(CSV_PATH)
    @Produces({ ExMediaType.TEXT_CSV })
    @POST
    public Response refresh(@FormParam("scheduleText") final String pScheduleText) {
        try {
            final List<BlackoutSchedule> list = this.blackoutScheduleService.parse(pScheduleText);
            final ExpandedCsvData newData = this.expandedCsvDataService.refresh(list);
            return Response.ok(newData.getData()) //
                    .lastModified(newData.getUpdated()) //
                    .build();

        } catch (final NotFound e) {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
}
