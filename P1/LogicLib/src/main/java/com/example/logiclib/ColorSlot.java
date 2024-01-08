package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;
import com.example.aninterface.Image;

public class ColorSlot extends GameObject {
	private String _name;
	private boolean _hasColor;
	private int _colorID;
	private Font _colorNum;
	private Text _numberText;
	private GameAttributes _gameAttributes;
	private Image _icon;
	private int _color;
	private final float _appearenceTime = .3f;
	private float _animationTime = 0;

	public ColorSlot(Engine engine, int positionX, int positionY, int width, int height, GameAttributes gameAttributes) {
		super(engine, positionX, positionY, width, height, 1f);

		_gameAttributes = gameAttributes;
		_hasColor = false;
		_colorNum = _graphics.newFont("Comfortaa-Regular.ttf", 24);
		_numberText = new Text("", _colorNum, engine, 0, 0, 0, true);
		_colorID = -1;

		_animationTime = _appearenceTime;

	}

	public ColorSlot(Engine engine, float positionX, float positionY, float width, float height, int colorID, GameAttributes gameAttributes) {
		super(engine, positionX, positionY, width, height, 1f);

		_gameAttributes = gameAttributes;
		_hasColor = colorID != -1 ? true : false;
		_colorNum = _graphics.newFont("Comfortaa-Regular.ttf", 24);

		_animationTime = _appearenceTime;

		//TODO quitar string width y height
		String number = String.valueOf(colorID);
		float textX = _positionX + _width / 2 - _graphics.getStringWidth(number, _colorNum) / 2;
		float textY = _positionY + _height / 2 + _graphics.getStringHeight(number, _colorNum) / 2;

		_numberText = new Text(Integer.toString(colorID), _colorNum, engine, textX, textY, 0, true);
		_colorID = colorID;

		if (_gameAttributes.selectedWorld == -1 && _hasColor) {
			_color = Colors.getColor(_colorID - 1);
			_icon = null;
		} else if (_gameAttributes.selectedWorld >= 0 && _hasColor) {
			_icon = _graphics.loadImage(GameData.Instance().getWorldDataByIndex(_gameAttributes.selectedWorld).getWorldName()
					+ "/icon" + _colorID + ".png");
			_color = Colors.getColor(_colorID - 1);
		}
	}

	@Override
	public void render(Graphics graphics) {
		if (hasColor()) {
			if (_icon != null)
				_graphics.drawImage(_icon, _positionX, _positionY, _width, _height, _scale);
			else
				_graphics.drawCircleWithBorder(_positionX + _width * _scale / 2,
						_positionY + _height * _scale / 2, _width / 2, 1f, _scale,
						_color, Colors.colorValues.get(Colors.ColorName.BLACK));

			if (_gameAttributes.isEyeOpen)
				_numberText.render(graphics);
		} else { // Gris
			_graphics.drawCircleWithBorder(_positionX + _width * _scale / 2,
					_positionY + _height * _scale / 2, _width / 2, 1f, _scale,
					Colors.colorValues.get(Colors.ColorName.LIGHTGRAY), Colors.colorValues.get(Colors.ColorName.BLACK));
			_graphics.drawCircle(_positionX + _width / 2, _positionY + _height / 2,
					_width / 8, _scale, Colors.colorValues.get(Colors.ColorName.DARKGRAY));
		}
	}

	@Override
	public void update(double deltaTime) {
		if (_animationTime < _appearenceTime) {
			float newScale = lerp(0, 1, _animationTime / _appearenceTime);
			_scale = newScale;
			_numberText.setScale(newScale);

			_animationTime += deltaTime;

			if (_animationTime >= _appearenceTime) {
				_scale = 1;
				_numberText.setScale(1);
			}
		}
	}

	@Override
	public boolean handleEvents(Input.TouchEvent e) {
		return e.type == Input.InputType.PRESSED && inBounds(e.x, e.y);
	}

	public void setColor(int color, boolean isEyeOpen) {
		_hasColor = true;
		_colorID = color;
		String num = String.valueOf(_colorID);

		//TODO quitar string width y height
		String number = String.valueOf(_colorID);
		float textX = _positionX + _width / 2 - _graphics.getStringWidth(number, _colorNum) / 2;
		float textY = _positionY + _height / 2 + _graphics.getStringHeight(number, _colorNum) / 2;

		_numberText.setText(number);
		_numberText.setPosition(textX, textY);

		if (_gameAttributes.selectedWorld == -1) {
			if (GameData.Instance().getCurrentCircles() < 0) {
				_color = Colors.getColor(_colorID - 1);
				_icon = null;
			} else {
				final Circles circles = GameData.Instance().getCircles().get(GameData.Instance().getCurrentCircles());

				if (circles.skin) {
					_icon = _graphics.loadImage(circles.packPath + "/icon" + _colorID + ".png");
					_color = Colors.getColor(_colorID - 1);
				} else {
					_icon = null;
					_color = Colors.parseARGB(circles.colors[_colorID]);
				}
			}
		} else {
			_icon = _graphics.loadImage(GameData.Instance().getWorldDataByIndex(_gameAttributes.selectedWorld).getWorldName()
					+ "/icon" + _colorID + ".png");
			_color = Colors.getColor(_colorID - 1);
		}
	}

	public void animate() {
		_animationTime = 0;
	}

	public boolean inBounds(int mouseX, int mouseY) {
		return _graphics.inBounds(_positionX, _positionY, mouseX, mouseY, _width, _height, _scale);
	}

	public boolean hasColor() {
		return _hasColor;
	}

	public void deleteColor() {
		_hasColor = false;
	}

	public float lerp(float a, float b, float t) {
		return a + t * (b - a);
	}

	public void setTextPositionY(int posY) {
		_numberText.setPosition(_numberText.getPositionX(), posY + _height / 2);
	}
}



