package com.example.libenginepc;

import com.example.aninterface.Sound;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundPC implements Sound {
	private final Clip _clip;
	private FloatControl _volumeControl;

	SoundPC(Clip clip) {
		_clip = clip;

		// Inicializar _volumeControl si el clip tiene uno
		if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
			_volumeControl = (FloatControl) _clip.getControl(FloatControl.Type.MASTER_GAIN);
		}
	}

	@Override
	public void play() {
		_clip.setFramePosition(0);
		_clip.start();
	}

	@Override
	public void stop() {
		if (_clip.isRunning())
			_clip.stop();
	}

	@Override
	public void setVolume(float newVolume) {
		newVolume = Math.min(1.0f, Math.max(0.0f, newVolume));

		float gbGain = 20 * (float) Math.log10(newVolume);

		_volumeControl.setValue(gbGain);
	}
}
