/**
 * 
 */
package jabara.web_tools.service;

/**
 * @author jabaraster
 */
public class NotFound extends Exception {
    private static final long serialVersionUID = -7898509779818568249L;

    /**
     * 
     */
    public NotFound() {
        //
    }

    /**
     * @param pCause
     */
    public NotFound(final Throwable pCause) {
        super(pCause);
    }

}
