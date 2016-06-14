package com.mycompany.behear;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by baralon on 07/05/2016.
 */
public class SoundPool {
    HashMap<Pair<Parameters,Integer>, ArrayList<Pair<Parameters,MediaPlayer>>> pool;
    Context context;
    private Random randomGenerator;
    static int seconds = 20;

    public SoundPool(Context context) {
        pool = new HashMap<>();
        this.context = context;
        randomGenerator = new Random();
    }

    public void startSound(final Parameters param, final int level) {
        if (isPoliticsParam(param) && (this.pool.get(Pair.create(Parameters.politics, level)) == null))
                this.add(param, level);
        else if (!isPoliticsParam(param) && this.pool.get(Pair.create(param, level)) == null)
            this.add(param, level);

        //is playing a sound in the exact current catagory
        ArrayList<MediaPlayer> soundsList =  getAllSounds(param, level);
        for (MediaPlayer sound: soundsList) {
            if (sound.isPlaying())
                return;
        }

        //stopping any sound in the bigger catagory that might be playing
        if (isPoliticsParam(param))
            stopSound(Parameters.politics);
        else
            stopSound(param);
        //playing sound
        final MediaPlayer currentSound = soundsList.get(this.randomGenerator.nextInt(soundsList.size()));
        currentSound.start();
//        if (!isPoliticsParam(param)) {
//            Handler mHandler = new Handler(Looper.getMainLooper());
//            mHandler.postDelayed(new Runnable() {
//                public void run() {
//                    try {
//                        currentSound.pause();
//                        currentSound.seekTo(0);
//                    }catch(Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, seconds * 1000);
//        }
    }


    public ArrayList<MediaPlayer> getAllSounds(Parameters param, int level) {
        ArrayList<MediaPlayer> list = new ArrayList<>();
        ArrayList<Pair<Parameters, MediaPlayer>> poolSoundsList;
        if (isPoliticsParam(param))
            poolSoundsList = pool.get(Pair.create(Parameters.politics, level));
        else
            poolSoundsList = pool.get(Pair.create(param, level));
        if (param == Parameters.politics || param == Parameters.econ || param == Parameters.property_crime) {
            for (int i = 0; i < poolSoundsList.size(); i++) {
                list.add(poolSoundsList.get(i).second);
            }
        }
        else if (param == Parameters.politics_econ || param == Parameters.politics_education) {
            for (int i = 0; i < poolSoundsList.size(); i++) {
                if (poolSoundsList.get(i).first == param)
                    list.add(poolSoundsList.get(i).second);
            }
        }
        return list;
    }

    public ArrayList<MediaPlayer> getAllSounds(Parameters param) {
        ArrayList<MediaPlayer> retList = new ArrayList<>();
        for (Pair<Parameters, Integer> key: pool.keySet()){
            if (key.first == param) {
                ArrayList<Pair<Parameters, MediaPlayer>> curretList = pool.get(key);
                for (int i = 0; i < curretList.size(); i++){
                    retList.add(curretList.get(i).second);
                }
            }
        }
        return retList;
    }

    public void stopSound(Parameters param, int level) {
        if (this.pool.get(Pair.create(param, level)) == null) {
            return;
        }
        ArrayList<MediaPlayer> sounds = getAllSounds(param, level);
        for (MediaPlayer sound: sounds){
            if (sound.isPlaying()) {
                sound.pause();
                sound.seekTo(0);
            }
        }
    }

    public void stopSound(Parameters param) {
        ArrayList<MediaPlayer> sounds = getAllSounds(param);
        for (MediaPlayer sound: sounds) {
            if (sound.isPlaying()) {
                sound.pause();
                sound.seekTo(0);
            }
        }
    }

//    MediaPlayer getMedia(Parameters param, int level) {
//        return pool.get(Pair.create(param, level));
//    }
//
//    public boolean isPlaying(Parameters param, int level) {
//        if (this.getMedia(param, level) != null) {
//            return this.getMedia(param, level).isPlaying();
//        }
//        return false;
//    }
//
//    /**
//     * checking if another sound of the same parameter is playing
//     * @param param
//     * @param level
//     * @return if is playing returning it's level, else return -1.
//     */
//    public int isPlayingDifferentLevel(Parameters param, int level) {
//        for (Pair<Parameters, Integer> key : this.pool.keySet() ) {
//            if(key.first.equals(param) && (key.second.equals(Integer.valueOf(level))  == false) && this.getMedia(param, key.second).isPlaying()) {
//                return key.second;
//            }
//        }
//        return -1;
//    }

