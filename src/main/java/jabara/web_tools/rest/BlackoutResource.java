/**
 * 
 */
package jabara.web_tools.rest;

import jabara.web_tools.service.IExpandedCsvDataService;
import jabara.web_tools.service.Injector;
import jabara.web_tools.service.NotFound;
import jabara.web_tools.service.NotModified;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * @author jabaraster
 */
@Path("/blackout")
public class BlackoutResource {

    /**
     * @return 計画停電スケジュール.
     */
    @SuppressWarnings("static-method")
    @Path("/schedule.csv")
    @Produces({ "text/plain; charset=" + IExpandedCsvDataService.TEXT_ENCODING })
    @GET
    public Response getExpandedSchedule() {
        try {
            return Response.ok(Injector.getInstance(IExpandedCsvDataService.class).get().getData()).build();

        } catch (final NotFound e) {
            return Response.status(Status.NOT_FOUND).build();

        } catch (final NotModified e) {
            return Response.notModified().build();
        }
    }
}
