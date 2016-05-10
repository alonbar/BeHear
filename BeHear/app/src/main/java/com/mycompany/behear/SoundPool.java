package com.mycompany.behear;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Pair;

import java.util.HashMap;

/**
 * Created by baralon on 07/05/2016.
 */
public class SoundPool {
    HashMap<Pair<Parameters,Integer>, MediaPlayer> pool;
    Context context;

    public SoundPool(Context context) {
        pool = new HashMap<>();
        this.context = context;
    }

    public void add(Parameters param, int level) {
        if (this.getMedia(param, level) == null) {
            int resource = this.translateToResouceId(param,level);
            this.context.getSystemService(Context.AUDIO_SERVICE);
            MediaPlayer mp = MediaPlayer.create(this.context, translateToResouceId(param, level));
            pool.put(Pair.create(param, level), mp);
        }
    }

    public void startSound(Parameters param, int level) {
        if (this.getMedia(param, level) == null) {
            this.add(param, level);
        }
        if (this.getMedia(param, level).isPlaying() == false) {
            this.pool.get(Pair.create(param,level)).start();
        }
    }

    public void stopSound(Parameters param, int level) {
        if (this.getMedia(param, level) == null) {
            this.add(param, level);
        }
        this.pool.get(Pair.create(param,level)).stop();
    }

    MediaPlayer getMedia(Parameters param, int level) {
        return pool.get(Pair.create(param, level));
    }

    public boolean isPlaying(Parameters param, int level) {
        if (this.getMedia(param, level) != null) {
            return this.getMedia(param, level).isPlaying();
        }
        return false;
    }

    /**
     * checking if another sound of the same parameter is playing
     * @param param
     * @param level
     * @return if is playing returning it's level, else return -1.
     */
    public int isPlayingDifferentLevel(Parameters param, int level) {
        for (Pair<Parameters, Integer> key : this.pool.keySet() ) {
            if(key.first.equals(param) && (key.second.equals(Integer.valueOf(level))  == false) && this.getMedia(param, key.second).isPlaying()) {
                return key.second;
            }
        }
        return -1;
    }

    private int translateToResouceId(Parameters param, int level){
        if (param.equals(Parameters.politics)) {
            switch (level) {
                case 0:
                    return R.raw.zehava;
                case 1:
                    return R.raw.benet;
                case 2:
                    return R.raw.shas;
            }

        }
        return R.raw.shas;
    }
}
