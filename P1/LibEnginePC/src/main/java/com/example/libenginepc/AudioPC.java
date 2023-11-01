package com.example.libenginepc;

import com.example.aninterface.Audio;
import com.example.aninterface.Sound;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

public class AudioPC implements Audio {
    HashMap<String, SoundPC> _sounds = new HashMap<>(); //Clave para identificar NombreSonido

    @Override
    public Sound loadSound(String file, boolean loop) {     //NUEVO SONIDO
        if (isLoaded(file))
            return getSound(file);

        AudioInputStream audioStream = null;
        Clip clip = null;

        try {
            File audioFile = new File("data/" + file);
            audioStream = AudioSystem.getAudioInputStream(audioFile);
        } catch (Exception e) {
            System.err.println("Couldn't load audio file");
            e.printStackTrace();
        }
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        try {
            clip.open(audioStream);
            if (loop)
                clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }

        SoundPC sound = new SoundPC(clip);
        _sounds.put(file, sound);
        return sound;
    }

    @Override
    public Sound getSound(String file) {
        if (!isLoaded(file))
            return null;

        return _sounds.get(file);
    }

    @Override
    public boolean isLoaded(String file) {
        return _sounds.containsKey(file);
    }
}
