package GameEngine;

/**
 * Created by Camiel on 21-Nov-15.
 */
public class ScreenFactory {

    private final Game game;
    private Screen screen;

    public ScreenFactory(Game game) {
        this.game = game;
    }

    public void showScreen(Screen screen) {
        this.screen = screen;
        this.screen.onCreate();
    }

    public Screen getCurrentScreen() {
        return screen;
    }

    public Game getGame() {
        return game;
    }

}
