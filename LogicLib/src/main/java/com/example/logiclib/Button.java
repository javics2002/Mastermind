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
	protected float _arc;
	protected String _text;
	protected Font _font;
	protected final Audio _audio;

	Button(int backgroundColor, String text, Font font, Engine engine,
	       float positionX, float positionY, float width, float height, float arc) {
		super(engine, positionX, positionY, width, height, 1f);

		_image = null;

		_arc = arc;

		_backgroundColor = backgroundColor;

		_text = text;
		_font = font;

		_audio=_engine.getAudio();
		_clickSound = _audio.loadSound("click.wav", false);
		_clickSound.setVolume(.5f);
	}

	Button(String filename, Colors.ColorName backgroundColor, String text, Font font, Engine engine,
	       float positionX, float positionY, float width, float height, float arc) {
		super(engine, positionX, positionY, width, height, 1f);

		_image = _graphics.loadImage(filename);
		_positionX = positionX;
		_positionY = positionY;
		_width = width;
		_height = height;
		_arc = arc;

		_backgroundColor = Colors.colorValues.get(backgroundColor);

		_text = text;
		_font = font;

		_audio = _engine.getAudio();
		_clickSound = _audio.loadSound("click.wav", false);
		_clickSound.setVolume(.5f);
	}

	Button(String filename, Engine engine, float positionX, float positionY, float width, float height) {
		super(engine, positionX, positionY, width, height, 1f);

		_image = _graphics.loadImage(filename);

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
			_graphics.drawText(_text, _font, _positionX + (int) _width / 2f - _graphics.getStringWidth(_text, _font) / 2,
					_positionY + (int) _height / 2f + _graphics.getStringHeight(_text, _font) / 2, _scale, 0);
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
