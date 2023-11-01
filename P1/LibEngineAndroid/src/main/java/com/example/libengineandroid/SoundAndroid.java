package com.example.libengineandroid;

import android.media.SoundPool;

import com.example.aninterface.Sound;

public class SoundAndroid implements Sound {
    private final int _id;
    private final boolean _loop;
    private final SoundPool _soundPool;
    private float _volume;

    public SoundAndroid(int id, boolean loop, SoundPool soundPool) {
        _id = id;
        _loop = loop;
        _soundPool = soundPool;
    }

    @Override
    public void play() {
        _soundPool.play(_id, _volume, _volume, 1, _loop ? 1 : 0, 1);
    }

    @Override
    public void stop() {
        _soundPool.stop(_id);
    }

    @Override
    public void setVolume(float newVolume) {
        _volume = Math.max(0.0f, Math.min(1.0f, newVolume));
    }
}
