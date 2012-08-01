/**
 * 
 */
package jabara.web_tools.rest;

import java.io.IOException;
import java.util.GregorianCalendar;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.httpclient.util.DateUtil;
import org.junit.Test;

/**
 * @author jabaraster
 */
public class BlackoutResourceTest {

    /**
     * Test method for {@link jabara.web_tools.rest.BlackoutResource#getHead()}.
     * 
     * @throws IOException
     * @throws HttpException
     * @throws DateParseException
     */
    @SuppressWarnings("static-method")
    @Test
    public void _getHead() throws HttpException, IOException, DateParseException {
        final HttpClient client = new HttpClient();
        int responseCode;

        final GetMethod method = new GetMethod("http://localhost:8081/rest/blackout/schedule.csv"); //$NON-NLS-1$
        method.addRequestHeader("If-Modified-Since", DateUtil.formatDate(new GregorianCalendar(1975, 9, 29).getTime())); //$NON-NLS-1$
        //        method.addRequestHeader("If-Modified-Since", DateUtil.formatDate(new Date())); //$NON-NLS-1$
        responseCode = client.executeMethod(method);

        System.out.println(responseCode);
        System.out.println(DateUtil.parseDate(method.getResponseHeader("Last-Modified").getValue())); //$NON-NLS-1$
        System.out.println(method.getResponseHeader("Last-Modified").getValue()); //$NON-NLS-1$
        System.out.println(method.getResponseBodyAsString());
    }

    @SuppressWarnings("unused")
    private static void printResponseHeaders(final HttpMethod pMethod) {
        for (final Header header : pMethod.getResponseHeaders()) {
            System.out.print(header);
        }
    }

}
