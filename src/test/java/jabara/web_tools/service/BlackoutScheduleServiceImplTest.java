/**
 * 
 */
package jabara.web_tools.service;

import jabara.web_tools.entity.BlackoutSchedule;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * @author jabara
 * 
 */
public class BlackoutScheduleServiceImplTest {

    /**
     * Test method for {@link jabara.web_tools.service.BlackoutScheduleServiceImpl#parse(java.lang.String)}.
     * 
     * @throws IOException
     */
    @Test
    public void _parse() throws IOException {
        final String testText = loadTestText();
        final List<BlackoutSchedule> list = BlackoutScheduleServiceImpl.parseCore(testText);
        for (final BlackoutSchedule s : list) {
            System.out.println(s);
        }
    }

    private static String loadTestText() {
        try {
            final InputStream in = BlackoutScheduleServiceImplTest.class.getResourceAsStream("/blackout_schedule.txt");
            final ByteArrayOutputStream mem = new ByteArrayOutputStream();
            IOUtils.copy(in, mem);
            return new String(mem.toByteArray(), Charset.forName("UTF-8"));
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
