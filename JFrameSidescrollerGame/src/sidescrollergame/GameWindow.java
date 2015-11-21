package sidescrollergame;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 *
 * @author Camiel
 * @date august 2015
 * 
 * discription: this class represents the central game control. 
 * tasks include drawing the main window and keeping track of game ticks, executing all operations needed per game tick
 * also checks for user spacebar input.
 * 
 */
public class GameWindow extends JPanel {
    
    
    private Player player = new Player();
    private Field field = new Field();
    static boolean gameOver = false;
    
    @Override
    public void paintComponent(Graphics g) { //call to paint all game objects
        super.paintComponent(g);
        field.paint(g);
        player.paint(g);
    }
    
    public void update () { //calls update functions to update all object variables
        field.update();
        player.update();
    }
    
    public void play (String difficulty) throws InterruptedException { //main game method
        JFrame frame = new JFrame("sidescroller");
        frame.setSize(1000, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        GameWindow game = new GameWindow();
        frame.add(game);
        frame.add(this);
        
        //initiate key binding for spacebar (jump when pressed)
        Action jump = new AbstractAction () {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.jump();
                System.out.println("jump");
            }
        };
        this.getInputMap().put(KeyStroke.getKeyStroke("SPACE"),"pressed");
        this.getActionMap().put("pressed", jump);
        
        int overallSpeed = 20; //at 50, 75 or 100 fps >> 20, 15 or 10 /1000 ms)
        if (difficulty.equals("hard")) {overallSpeed = 10;}
        else if (difficulty.equals("normal")) {overallSpeed = 15;}
        else if (difficulty.equals("easy")) {overallSpeed = 20;}
        
        while (!gameOver) { //game tick
            update();
            frame.repaint();
            Thread.sleep(overallSpeed);
        }
        //frame.dispose();
        frame.setVisible(false);
    }
    
    public void gameOver () { //not yet implemented //show gameover menu when lives run out, possibly starting new game?
        gameOver = true;
        GameOverScreen gameOverMenu = new GameOverScreen(); 
        gameOverMenu.setVisible(true);
        System.out.println("game over");
    }
    
    public void reset() {
        System.out.println("resetting gamewindow..");
        gameOver = false;
    }
}
