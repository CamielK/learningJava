package Game;

import Game.Audio.SoundEngine;
import Game.Clouds.CloudFactory;
import Game.Map.CollisionChecker;
import Game.Map.MapCoordinateTranslator;
import Game.Npcs.Terrorist;
import Game.Player.Player;
import GameEngine.Screen;
import GameEngine.ScreenFactory;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Camiel on 21-Nov-15.
 */
public class GameUpdater extends Screen {

    //import global settings
    private GlobalSettings settings = new GlobalSettings();
    private final int playerSize = settings.getPlayerSize();

    public GameUpdater(ScreenFactory screenFactory) {
        super(screenFactory);
    }
    private GameGUI gui = new GameGUI();
    private Player player = new Player();
    private CloudFactory cloudFactory = new CloudFactory();
    private Point playerLocataion;

    private SoundEngine soundEngine = new SoundEngine();

    //firing
    private static int firerate = 4; //= 20 per second = 1200/min
    private static int fireCooldown = firerate; //countdown for new bullet
    private static boolean firing = false;

    private CollisionChecker collisionChecker;

    private Terrorist terrorist;

    private MapCoordinateTranslator mapCoordinateTranslator = new MapCoordinateTranslator();


    @Override
    public void onCreate() {
        System.out.println("Creating!");

        collisionChecker = new CollisionChecker();

        //Point spawnPoint = mapCoordinateTranslator.getScreenPoint(new Point(1200,1200));
        //terrorist = new Terrorist(new Point(1200,1200));

        //background track
        soundEngine.playLooped("Resources/AUDIO/10_Min_Escalating_Ambient_Battle_soundFX.wav", -3.0f);

        //generate initial clouds
        cloudFactory.fillField();
    }

    @Override
    public void onUpdate() {
        //check WASD keys for movement
        if (getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_A)) { moveLeft(); }
        if (getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_D)) { moveRight(); }
        if (getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_W)) { moveUp(); }
        if (getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_S)) { moveDown(); }

        //update fire cooldown
        if (fireCooldown >= firerate) { firing = false; }
        else if (fireCooldown < firerate) { fireCooldown++; firing = true; }

        //set player direction pointing towards mouse
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        player.setRotation(mouseLocation);

        //check mouseclick for shooting
        //TODO: enable mouse for shooting instead of spacebar
        //if (getScreenFactory().getGame().getMousepadListener().isMousePressed()) { shoot(); }
        if (getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_SPACE)) { shoot(); }
        if (getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_R)) { player.reloadWeapon(); }

        //running adjust speed:
        if (getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_SHIFT)) { settings.setRunSpeed(); }
        else { settings.setWalkSpeed(); }

        //update player
        player.updatePlayer();

        //update clouds
        cloudFactory.updateClouds();
    }

    @Override
    public void onDraw(Graphics2D g2d) {
        gui.drawGUI(g2d);
        player.drawPlayer(g2d);

        if (terrorist != null) { terrorist.drawTerrorist(g2d); }

        cloudFactory.drawClouds(g2d);
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
            mapCoordinateTranslator.updateDisplayedMapResolution("Up");
        }
    }

    private void moveDown () {
        if (!collision("Down")) {
            player.movePlayer("Down");
            gui.moveBackground("Down");
            mapCoordinateTranslator.updateDisplayedMapResolution("Down");
        }
    }

    private void moveLeft () {
        if (!collision("Left")) {
            player.movePlayer("Left");
            gui.moveBackground("Left");
            mapCoordinateTranslator.updateDisplayedMapResolution("Left");
        }
    }

    private void moveRight () {
        if (!collision("Right")) {
            player.movePlayer("Right");
            gui.moveBackground("Right");
            mapCoordinateTranslator.updateDisplayedMapResolution("Right");
        }
    }

    private boolean collision(String direction) {
        boolean collided = false;
        playerLocataion = player.getLocation();
        int movespeed = player.getMovementSpeed();
        Point nextLocation = new Point(playerLocataion.x, playerLocataion.y);

        //calculate next position
        switch (direction) {
            case "Up":
                nextLocation.y -= movespeed;
                break;
            case "Down":
                nextLocation.y += movespeed;
                break;
            case "Left":
                nextLocation.x -= movespeed;
                break;
            case "Right":
                nextLocation.x += movespeed;
                break;
            default:
                break;
        }

        collided = collisionChecker.blockMapCollision(new Rectangle(nextLocation.x+(playerSize/4), nextLocation.y+(playerSize/4), playerSize/2, playerSize/2));
        return collided;
    }
}
