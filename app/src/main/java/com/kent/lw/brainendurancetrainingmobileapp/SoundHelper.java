package com.kent.lw.brainendurancetrainingmobileapp;

import android.app.Application;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

public class SoundHelper extends Application {

    // soundpool
    public SoundPool sp;
    public int beepSound, beepSoundLanguage, speedupSound, startSound, finishSound;
    public int nogoSound, nogoSoundLangEasy, nogoSoundLangMedium, nogoSoundLangHard;
    public int kidPlayingSound, musicSound;
    public int beepSound1;
    // noise sound playing ref used when pause and resume
    public int noiseplay;

    public MediaPlayer noiseMP;

    public Context c;


    public SoundHelper(Context context) {

        c = context;
        init(context);

        AudioManager audioManager =
                (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)/3, 0);
    }

    public void init(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            sp = new SoundPool.Builder().setMaxStreams(6).setAudioAttributes(audioAttributes).build();
        } else {
            sp = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }

        beepSound = sp.load(context, R.raw.beep, 1);
        nogoSound = sp.load(context, R.raw.nogo, 1);
        //for language task
        beepSoundLanguage = sp.load(context, R.raw.language_go, 1);
        nogoSoundLangEasy = sp.load(context, R.raw.language_nogo_easy, 1);
        nogoSoundLangMedium = sp.load(context, R.raw.language_nogo_medium, 1);
        nogoSoundLangHard = sp.load(context, R.raw.language_nogo_hard, 1);

        speedupSound = sp.load(context, R.raw.speedup, 1);
        startSound = sp.load(context, R.raw.start, 1);
        finishSound = sp.load(context, R.raw.finish, 1);

        // for init noise sound

        kidPlayingSound = sp.load(context, R.raw.kidsplaying, 1);
        musicSound = sp.load(context, R.raw.music, 1);

        beepSound1 = sp.load(context, R.raw.beep_1, 1);

    }

    private int getNoiseRawId(int noiseTypeIndexSelected) {
        switch (noiseTypeIndexSelected){
            case 0:
                return R.raw.whitenoise_relaxing;
            case 1:
                return R.raw.whitenoise_rainsound;
            case 2:
                return R.raw.whitenoise_windsound;
            case 3:
                return R.raw.bpm110_124_runbabyrun;
            case 4:
                return R.raw.bpm110_124_runaway;
            case 5:
                return R.raw.bpm110_124_runningwiththenight;
            case 6:
                return R.raw.bpm125_134_daynnite;
            case 7:
                return R.raw.bpm125_134_runtoyou;
            case 8:
                return R.raw.bpm125_134_wherearewerunnin;
            case 9:
                return R.raw.bpm135_150_burntorun;
            case 10:
                return R.raw.bpm135_150_heaven;
            case 12:
                return R.raw.bpm135_150_runnin;
            case 13:
                return R.raw.bpm151_175_runningfree;
            case 14:
                return R.raw.bpm151_175_runwiththewolve;
        }
        return R.raw.whitenoise_relaxing;
    }

    public void playBeepSound(final String s, final float leftVolume, final float rightVolume, final int priority, final int loop, final float rate) {
        if(MainActivity.trainingData.getTask().equals("Language")) {
            sp.play(beepSoundLanguage, leftVolume, rightVolume, priority, loop, rate);
        } else {
            final int audio = sp.load(c, (this.c.getResources().getIdentifier(s, "raw", this.c.getPackageName())), 1);
            sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    sp.play(audio, leftVolume, rightVolume, priority, loop, rate);
                }
            });
        }

    }

    public void playBeepSound1(float leftVolume, float rightVolume, int priority, int loop, float rate) {
         sp.play(beepSound1, leftVolume, rightVolume, priority, loop, rate);
    }


    public void playSpeedupSound(float leftVolume, float rightVolume, int priority, int loop, float rate) {
        sp.play(speedupSound, leftVolume, rightVolume, priority, loop, rate);
    }

    public void playStartSound(float leftVolume, float rightVolume, int priority, int loop, float rate) {
        sp.play(startSound, leftVolume, rightVolume, priority, loop, rate);
    }

    public void playFinishSound(float leftVolume, float rightVolume, int priority, int loop, float rate) {
        sp.play(finishSound, leftVolume, rightVolume, priority, loop, rate);
    }

    public void playNoiseSound(float leftVolume, float rightVolume, int priority, int loop, float rate) {
//
//        // do noise type check switch here
//        if (MainActivity.task.getNoiseType() == 0) {
//            noiseplay = sp.play(kidPlayingSound, leftVolume, rightVolume, priority, loop, rate);
//        } else if (MainActivity.task.getNoiseType() == 1) {
//            noiseplay = sp.play(musicSound, leftVolume, rightVolume, priority, loop, rate);
//        }


        noiseMP = MediaPlayer.create(c, getNoiseRawId(MainActivity.task.getNoiseType()));

        if(noiseMP != null){
            noiseMP.setLooping(true);
            noiseMP.setVolume(leftVolume, rightVolume);
            noiseMP.start();
        }
    }

    public void stopNoiseSound() {
//        sp.stop(noiseplay);
        if(noiseMP != null) {
            noiseMP.release();
            noiseMP = null;
        }
    }

    public void playNogoSound(float leftVolume, float rightVolume, int priority, int loop, float rate) {
        if(MainActivity.trainingData.getTask().equals("Language")) {
            switch(MainActivity.task.getCurDif()){
                case "Easy":
                    sp.play(nogoSoundLangEasy, leftVolume, rightVolume, priority, loop, rate);
                    break;
                case "Custom":
                case "Medium":
                    sp.play(nogoSoundLangMedium, leftVolume, rightVolume, priority, loop, rate);
                    break;
                case "Hard":
                    sp.play(nogoSoundLangHard, leftVolume, rightVolume, priority, loop, rate);
                    break;
            }
        }
        else { sp.play(nogoSound, leftVolume, rightVolume, priority, loop, rate); }
    }

    public void play(float leftVolume, float rightVolume, int priority, int loop, float rate) {

        if(noiseMP != null){
            noiseMP.setLooping(true);
            noiseMP.setVolume(leftVolume, rightVolume);
            noiseMP.start();
        }
    }

    public void stopNoiseMP () {
        if(noiseMP != null) {
            noiseMP.release();
            noiseMP = null;
        }
    }

    public void playMemoryTask(String s, final float leftVolume, final float rightVolume, final int priority, final int loop, final float rate) {
        final int audio = sp.load(c, (this.c.getResources().getIdentifier(s, "raw", this.c.getPackageName())), 1);
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                sp.play(audio, leftVolume, rightVolume, priority, loop, rate);
            }
        });
    }
}
