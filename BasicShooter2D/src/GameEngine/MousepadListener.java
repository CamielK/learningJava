package GameEngine;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by Camiel on 21-Nov-15.
 */
public class MousepadListener implements MouseListener {

    private int mouseX, mouseY;
    private boolean clicked;

    @Override
    public void mouseClicked(MouseEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();
        clicked = true;
        //System.out.println("mouseX: " + mouseX + ". mouseY: " + mouseY);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        mouseClicked(event);
        //System.out.println("mouse clicked");
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        clicked = false;
        //System.out.println("mouse released");
    }

    @Override
    public void mouseEntered(MouseEvent event) {

    }

    @Override
    public void mouseExited(MouseEvent event) {

    }

    public boolean isMousePressed() {
        //System.out.println(clicked);
        return clicked;

    }

    public int getX() {
        return mouseX;
    }

    public int getY() {
        return mouseY;
    }

}
