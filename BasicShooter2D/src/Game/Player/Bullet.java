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

    public Bullet(int bulletX, int bulletY, double bulletDirection) {
        this.bulletX = bulletX;
        this.bulletY = bulletY;
        this.bulletDirection = bulletDirection;
        //System.out.println("bullet spawn (x:y:direction) = " + bulletX + ":" + bulletY + ":" + bulletDirection+ ")");
    }


    public void drawBullet(Graphics2D g2d) {
        //TODO screenP1 can be called from MapCoordinateTranslator
        //translate map positions to screen positions
        //draw bullet //subtract screen X1 from bullet X and screen Y1 from bullet Y to get current screen position

        Point screenPos = mapCoordinateTranslator.getScreenPoint(new Point((int)bulletX,(int)bulletY));
        g2d.fillRect((int) screenPos.getX(), (int) screenPos.getY(), 5, 5);
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

        double xMovePercentage = 0, yMovePercentage = 0;

        //System.out.println(bulletDirection);

        if (bulletDirection >= 0 && bulletDirection <=90) { //south-east
            xMovePercentage = 1 - (bulletDirection / 90);
            yMovePercentage = (bulletDirection / 90);
            bulletX += (bulletSpeed * xMovePercentage);
            bulletY += (bulletSpeed * yMovePercentage);
        }
        else if (bulletDirection >= 90 && bulletDirection <= 180) { //south-west
            bulletDirection -= 90;
            xMovePercentage = (bulletDirection / 90);
            yMovePercentage = 1 - (bulletDirection / 90);
            bulletX -= (bulletSpeed * xMovePercentage);
            bulletY += (bulletSpeed * yMovePercentage);
            bulletDirection += 90;
        }
        else if (bulletDirection <=0 && bulletDirection >= -90) { //north-east
            xMovePercentage = 1 - (Math.abs(bulletDirection / 90));
            yMovePercentage = (Math.abs(bulletDirection / 90));
            bulletX += (bulletSpeed * xMovePercentage);
            bulletY -= (bulletSpeed * yMovePercentage);
        }
        else if (bulletDirection <= -90 && bulletDirection >= -180) { //north-west
            bulletDirection += 90;
            xMovePercentage = (Math.abs(bulletDirection / 90));
            yMovePercentage = 1 - (Math.abs(bulletDirection / 90));
            bulletX -= (bulletSpeed * xMovePercentage);
            bulletY -= (bulletSpeed * yMovePercentage);
            bulletDirection -= 90;
        }



        boolean collision = false;
        collision = collisionChecker.blockMapCollision(new Rectangle((int) bulletX - 5, (int) bulletY - 5, 15, 15)); //bullets are 5x5, this checks for the 15x15 area around the bullet
        if (collision) {
            expiredStatus = true;
        }
    }

    public boolean isExpired() {
        return expiredStatus;
    }
}
