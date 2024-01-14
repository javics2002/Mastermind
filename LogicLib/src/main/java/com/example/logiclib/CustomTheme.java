package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;
import com.example.aninterface.Sound;

import java.io.ObjectOutputStream;

public class CustomTheme extends Button {
	private final int borderWidth = 5, _textCoinSeparation = 10, _coinSize = 20;

	private boolean _selected, _adquired;
	private int _index, _price;
	private int _priceGap;
	private final Sound _purchaseSound;
	private final Image _coin;
	private final Text _moneyText;
	private final int _backgroundColor, _buttonColor;

	CustomTheme(boolean selected, int index, int price, String backgroundColor, String buttonColor,
	            Font font, Engine engine, float positionX, float positionY, float width, float height,
	            float arc, int priceGap, Image coin, Text moneyText) {
		super(Colors.colorValues.get(selected ? Colors.ColorName.GREEN : Colors.ColorName.BLACK),
				Integer.toString(price), font, engine, positionX, positionY, width, height, arc);

		_index = index;
		_price = price;
		_selected = selected;
		_adquired = GameData.Instance().hasTheme(_index);
		_priceGap = priceGap;

		_backgroundColor = Colors.parseARGB(backgroundColor);
		_buttonColor = Colors.parseARGB(buttonColor);

		_purchaseSound = _engine.getAudio().loadSound("buy.mp3", false);
		_purchaseSound.setVolume(.5f);

		_arc = 10;

		_coin = coin;
		_moneyText = moneyText;
	}

	CustomTheme(boolean selected, int index, int price, Colors.ColorName backgroundColor,
	            Colors.ColorName buttonColor, Font font, Engine engine, float positionX, float positionY,
	            float width, float height, float arc, int priceGap, Image coin, Text moneyText) {
		super(Colors.colorValues.get(selected ? Colors.ColorName.GREEN : Colors.ColorName.BLACK),
				Integer.toString(price), font, engine, positionX, positionY, width, height, arc);

		_index = index;
		_price = price;
		_selected = selected;
		_adquired = GameData.Instance().hasTheme(_index);
		_priceGap = priceGap;

		_backgroundColor = Colors.colorValues.get(backgroundColor);
		_buttonColor = Colors.colorValues.get(buttonColor);

		_purchaseSound = _engine.getAudio().loadSound("buy.mp3", false);
		_purchaseSound.setVolume(.5f);

		_arc = 10;

		_coin = coin;
		_moneyText = moneyText;
	}

	@Override
	public boolean handleEvents(Input.TouchEvent event) {
		if (event.type == Input.InputType.PRESSED && inBounds(event.x, event.y)) {
			callback();

			return true;
		}

		return false;
	}

	@Override
	public void callback() {
		if (GameData.Instance().hasTheme(_index)) {
			_engine.getAudio().playSound(_clickSound);

			GameData.Instance().setTheme(_index);
		} else if (GameData.Instance().purchaseTheme(_index, _price)) {
			_engine.getAudio().playSound(_purchaseSound);

			_moneyText.setText(Integer.toString(GameData.Instance().getMoney()));
			_adquired = true;

			// Guardar datos
			GameData.Instance().saveGameData(_engine);

			GameData.Instance().setTheme(_index);
		}
	}

	@Override
	public void update(double deltaTime) {
		_selected = GameData.Instance().getCurrentTheme() == _index;
	}

	@Override
	public void render(Graphics graphics) {
		_graphics.drawRoundedRect(_positionX - borderWidth, _positionY - borderWidth,
				_width + 2 * borderWidth, _height + 2 * borderWidth,
				_arc, _arc, _scale,
				Colors.colorValues.get(_selected ? Colors.ColorName.GREEN : Colors.ColorName.BLACK));

		_graphics.drawRect(_positionX, _positionY, _width, _height / 2, _scale, _backgroundColor);
		_graphics.drawRect(_positionX, _positionY + _height / 2, _width, _height / 2,
				_scale, _buttonColor);

		if (!_adquired) {
			float priceTagWidth = _graphics.getStringWidth(_text, _font) + _textCoinSeparation + _coinSize;
			_graphics.drawText(_text, _font, _positionX + _width / 2 - priceTagWidth / 2,
					_positionY + _height + _priceGap / 2f + _graphics.getStringHeight(_text, _font) / 2, _scale, 0);
			_graphics.drawImage(_coin, _positionX + _width / 2 + priceTagWidth / 2 - _coinSize,
					_positionY + _height + _priceGap / 2f - _coinSize / 2f, _coinSize, _coinSize, _scale);
		}
	}
}
