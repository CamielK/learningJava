package Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Camiel on 28-Nov-15.
 */
public class Terrorist {

    private String name;
    private int x1CorOnMap, y1CorOnMap, x2CorOnMap, y2CorOnMap;
    private int x1CorOnScreen, y1CorOnScreen;
    private int movementSpeed = 4;

    private static int uniqueTerroristID = 0;

    private final String terroristFileName = "Resources/player.png";
    private BufferedImage terroristImg;

    private MapTileUpdater mapTileUpdater;

    public Terrorist(int spawnXcor, int spawnYcor) {
        this.x1CorOnMap = spawnXcor;
        this.y1CorOnMap = spawnYcor;
        this.x2CorOnMap = spawnXcor + 64;
        this.y2CorOnMap = spawnYcor + 64;


        URL imgUrl = getClass().getClassLoader().getResource(terroristFileName);
        if (imgUrl == null) { System.err.println("Couldn't find file: " + terroristFileName); }
        else {
            try { terroristImg = ImageIO.read(imgUrl); } catch (IOException ex) { ex.printStackTrace(); }
        }
    }

    public void drawTerrorist(Graphics2D g2d) {
        g2d.fillRect(x1CorOnMap, y1CorOnMap, 64, 64);
    }

    public Point getP1LocationOnMap() {
        return new Point (x1CorOnMap, y1CorOnMap);
    }

    public Point getP2LocationOnMap() {
        return new Point (x2CorOnMap, y2CorOnMap);
    }

    public void moveTerrorist(String direction) {
        mapTileUpdater.setTileStatus(new Point (x1CorOnMap, y1CorOnMap), new Point (x2CorOnMap, y2CorOnMap), "empty"); //clear old location
        switch (direction) {
            case "Up":
                x1CorOnMap -= movementSpeed;
                break;
            case "Down":
                y1CorOnMap += movementSpeed;
                break;
            case "Left":
                x1CorOnMap -= movementSpeed;
                break;
            case "Right":
                y2CorOnMap += movementSpeed;
                break;
            default:
                break;
        }
        x2CorOnMap = x1CorOnMap +64;
        y2CorOnMap = x1CorOnMap +64;
        mapTileUpdater.setTileStatus(new Point (x1CorOnMap, y1CorOnMap), new Point (x2CorOnMap, y2CorOnMap), ("terrorist_" + Integer.toString(uniqueTerroristID))); //set new location
    }
}
