package Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Created by Camiel on 24-Nov-15.
 */
public class Player {

    public int playerXonMap = 1268, playerYonMap = 1168;
    public final int playerXonScreen = 568, playerYonScreen = 468;
    public double rotation = Math.toRadians (90);
    public String orientation = "right";
    public double rotationDegrees = 90;

    private int movementSpeed = 3;

    private List<Bullet> bullets = new ArrayList<Bullet>();

    //player image creation:
    //private final String playerFileName = "Resources/player.png";
    private final String playerFileName = "Resources/player1.png";
    private BufferedImage playerImg;
    public Player(){
        URL imgUrl = getClass().getClassLoader().getResource(playerFileName);
        if (imgUrl == null) { System.err.println("Couldn't find file: " + playerFileName); }
        else {
            try { playerImg = ImageIO.read(imgUrl); } catch (IOException ex) { ex.printStackTrace(); }
        }
    }
    public void drawPlayer (Graphics2D g2d) {
        //minimap player:
        g2d.fillRect(1070,120,10,10);

        //draw and update bullets
        for (Iterator<Bullet> iter = bullets.listIterator(); iter.hasNext(); ) {
            Bullet bullet = iter.next();
            bullet.drawBullet(g2d, playerXonMap - 568, playerYonMap - 468);
            if (bullet.isExpired()) {
                iter.remove();
            }
        }

        //draw player
        AffineTransform tx = AffineTransform.getRotateInstance(rotation, 32, 32);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        g2d.drawImage(op.filter(playerImg, null), playerXonScreen, playerYonScreen, null);
        g2d.drawRect(playerXonScreen, playerYonScreen, 64, 64);
    }

    public void updatePlayer() {
        //update bullets
        for (Iterator<Bullet> iter = bullets.listIterator(); iter.hasNext(); ) {
            iter.next().updateBullet();
        }
    }


    public void setRotation (Point mouseLocation) { //calculate angle mouseLocation from center
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        double xDistance = mouseLocation.getX() - center.getX();
        double yDistance = mouseLocation.getY() - center.getY();
        double degrees = Math.toDegrees(Math.atan2(yDistance, xDistance));
        rotation = Math.toRadians (degrees);
        rotationDegrees = degrees;

    }

    public Point getLocation() {
        return new Point(playerXonMap, playerYonMap);
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public void movePlayer(String direction) {
        switch (direction) {
            case "Up":
                playerYonMap -= movementSpeed;
                orientation = "up";
                break;
            case "Down":
                playerYonMap += movementSpeed;
                orientation = "down";
                break;
            case "Left":
                playerXonMap -= movementSpeed;
                orientation = "left";
                break;
            case "Right":
                playerXonMap += movementSpeed;
                orientation = "right";
                break;
            default:
                break;
        }
    }

    public void fireWeapon() {
        bullets.add(new Bullet(playerXonMap+32,playerYonMap+32,rotationDegrees,0)); //add new bullet
    }


}
