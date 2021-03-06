package com.mycompany.behear;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Pair;
import java.util.HashMap;

/**
 * Created by baralon on 04/05/2016.
 * This class os The SoundManager, it will receive requests to start sounds according to parameters and levels and find the matching sounds.
 */
public class SoundManager {
    Context context;
    //pool of different sounds
    SoundPool soundPool;
    Parameters lastLevel;
    public SoundManager(Context context_) {
        this.context = context_;
        this.soundPool = new SoundPool(context_);
    }

    //If the sound is already played, then it will just let it continue.
    //If the sound is not played but another sound ot the same category is played (i.e different level) it will stop the sound and start it with the new level.
    public boolean playSound(Parameters param, int level) {

        if (this.isPlaying(param, level)) {
            return false;
        }
        else {
            int currentSoundLevel = this.isPlayingDifferentLevel(param, level);
            if (currentSoundLevel != -1) {
                this.stopSound(param, currentSoundLevel);
            }
            this.startSound(param, level);
            return true;
        }
    }

    //Checking if a certain sound is played./
    private boolean isPlaying(Parameters param, int level) {
       return soundPool.isPlaying(param, level);
    }

    //Checking if a sound with the same category but different level is played.
    //if so returns it's level, otherwise return -1.
    private int isPlayingDifferentLevel(Parameters param, int level) {
        return soundPool.isPlayingDifferentLevel(param, level);
    }

    //Starting a sound
    private boolean startSound (Parameters param, int level) {
        //ToDo - after fixing all the sounds nned to convert a param and level to the Address of R.raw.zehava
        this.soundPool.startSound(param, level);
        return true;
    }

    //stopping a sound.
    public boolean stopSound (Parameters param, int level) {
        //ToDo - after fixing all the sounds nned to convert a param and level to the Address of R.raw.zehava
        this.soundPool.stopSound(param, level);
        return true;
    }

}
