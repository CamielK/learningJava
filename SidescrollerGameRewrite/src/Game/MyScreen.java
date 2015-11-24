package Game;

import GameEngine.Screen;
import GameEngine.ScreenFactory;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Camiel on 21-Nov-15.
 */
public class MyScreen extends Screen {

    private int x = 0, y = 0;

    public MyScreen(ScreenFactory screenFactory) {
        super(screenFactory);
    }

    @Override
    public void onCreate() {
        System.out.println("Creating!");
    }

    @Override
    public void onUpdate() {

        //move rect with arrow keys

        if (getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_LEFT)) {
            x -= 2;
        }
        if (getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_RIGHT)) {
            x += 2;
        }
        if (getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_UP)) {
            y -= 2;
        }
        if (getScreenFactory().getGame().getKeyboardListener().isKeyPressed(KeyEvent.VK_DOWN)) {
            y += 2;
        }

        if (y >= 600-64) {
            y =  600-64;
        }
        if (y <= 0) {
            y =  0;
        }
        if (x >= 800-64) {
            x =  800-64;
        }
        if (x <= 0) {
            x =  0;
        }
    }

    @Override
    public void onDraw(Graphics2D g2d) {
        g2d.setColor(Color.black);
        g2d.fillRect(x, y, 64, 64);
    }
}
