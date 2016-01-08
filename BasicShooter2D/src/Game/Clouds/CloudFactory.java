package Game.Clouds;

import Game.Map.MapCoordinateTranslator;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Camiel on 05-Jan-16.
 */
public class CloudFactory {

    private static List<Cloud> clouds = new ArrayList<Cloud>();
    private static final int maxCloudCount = 15;
    private static int currentCloudCount = 0;
    private static final int spawnRate = 10; //chance of spawn out of 1000
    private MapCoordinateTranslator mapCorTranslator = new MapCoordinateTranslator();
    private static boolean updatingClouds = false;
    private static boolean paintingClouds = false;
    private static final int updateRate = 30; //30 updates per second at 60 game ticks per second
    private static int skippedUpdates = 0;
    private Random random = new Random();

    public CloudFactory() {
//        Point mapLocation = mapCorTranslator.getCurrentMapLocation();
//        int spawnX = (int) mapLocation.getX() + 600;
//        int spawnY = (int) (mapLocation.getY() + 500); //get a random vertical position (random+1 to prevent devide by zero)
//
//        //add new cloud
//        clouds.add(new Cloud(new Point(spawnX,spawnY),1,1));
//        clouds.get(0).update();
    }


    public void updateClouds() {

        if (skippedUpdates >= 2 && !paintingClouds) {

            //spawn new clouds
            if (currentCloudCount < maxCloudCount) {
                if (spawnRate >= random.nextInt(1001)) {
                    //calculate spawn location
                    Point mapLocation = mapCorTranslator.getCurrentMapLocation();
                    int spawnX = (int) mapLocation.getX() + 1200;
                    int spawnY = (int) (mapLocation.getY() + (1000*random.nextDouble())); //get a random vertical position (random+1 to prevent devide by zero)

                    //add new cloud
                    updatingClouds = true;
                    clouds.add(new Cloud(new Point(spawnX,spawnY),1,1));
                    currentCloudCount++;

                }
            }

            //update old clouds
            for (Iterator<Cloud> iter = clouds.listIterator(); iter.hasNext(); ) {
                Cloud cloud = iter.next();
                cloud.update();
                if (cloud.isExpired()) {
                    iter.remove();
                    currentCloudCount--;
                }
            }
            updatingClouds = false;
            skippedUpdates=0;
        }
        else {
            skippedUpdates++;
        }
        if (paintingClouds) {
            System.out.println("Could not update clouds. (clouds are being painted)");
        }

    }


    public void drawClouds(Graphics2D g2d) {
        //draw clouds
        if (!updatingClouds) {
            paintingClouds = true;
            for (Iterator<Cloud> iter = clouds.listIterator(); iter.hasNext(); ) {
                Cloud cloud = iter.next();
                cloud.draw(g2d);
            }
            paintingClouds = false;
        }
        else {
            System.out.println("Could not paint clouds. (clouds are being updated)");
        }
    }

    public void fillField() {
        //TODO this function should generate a cloud field so that when the game starts there are already clouds present
    }

}
