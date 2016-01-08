package GameEngine;

import Game.Gfx.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Camiel on 21-Nov-15.
 */
public class GameThread extends JPanel implements Runnable{

    private final Game game;
    private boolean running = false;
    private static int lastFrameCount = 0;

    int frames = 0;

    private static BufferedImage loadingScreen, loadingDot;
    private static long lastLoadUpdateTime = System.currentTimeMillis();

    public GameThread(Game game) {
        this.game = game;
        setFocusable(true);
        running = true;
    }

    @Override
    public void run() {
        loadingScreen = new ImageLoader().loadImage("Resources/loading.png");
        loadingDot = new ImageLoader().loadImage("Resources/loadDot.png");

        long lastTime = System.nanoTime(); //save start time in nanoseconds since epoch
        double nsPerTick = 1000000000D/60D; //nano seconds per tick (60TPS)

        int ticks = 0;
        frames = 0;

        long lastTimer = System.currentTimeMillis(); //save start time in milliseconds
        double delta = 0;

        while (running) {
            long now = System.nanoTime(); //save current time in nanoseconds
            delta += (now-lastTime)/nsPerTick; //get difference between start time and current time. devide by nsPerTick to get the passed ammount of a tick. this is added to delta
            lastTime = now; //update last run time to current run time

            while (delta >= 1) { //when delta becomes greater than 1, a tick has passed
                ticks++;
                tick();
                delta -= 1;
            }

            if (System.currentTimeMillis() - lastTimer >= 1000) { //update counters when second passes
                lastTimer += 1000;
                System.out.println(frames + ", " + ticks);
                lastFrameCount = frames;
                frames = 0;
                ticks = 0;
            }
        }
    }

    public void tick() {
        //update game logic
        if (game.getScreenFactory().getCurrentScreen() != null) {
            game.getScreenFactory().getCurrentScreen().onUpdate();
        }
    }

    public void paint(Graphics g) {

        frames++;
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (game.getScreenFactory().getCurrentScreen() != null) {
            game.getScreenFactory().getCurrentScreen().onDraw(g2d);
        }
        else {
            long timeDiff = System.currentTimeMillis() - lastLoadUpdateTime;
            g2d.drawImage(loadingScreen , 0, 0, null); //loading screen
            if (timeDiff >= 200) g2d.drawImage(loadingDot , 475, 700, null); //loading screen
            if (timeDiff >= 400) g2d.drawImage(loadingDot , 575, 700, null); //loading screen
            if (timeDiff >= 600) g2d.drawImage(loadingDot , 675, 700, null); //loading screen
            if (timeDiff >= 800) lastLoadUpdateTime = System.currentTimeMillis();
        }
        repaint();



        //dynamicly limits sleeptime when performance drops
        int sleeptime = 6;
        if (lastFrameCount < 120) {sleeptime = 3;}
        else if (lastFrameCount < 90) {sleeptime = 1;}
        else if (lastFrameCount < 60) {sleeptime = 0;}
        try { Thread.sleep(sleeptime); } catch (InterruptedException e) { e.printStackTrace(); }

    }



}
