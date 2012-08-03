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
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.httpclient.util.DateUtil;

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
     * @param pHeaders
     * @return 計画停電スケジュール.
     */
    @Path(CSV_PATH)
    @Consumes({ "text/csv", "text/plain", "text/comma-separated-values" })
    @Produces({ ExMediaType.TEXT_PLAIN, ExMediaType.TEXT_CSV })
    @GET
    public Response getExpandedSchedule(@Context final HttpHeaders pHeaders) {
        try {
            final ExpandedCsvData data = this.expandedCsvDataService.get();
            final List<String> ifModifiedSinceStr = pHeaders.getRequestHeader("If-Modified-Since"); //$NON-NLS-1$
            if (ifModifiedSinceStr != null && !ifModifiedSinceStr.isEmpty()) {
                try {
                    final Date ifModifiedSince = DateUtil.parseDate(ifModifiedSinceStr.get(0));
                    if (data.getUpdated().after(ifModifiedSince)) {
                        return Response.ok(data.getData()) //
                                .lastModified(data.getUpdated()) //
                                .build();
                    }
                    return Response.notModified() //
                            .lastModified(data.getUpdated()) //
                            .build();

                } catch (final DateParseException e) {
                    // 無視して次の処理へ.
                }
            }
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
     * @return -
     */
    @Path("lastModified")
    @GET
    @Produces({ MediaType.TEXT_PLAIN })
    public String getLastModified() {
        try {
            final ExpandedCsvData data = this.expandedCsvDataService.get();
            return new SimpleDateFormat("yyyy/MM/dd HH:mm/SS").format(data.getUpdated()); //$NON-NLS-1$
        } catch (final NotFound e) {
            return "no data found."; //$NON-NLS-1$
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
