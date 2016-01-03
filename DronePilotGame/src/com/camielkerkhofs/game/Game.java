package com.camielkerkhofs.game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Created by Camiel on 02-Dec-15.
 */
public class Game extends JPanel implements Runnable{

    private static final long serialVersionUID = 1L;

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 1000;
    public static final int SCALE = 1;
    public static final String NAME = "Drone Pilot Game";

    private JFrame frame;

    public boolean running = false;
    public int tickCount = 0;

    public Game() {
        setMinimumSize(new Dimension(WIDTH* SCALE, HEIGHT * SCALE));
        setMaximumSize(new Dimension(WIDTH* SCALE, HEIGHT * SCALE));
        setPreferredSize(new Dimension(WIDTH* SCALE, HEIGHT * SCALE));

        frame = new JFrame(NAME);

        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.add(this, BorderLayout.CENTER);
        frame.pack();

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public synchronized void start() {
        running = true;
        new Thread(this).start();
    }

    public synchronized void stop() {

    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D/60D; // 1 billion nanoseconds are in a second. 60 fps

        int ticks = 0;
        int frames = 0;

        long lastTimer = System.currentTimeMillis();
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime)/nsPerTick;
            lastTime = now;
            boolean shouldRender = true;

            while (delta >= 1) {
                ticks++;
                tick();
                delta -= 1;
                shouldRender = true;
            }

            try { Thread.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
            if (shouldRender) {
                frames++;
                render();
            }

            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;
                System.out.println(frames + ", " + ticks);
                frames = 0;
                ticks = 0;
            }
        }
    }

    public void tick() {
        tickCount++;
    }

    public void render() {
    }

    public static void main(String[] args) {
        new Game().start();
    }
}

