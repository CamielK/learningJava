package GameEngine;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Camiel on 21-Nov-15.
 */
public abstract class Screen {

    private final ScreenFactory screenFactory;
    private final JFrame window;

    public Screen(ScreenFactory screenFactory, JFrame window) {
        this.screenFactory = screenFactory;
        this.window = window;
    }

    public abstract void onCreate();

    public abstract void onUpdate();

    public abstract void onDraw(Graphics2D g2d);

    public ScreenFactory getScreenFactory() {
        return screenFactory;
    }

    public JFrame getWindow() { return window; }

}
