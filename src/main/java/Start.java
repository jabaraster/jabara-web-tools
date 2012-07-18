import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * @author じゃばら
 */
public class Start {
    /**
     * @param args
     * @throws Exception
     */
    @SuppressWarnings("nls")
    public static void main(final String[] args) throws Exception {
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8081";
        }
        final String webappDirLocation = "src/main/webapp/";

        final Server server = new Server(Integer.parseInt(webPort));
        final WebAppContext root = new WebAppContext();
        root.setContextPath("/");
        root.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
        root.setResourceBase(webappDirLocation);
        root.setParentLoaderPriority(true);

        server.setHandler(root);
        server.start();
        server.join();
    }
}
