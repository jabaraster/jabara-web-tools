/**
 * 
 */
package jabara.web_tools.rest;

import javax.ws.rs.Path;

/**
 * @author jabaraster
 */
@Path("images")
public class ImageResource {

    // TODO 画像リサイズサービス

    // final BufferedImage src = ImageIO.read(new File("/Users/jabaraster/Downloads/King.png"));
    // final AreaAveragingScaleFilter filter = new AreaAveragingScaleFilter(144, 144);
    // final Image im = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(src.getSource(), filter));
    //
    // final BufferedImage dst = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_ARGB);
    // dst.createGraphics().drawImage(im, 0, 0, null);
    //
    // ImageIO.write(dst, "png", new File("/Users/jabaraster/Downloads/app_icon_retina.png"));

}
