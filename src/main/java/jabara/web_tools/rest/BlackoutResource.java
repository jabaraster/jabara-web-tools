/**
 * 
 */
package jabara.web_tools.rest;

import jabara.web_tools.entity.ExpandedCsvData;
import jabara.web_tools.service.IExpandedCsvDataService;
import jabara.web_tools.service.Injector;
import jabara.web_tools.service.NotFound;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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

    private static final String           CSV_PATH = "/schedule.csv"; //$NON-NLS-1$

    private final IExpandedCsvDataService expandedCsvDataService;

    /**
     * 
     */
    public BlackoutResource() {
        this.expandedCsvDataService = Injector.getInstance(IExpandedCsvDataService.class);
    }

    /**
     * @param pData
     * @param pFileDetail
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
    @Path("/schedule.csv")
    @Consumes({ "text/csv", "text/plain", "text/comma-separated-values" })
    @Produces({ "text/plain; charset=" + IExpandedCsvDataService.TEXT_ENCODING, "text/csv; charset=" + IExpandedCsvDataService.TEXT_ENCODING })
    @GET
    public Response getExpandedSchedule(@QueryParam("nocache") final Boolean pNoCache) {
        try {
            final ExpandedCsvData data = this.expandedCsvDataService.get();
            if (data.isLoaded()) {
                if (isTrue(pNoCache)) {
                    return Response.ok(data.getData()).build();
                }
                return Response.notModified().build();
            }
            data.setLoaded(true);
            this.expandedCsvDataService.update(data);

            return Response.ok(data.getData()).build();

        } catch (final NotFound e) {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    /**
     * @param pPath
     * @return
     * @throws IOException
     */
    @SuppressWarnings("static-method")
    @Path("scrape")
    @Produces({ MediaType.TEXT_HTML })
    @GET
    public InputStream loadFromQDenHtml(@QueryParam("path") final String pPath) throws IOException {
        try {
            TimeUnit.MILLISECONDS.sleep(500); // 九電サイトへの負荷対策.
        } catch (final InterruptedException e) {
            // 　処理なし
        }
        return new URL("http://www2.kyuden.co.jp/kt_search/index.php" + pPath).openStream(); //$NON-NLS-1$
    }

    /**
     * @return
     */
    @Path(CSV_PATH)
    @Produces({ "text/plain; charset=" + IExpandedCsvDataService.TEXT_ENCODING })
    @POST
    public Response refresh() {
        try {
            final ExpandedCsvData newData = this.expandedCsvDataService.refresh();
            return Response.ok(newData.getData()).build();

        } catch (final NotFound e) {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    private static boolean isTrue(final Boolean pBoolean) {
        return pBoolean != null && pBoolean.booleanValue();
    }
}
