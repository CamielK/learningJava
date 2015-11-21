/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sidescrollergame;

import static java.awt.Color.white;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 *
 * @author Camiel
 * 
 * date: 9-2015
 * 
 * Field forms the background, floor and moving objects aswell as checking for player collisions.
 */


public class Field {
    
    
    //----- Field parameters -----//
    
    //object parameters:
    static int scrollSpeed = 5; //default movement speed of objects (in pixels per tick)
    static Point object1 = new Point(1000,375) //stores coordinates of the objects. max 3 objects in view at once
            ,object2 = new Point(1000,375)
            ,object3 = new Point(1000,375);
    static boolean object1moving, object2moving, object3moving; //true when moving, false when not used
    
    //background image parameters:
    private final String backgroundFileName = "images/backgroundWhite.png";
    private Image background;
    static int backgroundScrollSpeed = 5; //ticks per new draw
    static int backgroundCounter = 0; //used to count delay between background movement
    static int srcX1 = 0, srcY1 = 0, srcX2 = 1000, srcY2 = 500; // default source window of background
    static final int destX1 = 0, destY1 = 0, destX2 = 1000, destY2 = 500; // destination coordiantes of background
    
    //spawn parameters:
    static int maxSpawnChance = 1000; // object spawns if random value between MIN_SPAWN_CHANCE and MAX_SPAWN_CHANCE is greater than SPAWN_VALUE. (happens 50 times per second)
    static int minSpawnChance = 0;
    static int spawnValue = 985; 
    static int spawnTimer = 0; //used to count delay between object spawns
    
    //collision parameters:
    static boolean collision = false;
    Player player = new Player(); //player instance to check collisions
    static int collisionDelay = 0; //delay between hit registration (in ticks)
    
    //----- end parameters -----//
    
    
    
    //paints all relevant field objects to JFrame
    public void paint (Graphics g) {
        //background:
        URL imgUrl = getClass().getClassLoader().getResource(backgroundFileName);
        if (imgUrl == null) {
            System.err.println("Couldn't find file: " + backgroundFileName);
        } else {
            try {
                background = ImageIO.read(imgUrl);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        g.drawImage(background, destX1, destY1, destX2, destY2, srcX1, srcY1, srcX2, srcY2, null);
        
        g.fillRect(0, 400, 1000, 100); //floor
        g.fillRect(object1.x,object1.y,10,10);
        g.fillRect(object2.x,object2.y,10,10);
        g.fillRect(object3.x,object3.y,10,10);
        
        g.setColor(white); g.drawString("use spacebar to jump!", 400, 450);
    }
    
    
    
    //updates all parameters with calls to their update functions
    public void update () {
        moveLeft();
        spawnControl();
        checkCollisions();
    }
    
    //moves objects and background left at their relative speeds
    private void moveLeft () {
        if (object1moving) { object1.x -= scrollSpeed;}
        if (object2moving) { object2.x -= scrollSpeed;}
        if (object3moving) { object3.x -= scrollSpeed;}
        
        backgroundCounter++;
        if (backgroundCounter >= backgroundScrollSpeed) {
            srcX1 += 1; srcX2 += 1;
            backgroundCounter = 0;
        }
    }
    
    //checks for object resets and spawns new objects
    private void spawnControl () {
        //check for object despawns:
        if (object1.x <= (0-scrollSpeed)) {object1.x = 1000; object1moving = false;}
        if (object2.x <= (0-scrollSpeed)) {object2.x = 1000; object2moving = false;}
        if (object3.x <= (0-scrollSpeed)) {object3.x = 1000; object3moving = false;}
        
        //spawn a new object if conditions are met:
        if (spawnTimer > 0) {spawnTimer--;}
        Random rand = new Random();
        int spawn = rand.nextInt((maxSpawnChance - minSpawnChance) + 1) + minSpawnChance;
        if (spawn >= spawnValue && spawnTimer == 0) { //spawns an object if at least 1 second has passed and random value is above treshold
            if (!object1moving) { object1moving = true; spawnTimer = 40;}
            else if (!object2moving) { object2moving = true; spawnTimer = 40;}
            else if (!object3moving) { object3moving = true; spawnTimer = 40;}
            //no object is spawned if all 3 objects are already moving
        }
    }
    
    //checks for collisions between player and objects
    private void checkCollisions () {
        if (collisionDelay == 0) {
            Point playerPos = player.getPos(); //returns bottom right corner player coordiantes
            if (playerPos.x >= (object1.x-3) && playerPos.x <= (object1.x+12) && playerPos.y >= object1.y) { collision = true; } //check if player is within range of object and above/below it (collision)
            else if (playerPos.x >= (object2.x-3) && playerPos.x <= (object2.x+12) && playerPos.y >= object2.y) { collision = true; } 
            else if (playerPos.x >= (object3.x-3) && playerPos.x <= (object3.x+12) && playerPos.y >= object3.y) { collision = true; } 
            if (collision) { player.decreaseLife(); collisionDelay = 10; collision = false;}
            
        }
        else if (collisionDelay > 0) {collisionDelay--;}
    }

    public void reset() {
        System.out.println("resetting Field..");
        scrollSpeed = 5;
        object1 = new Point(1000,375); object2 = new Point(1000,375); object3 = new Point(1000,375);
        object1moving = false; object2moving = false; object3moving = false;
        backgroundScrollSpeed = 5; //ticks per new draw
        backgroundCounter = 0; //used to count delay between background movement
        srcX1 = 0; srcY1 = 0; srcX2 = 1000; srcY2 = 500; // default source window of background
        // final? destX1 = 0; destY1 = 0; destX2 = 1000; destY2 = 500; // destination coordiantes of background
        collision = false;
        player = new Player();
        collisionDelay = 0; //delay between hit registration (in ticks)
    }
}
