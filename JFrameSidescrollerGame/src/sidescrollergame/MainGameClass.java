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
public class MainGameClass {
    
    //static String difficulty = "hard";
    static String difficulty;
    
    public void startGame(){
        // call startmenu
        StartMenu menu = new StartMenu(); 
        menu.setVisible(true);
        
        
        //check if startmenu is done, continue when difficulty is selected
        while (difficulty == null) {
            difficulty = menu.getDifficulty();
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainGameClass.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        try {
            //start a new game with the selected difficulty
            new GameWindow().play(difficulty);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainGameClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void restart() {
        difficulty = null;
        this.startGame();
        System.out.println("restarting..");
    }

}
