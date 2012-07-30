/**
 * 
 */
package jabara.web_tools.service;

import javax.ws.rs.core.MediaType;

/**
 * @author jabara
 * 
 */
public final class ExMediaType {

    /**
     * 
     */
    public static final String TEXT_PLAIN    = MediaType.TEXT_PLAIN + "; charset=" + ExMediaType.TEXT_ENCODING; //$NON-NLS-1$

    /**
     * 
     */
    public static final String TEXT_CSV      = "text/csv; charset=" + ExMediaType.TEXT_ENCODING;               //$NON-NLS-1$

    /**
     * 
     */
    public static final String TEXT_ENCODING = "UTF-8";                                                        //$NON-NLS-1$

    private ExMediaType() {
        //
    }
}
