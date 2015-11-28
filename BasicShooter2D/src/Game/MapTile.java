package Game;

import java.awt.*;

/**
 * Created by Camiel on 28-Nov-15.
 */
public class MapTile {

    //allows for list of map tiles to be made
    // a tile can have various conditions such as blocked (by objects), transparent (object in way but can walk trough), occupied by player, occupied by npc or empty

    private int x1,y1,x2,y2;
    private String status = "empty";


    public MapTile(int x1, int y1, int x2, int y2, String status) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.status = status;
    }

    public Point getTileP1() {
        Point P1 = new Point(x1, y1);
        return P1;
    }

    public Point getTileP2() {
        Point P2 = new Point(x2, y2);
        return P2;
    }

    public boolean tileContains(Point location) {
        //check if location is inside tile
        return (location.x >= x1 && location.x <= x2 && location.y >= y1 && location.y <= y2);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
