package Game;

import java.awt.*;
import java.util.ArrayList;
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


    public boolean blockMapCollision(Rectangle object) {
        //checks if object (rectangle) collides with blocked tiles
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
}
