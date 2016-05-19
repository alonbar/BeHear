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
            return;
        }
        this.getMedia(param, level).pause();
        this.getMedia(param, level).seekTo(0);
    }

    public void stopSound(Parameters param) {
        for (Pair<Parameters, Integer> key: this.pool.keySet()) {
            if (key.first.equals(param)) {
                this.getMedia(param, key.second).pause();
                this.getMedia(param, key.second).seekTo(0);
            }
        }
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
            if (SoundManager.partiesHashTable.get("G") == level) {
                return R.raw.g_yahadot_hatorah;
            } else if (SoundManager.partiesHashTable.get("S") == level) {
                return R.raw.s_shas;
            } else if (SoundManager.partiesHashTable.get("M") == level) {
                return R.raw.m_likud;
            } else if (SoundManager.partiesHashTable.get("A") == level) {
                return R.raw.a_machane;
            } else if (SoundManager.partiesHashTable.get("K") == level) {
                return R.raw.k_yachad;
            } else if (SoundManager.partiesHashTable.get("T") == level) {
                return R.raw.t_bait_yehudi;
            } else if (SoundManager.partiesHashTable.get("V") == level) {
                return R.raw.a_machane;
            } else if (SoundManager.partiesHashTable.get("L") == level) {
                return R.raw.l_israel_beyteno;
            }
            else {
                System.out.print("shouldn't happen");
            }
        }
        else if (param.equals(Parameters.econ)) {
            //laughing
            if (level == 0) {
                return R.raw.econ_1;
            }
            else if (level == 1) {
                return R.raw.econ_2;
            }
            else if (level == 2) {
                return R.raw.econ_3;
            }
            else if (level == 3) {
                return  R.raw.econ_4;
            }
            //carying
            else if (level == 4) {
                return R.raw.econ_5;
            }
        }
        return 0;
    }
}
