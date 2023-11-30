package com.example.libengineandroid;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.example.aninterface.Audio;
import com.example.aninterface.Sound;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AudioAndroid implements Audio {
    private Context _context;
    private Map<String, SoundAndroid> _soundMap;
    private SoundPool _soundPool;
    private MediaPlayer _mediaPlayer;

    public AudioAndroid(Context context) {
        _context = context;
        _soundMap = new HashMap<>();
        _soundPool = new SoundPool.Builder().setMaxStreams(10).build();
        _mediaPlayer = new MediaPlayer();
        _mediaPlayer.reset();
    }

    @Override
    public Sound loadSound(String file, boolean loop) {
        if (isLoaded(file))
            return getSound(file);

        try {
            AssetFileDescriptor assetFileDescriptor = _context.getAssets().openFd("Music/" + file);

            SoundAndroid sound = new SoundAndroid(_soundPool.load(assetFileDescriptor, 1), loop, _soundPool);
            _soundMap.put(file, sound);
            return sound;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Sound getSound(String file) {
        return _soundMap.get(file);
    }

    @Override
    public boolean isLoaded(String file) {
        return _soundMap.containsKey(file);
    }
}
