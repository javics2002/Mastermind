package com.example.logiclib;

import com.example.aninterface.Audio;
import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;
import com.example.aninterface.Sound;

public class Button extends GameObject {
	protected Image _image;
	protected final Sound _clickSound;
	protected int _backgroundColor;
	protected int _arc;
	protected String _text;
	protected Font _font;
	protected final Audio _audio;

	Button(Colors.ColorName backgroundColor, String text, Font font, Engine engine, int positionX, int positionY, int width, int height) {
		super(engine, positionX, positionY, width, height, 1f);

		_image = null;

		_backgroundColor = Colors.colorValues.get(backgroundColor);
		_arc = 20;

		_text = text;
		_font = font;

		_audio = _engine.getAudio();
		_clickSound = _audio.loadSound("click.wav", false);
		_clickSound.setVolume(.5f);
	}

	Button(String filename, Engine engine, int positionX, int positionY, int width, int height) {
		super(engine, positionX, positionY, width, height, 1f);

		_image = _graphics.loadImage(filename);

		_audio = _engine.getAudio();
		_clickSound = _audio.loadSound("click.wav", false);
		_clickSound.setVolume(.5f);
	}

	Button(Engine engine, int positionX, int positionY, int width, int height) {
		super(engine, positionX, positionY, width, height, 1f);

		_image = null;

		_audio = _engine.getAudio();
		_clickSound = _audio.loadSound("click.wav", false);
		_clickSound.setVolume(.5f);
	}

	@Override
	public boolean handleEvents(Input.TouchEvent event) {
		if (event.type == Input.InputType.PRESSED && inBounds(event.x, event.y)) {
			_audio.playSound(_clickSound);
			callback();

			return true;
		}

		return false;
	}

	@Override
	public void update(double deltaTime) {
	}

	@Override
	public void render(Graphics graphics) {
		if (_image != null)
			_graphics.drawImage(_image, _positionX, _positionY, _width, _height, _scale);
		else {
			_graphics.drawRoundedRect(_positionX, _positionY, _width, _height, _arc, _arc, _scale, _backgroundColor);
			_graphics.drawText(_text, _font, _positionX + (int) _width / 2 - _graphics.getStringWidth(_text, _font) / 2,
					_positionY + (int) _height / 2 + _graphics.getStringHeight(_text, _font) / 2, _scale, 0);
		}
	}

	public boolean inBounds(int mouseX, int mouseY) {
		return _graphics.inBounds(_positionX, _positionY, mouseX, mouseY, _width, _height, _scale);
	}

	public void callback() {
	}

	public void setImage(String path) {
		_image = _graphics.loadImage(path);
	}
}
