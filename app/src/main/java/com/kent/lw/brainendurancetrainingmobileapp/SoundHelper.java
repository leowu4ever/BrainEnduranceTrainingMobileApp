package com.kent.lw.brainendurancetrainingmobileapp;

import android.app.Application;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

public class SoundHelper extends Application {

    // soundpool
    public SoundPool sp;
    public int beepSound, speedupSound;


    public void initSoundHelper(Context context) {
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
        speedupSound = sp.load(context, R.raw.speedup, 1);
    }

    public void playBeepSound (float leftVolume, float rightVolume, int priority, int loop, float rate) {
        sp.play(beepSound, leftVolume, rightVolume, priority, loop, rate);
    }

    public void playSpeedupSound (float leftVolume, float rightVolume, int priority, int loop, float rate) {
        sp.play(speedupSound, leftVolume, rightVolume, priority, loop, rate);
    }
}
