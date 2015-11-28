package Game;

import GameEngine.Screen;
import GameEngine.ScreenFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Created by Camiel on 21-Nov-15.
 */
public class GameUpdater extends Screen {

    public GameUpdater(ScreenFactory screenFactory) {
        super(screenFactory);
    }
    private GameGUI gui = new GameGUI();
    private Player player = new Player();
    private Point playerLocataion;

    private int movementSpeed = 3;
    private int borderX = 700, borderY = 700;

    //firing
    private int firerate = 25; //4 per second
    private int fireCooldown = firerate; //countdown for new bullet
    private boolean firing = false;

    private MapTileUpdater mapTileUpdater = new MapTileUpdater();
    private List<MapTile> mapTiles;


    @Override
    public void onCreate() {
        System.out.println("Creating!");
        mapTileUpdater.generateMapTileList(); // generate map tiles
        mapTiles = mapTileUpdater.getMapTilesList();
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

        //set player direction pointing towards mouse
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        player.setRotation(mouseLocation);

        //check mouseclick for shooting
        //TODO: enable mouse for shooting instead of spacebar
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
        if (!collision("Up")) {
            player.movePlayer("Up");
            gui.moveBackground("Up");
        }
    }

    private void moveDown () {
        if (!collision("Down")) {
            player.movePlayer("Down");
            gui.moveBackground("Down");
        }
    }

    private void moveLeft () {
        if (!collision("Left")) {
            player.movePlayer("Left");
            gui.moveBackground("Left");
        }
    }

    private void moveRight () {
        if (!collision("Right")) {
            player.movePlayer("Right");
            gui.moveBackground("Right");
        }
    }

    private boolean collision(String direction) {
        boolean collided = false;
        playerLocataion = player.getLocation();
        int movespeed = player.getMovementSpeed();
        Point nextLocationBorder = new Point(playerLocataion.x, playerLocataion.y);

        switch (direction) {
            case "Up":
                nextLocationBorder.y -= movespeed;
                break;
            case "Down":
                nextLocationBorder.y += (movespeed + 64);
                break;
            case "Left":
                nextLocationBorder.x -= movespeed;
                break;
            case "Right":
                nextLocationBorder.x += (movespeed + 64);
                break;
            default:
                break;
        }

        for (int i = 0; i < mapTiles.size(); i++) {
            MapTile currentTile = mapTiles.get(i);
            if (currentTile.tileContains(nextLocationBorder) && currentTile.getStatus().equals("blocked")) {
                collided = true;
                System.out.println("tile P1: " + currentTile.getTileP1().x + "-" + currentTile.getTileP1().y + "status: " + mapTiles.get(i).getStatus());
                i = mapTiles.size();
            }
        }

        return collided;
    }


//    private void moveUp () {
//        borderY -= movementSpeed;
//        if (borderY >= movementSpeed) {
//            player.movePlayer("Up");
//            gui.moveBackground("Up");
//        }
//        else if (borderY < movementSpeed) {
//            borderY += movementSpeed;
//        }
//    }
//
//    private void moveDown () {
//        borderY += movementSpeed;
//        if (borderY < (1500-movementSpeed)) {
//            player.movePlayer("Down");
//            gui.moveBackground("Down");
//        }
//        else if (borderY >= (1500-movementSpeed)) {
//            borderY -= movementSpeed;
//        }
//    }
//
//    private void moveLeft () {
//        borderX -= movementSpeed;
//        if (borderX >= movementSpeed) {
//            player.movePlayer("Left");
//            gui.moveBackground("Left");
//        }
//        else if (borderX < movementSpeed) {
//            borderX += movementSpeed;
//        }
//    }
//
//    private void moveRight () {
//        collision("Right");
//        borderX += movementSpeed;
//        if (borderX < (1300-movementSpeed)) {
//            player.movePlayer("Right");
//            gui.moveBackground("Right");
//        }
//        else if (borderX >= (1300-movementSpeed)) {
//            borderX -= movementSpeed;
//        }
//    }
}
