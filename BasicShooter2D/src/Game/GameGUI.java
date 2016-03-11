package Game;

import Game.Gfx.ImageLoader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * Created by Camiel on 24-Nov-15.
 */
public class GameGUI {

    //import global settings
    private GlobalSettings settings = new GlobalSettings();
    private int movementSpeed = settings.getMovespeed();

    //background:
    //private final String backgroundFileName = "Resources/blockMap.png"; //regular map
    private final String backgroundFileName = "Resources/bgMap.png"; //blocked area map
    private static Image background;
    private final String guiFileName = "Resources/GUI.png";
    private static Image gui;
    private final String backgroundTemplateFileName = "Resources/bgMapTemplate.png";
    private static Image backgroundTemplate;
    static int srcX1 = 700, srcY1 = 700, srcX2 = 1900, srcY2 = 1700; // default source window of background
    static final int destX1 = 0, destY1 = 0, destX2 = 1200, destY2 = 1000; // destination coordiantes of backgrounda


    public GameGUI(){
        background = new ImageLoader().loadImage(backgroundFileName);
        gui = new ImageLoader().loadImage(guiFileName);
        backgroundTemplate = new ImageLoader().loadImage(backgroundTemplateFileName);
    }

    public void drawGUI (Graphics2D g2d) {
        //background
        g2d.drawImage(background, destX1, destY1, destX2, destY2, srcX1, srcY1, srcX2, srcY2, null); //draw map
        //g2d.drawImage(backgroundTemplate, destX1, destY1, destX2, destY2, srcX1, srcY1, srcX2, srcY2, null); //draw raster lines
        //g2d.drawImage(backgroundTemplate, destX1, destY1, destX2, destY2, srcX1, srcY1, srcX2, srcY2, null); //draw raster lines

        //minimap
        g2d.setColor(Color.BLACK);
        g2d.fillRect(955, 0, 245,245);
        g2d.drawImage(background, 950, 0, 1200, 250, srcX1, srcY1, srcX2, srcY2, null);

        //GUI
        g2d.drawImage(gui, 0,0,1200,1000,0,0,1200,1000, null);

    }

    public void moveBackground(String direction) {
        movementSpeed = settings.getMovespeed();

        switch (direction) {
            case "Up":
                srcY1 -= movementSpeed; srcY2 -= movementSpeed;
                break;
            case "Down":
                srcY1 += movementSpeed; srcY2 += movementSpeed;
                break;
            case "Left":
                srcX1 -= movementSpeed; srcX2 -= movementSpeed;
                break;
            case "Right":
                srcX1 += movementSpeed; srcX2 += movementSpeed;
                break;
            default:
                break;
        }
    }
}
