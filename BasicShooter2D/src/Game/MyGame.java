package Game;

import GameEngine.Game;

/**
 * Created by Camiel on 21-Nov-15.
 */
public class MyGame {

    private Game game;
    private GlobalSettings settings =  new GlobalSettings();

    public MyGame() {
        game = new Game(settings.getScreenWidth(), settings.getScreenHeight(), "Shooter");
        game.getScreenFactory().showScreen(new GameUpdater(game.getScreenFactory(), game.getWindow()));
    }

    public static void main (String[] args) {
//
//        double aboutStanding,adjacent;
//        double angle = 1;
//        int weaponPixelOffset = 17;
//
//        aboutStanding = Math.sin(Math.toRadians(angle)) * weaponPixelOffset;
//        adjacent = Math.cos(Math.toRadians(angle)) * weaponPixelOffset;
//
//        System.out.println(aboutStanding + ":" + adjacent + "   >   " + Math.cos(63));
//

        new MyGame();
    }


}
