/**
 * 
 */
package jabara.web_tools.rest;

import jabara.web_tools.TestHelper;
import jabara.web_tools.service.ExMediaType;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.PostMethod;
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
     * @throws InterruptedException
     * @throws DateParseException
     */
    @SuppressWarnings("static-method")
    @Test
    public void _getHead() throws HttpException, IOException, InterruptedException, DateParseException {
        final HttpClient client = new HttpClient();
        int responseCode;

        final HeadMethod headMethod = new HeadMethod("http://localhost:8081/rest/blackout/schedule.csv"); //$NON-NLS-1$
        responseCode = client.executeMethod(headMethod);

        System.out.println(responseCode);
        System.out.println(DateUtil.parseDate(headMethod.getResponseHeader("Last-Modified").getValue())); //$NON-NLS-1$

        TimeUnit.SECONDS.sleep(2);

        final PostMethod postMethod = new PostMethod("http://localhost:8081/rest/blackout/schedule.csv"); //$NON-NLS-1$
        postMethod.setRequestHeader("Content-Type", ExMediaType.TEXT_PLAIN); //$NON-NLS-1$
        postMethod.setRequestBody(new NameValuePair[] { new NameValuePair("scheduleText", TestHelper.loadTestText()) }); //$NON-NLS-1$
        responseCode = client.executeMethod(postMethod);
        System.out.println(responseCode);
        System.out.println(DateUtil.parseDate(postMethod.getResponseHeader("Last-Modified").getValue())); //$NON-NLS-1$
    }

    @SuppressWarnings("unused")
    private static void printResponseHeaders(final HttpMethod pMethod) {
        for (final Header header : pMethod.getResponseHeaders()) {
            System.out.print(header);
        }
    }

}
