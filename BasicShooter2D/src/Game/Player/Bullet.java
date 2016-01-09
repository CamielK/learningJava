package Game.Player;

import Game.Map.CollisionChecker;
import Game.Map.MapCoordinateTranslator;

import java.awt.*;

/**
 * Created by Camiel on 25-Nov-15.
 */
public class Bullet {

    private double bulletX;
    private double bulletY;
    private double bulletDirection;
    private int lifetime = 0;
    private final int maxBulletLifetime = 45; //0.75 seconds
    private final int bulletSpeed = 60;
    private boolean expiredStatus = false;
    private static final CollisionChecker collisionChecker = new CollisionChecker();
    private static final MapCoordinateTranslator mapCoordinateTranslator = new MapCoordinateTranslator();

    private Point spawnPoint;
    private Point trajectoryEnd;

    public Bullet(int bulletX, int bulletY, double bulletDirection) {
        this.bulletX = bulletX;
        this.bulletY = bulletY;
        this.bulletDirection = bulletDirection;
        //System.out.println("bullet spawn (x:y:direction) = " + bulletX + ":" + bulletY + ":" + bulletDirection+ ")");

        spawnPoint = new Point(bulletX,bulletY);
        trajectoryEnd = calculateNextPosition(spawnPoint, 400);
    }


    public void drawBullet(Graphics2D g2d) {
        //translate map positions to screen positions

        //draw bullet
        if (lifetime >= 2) { //dont draw the first 2 times
            Point screenPos = mapCoordinateTranslator.getScreenPoint(new Point((int)bulletX,(int)bulletY));
            g2d.fillRect((int) screenPos.getX(), (int) screenPos.getY(), 5, 5);
        }

        //draw trajectory
//        g2d.setColor(Color.RED);
//        Point spawnPointOnScreen = mapCoordinateTranslator.getScreenPoint(spawnPoint);
//        Point endPointOnScreen = mapCoordinateTranslator.getScreenPoint(trajectoryEnd);
//        g2d.drawLine((int)spawnPointOnScreen.getX(),(int)spawnPointOnScreen.getY(),(int)endPointOnScreen.getX(),(int)endPointOnScreen.getY());
//        g2d.setColor(Color.BLACK);
    }

    public int getLifetime() {
        return lifetime;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    public double getBulletX() {
        return bulletX;
    }

    public void setBulletX(int bulletX) {
        this.bulletX = bulletX;
    }

    public double getBulletY() {
        return bulletY;
    }

    public void setBulletY(int bulletY) {
        this.bulletY = bulletY;
    }

    public double getBulletDirection() {
        return bulletDirection;
    }

    public void setBulletDirection(float bulletDirection) {
        this.bulletDirection = bulletDirection;
    }

    public void updateBullet() {
        //returns true when updated, false when bullet lifetime has expired
        lifetime++;
        if (lifetime >= maxBulletLifetime) { expiredStatus = true; }

        Point oldPosition = new Point((int)bulletX, (int)bulletY);
        Point newPosition = calculateNextPosition(oldPosition, bulletSpeed);
        bulletX = (int) newPosition.getX();
        bulletY = (int) newPosition.getY();

        boolean collision = false;
        collision = collisionChecker.blockMapCollision(new Rectangle((int) bulletX - 5, (int) bulletY - 5, 15, 15)); //bullets are 5x5, this checks for the 15x15 area around the bullet
        if (collision) {
            expiredStatus = true;
        }
    }

    public boolean isExpired() {
        return expiredStatus;
    }

    private Point calculateNextPosition(Point oldPos, int speed){
        int newX = (int)oldPos.getX(), newY = (int)oldPos.getY();
        double adjacent = 0, aboutStanding = 0;

        aboutStanding = Math.sin(Math.toRadians(bulletDirection)) * speed;
        adjacent = Math.cos(Math.toRadians(bulletDirection)) * speed;

        newY += aboutStanding;
        newX += adjacent;

        return new Point(newX, newY);
    }
}
