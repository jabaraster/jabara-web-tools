/**
 * 
 */
package jabara.web_tools.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author jabara
 * 
 */
@Entity
public class BlackoutSchedule extends EntityBase<BlackoutSchedule> {
    private static final long serialVersionUID = 7272322382707171493L;

    /**
     * 
     */
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    protected Date            date;

    /**
     * 
     */
    @Column(nullable = false)
    @Temporal(TemporalType.TIME)
    protected Date            startTime;

    @Column(nullable = false)
    @Temporal(TemporalType.TIME)
    protected Date            endTime;

    /**
     * 
     */
    @Column(nullable = false, length = 20)
    protected String          group;

    /**
     * 
     */
    @Column(nullable = false)
    protected byte            priority;

    /**
     * @return the date
     */
    public Date getDate() {
        return this.date == null ? null : new Date(this.date.getTime());
    }

    /**
     * @return the endTime
     */
    public Date getEndTime() {
        return this.endTime == null ? null : new Date(this.endTime.getTime());
    }

    /**
     * @return the group
     */
    public String getGroup() {
        return this.group;
    }

    /**
     * @return the priority
     */
    public byte getPriority() {
        return this.priority;
    }

    /**
     * @return the startTime
     */
    public Date getStartTime() {
        return this.startTime == null ? null : new Date(this.startTime.getTime());
    }

    /**
     * @param pDate the date to set
     */
    public void setDate(final Date pDate) {
        this.date = pDate == null ? null : new Date(pDate.getTime());
    }

    /**
     * @param pEndTime the endTime to set
     */
    public void setEndTime(final Date pEndTime) {
        this.endTime = pEndTime == null ? null : new Date(pEndTime.getTime());
    }

    /**
     * @param pGroup the group to set
     */
    public void setGroup(final String pGroup) {
        this.group = pGroup;
    }

    /**
     * @param pPriority the priority to set
     */
    public void setPriority(final byte pPriority) {
        this.priority = pPriority;
    }

    /**
     * @param pStartTime the startTime to set
     */
    public void setStartTime(final Date pStartTime) {
        this.startTime = pStartTime == null ? null : new Date(pStartTime.getTime());
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(BlackoutSchedule.class.getSimpleName()).append("[");
        sb.append("date=" + (this.date == null ? null : new SimpleDateFormat("yyyy/MM/dd").format(this.date)));
        sb.append(",startTime=" + (this.startTime == null ? null : new SimpleDateFormat("HH:mm").format(this.startTime)));
        sb.append(",endTime=" + (this.endTime == null ? null : new SimpleDateFormat("HH:mm").format(this.endTime)));
        sb.append(",group=" + this.group);
        sb.append(",priority=" + this.priority);
        sb.append("]");
        return new String(sb);
    }
}
