package Game.Player;

import Game.Audio.SoundEngine;
import Game.Gfx.Animation;
import Game.Gfx.ImageLoader;
import Game.GlobalSettings;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * Created by Camiel on 24-Nov-15.
 */
public class Player {

    //########## player variables ##########
    //import global settings
    private static GlobalSettings settings = new GlobalSettings();

    //player logic:
    //private int playerXonMap = 1268, playerYonMap = 1168;
    //private final int playerXonScreen = 568, playerYonScreen = 468;
    private static final int playerXonScreen = settings.getPlayerXonScreen(), playerYonScreen = settings.getPlayerYonScreen();
    private static final int playerSize = settings.getPlayerSize();
    private static int playerXonMap = (1300 - (playerSize/2)), playerYonMap = (1200 - (playerSize/2));

    private static double rotation = Math.toRadians (90);
    private static String orientation = "right";
    private static double rotationDegrees = 90;
    private int movementSpeed = settings.getMovespeed();
    private boolean running = false;

    //ammunitions:
    private static int totalBullets = 300;
    private final static int clipSize = 30;
    private static int currentClip = 30;
    private static boolean reloading = false;

    //bullets
    private static List<Bullet> bullets = new ArrayList<Bullet>();
    private static boolean updatingBullets = false;
    private static boolean paintingBullets = false;

    private static SoundEngine soundEngine = new SoundEngine();
    private static Animation animation = new Animation(2); //update every 3rd tick

    //load player image:
    private BufferedImage playerImg = new ImageLoader().loadImage("Resources/player3.png");
    AffineTransform tx = AffineTransform.getRotateInstance(rotation, 64, 64);
    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

    //########## end of variables ##########

    public Player(){
    }


    public void drawPlayer (Graphics2D g2d) {
        //minimap player:
        g2d.fillRect(1070,120,10,10);

        //draw bullets
        if (!updatingBullets) {
            paintingBullets = true;
            for (Iterator<Bullet> iter = bullets.listIterator(); iter.hasNext(); ) {
                Bullet bullet = iter.next();
                bullet.drawBullet(g2d, playerXonMap - playerXonScreen, playerYonMap - playerYonScreen);
            }
            paintingBullets = false;
        }
        else {
            System.out.println("Could not paint bullets. (bullets are being updated)");
        }

        animation.draw(g2d);

        //draw player
        //g2d.drawImage(op.filter(playerImg, null), playerXonScreen, playerYonScreen, null);
        //g2d.drawRect(playerXonScreen+(playerSize/4), playerYonScreen+(playerSize/4), playerSize/2, playerSize/2);

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
        //update bullet
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

        animation.update();
    }


    public void setRotation (Point mouseLocation) { //calculate angle mouseLocation from center
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        center.move((int) center.getX(), (int) center.getY()+32); //correct for real center (player)
        double xDistance = mouseLocation.getX() - center.getX();
        double yDistance = mouseLocation.getY() - center.getY();
        double degrees = Math.toDegrees(Math.atan2(yDistance, xDistance));
        rotation = Math.toRadians (degrees);
        rotationDegrees = degrees;

        tx = AffineTransform.getRotateInstance(rotation, 64, 64);
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
    }

    public double getRotation() {
        return rotation;
    }

    public Point getLocation() {
        return new Point(playerXonMap, playerYonMap);
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public void movePlayer(String direction) {
        movementSpeed = settings.getMovespeed();

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
            bullets.add(new Bullet(playerXonMap+(playerSize/2),playerYonMap+(playerSize/2),rotationDegrees,0)); //add new bullet
            currentClip--;
            soundEngine.playOnce("Resources/AUDIO/gunshot"+ (new Random().nextInt(3)+1) +".wav", -1.0f);
        }
        else if (currentClip == 0) {
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
