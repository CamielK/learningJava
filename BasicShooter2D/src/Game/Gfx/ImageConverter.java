package Game.Gfx;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Camiel on 08-Jan-16.
 */
public class ImageConverter {


    public BufferedImage convertImage(BufferedImage oldImg, double resizeFactor, int newWidth, int newHeight, int relocateXOffset, int relocateYOffset) {
        Image resizedImg = oldImg.getScaledInstance((int)(oldImg.getWidth()/resizeFactor), (int)(oldImg.getHeight()/resizeFactor), Image.SCALE_DEFAULT); //get scaled version of img (2.85)
        BufferedImage newImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB); // Create a buffered image with transparency

        Graphics2D bGr = newImg.createGraphics();
        bGr.drawImage(resizedImg,relocateXOffset, relocateYOffset, null); //x20, y24
        bGr.dispose();

        return newImg;

        //examples:
        //convertImage(oldPlayerImg, 2.85, 124, 124, 20, 24) << converts old player sprite into new 124x124
        //convertImage(oldFeetImg, 2.85, 124, 124, 20, (24+(83/2.85))) << converts old player sprite into new 124x124

    }



}
