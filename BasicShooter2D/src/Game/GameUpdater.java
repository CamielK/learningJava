package Game;

import GameEngine.Screen;
import GameEngine.ScreenFactory;

import java.awt.*;
import java.awt.event.KeyEvent;
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

    //firing
    private int firerate = 25; //4 per second
    private int fireCooldown = firerate; //countdown for new bullet
    private boolean firing = false;

    private MapTileUpdater mapTileUpdater = new MapTileUpdater(2500, 2500, 10, "Resources/blockMap.png");
    private List<MapTile> mapTiles;

    private Terrorist terrorist = new Terrorist(1000,1000);


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
        terrorist.drawTerrorist(g2d);
    }

    private void shoot () {
        //check fire cooldown
        if (!firing) {
            player.fireWeapon();
            fireCooldown = 0;
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
                i = mapTiles.size();
            }
        }

        return collided;
    }
}
