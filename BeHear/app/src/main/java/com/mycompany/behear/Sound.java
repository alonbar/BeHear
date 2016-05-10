package com.mycompany.behear;
import android.media.MediaPlayer;
/**
 * Created by baralon on 07/05/2016.
 */
public class Sound {
    Parameters param;
    int level;
    MediaPlayer mp;
    public Sound(Parameters param, int level, int resourceID,  MediaPlayer mp) {
        this.param = param;
        this.level = level;
        this.mp = mp;
    }

}
