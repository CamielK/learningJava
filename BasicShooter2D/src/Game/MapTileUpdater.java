package Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Camiel on 26-Nov-15.
 */
public class MapTileUpdater {
    //TODO: outer tiles should not be updated

    //generates and updates list of map tiles
    private int horizontalMaxTiles = 250, verticalMaxTiles = 250; //map size = 2500x2500
    private int tileSize = 10; //tile size = 10x10

    private final String backgroundFileName = "Resources/blockMap.png";
    private BufferedImage blockMap;

    private List<MapTile> mapTiles = new ArrayList<MapTile>();

    public void generateMapTileList() {

        //load blockMap image to generate blocked pixels.
        //red rgb = blocked, blue rgb = transparent, green rgb = empty
        URL imgUrl = getClass().getClassLoader().getResource(backgroundFileName);
        if (imgUrl == null) { System.err.println("Couldn't find file: " + backgroundFileName); }
        else {
            try { blockMap = ImageIO.read(imgUrl); } catch (IOException ex) { ex.printStackTrace(); }
        }

        //fill initial list
        int x1=0, y1=0, x2=10, y2=10; //first tile
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
            x1 = 0; x2 = 10;

        }
    }


    public List<MapTile> getMapTilesList() {
        return mapTiles;
    }


    public void setTileStatus(Point objectLocation, String objectType) {
        // object type can be a player or NPC. this is used to identify the size of the object
    }




}
