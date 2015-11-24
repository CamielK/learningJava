package Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Camiel on 24-Nov-15.
 */
public class Player {

    public int playerXonMap = 568, playerYonMap = 468;
    public final int playerXonScreen = 568, playerYonScreen = 468;
    public double rotation = Math.toRadians (90);
    private int movementSpeed = 3;

    //player image creation:
    private final String playerFileName = "Resources/player.png";
    private BufferedImage playerImg;
    public Player(){
        URL imgUrl = getClass().getClassLoader().getResource(playerFileName);
        if (imgUrl == null) { System.err.println("Couldn't find file: " + playerFileName); }
        else {
            try { playerImg = ImageIO.read(imgUrl); } catch (IOException ex) { ex.printStackTrace(); }
        }
    }
    public void drawPlayer (Graphics2D g2d) {
        AffineTransform tx = AffineTransform.getRotateInstance(rotation, 32, 32);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        g2d.drawImage(op.filter(playerImg, null), playerXonScreen, playerYonScreen, null);


        g2d.drawImage(playerImg, 1070,120,1080,130,0,0,64,64, null);
        g2d.fillRect(1070,120,10,10);
    }

    public void setRotation (Point mouseLocation) { //calculate angle mouseLocation from center
        Point center =GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        double xDistance = mouseLocation.getX() - center.getX();
        double yDistance = mouseLocation.getY() - center.getY();
        double degrees = Math.toDegrees(Math.atan2(yDistance, xDistance));
        rotation = Math.toRadians (degrees+90);
    }

    public void movePlayer(String direction) {
        switch (direction) {
            case "Up":
                playerYonMap -= movementSpeed;
                break;
            case "Down":
                playerYonMap += movementSpeed;
                break;
            case "Left":
                playerXonMap -= movementSpeed;
                break;
            case "Right":
                playerXonMap += movementSpeed;
                break;
            default:
                break;
        }
    }
}
