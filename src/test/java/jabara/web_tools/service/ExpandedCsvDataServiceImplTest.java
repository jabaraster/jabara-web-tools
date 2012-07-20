package jabara.web_tools.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jabara.web_tools.entity.ExpandedCsvData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

/**
 * @author jabaraster
 */
@SuppressWarnings("static-method")
public class ExpandedCsvDataServiceImplTest {

    /**
     * @throws NotFound
     */
    @SuppressWarnings("boxing")
    @Test
    public void _get_refresh() throws NotFound {
        final IExpandedCsvDataService service = Injector.getInstance(IExpandedCsvDataService.class);

        final ExpandedCsvData firstData = service.get();
        assertThat(true, is(firstData.isFromWeb()));

        final ExpandedCsvData secondData = service.get();
        assertThat(false, is(secondData.isFromWeb()));

        final ExpandedCsvData refreshData = service.refresh();
        assertThat(true, is(refreshData.isFromWeb()));

        service.refresh();
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("boxing")
    @Test
    public void _getExpandedSchedule() throws Exception {
        final List<String> list = ExpandedCsvDataServiceImpl.getExpandedScheduleCore();
        final Set<String> set = new HashSet<String>(list);
        assertThat(true, is(list.size() == set.size()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void _parseLine() throws Exception {
        final List<String> sb = new ArrayList<String>();
        ExpandedCsvDataServiceImpl.parseLine(sb, "\"2012/9/3\",\"8:30\",\"11:00\",\"B21～B30\""); //$NON-NLS-1$
    }

}
