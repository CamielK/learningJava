package Game.Gfx;

import Game.Gfx.ImageLoader;
import Game.GlobalSettings;
import Game.Player.Player;
import javafx.scene.transform.Affine;

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
 * Created by Camiel on 06-Jan-16.
 */
public class Animation {

    //import global settings
    private static GlobalSettings settings = new GlobalSettings();
    private static final int playerXonScreen = settings.getPlayerXonScreen(), playerYonScreen = settings.getPlayerYonScreen();
    private static final int playerSize = settings.getPlayerSize();

    private static int feetSpriteIndex = 0, characterSpriteIndex = 0;
    private static int updateSpeed = 3, ticksSinceLastUpdate = 1; //wait 3 ticks for next sprite
    private static List<BufferedImage> feetImagesRunning = new ArrayList<BufferedImage>();
    private static List<BufferedImage> playerShotgunImages = new ArrayList<BufferedImage>();
    private static BufferedImage playerImg, playerBox;
    private static Player player = new Player();
    private static String weaponStatus = "idle", moveStatus = "idle";
    private static boolean shooting = false;

    private static int characterAnimationTick = 0, feetAnimationTick = 0;

    AffineTransform tx = AffineTransform.getRotateInstance(player.getRotation(), 64, 64);
    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);


    public Animation() {
        //TODO bake image sources instead of recalculating every time?
        //this.updateSpeed = updateSpeed;

        //load all sprites
        for (int i = 0; i <= 79; i++) {

            if (i >= 0 && i <= 19) { //first 20 sprites
                feetImagesRunning.add(new ImageLoader().loadImage("Resources/playerSprites/feet/walk/survivor-walk_" + i + ".png")); // walk feet sprites
                playerShotgunImages.add(new ImageLoader().loadImage("Resources/playerSprites/shotgun/idle/survivor-idle_shotgun_" + i + ".png")); //idle shotgun sprites
            }
            else if (i >= 20 && i <= 39) { //second 20 sprites
                int index = i - 20;
                feetImagesRunning.add(new ImageLoader().loadImage("Resources/playerSprites/feet/run/survivor-run_" + index + ".png")); // run feet sprites
                if (index < 15) {playerShotgunImages.add(new ImageLoader().loadImage("Resources/playerSprites/shotgun/meleeattack/survivor-meleeattack_shotgun_" + index + ".png"));} // meleeattack shotgun sprites <<< lasts only 15 sprites
                else { playerShotgunImages.add(null); } //add 5 nulls to make sure next set of sprites starts at 40
            }
            else if (i >= 40 && i <= 59) { //third 20 sprites
                int index = i - 40;
                feetImagesRunning.add(new ImageLoader().loadImage("Resources/playerSprites/feet/strafe_left/survivor-strafe_left_" + index + ".png")); // strafe left feet sprites
                playerShotgunImages.add(new ImageLoader().loadImage("Resources/playerSprites/shotgun/move/survivor-move_shotgun_" + index + ".png")); //movement shotgun sprites
            }
            else if (i >= 60 && i <= 79) { //fourth 20 sprites
                int index = i - 60;
                feetImagesRunning.add(new ImageLoader().loadImage("Resources/playerSprites/feet/strafe_right/survivor-strafe_right_" + index + ".png")); // strafe right feet sprites
                playerShotgunImages.add(new ImageLoader().loadImage("Resources/playerSprites/shotgun/reload/survivor-reload_shotgun_" + index + ".png")); //reload shotgun sprites
            }
        }

        //dont use this one, wrong resolution
        // add last idle feet sprite at index 80
        //feetImagesRunning.add(new ImageLoader().loadImage("Resources/playerSprites/feet/idle/survivor-idle_0.png"));

        // add last tree shoot sprites at index 80, 81 and 82
        for (int i = 0; i < 3; i++) { playerShotgunImages.add(new ImageLoader().loadImage("Resources/playerSprites/shotgun/shoot/survivor-shoot_shotgun_" + i + ".png"));}

    }

    public void update() {
        if (ticksSinceLastUpdate >= updateSpeed) {
            updateFeet();
            updateCharacter();
            ticksSinceLastUpdate = 1;
        }
        else {
            ticksSinceLastUpdate++;
        }

        tx = AffineTransform.getRotateInstance(player.getRotation(), playerSize/2, playerSize/2); //rotates coordinates around an anchor point
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
    }

    public void draw(Graphics2D g2d) {

        drawFeet(g2d);
        drawCharacter(g2d);
    }




    //########### private methods ###########
    private void updateFeet() {
        //TODO calculate feet sprite index depending on: running? walking? idle? strafing left? strafing right?

        String oldStatus = moveStatus;
        moveStatus = player.getMoveStatus();

        if (!oldStatus.equals(moveStatus)) {
            //restart animation tick when move status changes
            feetAnimationTick = 0;
        }

        if (moveStatus.equals("idle")) {
            feetSpriteIndex = 0;
        }
        else if (moveStatus.equals("walking")) {
            feetSpriteIndex = feetAnimationTick;
        }
        else if (moveStatus.equals("running")) {
            feetSpriteIndex = 20 + feetAnimationTick;
        }
        else if (moveStatus.equals("strafeLeft")) {
            feetSpriteIndex = 40 + feetAnimationTick;
        }
        else if (moveStatus.equals("strafeRight")) {
            feetSpriteIndex = 60 + feetAnimationTick;
        }
        feetAnimationTick++;
        if (feetAnimationTick >= 20) {
            feetAnimationTick = 0;
        }
    }

    private void drawFeet(Graphics2D g2d) {
        //draws feet image with calculated feet sprite index
        BufferedImage oldImg = feetImagesRunning.get(feetSpriteIndex);
        BufferedImage newImg = null;
        if (player.getMoveStatus().equals("strafeLeft") || player.getMoveStatus().equals("strafeRight")) {
            newImg = new ImageConverter().convertImage(oldImg, 2.85, 124, 124, 30, 40); //strafing leg position
        }
        else {
            newImg = new ImageConverter().convertImage(oldImg, 2.85, 124, 124, 25, 49); //normal leg position
        }


        g2d.drawImage(op.filter(newImg, null), playerXonScreen, playerYonScreen, null);
    }

    private void updateCharacter() {

        String oldStatus = weaponStatus;
        weaponStatus = player.getWeaponStatus();

        if (!oldStatus.equals(weaponStatus) || (shooting != player.isShotFired())) {
            characterAnimationTick = 0;//start animation at 0 the first time status changes
            shooting = player.isShotFired();
        }

        if (weaponStatus.equals("reloading")) {
            characterSpriteIndex = 60 + characterAnimationTick;
            characterAnimationTick++;
            if (characterAnimationTick >= 20) { player.setWeaponStatus("idle"); characterAnimationTick = 0; }
        }
        else if (weaponStatus.equals("idle")) {
            if (player.getMoveStatus().equals("idle")) {
                characterSpriteIndex = characterAnimationTick;
            }
            else {
                characterSpriteIndex = 40 + characterAnimationTick;
            }
            characterAnimationTick++;
            if (characterAnimationTick >= 20) { characterAnimationTick = 0; }
        }
        if (shooting) {
            characterSpriteIndex = 80 + characterAnimationTick;
            characterAnimationTick++;
            if (characterAnimationTick >= 2) { player.setWeaponStatus("idle"); characterAnimationTick = 0; }
        }
    }

    private void drawCharacter(Graphics2D g2d) {
        //draws player image with calculated player sprite index
        BufferedImage oldImg = playerShotgunImages.get(characterSpriteIndex);
        BufferedImage newImg = new ImageConverter().convertImage(oldImg, 2.85, 124, 124, 20, 24);

        g2d.drawImage(op.filter(newImg, null), playerXonScreen, playerYonScreen, null);

        g2d.drawRect(playerXonScreen, playerYonScreen, playerSize, playerSize);//outer lines (img border)
        g2d.drawRect(playerXonScreen+(playerSize/4), playerYonScreen+(playerSize/4), playerSize/2, playerSize/2);//inner lines (collides)


        //examples:
        //new ImageConverter().convertImage(playerShotgunImagesIdle.get(characterSpriteIndex), 2.85, 124, 124, 20, 24);
        //convertImage(oldFeetImg, 2.85, 124, 124, 20, (24+(83/2.85))) << converts old player sprite into new 124x124
    }
}
