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
import javax.imageio.ImageIO;

/**
 *
 * @author Camiel
 */
public class Player {
    
    static int lives = 3;
    
    private String spriteImgFileName = "images/stickmanSprite.png";
    private String fullLiveImgFileName = "images/fullLive.png";
    private String emptyLiveImgFileName = "images/emptyLive.png";
    static boolean firstRun = true;
    private Image spriteImg, emptyLiveImg, fullLiveImg;
    static int animationStep = 0;
    static int animationSpeed = 8; //ticks per new draw
    
    static int srcX1 = 0, srcY1 = 64, srcX2 = 32, srcY2 = 128; //original sprite size = 32x64
    static int destX1 = 200, destY1 = 314, destX2 = 248, destY2 = 410; //expand image by 1.5 to 48x96
    
    static boolean jumping = false;
    static int jumpCounter = 0;
    
    
    
    public void update () {
        
        if (!jumping) {
            animationStep++;
            if (animationStep >= ((50/animationSpeed)*8)) {animationStep = 0; srcX1 = 0; srcX2 = 32;} //8 different frames

            if (animationStep%(50/animationSpeed) == 0) {
                srcX1 += 32; 
                srcX2 += 32;
            }
        }
        else if (jumping) {
            srcX1 = 128; srcX2 = 160;
            if (jumpCounter >=0 && jumpCounter <=20) {destY1 -= 5;destY2 -= 5;}
            else if (jumpCounter >20 && jumpCounter <=25) {destY1 -= 2;destY2 -= 2 ;}
            else if (jumpCounter >25 && jumpCounter <=30) {destY1 += 2;destY2 += 2 ;}
            else if (jumpCounter >30 && jumpCounter <=50) {destY1 += 5;destY2 += 5 ;}
            
            jumpCounter++;
            if (jumpCounter > 50) {jumpCounter = 0; jumping = false; destY1 = 314; destY2 = 410; animationStep = 0; srcX1 = 0; srcX2 = 32;}
        } 
    }
    
    
    
    public void paint (Graphics g) {
        if (firstRun) {
            firstRun = false;
            
            //load stickman spritesheet
            URL imgUrl = getClass().getClassLoader().getResource(spriteImgFileName);
            if (imgUrl == null) { System.err.println("Couldn't find file: " + spriteImgFileName); } 
            else { try { spriteImg = ImageIO.read(imgUrl); } catch (IOException ex) { ex.printStackTrace(); }}
            
            //load emptyLive img
            imgUrl = getClass().getClassLoader().getResource(emptyLiveImgFileName);
            if (imgUrl == null) { System.err.println("Couldn't find file: " + emptyLiveImgFileName); } 
            else { try { emptyLiveImg = ImageIO.read(imgUrl); } catch (IOException ex) { ex.printStackTrace(); }}
            
            //load fullLive img
            imgUrl = getClass().getClassLoader().getResource(fullLiveImgFileName);
            if (imgUrl == null) { System.err.println("Couldn't find file: " + fullLiveImgFileName); } 
            else { try { fullLiveImg = ImageIO.read(imgUrl); } catch (IOException ex) { ex.printStackTrace(); }}
            
        }
        
        
        g.drawImage(spriteImg, destX1, destY1, destX2, destY2, srcX1, srcY1, srcX2, srcY2, null); 
        
        g.setColor(white); g.drawString("lives: ", 50, 430); //lives
        
        int Xcor = 90;
        for (int f = 1; f <= lives; f++) {
            g.drawImage(fullLiveImg, Xcor, 400, null);
            Xcor += 80;
        }
        for (int k = 3; k > lives; k--) {
            g.drawImage(emptyLiveImg, Xcor, 400, null);
            Xcor += 80;
        }
        
        
        
        
    }
    
    
    
    public void jump () {
        jumping = true;
    }
    
    
    public Point getPos() {
        Point playPos = new Point(destX2, destY2);
        return playPos;
    }
    
    
    public void decreaseLife () {
        lives--;
        System.out.println("hit!");
        if (lives == 0) { new GameWindow().gameOver(); }
    }
    
    
    public int getLives () {
        return lives;
    }

    public void reset() {
        System.out.println("resetting Player..");
        lives = 3;
        firstRun = true;
        animationStep = 0;
        animationSpeed = 8;
        srcX1 = 0; srcY1 = 64; srcX2 = 32; srcY2 = 128; //original sprite size = 32x64
        jumping = false;
        jumpCounter = 0;
    }
}
