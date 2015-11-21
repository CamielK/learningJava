  /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sidescrollergame;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Camiel
 */
public class SideScrollerGame {
    
    //static String difficulty = "hard";
    static String difficulty;
    static StartMenu menu = new StartMenu();
    
    public static void main(String[] args) throws InterruptedException {
        
        System.out.println("starting game..");
        restart();
//        MainGameClass game = new MainGameClass();
//        game.startGame();
    }
    
    public static void playGame(){
        // call startmenu
        System.out.println("playing..");
        //StartMenu menu = new StartMenu();
        //new StartMenu().reset();
        menu.setVisible(true);
        
        
        //check if startmenu is done, continue when difficulty is selected
        while (difficulty == null) {
            difficulty = menu.getDifficulty();
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                Logger.getLogger(SideScrollerGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        menu.setVisible(false);
        
        try {
            //start a new game with the selected difficulty
            new GameWindow().play(difficulty);
        } catch (InterruptedException ex) {
            Logger.getLogger(SideScrollerGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void restart() {
        System.out.println("restarting");
        difficulty = null;
        new GameWindow().reset();
        menu.reset();
        new Player().reset();
        new Field().reset();
        playGame();
        
        
    }
}
