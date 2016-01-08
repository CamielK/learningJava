package Game;

/**
 * Created by Camiel on 03-Jan-16.
 */
public class GlobalSettings {

    //movement speed settings:
    private static final int walkSpeed = 2;
    private static final int runSpeed = 3;
    private static int currentMoveSpeed = 2;
    public int getMovespeed() { return currentMoveSpeed; }
    public void setRunSpeed() { currentMoveSpeed = runSpeed; }
    public void setWalkSpeed() { currentMoveSpeed = walkSpeed; }

    //map size settings:
    private static final int mapSizeX = 2500;
    private static final int mapSizeY = 2500;
    private static final int tileSize = 3;
    public int getTileSize() {return tileSize;}
    public int getMapSizeX() {return mapSizeX;}
    public int getMapSizeY() {return mapSizeY;}

    //player:
    private static final int playerSize = 124; //124x124
    //private static final int playerSize = 313; //313x313
    private static final int playerXonScreen = 600 - (playerSize / 2);
    private static final int playerYonScreen = 500 - (playerSize / 2);
    public int getPlayerSize() {return playerSize;}
    public int getPlayerXonScreen() {return playerXonScreen;}
    public int getPlayerYonScreen() {return playerYonScreen;}

}
