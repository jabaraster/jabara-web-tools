/**
 * 
 */
package jabara.web_tools.rest;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.AreaAveragingScaleFilter;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.multipart.FormDataParam;

/**
 * @author jabaraster
 */
@Path("image")
public class ImageResource {

    /**
     * @param pWidth
     * @param pHeight
     * @param pImage
     * @param pFormat
     * @return -
     * @throws IOException
     */
    @Path("resized")
    @Consumes({ MediaType.MULTIPART_FORM_DATA })
    @POST
    public static Response resizeImage( //
            @FormDataParam("width") final int pWidth //
            , @FormDataParam("height") final int pHeight //
            , @FormDataParam("image") final InputStream pImage //
            , @FormDataParam("format") final String pFormat //
    ) throws IOException {
        final byte[] responseData = resizeAndEncodeImage(pImage, pWidth, pHeight, ImageFormat.valueOf(pFormat));
        return Response.ok(responseData) //
                .type("image/" + pFormat) // //$NON-NLS-1$
                .build();
    }

    private static Image getResizedImage(final InputStream pImage, final int pWidth, final int pHeight) throws IOException {
        final AreaAveragingScaleFilter filter = new AreaAveragingScaleFilter(pWidth, pHeight);
        final Image resized = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(ImageIO.read(pImage).getSource(), filter));
        return resized;
    }

    private static byte[] resizeAndEncodeImage(final InputStream pImage, final int pWidth, final int pHeight, final ImageFormat pFormat)
            throws IOException {
        final BufferedImage ret = new BufferedImage(pWidth, pHeight, BufferedImage.TYPE_INT_ARGB);
        ret.createGraphics().drawImage(getResizedImage(pImage, pWidth, pHeight), 0, 0, null);
        final ByteArrayOutputStream mem = new ByteArrayOutputStream();
        ImageIO.write(ret, pFormat.getFormat(), mem);
        return mem.toByteArray();
    }

    enum ImageFormat {
        /**
         * 
         */
        PNG("png"), //$NON-NLS-1$

        /**
         * 
         */
        JPEG("jpeg"), //$NON-NLS-1$

        ;

        private final String format;

        ImageFormat(final String pFormat) {
            this.format = pFormat;
        }

        /**
         * @return the format
         */
        public String getFormat() {
            return this.format;
        }

    }

}
