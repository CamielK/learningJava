package Game;

import java.awt.*;
import java.util.List;

/**
 * Created by Camiel on 02-Dec-15.
 *
 *
 * checks for collisions
 */
public class CollisionChecker {

    private MapTileUpdater mapTileUpdater = new MapTileUpdater(2500, 2500, 5, "Resources/blockMap.png");
    private List<MapTile> mapTiles;

    public CollisionChecker () {
        mapTileUpdater.generateMapTileList(); // generate map tiles
        mapTiles = mapTileUpdater.getMapTilesList();
    }

    public String getCollision(Point objectP1, Point objectP2) {
        String collision = "None";

        //TODO: limit collision checker to only check around objects location (to prevent entire map from being unnecessarily checked)


        //TODO: check if provided object collides with blockmap


        //TODO: check if provided object collides with npc

        //TODO: check if provided object collides with

        return collision;
    }


    public boolean blockMapCollision(Rectangle object) {
        //checks if object (rectangle) collides with blocked tiles

        boolean collided = false;
        for (int i = 0; i < mapTiles.size(); i++) { //loop all map tiles to check for collisions.
            //TODO only check local tiles instead of entire tile map
            // translate location to mapTiles index??
            // get all nearby tiles, check if they are blocked.
            MapTile currentTile = mapTiles.get(i);
            if (currentTile.tileContains(object) && currentTile.getStatus().equals("blocked")) {
                collided = true;
                i = mapTiles.size();
            }
        }
        return collided;
    }
}
