/**
 * 
 */
package jabara.web_tools.entity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
     * 秒より下を切り落とした、最終更新日時.
     */
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date            lastModified;

    /**
     * @return the data
     */
    public byte[] getData() {
        return this.data;
    }

    /**
     * @return the lastModified
     */
    public Date getLastModified() {
        return this.lastModified == null ? null : new Date(this.lastModified.getTime());
    }

    /**
     * @param pData the data to set
     */
    public void setData(final byte[] pData) {
        this.data = pData;
    }

    @PrePersist
    @PreUpdate
    void refreshLastModified() {
        this.lastModified = omitMillisec(Calendar.getInstance().getTime());
    }

    private static Date omitMillisec(final Date pDate) {
        try {
            final DateFormat fmt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); //$NON-NLS-1$
            return fmt.parse(fmt.format(pDate));
        } catch (final ParseException e) {
            throw new IllegalStateException(e);
        }
    }

}
