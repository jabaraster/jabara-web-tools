/**
 * 
 */
package jabara.web_tools.service;

import jabara.web_tools.entity.ExpandedCsvData;

import com.google.inject.ImplementedBy;

/**
 * @author jabaraster
 */
@ImplementedBy(ExpandedCsvDataServiceImpl.class)
public interface IExpandedCsvDataService {

    /**
     * 
     */
    final String TEXT_ENCODING = "UTF-8"; //$NON-NLS-1$

    /**
     * @return CSVデータ.
     * @throws NotFound
     * @throws NotModified
     */
    ExpandedCsvData get() throws NotFound, NotModified;
}
