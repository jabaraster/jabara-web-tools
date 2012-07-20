/**
 * 
 */
package jabara.web_tools.service;

import java.io.Serializable;

import javax.persistence.EntityManager;

/**
 * @author じゃばら
 */
public abstract class DaoBase implements Serializable {
    private static final long serialVersionUID = -654742979492413270L;

    /**
     * @return {@link EntityManager}.
     */
    @SuppressWarnings("static-method")
    public EntityManager getEntityManager() {
        return Injector.getInstance(EntityManager.class);
    }

    /**
     * @param <E>
     * @param pEntity
     */
    protected <E> void deleteCore(final E pEntity) {
        final EntityManager em = getEntityManager();
        em.remove(em.merge(pEntity));
    }

    /**
     * @param <E>
     * @param pEntity
     */
    protected <E> void insertCore(final E pEntity) {
        getEntityManager().persist(pEntity);
    }
}
