package Game;

import GameEngine.Game;

import java.awt.*;

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

//        double aboutStanding,adjacent;
//        double angle = 1;
//        int weaponPixelOffset = 17;
//
//        aboutStanding = Math.sin(Math.toRadians(angle)) * weaponPixelOffset;
//        adjacent = Math.cos(Math.toRadians(angle)) * weaponPixelOffset;
//
//        double correctionAngle = Math.toDegrees(Math.atan2(300, weaponPixelOffset));
//        double rotationOfsset = 90 - correctionAngle;
//
//        //System.out.println(aboutStanding + ":" + adjacent + "   >   " + Math.cos(63));
//        System.out.println(correctionAngle);
//


//        int a = 50;
//        int b = -20;
//        a += b;
//
//        Point l1p1 = new Point(5,8);
//        Point l1p2 = new Point(10,2);
//        Point l2p1 = new Point(15,5);
//        Point l2p2 = new Point(20,5);
//
//        float q = (l1p1.y - l2p1.y) * (l2p2.x - l2p1.x) - (l1p1.x - l2p1.x) * (l2p2.y - l2p1.y);
//        float d = (l1p2.x - l1p1.x) * (l2p2.y - l2p1.y) - (l1p2.y - l1p1.y) * (l2p2.x - l2p1.x);
//
//        if (d == 0) {
//            System.out.println("false1");
//        }
//
//        float r = q / d;
//
//        q = (l1p1.x - l2p1.y) * (l1p2.x - l1p1.x) - (l1p1.x - l2p1.x) * (l1p2.y - l1p1.y);
//        float s = q / d;
//
//        if (r < 0 || r > 1 || s < 0 || s > 1) {
//            System.out.println("false2");
//        }
//        else {
//            System.out.println("true");
//        }





        new MyGame();
    }


}
