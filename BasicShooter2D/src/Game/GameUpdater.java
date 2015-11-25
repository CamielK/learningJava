package Game;

import GameEngine.Screen;
import GameEngine.ScreenFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Camiel on 21-Nov-15.
 */
public class GameUpdater extends Screen {

    public GameUpdater(ScreenFactory screenFactory) {
        super(screenFactory);
    }
    private GameGUI gui = new GameGUI();
    private Player player = new Player();

    private int movementSpeed = 3;
    private int borderX = 0, borderY = 0;

    //firing
    private int firerate = 25; //4 per second
    private int fireCooldown = firerate; //countdown for new bullet
    private boolean firing = false;


    @Override
    public void onCreate() {
        System.out.println("Creating!");
    }

    @Override
    public void onUpdate() {
        //check WASD keys for movement
        if (getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_A)) { moveLeft(); }
        if (getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_D)) { moveRight(); }
        if (getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_W)) { moveUp(); }
        if (getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_S)) { moveDown(); }


        //update fire cooldown
        if (fireCooldown >= firerate) {
            firing = false;
        }
        else if (fireCooldown < firerate) {
            fireCooldown++;
            firing = true;
        }
        //System.out.println("firerate: " + fireCooldown);


        //set player direction pointing towards mouse
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        player.setRotation(mouseLocation);

        //check mouseclick for shooting
        //if (getScreenFactory().getGame().getMousepadListener().isMousePressed()) { shoot(); }
        if (getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_SPACE)) { shoot(); }
    }

    @Override
    public void onDraw(Graphics2D g2d) {
        gui.drawGUI(g2d);
        player.drawPlayer(g2d);
    }

    private void shoot () {
        //check fire cooldown
        if (!firing) {
            player.fireWeapon();
            fireCooldown = 0;
            //System.out.println("fired weapon");
        }
    }


    private void moveUp () {
        borderY -= movementSpeed;
        if (borderY >= movementSpeed) {
            player.movePlayer("Up");
            gui.moveBackground("Up");
        }
        else if (borderY < movementSpeed) {
            borderY += movementSpeed;
        }
    }

    private void moveDown () {
        borderY += movementSpeed;
        if (borderY < (1500-movementSpeed)) {
            player.movePlayer("Down");
            gui.moveBackground("Down");
        }
        else if (borderY >= (1500-movementSpeed)) {
            borderY -= movementSpeed;
        }
    }

    private void moveLeft () {
        borderX -= movementSpeed;
        if (borderX >= movementSpeed) {
            player.movePlayer("Left");
            gui.moveBackground("Left");
        }
        else if (borderX < movementSpeed) {
            borderX += movementSpeed;
        }
    }

    private void moveRight () {
        borderX += movementSpeed;
        if (borderX < (1300-movementSpeed)) {
            player.movePlayer("Right");
            gui.moveBackground("Right");
        }
        else if (borderX >= (1300-movementSpeed)) {
            borderX -= movementSpeed;
        }
    }
}