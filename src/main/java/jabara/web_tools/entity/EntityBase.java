/**
 * 
 */
package jabara.web_tools.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @param <E>
 * @author jabaraster
 */
@MappedSuperclass
public abstract class EntityBase<E extends EntityBase<E>> implements IEntity, Cloneable, Serializable {
    private static final long serialVersionUID = -4541213085442034458L;

    /**
     * 
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long            id;

    /**
     * 
     */
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date            created;

    /**
     * 
     */
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date            updated;

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EntityBase<?> other = (EntityBase<?>) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    /**
     * @see jabara.web_tools.entity.IEntity#getCreated()
     */
    @Override
    public Date getCreated() {
        return this.created == null ? null : new Date(this.created.getTime());
    }

    /**
     * @see jabara.web_tools.entity.IEntity#getId()
     */
    @Override
    public Long getId() {
        return this.id;
    }

    /**
     * @see jabara.web_tools.entity.IEntity#getUpdated()
     */
    @Override
    public Date getUpdated() {
        return this.updated == null ? null : new Date(this.updated.getTime());
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.id == null ? 0 : this.id.hashCode());
        return result;
    }

    /**
     * 
     */
    @PrePersist
    void prePersist() {
        this.created = Calendar.getInstance().getTime();
        this.updated = new Date(this.created.getTime());
    }

    @PreUpdate
    void preUpdate() {
        this.updated = Calendar.getInstance().getTime();
    }

}
