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

    //import global settings
    private GlobalSettings settings = new GlobalSettings();
    private int movementSpeed = settings.getMovespeed();

    private String name;
    private int x1CorOnMap, y1CorOnMap, x2CorOnMap, y2CorOnMap;
    private int x1CorOnScreen, y1CorOnScreen;

    private static int uniqueTerroristID = 0;

    private final String terroristFileName = "Resources/player.png";
    private BufferedImage terroristImg;

    private MapTileUpdater mapTileUpdater;
    private MapCoordinateTranslator mapCoordinateTranslator = new MapCoordinateTranslator();

    public Terrorist(Point spawnPoint) {

        this.x1CorOnMap = spawnPoint.x;
        this.y1CorOnMap = spawnPoint.y;
        this.x2CorOnMap = spawnPoint.x + 64;
        this.y2CorOnMap = spawnPoint.y + 64;
        System.out.println("new terrorist spawned at: " + x1CorOnMap + ", " + y1CorOnMap);


        URL imgUrl = getClass().getClassLoader().getResource(terroristFileName);
        if (imgUrl == null) { System.err.println("Couldn't find file: " + terroristFileName); }
        else {
            try { terroristImg = ImageIO.read(imgUrl); } catch (IOException ex) { ex.printStackTrace(); }
        }
    }

    public void drawTerrorist(Graphics2D g2d) {
        Point drawPoint = mapCoordinateTranslator.getScreenPoint(new Point(x1CorOnMap,y1CorOnMap)); //get current screen loc
        g2d.fillRect(drawPoint.x, drawPoint.y, 64, 64);
        //System.out.println(drawPoint.x + ", " + drawPoint.y);
    }

    public Point getP1LocationOnMap() {
        return new Point (x1CorOnMap, y1CorOnMap);
    }

    public Point getP2LocationOnMap() {
        return new Point (x2CorOnMap, y2CorOnMap);
    }

    public void moveTerrorist(String direction) {
        movementSpeed = settings.getMovespeed();

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
