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

    //########## player variables ##########
    //player logic:
    private int playerXonMap = 1268, playerYonMap = 1168;
    private final int playerXonScreen = 568, playerYonScreen = 468;
    private double rotation = Math.toRadians (90);
    private String orientation = "right";
    private double rotationDegrees = 90;
    private int movementSpeed = 3;

    //ammunitions:
    private static int totalBullets = 300;
    private final static int clipSize = 30;
    private static int currentClip = 30;
    private boolean reloading = false;

    //bullets
    private List<Bullet> bullets = new ArrayList<Bullet>();
    private static boolean updatingBullets = false;
    private static boolean paintingBullets = false;

    //load player image:
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
    //########## end of variables ##########


    public void drawPlayer (Graphics2D g2d) {
        //minimap player:
        g2d.fillRect(1070,120,10,10);

        //draw bullets
        if (!updatingBullets) {
            paintingBullets = true;
            for (Iterator<Bullet> iter = bullets.listIterator(); iter.hasNext(); ) {
                Bullet bullet = iter.next();
                bullet.drawBullet(g2d, playerXonMap - 568, playerYonMap - 468);
            }
            paintingBullets = false;
        }
        else {
            System.out.println("Could not paint bullets. (bullets are being updated)");
        }

        //draw player
        AffineTransform tx = AffineTransform.getRotateInstance(rotation, 32, 32);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        g2d.drawImage(op.filter(playerImg, null), playerXonScreen, playerYonScreen, null);
        g2d.drawRect(playerXonScreen, playerYonScreen, 64, 64);

        //ammunition:
        g2d.drawString("Ammo: "+Integer.toString(currentClip)+" / "+totalBullets,50,950);
        if (totalBullets == 0) {
            g2d.drawString("You are out of ammo!",550,550);
        }
        else if (currentClip == 0) {
            g2d.drawString("You need to reload! (R)",550,550);
        }

        //reload
        if (reloading) {
            reloading = false;
            //TODO implement reload animation here
        }

    }

    public void updatePlayer() {
        //update bullets
        if (!paintingBullets) {
            updatingBullets = true;
            for (Iterator<Bullet> iter = bullets.listIterator(); iter.hasNext(); ) {
                Bullet bullet = iter.next();
                bullet.updateBullet();
                if (bullet.isExpired()) {
                    iter.remove();
                }
            }
            updatingBullets = false;
        }
        else {
            System.out.println("Could not update bullets. (bullets are being painted)");
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
        if (!updatingBullets && !paintingBullets && currentClip > 0) {
            bullets.add(new Bullet(playerXonMap+32,playerYonMap+32,rotationDegrees,0)); //add new bullet
            currentClip--;
        }
        else if (currentClip == 0) {
            System.out.println("Could not add bullet. empty clip!");
        }
        else {
            System.out.println("Could not add bullet. (bullets are being painted or updated)");
        }
    }

    public void reloadWeapon() {
        reloading = true;
        totalBullets += currentClip;
        if (totalBullets >= clipSize) {
            currentClip = clipSize;
            totalBullets -= clipSize;
        }
        else {
            currentClip = totalBullets;
            totalBullets = 0;
        }
    }

}
