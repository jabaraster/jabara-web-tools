/**
 * 
 */
package jabara.web_tools.service;

import jabara.web_tools.entity.BlackoutSchedule;
import jabara.web_tools.entity.ExpandedCsvData;

import java.util.List;

import com.google.inject.ImplementedBy;

/**
 * @author jabaraster
 */
@ImplementedBy(ExpandedCsvDataServiceImpl.class)
public interface IExpandedCsvDataService {

    /**
     * @return CSVデータ.
     * @throws NotFound
     */
    ExpandedCsvData get() throws NotFound;

    /**
     * @param pSchedules
     * @return
     * @throws NotFound
     */
    ExpandedCsvData refresh(List<BlackoutSchedule> pSchedules) throws NotFound;

    /**
     * @param pEntity
     */
    void update(ExpandedCsvData pEntity);
}
