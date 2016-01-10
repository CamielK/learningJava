package Game.Map;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * generates and keeps track of all map tiles and their status
 *
 *
 * Created by Camiel on 26-Nov-15.
 */
public class MapTileUpdater {
    //TODO: outer tiles should not be updated

    //generates and updates list of map tiles
    private int mapSizeX, mapSizeY, tileSize, horizontalMaxTiles, verticalMaxTiles;
    private final String blockMapFileName;
    private BufferedImage blockMap;
    private static List<MapTile> mapTiles = new ArrayList<MapTile>();

    public MapTileUpdater(int mapSizeX, int mapSizeY, int tileSize, String blockMapFileName) {
        this.mapSizeX = mapSizeX;
        this.mapSizeY = mapSizeY;
        this.tileSize = tileSize;
        this.horizontalMaxTiles = mapSizeX / tileSize;
        this.verticalMaxTiles = mapSizeY / tileSize;
        this.blockMapFileName = blockMapFileName;
    }

    public void generateMapTileList() {
        //load blockMap image to generate blocked pixels.
        //red rgb = blocked, blue rgb = transparent, green rgb = empty
        URL imgUrlBlockMap = getClass().getClassLoader().getResource(blockMapFileName);
        if (imgUrlBlockMap == null) { System.err.println("Couldn't find file: " + blockMapFileName); }
        else {
            try { blockMap = ImageIO.read(imgUrlBlockMap); } catch (IOException ex) { ex.printStackTrace(); }
        }

        //fill initial list
        int x1=0, y1=0, x2=tileSize, y2=tileSize; //first tile
        for (int i=1; i <= verticalMaxTiles; i++) {
            for (int k=1; k <= horizontalMaxTiles; k++) {
                String status = "empty";
                int argb = blockMap.getRGB(x1, y1);

                int rgb[] = new int[] {
                        (argb >> 16) & 0xff, //red
                        (argb >>  8) & 0xff, //green
                        (argb      ) & 0xff  //blue
                };

                //check status depending on color:
                if (rgb[0] == 255) {status = "blocked";} //red = blocked
                if (rgb[1] == 255) {status = "empty";} //green = empty
                if (rgb[2] == 255) {status = "transparent";} //blue = transparent

                mapTiles.add(new MapTile(x1,y1,x2,y2,status));
                x1 += tileSize; x2 +=tileSize;
            }
            y1 += tileSize; y2 += tileSize;
            x1 = 0; x2 = tileSize;
        }
    }

    public List<MapTile> getMapTilesList() {
        return mapTiles;
    }

    public void setTileStatus(Point objectLocationP1, Point objectLocationP2, String status) {
        //updates the status of all maptiles intersecting with the given rectangle

    }

    public List<Integer> getMapTileIndexes(Rectangle object) {
        //returns a list of indexes that point to the maptiles intersecting with the given rectangle

        int objectP1Index = getMapTileIndex(new Point( (int) object.getX(), (int) object.getY()));
        int objectP2Index = getMapTileIndex(new Point( (int) (object.getX() + object.getWidth()), (int) (object.getY() + object.getHeight())));


        //get a list of maptile indexes for all tiles between P1 and P2
        List<Integer> mapTileIndexes = new ArrayList<Integer>();

        //get horizontal and vertical delta's
        int deltaX = (objectP2Index%(mapSizeX/tileSize)) - (objectP1Index%(mapSizeX/tileSize)); //ammount of horizontal tiles between P1 and P2
        int deltaY = ((objectP2Index-deltaX) - (objectP1Index)) / (mapSizeX/tileSize); //ammount of rows betweeen P1 and P2

        //calculate all maptile indexes and add them to the list
        int i = objectP1Index;
        int x = deltaX;
        int y = deltaY;
        while (i <= objectP2Index) {
            mapTileIndexes.add(i); //add tile to list

            //calculate next tile:
            if (x > 0) { i++; x--; }
            else if (y > 0) {//jump to next row when delta X are depleted, reset delta X counter
                i += (mapSizeX/tileSize);
                i -= deltaX;
                y--;
                x = deltaX;
            }
            else {
                i++;
            }

        }

        return mapTileIndexes;
    }

    private int getMapTileIndex(Point objectPoint) {
        //returns index of maptile that matches the objects P1
        double tileX = objectPoint.getX() / tileSize;
        double tileRow = objectPoint.getY() / tileSize;

        return (int) ((((int) tileRow) * (mapSizeX/tileSize)) + tileX);
    }
}
