/**
 * 
 */
package jabara.web_tools.service;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.postgresql.Driver;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;

/**
 * @author jabaraster
 */
public final class Injector {

    private static final EntityManagerFactory       _emf      = Persistence.createEntityManagerFactory("pu", createDbProperties()); //$NON-NLS-1$

    private static final ThreadLocal<EntityManager> _emHolder = new ThreadLocal<EntityManager>();

    private static com.google.inject.Injector       _injector = createInjector();

    private Injector() {
        //
    }

    /**
     * @return 唯一の{@link com.google.inject.Injector}インスタンス.
     */
    public static com.google.inject.Injector getGuiceInjector() {
        return _injector;
    }

    /**
     * @param <B>
     * @param pType
     * @return B型のオブジェクト.
     */
    public static <B> B getInstance(final Class<B> pType) {
        return _injector.getInstance(pType);
    }

    @SuppressWarnings("nls")
    private static Map<String, String> createDbProperties() {
        try {
            final String u = System.getenv("DATABASE_URL");
            if (u == null) {
                return createDbProperties2();
            }
            final URI dbUri = new URI(u);

            final String username = dbUri.getUserInfo().split(":")[0];
            final String password = dbUri.getUserInfo().split(":")[1];
            final String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath() + ":" + dbUri.getPort();

            final Map<String, String> ret = new HashMap<String, String>();
            ret.put("javax.persistence.driver", "org.postgres.Driver");
            ret.put("javax.persistence.jdbc.url", dbUrl);
            ret.put("javax.persistence.jdbc.user", username);
            ret.put("javax.persistence.jdbc.password", password);

            return ret;

        } catch (final URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("nls")
    private static Map<String, String> createDbProperties2() {
        final Map<String, String> ret = new HashMap<String, String>();

        ret.put("javax.persistence.driver", Driver.class.getName());
        ret.put("javax.persistence.jdbc.url", "jdbc:postgresql://localhost:5432/postgres");
        ret.put("javax.persistence.jdbc.user", "postgres");
        ret.put("javax.persistence.jdbc.password", "postgres");

        return ret;
    }

    private static com.google.inject.Injector createInjector() {
        final Module txModule = new AbstractModule() {

            @Override
            protected void configure() {
                final Matcher<? super Class<?>> classMatcher = Matchers.subclassesOf(DaoBase.class);
                final Matcher<? super Method> methodMatcher = new AbstractMatcher<Method>() {
                    @Override
                    public boolean matches(final Method pT) {
                        return Modifier.isPublic(pT.getModifiers());
                    }
                };
                final MethodInterceptor interceptor = new MethodInterceptor() {

                    @SuppressWarnings("synthetic-access")
                    @Override
                    public Object invoke(final MethodInvocation pInvocation) throws Throwable {
                        EntityManager em = _emHolder.get();
                        final boolean startTxThisFrame = em == null;
                        if (em == null) {
                            em = _emf.createEntityManager();
                            _emHolder.set(em);
                            em.getTransaction().begin();
                        }
                        try {
                            final Object ret = pInvocation.proceed();
                            if (startTxThisFrame) {
                                em.getTransaction().commit();
                            }
                            return ret;

                        } catch (final Throwable e) {
                            if (startTxThisFrame) {
                                em.getTransaction().rollback();
                            }
                            throw e;

                        } finally {
                            if (startTxThisFrame) {
                                em.close();
                                _emHolder.set(null);
                            }
                        }
                    }
                };
                this.bindInterceptor(classMatcher, methodMatcher, interceptor);
                this.bind(EntityManager.class).toProvider(new Provider<EntityManager>() {

                    @SuppressWarnings({ "synthetic-access", "nls" })
                    @Override
                    public EntityManager get() {
                        final EntityManager em = _emHolder.get();
                        if (em == null) {
                            throw new IllegalStateException("トランザクションが開始されていないため、" + EntityManager.class.getSimpleName()
                                    + "を供給できませんでした. メソッド呼び出し階層の上位に" + DaoBase.class.getSimpleName() + "を継承したクラスのpublicメソッドが必要です.");
                        }
                        return em;
                    }
                });
            }
        };
        return Guice.createInjector(txModule);
    }
}
