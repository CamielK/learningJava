package GameEngine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Camiel on 21-Nov-15.
 */
public class KeyboardListener implements KeyListener {

    private boolean[] keys = new boolean[256];

    @Override
    public void keyTyped(KeyEvent event) {
    }

    @Override
    public void keyPressed(KeyEvent event) {
        keys[event.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent event) {
        keys[event.getKeyCode()] = false;
    }

    public boolean isKeyPressed(int key) {
        return keys[key];
    }

    public boolean isKeyReleased(int key) {
        return !keys[key];
    }

}
