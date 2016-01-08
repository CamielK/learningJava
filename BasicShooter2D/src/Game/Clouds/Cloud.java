package Game.Clouds;

import Game.Map.MapCoordinateTranslator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Camiel on 05-Jan-16.
 */
public class Cloud {

    private final String cloudFileName = "Resources/cloud.png";
    private Image cloud;
    private static final int srcX1 = 0, srcY1 = 0, srcX2 = 300, srcY2 = 300; // default source window
    private int destX1 = 0, destY1 = 0, destX2 = 0, destY2 = 0, oldDestY1 = 0; // initial destination coordiantes of backgrounda
    private int cloudMapPosX = 0, cloudMapPosY = 0;
    private int lifetime = 0;
    private final int maxLifetime = 3000; // = 50 seconds at 60 updates per second
    private double sizeFactor = 1;
    private double moveFactor = 1;
    private int moveSpeed = 1;
    private MapCoordinateTranslator mapCorTranslator = new MapCoordinateTranslator();


    public Cloud(Point spawnPoint, double sizeFactor, double moveFactor) {
        //init cloud image
        if (cloud == null) {
            URL imgUrl = getClass().getClassLoader().getResource(cloudFileName);
            if (imgUrl == null) { System.err.println("Couldn't find file: " + cloudFileName); }
            else {
                try { cloud = ImageIO.read(imgUrl); } catch (IOException ex) { ex.printStackTrace(); }
            }
        }

        //set spawn location
        this.cloudMapPosX = (int) spawnPoint.getX();
        this.cloudMapPosY = (int) spawnPoint.getY();
        this.sizeFactor = sizeFactor;
        this.moveFactor = moveFactor;
        Point spawnPointOnScreen = mapCorTranslator.getScreenPoint(new Point(cloudMapPosX,cloudMapPosY));
        this.destX1 = (int) spawnPointOnScreen.getX();
        this.destY1 = (int) spawnPointOnScreen.getY();
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(cloud, destX1, destY1, destX2, destY2, srcX1, srcY1, srcX2, srcY2, null);
    }

    public void update() {
        lifetime++;

        //update cloud location
        cloudMapPosX -= (moveSpeed*moveFactor);

        calcScreenPos();
    }

    public boolean isExpired() {
        return (lifetime >= maxLifetime);
    }

    private void calcScreenPos() {
        Point drawPoint = mapCorTranslator.getScreenPoint(new Point(cloudMapPosX,cloudMapPosY)); //get current screen loc
        this.destX1 = (int) drawPoint.getX();
        this.destY1 = destY1 + (((int) drawPoint.getY() - destY1)/10); //vertical movement is slowed by factor 10
        this.destX2 = (int) (destX1 + (srcX2*sizeFactor));
        this.destY2 = (int) (destY1 + (srcY2*sizeFactor));
    }
}
