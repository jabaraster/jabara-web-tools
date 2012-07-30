package jabara.web_tools.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * @author jabaraster
 */
@SuppressWarnings("static-method")
public class ExpandedCsvDataServiceImplTest {

    /**
     * @throws Exception
     */
    @Test
    public void _parseLine() throws Exception {
        final List<String> sb = new ArrayList<String>();
        ExpandedCsvDataServiceImpl.parseLine(sb, "\"2012/9/3\",\"8:30\",\"11:00\",\"B21～B30\""); //$NON-NLS-1$
    }

}
