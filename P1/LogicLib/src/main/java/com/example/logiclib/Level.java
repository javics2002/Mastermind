package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;
import com.example.aninterface.Sound;

public class Level extends Button {
	private boolean _locked, _completed;
	private final Sound _lockedSound;

	Level(boolean locked, boolean completed, String text, Font font,
	      Engine engine, int positionX, int positionY, int width, int height) {
		super("UI/lock.png", completed ? Colors.ColorName.LEVELCOMPLETED : Colors.ColorName.LEVELUNCOMPLETED,
				text, font, engine, positionX, positionY, width, height);

		_locked = locked;
		_completed = completed;

		_lockedSound = _engine.getAudio().loadSound("locked.mp3", false);
		_lockedSound.setVolume(.5f);
	}

	@Override
	public boolean handleEvents(Input.TouchEvent event) {
		if (_locked && event.type == Input.InputType.PRESSED && inBounds(event.x, event.y)) {
			_engine.getAudio().playSound(_lockedSound);
			return true;
		}

		return super.handleEvents(event);
	}

	@Override
	public void render(Graphics graphics) {
		_graphics.drawRoundedRect(_positionX, _positionY, _width, _height, _arc, _arc, _scale, _backgroundColor);

		if (_locked) {
			_graphics.drawImage(_image, _positionX + _width / 4, _positionY + _height / 4,
					_width / 2, _height / 2, _scale);
		} else {
			_graphics.drawText(_text, _font, _positionX + _width / 2 - _graphics.getStringWidth(_text, _font) / 2,
					_positionY + _height / 2 + _graphics.getStringHeight(_text, _font) / 2, _scale, 0);
		}
	}

	public void unlockLevel() {
		_locked = false;
	}

	public void completeLevel() {
		_completed = true;
		_backgroundColor = Colors.colorValues.get(Colors.ColorName.LEVELCOMPLETED);
	}
}
