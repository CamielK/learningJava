package Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Created by Camiel on 24-Nov-15.
 */
public class Player {

    public int playerXonMap = 568, playerYonMap = 468;
    public final int playerXonScreen = 568, playerYonScreen = 468;
    public double rotation = Math.toRadians (90);
    //public double rotationDegrees = 90;
    private int movementSpeed = 3;


    private List<Bullet> bullets = new ArrayList<Bullet>();
    private int maxBulletLifetime = 300;
    private int bulletSpeed = 1;
    private int counter = 0;

    //player image creation:
    private final String playerFileName = "Resources/player.png";
    private BufferedImage playerImg;
    public Player(){
        URL imgUrl = getClass().getClassLoader().getResource(playerFileName);
        if (imgUrl == null) { System.err.println("Couldn't find file: " + playerFileName); }
        else {
            try { playerImg = ImageIO.read(imgUrl); } catch (IOException ex) { ex.printStackTrace(); }
        }
    }
    public void drawPlayer (Graphics2D g2d) {
        AffineTransform tx = AffineTransform.getRotateInstance(rotation, 32, 32);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        g2d.drawImage(op.filter(playerImg, null), playerXonScreen, playerYonScreen, null);

        //minimap player:
        g2d.fillRect(1070,120,10,10);

        //draw and update bullets

        for (int i = 0; i < bullets.size(); i++) {
            //get old bullet info
            int oldX = bullets.get(i).getBulletX();
            int oldY = bullets.get(i).getBulletY();
            float angle = (float) bullets.get(i).getBulletDirection();
            int oldLifetime = bullets.get(i).getLifetime();


            //draw bullet
            g2d.fillRect(oldX,oldY,5,5);
            //System.out.println(angle);


            //update bullet position (at 1/10th of the framerate)
            if (counter == 10) {
                double newBulletX, newBulletY, newLifetime;
                newLifetime = oldLifetime + 1;
                newBulletX = oldX + (bulletSpeed * Math.sin(angle));
                newBulletY = oldY + (bulletSpeed * Math.cos(angle));

                bullets.get(i).setBulletX((int) newBulletX);
                bullets.get(i).setBulletY((int) newBulletY);
                bullets.get(i).setLifetime((int) newLifetime);
                counter = 0;
            }
            else {
                counter++;
            }
        }

        //check for end of lifetime on bullets
        for (Iterator<Bullet> iter = bullets.listIterator(); iter.hasNext(); ) {
            if (iter.next().getLifetime() >= maxBulletLifetime) {
                iter.remove();
            }
        }
    }

    public void setRotation (Point mouseLocation) { //calculate angle mouseLocation from center
        Point center =GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        double xDistance = mouseLocation.getX() - center.getX();
        double yDistance = mouseLocation.getY() - center.getY();
        double degrees = Math.toDegrees(Math.atan2(yDistance, xDistance));
        rotation = Math.toRadians (degrees+90);
        //rotationDegrees = degrees;
        //System.out.println(degrees);
    }

    public void movePlayer(String direction) {
        switch (direction) {
            case "Up":
                playerYonMap -= movementSpeed;
                moveBullets("Up");
                break;
            case "Down":
                playerYonMap += movementSpeed;
                moveBullets("Down");
                break;
            case "Left":
                playerXonMap -= movementSpeed;
                moveBullets("Left");
                break;
            case "Right":
                playerXonMap += movementSpeed;
                moveBullets("Right");
                break;
            default:
                break;
        }
    }

    public void fireWeapon() {

        bullets.add(new Bullet(600,500,rotation,0)); //add new bullet
    }

    private void moveBullets(String direction) {
        for (int i = 0; i < bullets.size(); i++) {
            //get old bullet info
            int oldX = bullets.get(i).getBulletX();
            int oldY = bullets.get(i).getBulletY();

            double newBulletX = oldX, newBulletY = oldY;

            switch (direction) {
                case "Up":
                    newBulletY -= movementSpeed;
                    break;
                case "Down":
                    newBulletY += movementSpeed;
                    break;
                case "Left":
                    newBulletX -= movementSpeed;
                    break;
                case "Right":
                    newBulletX += movementSpeed;
                    break;
                default:
                    break;
            }

            bullets.get(i).setBulletX((int) newBulletX);
            bullets.get(i).setBulletY((int) newBulletY);
        }
    }
}
