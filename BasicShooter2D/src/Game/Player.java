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

    public int playerXonMap = 1268, playerYonMap = 1168;
    public final int playerXonScreen = 568, playerYonScreen = 468;
    public double rotation = Math.toRadians (90);
    public String orientation = "right";
    public double rotationDegrees = 90;


    private int movementSpeed = 3;

    private List<Bullet> bullets = new ArrayList<Bullet>();
    private int maxBulletLifetime = 500;
    private int bulletSpeed = 5;
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
        //minimap player:
        g2d.fillRect(1070,120,10,10);

        //draw and update bullets
        for (int i = 0; i < bullets.size(); i++) {
            //get old bullet info
            int oldX = bullets.get(i).getBulletX();
            int oldY = bullets.get(i).getBulletY();
            float degrees = (float) bullets.get(i).getBulletDirection();
            int oldLifetime = bullets.get(i).getLifetime();

            //translate map positions to screen positions
            //draw bullet //subtract screen X1 from bullet X and screen Y1 from bullet Y to get current screen position
            int screenX1 = playerXonMap-568, screenY1 = playerYonMap-468;
            g2d.fillRect(oldX-screenX1,oldY-screenY1,5,5);

            //update bullet position (at 1/10th of the framerate)
            if (counter == 8) {
                double newBulletX = 0, newBulletY = 0, newLifetime;
                newLifetime = oldLifetime + 1;

                if (degrees >= 0 && degrees <=90) {
                    float xMovePercentage = 1 - (degrees / 90);
                    float yMovePercentage = (degrees / 90);
                    newBulletX = oldX + (bulletSpeed * xMovePercentage);
                    newBulletY = oldY + (bulletSpeed * yMovePercentage);
                }
                else if (degrees >= 90 && degrees <= 180) {
                    degrees -= 90;
                    float xMovePercentage = (degrees / 90);
                    float yMovePercentage = 1 - (degrees / 90);
                    newBulletX = oldX - (bulletSpeed * xMovePercentage);
                    newBulletY = oldY + (bulletSpeed * yMovePercentage);
                }
                else if (degrees <=0 && degrees >= -90) {
                    float xMovePercentage = 1 - (Math.abs(degrees / 90));
                    float yMovePercentage = (Math.abs(degrees / 90));
                    newBulletX = oldX + (bulletSpeed * xMovePercentage);
                    newBulletY = oldY - (bulletSpeed * yMovePercentage);
                }
                else if (degrees <= -90 && degrees >= -180) {
                    degrees += 90;
                    float xMovePercentage = (Math.abs(degrees / 90));
                    float yMovePercentage = 1 - (Math.abs(degrees / 90));
                    newBulletX = oldX - (bulletSpeed * xMovePercentage);
                    newBulletY = oldY - (bulletSpeed * yMovePercentage);
                }

//                newBulletX = oldX + (bulletSpeed * Math.sin(degrees));
//                newBulletY = oldY + (bulletSpeed * Math.cos(degrees));

                //save
                bullets.get(i).setBulletX((int) newBulletX);
                bullets.get(i).setBulletY((int) newBulletY);
                bullets.get(i).setLifetime((int) newLifetime);
                counter = 0;
            }
            else { counter++; }
        }

        //check for end of lifetime on bullets
        for (Iterator<Bullet> iter = bullets.listIterator(); iter.hasNext(); ) {
            if (iter.next().getLifetime() >= maxBulletLifetime) {
                iter.remove();
            }
        }


        //draw player
//        if (orientation.equals("up")) {rotation = Math.toRadians (0);}
//        else if (orientation.equals("down")) {rotation = Math.toRadians (180);}
//        else if (orientation.equals("right")) {rotation = Math.toRadians (90);}
//        else if (orientation.equals("left")) {rotation = Math.toRadians (270);}
        AffineTransform tx = AffineTransform.getRotateInstance(rotation, 32, 32);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        g2d.drawImage(op.filter(playerImg, null), playerXonScreen, playerYonScreen, null);
        g2d.drawRect(playerXonScreen, playerYonScreen, 64, 64);
    }

    public void setRotation (Point mouseLocation) { //calculate angle mouseLocation from center
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        double xDistance = mouseLocation.getX() - center.getX();
        double yDistance = mouseLocation.getY() - center.getY();
        double degrees = Math.toDegrees(Math.atan2(yDistance, xDistance));
        rotation = Math.toRadians (degrees+90);
        rotationDegrees = degrees;

    }

    public Point getLocation() {
        return new Point(playerXonMap, playerYonMap);
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public void movePlayer(String direction) {
        switch (direction) {
            case "Up":
                playerYonMap -= movementSpeed;
                orientation = "up";
                break;
            case "Down":
                playerYonMap += movementSpeed;
                orientation = "down";
                break;
            case "Left":
                playerXonMap -= movementSpeed;
                orientation = "left";
                break;
            case "Right":
                playerXonMap += movementSpeed;
                orientation = "right";
                break;
            default:
                break;
        }
    }

    public void fireWeapon() {
        bullets.add(new Bullet(playerXonMap,playerYonMap,rotationDegrees,0)); //add new bullet
    }

}
