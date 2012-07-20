/**
 * 
 */
package jabara.web_tools.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;

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
     * 
     */
    @Column(nullable = false)
    protected boolean         loaded           = false;

    /**
     * 
     */
    @Transient
    protected boolean         fromWeb;

    /**
     * @return the data
     */
    public byte[] getData() {
        return this.data;
    }

    /**
     * @return the fromWeb
     */
    public boolean isFromWeb() {
        return this.fromWeb;
    }

    /**
     * @return the loaded
     */
    public boolean isLoaded() {
        return this.loaded;
    }

    /**
     * @param pData the data to set
     */
    public void setData(final byte[] pData) {
        this.data = pData;
    }

    /**
     * @param pFromWeb the fromWeb to set
     */
    public void setFromWeb(final boolean pFromWeb) {
        this.fromWeb = pFromWeb;
    }

    /**
     * @param pLoaded the loaded to set
     */
    public void setLoaded(final boolean pLoaded) {
        this.loaded = pLoaded;
    }

}
