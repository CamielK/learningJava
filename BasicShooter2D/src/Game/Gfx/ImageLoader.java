package Game.Gfx;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.Buffer;

/**
 * Created by Camiel on 07-Jan-16.
 */
public class ImageLoader {

    public BufferedImage loadImage (String filename) {
        BufferedImage image = null;
        URL imgUrl = getClass().getClassLoader().getResource(filename);
        if (imgUrl == null) { System.err.println("Couldn't find file: " + filename); }
        else {
            try { image = ImageIO.read(imgUrl); } catch (IOException ex) { ex.printStackTrace(); }
        }
        return image;
    }
}
