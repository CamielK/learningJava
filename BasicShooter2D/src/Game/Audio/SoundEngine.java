package Game.Audio;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;
import java.util.Random;

/**
 * Created by Camiel on 05-Jan-16.
 */
public class SoundEngine {

    public static synchronized void playLooped(final String fileName, final float volumeOffset)
    {
        new Thread(new Runnable() {
            public void run() {
                try {
                    URL url = getClass().getClassLoader().getResource(fileName);
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream ais = AudioSystem.getAudioInputStream(url);
                    clip.open(ais);

                    //stereo
                    //System.out.println(clip.getFormat());

                    //volume control
                    FloatControl gainControl =
                            (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(volumeOffset); // adjust volume with given offset.


                    clip.loop(-1); // loop continuously
                } catch (Exception e) {
                    System.out.println("play sound error: " + e.getMessage() + " for " + fileName);
                }
            }
        }).start();
    }

    public static synchronized void playOnce(final String fileName, final float volumeOffset)
    {
        new Thread(new Runnable() {
            public void run() {
                try {
                    URL url = getClass().getClassLoader().getResource(fileName);
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream ais = AudioSystem.getAudioInputStream(url);
                    clip.open(ais);

                    //volume control
                    FloatControl gainControl =
                            (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(volumeOffset); // Reduce volume by 10 decibels.

                    clip.loop(0);
                } catch (Exception e) {
                    System.out.println("play sound error: " + e.getMessage() + " for " + fileName);
                }
            }
        }).start();
    }
}
