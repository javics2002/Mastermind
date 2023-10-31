package com.example.libenginepc;
import com.example.aninterface.Sound;
import javax.sound.sampled.Clip;
public class SoundPC implements Sound {

    private Clip _clip;
    SoundPC(Clip clip){
        this._clip = clip;
    }

    public Clip getClip(){return this._clip;}
    @Override
    public void play() {
        this._clip.start();
    }
    public void stop() {
        if(this._clip.isRunning())
            this._clip.stop();
    }
}
