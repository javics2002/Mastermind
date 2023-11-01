package com.example.libenginepc;
import com.example.aninterface.Sound;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
public class SoundPC implements Sound {

    private Clip _clip;
    private FloatControl _volumeControl;

    SoundPC(Clip clip){
        this._clip = clip;
        // Inicializar _volumeControl si el clip tiene uno
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            _volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        }
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
    // Método para aumentar el volumen en una cantidad específica (en decibeles)
    public void increaseVolume(float increaseAmount) {
        float currentVolume =  _volumeControl.getValue();
        _volumeControl.setValue(currentVolume + increaseAmount);
    }

    // Método para disminuir el volumen en una cantidad específica (en decibeles)
    public void decreaseVolume(float decreaseAmount) {
        float currentVolume = _volumeControl.getValue();
        _volumeControl.setValue(currentVolume - decreaseAmount);
    }
}
