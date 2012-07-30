/**
 * 
 */
package jabara.web_tools;

import jabara.web_tools.service.BlackoutScheduleServiceImplTest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

/**
 * @author jabaraster
 */
public final class TestHelper {

    private TestHelper() {
        //
    }

    /**
     * @return テスト用スケジュールデータ.
     */
    public static String loadTestText() {
        try {
            final InputStream in = BlackoutScheduleServiceImplTest.class.getResourceAsStream("/blackout_schedule.txt"); //$NON-NLS-1$
            final ByteArrayOutputStream mem = new ByteArrayOutputStream();
            IOUtils.copy(in, mem);
            return new String(mem.toByteArray(), Charset.forName("UTF-8")); //$NON-NLS-1$
    
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
