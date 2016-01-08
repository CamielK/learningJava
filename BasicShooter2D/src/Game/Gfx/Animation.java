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
    private static List<BufferedImage> playerImages = new ArrayList<BufferedImage>();
    private static BufferedImage playerImg, playerBox;
    private Player player = new Player();

    AffineTransform tx = AffineTransform.getRotateInstance(player.getRotation(), 32, 32);
    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);


    public Animation(int updateSpeed) {
        //TODO bake image sources instead of recalculating every time?
        this.updateSpeed = updateSpeed;

        //load 20 sprites of each image
        for (int i = 0; i <= 19; i++) {
            feetImagesRunning.add(new ImageLoader().loadImage("Resources/playerSprites/feet/walk/survivor-walk_" + i + ".png"));
            playerImages.add(new ImageLoader().loadImage("Resources/playerSprites/shotgun/idle/survivor-idle_shotgun_" + i + ".png"));
        }

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
        //TODO combine these images to print only 1 image instead?

        drawFeet(g2d);
        drawCharacter(g2d);
    }

    private void updateFeet() {
        //TODO calculate feet sprite index depending on: running? walking? idle? strafing left? strafing right?

        feetSpriteIndex++;
        if (feetSpriteIndex >= 20) {
            feetSpriteIndex = 0;
        }
    }

    private void drawFeet(Graphics2D g2d) {
        //draws feet image with calculated feet sprite index

        if (feetSpriteIndex >= 0 && feetSpriteIndex <= 19) {
            BufferedImage oldImg = feetImagesRunning.get(feetSpriteIndex);
            BufferedImage newImg = new ImageConverter().convertImage(oldImg, 2.85, 124, 124, 20, 50); //(int) (24+(83/2.85))

            g2d.drawImage(op.filter(newImg, null), playerXonScreen, playerYonScreen, null);
        }
    }

    private void updateCharacter() {
        //TODO calculate character sprite index depending on: running? walking? idle? shooting? reloading? melee?

        characterSpriteIndex++;
        if (characterSpriteIndex >= 20) {
            characterSpriteIndex = 0;
        }
    }

    private void drawCharacter(Graphics2D g2d) {
        //draws player image with calculated player sprite index

        if (characterSpriteIndex >= 0 && characterSpriteIndex <= 19) {
            BufferedImage oldImg = playerImages.get(characterSpriteIndex);
            BufferedImage newImg = new ImageConverter().convertImage(oldImg, 2.85, 124, 124, 20, 24);

            g2d.drawImage(op.filter(newImg, null), playerXonScreen, playerYonScreen, null);

            g2d.drawRect(playerXonScreen, playerYonScreen, playerSize, playerSize);//outer lines (img border)
            g2d.drawRect(playerXonScreen+(playerSize/4), playerYonScreen+(playerSize/4), playerSize/2, playerSize/2);//inner lines (collides)



            //examples:
            //new ImageConverter().convertImage(playerImagesIdle.get(characterSpriteIndex), 2.85, 124, 124, 20, 24);
            //convertImage(oldFeetImg, 2.85, 124, 124, 20, (24+(83/2.85))) << converts old player sprite into new 124x124



        }
    }
}
