package Game;

import java.awt.*;

/**
 * Created by Camiel on 29-Nov-15.
 */
public class MapCoordinateTranslator {

    private int movementSpeed = 3;
    private int screenX1 = 0, screenY1 = 0, screenX2 = 1200, screenY2 = 1000; //screen resolution
    private int mapX1 =700, mapY1 = 700, mapX2 = 1900, mapY2 = 1700; //displayed part of map

    public Point getScreenPoint(Point mapPoint) {
        //translate mapPoint into current screen point. returns null if mapPoint is not in current view
        //if (isInView(mapPoint)) {
            int newX1 = mapPoint.x - mapX1;
            int newY1 = mapPoint.y - mapY1;
            return new Point(newX1, newY1);
        //}
        //else { return null; }
    }

    private boolean isInView(Point mapPoint) {
        //check if mapPoint is in current displayed part of map
        return (mapPoint.x < mapX2 && mapPoint.x > mapX2) && (mapPoint.y < mapY2 && mapPoint.y > mapY2);
    }

    public void updateDisplayedMapResolution(String direction) {
        switch (direction) {
            case "Up":
                mapY1 -= movementSpeed;
                break;
            case "Down":
                mapY1 += movementSpeed;
                break;
            case "Left":
                mapX1 -= movementSpeed;
                break;
            case "Right":
                mapX1 += movementSpeed;
                break;
            default:
                break;
        }
        mapX2 = mapX1 + 1200;
        mapY2 = mapY1 + 1000;
    }
}
