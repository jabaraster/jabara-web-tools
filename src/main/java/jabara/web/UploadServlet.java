/**
 * 
 */
package jabara.web;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * @author jabaraster
 */
@MultipartConfig
public class UploadServlet extends HttpServlet {
    private static final long serialVersionUID = -4835571876892976401L;

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest pRequest, final HttpServletResponse pResponse) throws ServletException, IOException {
        pRequest.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(pRequest, pResponse); //$NON-NLS-1$
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @SuppressWarnings("nls")
    @Override
    protected void doPost(final HttpServletRequest pRequest, final HttpServletResponse pResponse) throws ServletException, IOException {

        System.out.println(pRequest.getContentType());

        if (!String.valueOf(pRequest.getContentType()).toLowerCase().startsWith("multipart/form-data")) {
            pResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        final String userName = getStringFromPart(pRequest, "userName");
        final String password = getStringFromPart(pRequest, "password");

        System.out.println("==================================");
        System.out.println("= userName: " + userName);
        System.out.println("= password: " + password);
        System.out.println("==================================");
    }

    private static void close(final Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (final IOException e) {
                //
            }
        }
    }

    private static String getStringFromPart(final HttpServletRequest pRequest, final String pPartName) throws IOException, ServletException {
        final Part part = pRequest.getPart(pPartName);
        if (part == null) {
            return null;
        }

        InputStream in = null;
        try {
            in = new BufferedInputStream(part.getInputStream());

            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final byte[] buf = new byte[4096];
            for (int d = in.read(buf); d != -1; d = in.read(buf)) {
                out.write(buf, 0, d);
            }

            return new String(out.toByteArray(), Charset.forName("UTF-8")); //$NON-NLS-1$

        } finally {
            close(in);
        }
    }
}
