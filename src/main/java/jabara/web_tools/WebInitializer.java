/**
 * 
 */
package jabara.web_tools;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.http.HttpServlet;

import com.sun.jersey.spi.container.servlet.ServletContainer;

/**
 * @author jabaraster
 */
public class WebInitializer implements ServletContextListener {

    /**
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(@SuppressWarnings("unused") final ServletContextEvent pSce) {
        // 処理なし
    }

    /**
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(final ServletContextEvent pSce) {
        //        addServlet(pSce.getServletContext(), UploadServlet.class).addMapping("/images"); //$NON-NLS-1$
        initializeJersey(pSce.getServletContext());
    }

    private static Dynamic addServlet(final ServletContext pServletContext, final Class<? extends HttpServlet> pType) {
        return pServletContext.addServlet(pType.getName(), pType);
    }

    @SuppressWarnings("nls")
    private static void initializeJersey(final ServletContext pContext) {
        final ServletRegistration.Dynamic jerseyServlet = addServlet(pContext, ServletContainer.class);
        jerseyServlet.setInitParameter("com.sun.jersey.config.property.packages", "jabara.web_tools.rest");
        // jerseyServlet.setInitParameter("javax.ws.rs.Application", ClasspathResourceConfig.class.getName());
        // jerseyServlet.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", Boolean.TRUE.toString());
        jerseyServlet.addMapping("/rest/*");
    }
}