    private void add(Parameters param, int level) {
        Field[] fields = R.raw.class.getFields();
        for (int i = 0; i < fields.length; i++) {
            String fileName = fields[i].getName();
            this.context.getSystemService(Context.AUDIO_SERVICE);
            if (should_add_politics(fileName, level, param)) {
                MediaPlayer mp = MediaPlayer.create(this.context, context.getResources().getIdentifier(fileName, "raw", this.context.getPackageName()));
                if (!pool.containsKey(Pair.create(Parameters.politics, level)))
                    pool.put(Pair.create(Parameters.politics, level), new ArrayList<Pair<Parameters, MediaPlayer>>());
                if (fileName.startsWith("poli_econ")) {
                    pool.get(Pair.create(Parameters.politics, level)).add(Pair.create(Parameters.politics_econ, mp));
                } else if (fileName.startsWith("poli_edu")) {
                    pool.get(Pair.create(Parameters.politics, level)).add(Pair.create(Parameters.politics_education, mp));
                } else {
                    pool.get(Pair.create(Parameters.politics, level)).add(Pair.create(Parameters.politics, mp));
                }
            }
            else if (shouldAddEcon(fileName, level, param) || shouldAddPropertyCrime(fileName, level, param) ) {
                if (!pool.containsKey(Pair.create(param, level)))
                    pool.put(Pair.create(param, level), new ArrayList<Pair<Parameters, MediaPlayer>>());
                MediaPlayer mp = MediaPlayer.create(this.context, context.getResources().getIdentifier(fileName, "raw", this.context.getPackageName()));
                pool.get(Pair.create(param, level)).add(Pair.create(param, mp));
            }
        }
    }

//    private int translateToResouceId(Parameters param, int level){
//        if (param.equals(Parameters.politics)) {
//            if (SoundManager.partiesHashTable.get("G") == level) {
//                return R.raw.g_yahadot_hatorah;
//            } else if (SoundManager.partiesHashTable.get("S") == level) {
//                return R.raw.s_shas;
//            } else if (SoundManager.partiesHashTable.get("M") == level) {
//                return R.raw.m_likud;
//            } else if (SoundManager.partiesHashTable.get("A") == level) {
//                return R.raw.a_machane;
//            } else if (SoundManager.partiesHashTable.get("K") == level) {
//                return R.raw.k_yachad;
//            } else if (SoundManager.partiesHashTable.get("T") == level) {
//                return R.raw.t_bait_yehudi;
//            } else if (SoundManager.partiesHashTable.get("V") == level) {
//                return R.raw.a_machane;
//            } else if (SoundManager.partiesHashTable.get("L") == level) {
//                return R.raw.l_israel_beyteno;
//            }
//            else {
//                System.out.print("shouldn't happen");
//            }
//        }
//        else if (param.equals(Parameters.econ)) {
//            //laughing
//            if (level == 0) {
//                return R.raw.econ_1;
//            }
//            else if (level == 1) {
//                return R.raw.econ_2;
//            }
//            else if (level == 2) {
//                return R.raw.econ_3;
//            }
//            else if (level == 3) {
//                return  R.raw.econ_4;
//            }
//            //carying
//            else if (level == 4) {
//                return R.raw.econ_5;
//            }
//        }
//        return 0;
//    }

    private boolean should_add_politics(String fileName, int level, Parameters param) {
        return (fileName.startsWith("poli_") &&
                (fileName.substring(fileName.length() -1).hashCode() == level )&&
                isPoliticsParam(param));
    }

    private boolean shouldAddEcon (String fileName, int level, Parameters param) {
        return (param == Parameters.econ &&
                fileName.startsWith("econ_") &&
                (Integer.valueOf(fileName.substring(fileName.length() - 1)) == level));
    }

    private boolean shouldAddPropertyCrime(String fileName, int level, Parameters param) {
        return (param == Parameters.property_crime &&
                fileName.startsWith("property_") &&
                (Integer.valueOf(fileName.substring(fileName.length() - 1)) == level));
    }

    private boolean isPoliticsParam(Parameters param) {
        return (param.equals(Parameters.politics) || param.equals(Parameters.politics_education) || param.equals(Parameters.politics_econ));
    }


}
