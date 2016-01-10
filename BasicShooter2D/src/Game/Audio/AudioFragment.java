package Game.Audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

/**
 * Created by Camiel on 10-Jan-16.
 */
public class AudioFragment {

    private String filename = "Resources/AUDIO/clipname.wav";
    private boolean looping = false;
    private Clip clip;
    private AudioInputStream ais;
    private float volumeOffset = 0.0f;

    public AudioFragment(String filename, float volumeOffset, boolean loop) {
        this.filename = filename;
        this.looping = loop;
        this.volumeOffset = volumeOffset;

        try {
            URL url = getClass().getClassLoader().getResource(filename);
            ais = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
        }
        catch (Exception e) {
            System.out.println("play sound error: " + e.getMessage() + " for " + filename);
        }

        if (looping) { playLooped(); }
        else { playOnce(); }
    }

    private void playLooped() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    clip.open(ais);
                    adjustVolume(volumeOffset);
                    clip.loop(-1); // loop continuously
                } catch (Exception e) {
                    System.out.println("play sound error: " + e.getMessage() + " for " + filename);
                }
            }
        }).start();
    }

    private void playOnce() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    clip.open(ais);
                    adjustVolume(volumeOffset);
                    clip.loop(0);
                } catch (Exception e) {
                    System.out.println("play sound error: " + e.getMessage() + " for " + filename);
                }
            }
        }).start();
    }

    public void stopPlaying() {
        clip.close();
    }

    public void adjustVolume(float volumeOffset){
        this.volumeOffset = volumeOffset;
    }
}
