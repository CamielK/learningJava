package GameEngine;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Camiel on 21-Nov-15.
 */
public class GameThread extends JPanel implements Runnable{

    private final Game game;
    private boolean running = false;

    int frames = 0;

    public GameThread(Game game) {
        this.game = game;
        setFocusable(true);
        running = true;
    }

    @Override
    public void run() {
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

//            try { Thread.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
//
//            if (shouldRender) {
//                frames++;
//            }

            if (System.currentTimeMillis() - lastTimer >= 1000) { //update counters when second passes
                lastTimer += 1000;
                System.out.println(frames + ", " + ticks);
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
        //uncomment to limit framerate
        try { Thread.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
        frames++;

        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (game.getScreenFactory().getCurrentScreen() != null) {
            game.getScreenFactory().getCurrentScreen().onDraw(g2d);
        }
        repaint();
    }

}
