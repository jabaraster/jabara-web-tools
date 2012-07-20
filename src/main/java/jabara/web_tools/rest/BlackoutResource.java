/**
 * 
 */
package jabara.web_tools.rest;

import jabara.web_tools.entity.ExpandedCsvData;
import jabara.web_tools.service.IExpandedCsvDataService;
import jabara.web_tools.service.Injector;
import jabara.web_tools.service.NotFound;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * @author jabaraster
 */
@Path("/blackout")
public class BlackoutResource {

    private final IExpandedCsvDataService expandedCsvDataService;

    private static final String           CSV_PATH = "/schedule.csv"; //$NON-NLS-1$

    public BlackoutResource() {
        this.expandedCsvDataService = Injector.getInstance(IExpandedCsvDataService.class);
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
