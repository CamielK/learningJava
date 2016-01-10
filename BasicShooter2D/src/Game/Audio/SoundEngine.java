package Game.Audio;


import Game.Player.Bullet;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Camiel on 05-Jan-16.
 */
public class SoundEngine {

    private static List<AudioFragment> audioFragments = new ArrayList<AudioFragment>();
    private static boolean jetFlyby = false;

    public void update() {
        //TODO update audio logic


        //random jet flyby
        if (new Random().nextInt(10000) < 1) { //0.1% chance (&& !jetFlyby)
            jetFlyby = true;
            addSound("Resources/AUDIO/jet_flyby.wav", +2.0f, false);
        }

        //play();
    }

    private void play() {
        //TODO manage new audio being played and stop running audio
        for (Iterator<AudioFragment> iter = audioFragments.listIterator(); iter.hasNext(); ) {
            //
        }

    }

    public void addSound(String filename, float volumeOffset, boolean loop) {
        audioFragments.add(new AudioFragment(filename, volumeOffset, loop));
    }
}
