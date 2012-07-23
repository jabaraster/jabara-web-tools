/**
 * 
 */
package jabara.web_tools.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;

import com.sun.jersey.multipart.impl.MultiPartWriter;

/**
 * @author jabaraster
 */
@Provider
@Produces("multipart/*")
@Consumes("multipart/*")
public class ExMultiPartWriter extends MultiPartWriter {

    /**
     * @param pProviders
     */
    public ExMultiPartWriter(@Context final Providers pProviders) {
        super(pProviders);
    }

}
