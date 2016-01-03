package Game;

import java.awt.*;
import java.util.Iterator;

/**
 * Created by Camiel on 25-Nov-15.
 */
public class Bullet {

    private double bulletX;
    private double bulletY;
    private double bulletDirection;
    private int lifetime;
    private int maxBulletLifetime = 90; //1,5 seconds
    private int bulletSpeed = 20;
    private boolean expiredStatus = false;
    private static CollisionChecker collisionChecker = new CollisionChecker();

    public Bullet(int bulletX, int bulletY, double bulletDirection, int lifetime) {
        this.bulletX = bulletX;
        this.bulletY = bulletY;
        this.bulletDirection = bulletDirection;
        //System.out.println(bulletDirection);
        this.lifetime = lifetime;
    }


    public void drawBullet(Graphics2D g2d, double screenX1, double screenY1) {
        //translate map positions to screen positions
        //draw bullet //subtract screen X1 from bullet X and screen Y1 from bullet Y to get current screen position
        g2d.fillRect((int) (bulletX - screenX1), (int) (bulletY - screenY1), 5, 5);
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
