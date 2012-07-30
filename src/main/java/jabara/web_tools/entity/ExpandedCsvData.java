/**
 * 
 */
package jabara.web_tools.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * @author jabaraster
 */
@Entity
public class ExpandedCsvData extends EntityBase<ExpandedCsvData> {
    private static final long serialVersionUID = -8346294548965744876L;

    /**
     * 
     */
    @Lob
    @Column(nullable = false)
    protected byte[]          data;

    /**
     * @return the data
     */
    public byte[] getData() {
        return this.data;
    }

    /**
     * @param pData the data to set
     */
    public void setData(final byte[] pData) {
        this.data = pData;
    }

}
