package Game.Map;

import Game.GlobalSettings;

import java.awt.*;
import java.util.List;

/**
 * Created by Camiel on 02-Dec-15.
 *
 *
 * checks for collisions
 */
public class CollisionChecker {

    //import global settings
    private static GlobalSettings settings = new GlobalSettings();
    private static int mapSizeX = settings.getMapSizeX();
    private static int mapSizeY = settings.getMapSizeY();
    private static int tileSize = settings.getTileSize();

    private static MapTileUpdater mapTileUpdater;
    private static List<MapTile> mapTiles;
    private static boolean generated = false;

    public CollisionChecker () {
        if (!generated) {
            mapTileUpdater = new MapTileUpdater(mapSizeX, mapSizeY, tileSize, "Resources/blockMap.png");
            mapTileUpdater.generateMapTileList(); // generate map tiles
            mapTiles = mapTileUpdater.getMapTilesList();
            generated = true;
        }
    }

    public String getCollision(Point objectP1, Point objectP2) {
        String collision = "None";

        //TODO: limit collision checker to only check around objects location (to prevent entire map from being unnecessarily checked)


        //TODO: check if provided object collides with blockmap


        //TODO: check if provided object collides with npc

        //TODO: check if provided object collides with

        return collision;
    }

    public boolean rectIntersectsBlockMap(Rectangle object) {
        //checks if object (rectangle) collides with blockmap tiles
        boolean collided = false;

        //get list of all maptile indexes that contain the object
        List<Integer> mapTileIndexes = mapTileUpdater.getMapTileIndexes(object);

        //loop list to check blockmap
        for (int i = 0; i < mapTileIndexes.size(); i++) { //loop all map tiles to check for collisions.
            int currentIndex = mapTileIndexes.get(i);
            MapTile currentTile = mapTiles.get(currentIndex);
            if (currentTile.getStatus().equals("blocked")) {
                collided = true;
                i = mapTiles.size();
            }
        }

        return collided;
    }

    public int lineIntersectsBlockMap(Point p1, Point p2) {
        //returns the index of the maptile where the line intersects with the blockmap, returns 0 when no intersects

        return 0;
    }

    private boolean lineIntersectsLine(Point l1p1, Point l1p2, Point l2p1, Point l2p2) {
        //returns true if the gives lines intersect at any point

        float q = (l1p1.y - l2p1.y) * (l2p2.x - l2p1.x) - (l1p1.x - l2p1.x) * (l2p2.y - l2p1.y);
        float d = (l1p2.x - l1p1.x) * (l2p2.y - l2p1.y) - (l1p2.y - l1p1.y) * (l2p2.x - l2p1.x);

        if( d == 0 ) { return false; }

        float r = q / d;

        q = (l1p1.x - l2p1.y) * (l1p2.x - l1p1.x) - (l1p1.x - l2p1.x) * (l1p2.y - l1p1.y);
        float s = q / d;

        if( r < 0 || r > 1 || s < 0 || s > 1 )
        {
            return false;
        }

        return true;
    }
}
