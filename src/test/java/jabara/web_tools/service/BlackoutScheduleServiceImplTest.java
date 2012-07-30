/**
 * 
 */
package jabara.web_tools.service;

import jabara.web_tools.TestHelper;
import jabara.web_tools.entity.BlackoutSchedule;

import java.io.IOException;
import java.util.List;

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
    @SuppressWarnings("static-method")
    @Test
    public void _parse() throws IOException {
        final String testText = TestHelper.loadTestText();
        final List<BlackoutSchedule> list = BlackoutScheduleServiceImpl.parseCore(testText);
        for (final BlackoutSchedule s : list) {
            System.out.println(s);
        }
    }
}
