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
    private static final int playerXonScreen = settings.getPlayerXonScreen(), playerYonScreen = settings.getPlayerYonScreen();
    private static final int playerSize = settings.getPlayerSize();
    private static int playerXonMap = (1300 - (playerSize/2)), playerYonMap = (1200 - (playerSize/2));

    //player animation
    private static Animation animation = new Animation(); //update every 3rd tick
    private static double rotation = Math.toRadians (90);
    private static String orientation = "right";
    private static double rotationDegrees = 90;
    private static int movementSpeed = settings.getMovespeed();
    private static String moveStatus = "idle"; //for feet and character animation; can be idle, running, walking, strafing left or strafing right
    private static String weaponStatus = "idle"; //for character animation; can be idle, reloading, knifing
    private static String weaponType = "shotgun"; //for character animation; can be shotgun, rifle, knife, handgun or flashlight
    private static boolean shotFired = false;

    //ammunition variables
    private static int totalBullets = 300;
    private final static int clipSize = 30;
    private static int currentClip = 30;

    //bullet variables
    private static List<Bullet> bullets = new ArrayList<Bullet>();
    private static boolean updatingBullets = false;
    private static boolean paintingBullets = false;
    private static SoundEngine soundEngine = new SoundEngine(); //for bullet sound

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


        //ammunition:
        g2d.drawString("Ammo: "+Integer.toString(currentClip)+" / "+totalBullets,50,950);
        if (totalBullets == 0) {
            g2d.drawString("You are out of ammo!",550,550);
        }
        else if (currentClip == 0) {
            g2d.drawString("You need to reload! (R)",550,550);
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
    }

    public double getRotation() {
        return rotation;
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

    public Point getLocation() {
        return new Point(playerXonMap, playerYonMap);
    }

    public void fireWeapon() {
        if ((!updatingBullets) && (!paintingBullets) && (currentClip > 0) &&  (!weaponStatus.equals("reloading")) ) {
            bullets.add(new Bullet(playerXonMap+(playerSize/2),playerYonMap+(playerSize/2),rotationDegrees,0)); //add new bullet
            currentClip--;
            shotFired = true;
            soundEngine.playOnce("Resources/AUDIO/gunshot"+ (new Random().nextInt(3)+1) +".wav", -1.0f);
        }
//        else {
//            System.out.println("Could not add bullet. (bullets are being painted or updated)");
//        }
    }

    public void reloadWeapon() {
        if (!weaponStatus.equals("reloading")) {
            weaponStatus = "reloading";
        }
    }

    public String getMoveStatus() {
        return moveStatus;
    }

    public String getWeaponStatus(){
        return weaponStatus;
    }

    public void setWeaponStatus(String newStatus){
        if (weaponStatus.equals("reloading") && newStatus.equals("idle")) { //animation has finished reloading..
            totalBullets += currentClip;
            if (totalBullets >= clipSize) { currentClip = clipSize; totalBullets -= clipSize; }
            else { currentClip = totalBullets; totalBullets = 0; }
        }
        if (shotFired && newStatus.equals("idle")) {
            shotFired = false;
        }
        weaponStatus = newStatus;
    }

    public boolean isShotFired() {
        return shotFired;
    }

    public String getWeaponType() {
        return weaponStatus;
    }

    public String getOrientation() {
        return orientation;
    }

}
