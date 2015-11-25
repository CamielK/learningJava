package Game;

/**
 * Created by Camiel on 25-Nov-15.
 */
public class Bullet {

    private int bulletX;
    private int bulletY;
    private double bulletDirection;
    private int lifetime;

    public Bullet(int bulletX, int bulletY, double bulletDirection, int lifetime) {
        this.bulletX = bulletX;
        this.bulletY = bulletY;
        this.bulletDirection = bulletDirection;
        this.lifetime = lifetime;
    }


    public int getLifetime() {
        return lifetime;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    public int getBulletX() {
        return bulletX;
    }

    public void setBulletX(int bulletX) {
        this.bulletX = bulletX;
    }

    public int getBulletY() {
        return bulletY;
    }

    public void setBulletY(int bulletY) {
        this.bulletY = bulletY;
    }

    public double getBulletDirection() {
        return bulletDirection;
    }

    public void setBulletDirection(double bulletDirection) {
        this.bulletDirection = bulletDirection;
    }







}
