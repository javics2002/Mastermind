package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;
import com.example.aninterface.Image;

public class ColorButton extends GameObject {
	public int _colorID;
	private final Text _numberText;
	private final GameAttributes _gameAttributes;
	private final Image _icon;
	private final int _color;

	ColorButton(Engine engine, int positionX, int positionY, int width, int height, int colorID, GameAttributes gameAttributes) {
		super(engine, positionX, positionY, width, height, 1f);

		_gameAttributes = gameAttributes;

		_colorID = colorID;
		String num = String.valueOf(_colorID);

		final Font colorNum = _graphics.newFont("Comfortaa-Regular.ttf", 24);

		//TODO quitar string width y height
		float textX = _positionX + _width / 2 - _graphics.getStringWidth(num, colorNum) / 2;
		float textY = _positionY + _height / 2 + _graphics.getStringHeight(num, colorNum) / 2;
		_numberText = new Text(num, colorNum, engine, textX, textY, 0, true);

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
					_color = Colors.parseARGB(circles.colors[colorID]);
				}
			}
		} else {
			_icon = _graphics.loadImage(GameData.Instance().getWorldDataByIndex(_gameAttributes.selectedWorld).getWorldName()
					+ "/icon" + _colorID + ".png");
			_color = Colors.getColor(_colorID - 1);
		}
	}

	@Override
	public boolean handleEvents(Input.TouchEvent e) {
		return e.type == Input.InputType.PRESSED && inBounds(e.x, e.y);
	}

	@Override
	public void update(double deltaTime) {
	}

	@Override
	public void render(Graphics graphics) {
		// Cuando el botón del OJO se pulsa, este condicional se encarga de cambiar todos los colores
		// a sus respectivas imagenes para daltónicos. Además, si el modo daltónico está activado,
		// también se encarga de quitar los números para volver al modo normal.
		if (_icon != null)
			_graphics.drawImage(_icon, _positionX, _positionY, _width, _height, _scale);
		else
			_graphics.drawCircleWithBorder(_positionX + _width * _scale / 2, _positionY + _height * _scale / 2,
					_width / 2, 1f, _scale, _color, Colors.colorValues.get(Colors.ColorName.BLACK));

		if (_gameAttributes.isEyeOpen)
			_numberText.render(graphics);
	}

	public boolean inBounds(int mouseX, int mouseY) {
		return _graphics.inBounds(_positionX, _positionY, mouseX, mouseY, _width, _height, _scale);
	}
}
