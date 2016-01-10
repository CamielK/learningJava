package Game.Player;

import Game.Audio.SoundEngine;
import Game.Gfx.Animation;
import Game.GlobalSettings;
import Game.Map.MapCoordinateTranslator;

import javax.swing.*;
import java.awt.*;
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
    private static Point weaponPosition = new Point (0,0); //(is a Point to the right of the player center)
    private static MapCoordinateTranslator mapCoordinateTranslator = new MapCoordinateTranslator();
    private static int weaponPixelOffset = 17; //weapon is 17 pixels to the right of the player center

    //player animation
    private static Animation animation = new Animation(); //update every 3rd tick
    private static double rotation = Math.toRadians (90);
    private static String orientation = "right";
    private static double rotationDegrees = 90;
    private static int movementSpeed = settings.getMovespeed();
    private static String moveStatus = "idle"; //for feet and character animation; can be idle, running, walking, strafing left or strafing right
    private static String weaponStatus = "idle"; //for character animation; can be idle, reloading, knifing
    private static String weaponType = "shotgun"; //for character animation; can be shotgun, rifle, knife, handgun or flashlight
    private static boolean shotFired = false, playerMoved = false;

    //ammunition variables
    private static int totalBullets = 300;
    private final static int clipSize = 30;
    private static int currentClip = 30;

    //bullet variables
    private static List<Bullet> bullets = new ArrayList<Bullet>();
    private static boolean updatingBullets = false;
    private static boolean paintingBullets = false;
    private static SoundEngine soundEngine = new SoundEngine(); //for bullet sound
    private static Point mousePoint = new Point(0,0);
    private static double mouseDistance = 0;


    //########## end of variables ##########

    public Player(){
    }


    public void drawPlayer (Graphics2D g2d, JFrame window) {
        //minimap player:
        g2d.fillRect(1070,120,10,10);

        //draw bullets
        if (!updatingBullets) {
            paintingBullets = true;
            for (Iterator<Bullet> iter = bullets.listIterator(); iter.hasNext(); ) {
                Bullet bullet = iter.next();
                bullet.drawBullet(g2d);
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


        //uncomment to draw guidelines while testing
//        Point locOnScreen = window.getLocationOnScreen();// does not account for border wich is x3 and y30
//        g2d.drawLine(600, 500, (int) (mousePoint.getX() - (locOnScreen.getX()+3)), (int) (mousePoint.getY() - (locOnScreen.getY()+30)));
//        Point test = mapCoordinateTranslator.getScreenPoint(weaponPosition);
//        g2d.drawLine((int) test.getX(), (int) test.getY(), (int) (mousePoint.getX() - (locOnScreen.getX()+3)), (int) (mousePoint.getY() - (locOnScreen.getY()+30)));
    }

    public void updatePlayer() {
        //update weapon position
        calculateWeaponPosition(rotationDegrees);

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

        //calculate move status
        if (!playerMoved) {moveStatus = "idle";}
        else if (playerMoved) {

            if (movementSpeed == settings.getRunSpeed()) {
                moveStatus = "running";
            }
            else if (movementSpeed == settings.getWalkSpeed()) {
                moveStatus = "walking";
            }

            //check for strafing by comparing movement direction to orientation
            if (rotationDegrees >= -135 && rotationDegrees <= -45) { //north
                if (orientation.equals("right")) { moveStatus = "strafeRight"; }
                else if (orientation.equals("left")) { moveStatus = "strafeLeft"; }
            }
            else if (rotationDegrees >= -45 && rotationDegrees <= 45) { //east
                if (orientation.equals("down")) { moveStatus = "strafeRight"; }
                else if (orientation.equals("up")) { moveStatus = "strafeLeft"; }
            }
            else if (rotationDegrees >= 45 && rotationDegrees <= 135) { //south
                if (orientation.equals("left")) { moveStatus = "strafeRight"; }
                else if (orientation.equals("right")) { moveStatus = "strafeLeft"; }
            }
            else { //west
                if (orientation.equals("up")) { moveStatus = "strafeRight"; }
                else if (orientation.equals("down")) { moveStatus = "strafeLeft"; }
            }

            playerMoved = false;
        }

        //System.out.println(moveStatus);

        animation.update();
    }


    //calculates player rotation so that player points at mouse
    public void setRotation (Point mouseLocation, JFrame window) {
        if (mouseLocation.getX() != mousePoint.getX() || mouseLocation.getY() != mousePoint.getY()) { //only execute when mouse location has changed
            mousePoint = mouseLocation; //save mouse location for future reference

            //calculate angle in degrees between player and mouse
            Point center = window.getLocationOnScreen(); //player point as center of screen
            center.move((int) center.getX() + settings.getScreenWidth()/2 + 3, (int) center.getY() + settings.getScreenHeight()/2 + 30); // +3 and +30 correct for the border around the JFrame
            double xDistance = mouseLocation.getX() - center.getX(); //calculate x delta
            double yDistance = mouseLocation.getY() - center.getY(); //calculate y delta
            double degrees = Math.toDegrees(Math.atan2(yDistance, xDistance));

            //get length between player and mouse
            mouseDistance = Math.sqrt((center.getX()-mousePoint.getX())*(center.getX()-mousePoint.getX()) + (center.getY()-mousePoint.getY())*(center.getY()-mousePoint.getY())); //a^2 + b^2 = c^2

            //calculate rotation offset to point weapon at mouse instead of player
            double correctionAngle = Math.toDegrees(Math.atan2(mouseDistance, weaponPixelOffset));
            double rotationOffset = 90 - correctionAngle;

            //apply offset to degrees (resetting at -180)
            degrees -= rotationOffset;
            if (degrees < -180) {
                degrees = 180 - (Math.abs(degrees) - 180);
            }

            //save calculation results
            rotation = Math.toRadians (degrees);
            rotationDegrees = degrees;
        }
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

        playerMoved = true;
    }

    public Point getLocation() {
        return new Point(playerXonMap, playerYonMap);
    }

    public void fireWeapon() {
        //System.out.println("distance to mouse: " + mouseDistance);

        if ((!updatingBullets) && (!paintingBullets) && (currentClip > 0) && (!weaponStatus.equals("reloading")) && (!weaponStatus.equals("knifing")) ) {
            bullets.add(new Bullet(((int)weaponPosition.getX()),((int)weaponPosition.getY()),rotationDegrees)); //add new bullet
            //System.out.println("degrees = " + rotationDegrees + ".         weaponpoint (x:y) = (" + (int)weaponPosition.getX() + ":" + (int)weaponPosition.getY() + ").        playerpoint (x:y) = (" + (playerXonMap+64) + ":" + (playerYonMap+64) + ")");
            currentClip--;
            shotFired = true;
            soundEngine.addSound("Resources/AUDIO/gunshot"+ (new Random().nextInt(3)+1) +".wav", -1.0f, false);
        }
//        else {
//            System.out.println("Could not add bullet. (bullets are being painted or updated)");
//        }
    }

    public void reloadWeapon() {
        if (!weaponStatus.equals("reloading") && !weaponStatus.equals("knifing")) {
            weaponStatus = "reloading";
        }
    }

    public void knifeAttack() {
        if (!weaponStatus.equals("knifing") && !weaponStatus.equals("reloading")) {
            weaponStatus = "knifing";
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

    //TODO different weapons/items?
    public String getWeaponType() {
        return weaponStatus;
    }

    private void calculateWeaponPosition(double degrees) {
        //Pythagoras equation to determine the weapons position (17 pixels away from the center, at the given angle in degrees)
        double aboutStanding, adjacent;
        double wepXcor = playerXonMap + (playerSize/2), wepYcor = playerYonMap + (playerSize/2);

        degrees+=90; //weapon offset is applied to the right of the player

        aboutStanding = Math.sin(Math.toRadians(degrees)) * weaponPixelOffset;
        adjacent = Math.cos(Math.toRadians(degrees)) * weaponPixelOffset;

        wepYcor += aboutStanding;
        wepXcor += adjacent;

        //System.out.println("degrees-playerX-playerY-weaponX-weaponY = " + degrees + "-" + (playerXonMap+64) + "-" + (playerYonMap+64) + "-" + wepXcor + "-" + wepYcor);

        weaponPosition = new Point((int)wepXcor,(int)wepYcor);
    }
}
