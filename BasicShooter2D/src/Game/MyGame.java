package Game;

import GameEngine.Game;

/**
 * Created by Camiel on 21-Nov-15.
 */
public class MyGame {

    private Game game;

    public MyGame() {
        game = new Game(1200, 1000, "Shooter");
        game.getScreenFactory().showScreen(new GameUpdater(game.getScreenFactory()));
    }

    public static void main (String[] args) {





        new MyGame();
    }


}
