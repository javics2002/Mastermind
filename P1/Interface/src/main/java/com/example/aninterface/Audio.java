package com.example.aninterface;

public interface Audio {
    Sound playSound(String file, boolean loop);
    void stopSound(String file);
}
