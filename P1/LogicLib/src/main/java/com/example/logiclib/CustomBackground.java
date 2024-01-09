package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;
import com.example.aninterface.Sound;

public class CustomBackground extends Button {
	private boolean _selected, _adquired;
	private final int _index, _price;
	private final int _priceGap;
	private final Sound _purchaseSound;
	private final Image _coin;
	private final Text _moneyText;

	CustomBackground(boolean selected, int index, int price, Font font, String filename,
	                 Engine engine, float positionX, float positionY, float width, float height,
	                 float arc, int priceGap, Image coin, Text moneyText) {
		super(filename, selected ? Colors.ColorName.GREEN : Colors.ColorName.BLACK,
				Integer.toString(price), font, engine, positionX, positionY, width, height, arc);

		_index = index;
		_price = price;
		_selected = selected;
		_adquired = GameData.Instance().hasBackground(_index);
		_priceGap = priceGap;

		_purchaseSound = _engine.getAudio().loadSound("buy.mp3", false);
		_purchaseSound.setVolume(.5f);

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
		if (GameData.Instance().hasBackground(_index)) {
			_audio.playSound(_purchaseSound);

			GameData.Instance().setBackground(_index);
		} else if (GameData.Instance().purchaseBackground(_index, _price)) {
			_audio.playSound(_purchaseSound);

			_moneyText.setText(Integer.toString(GameData.Instance().getMoney()));
			_adquired = true;

			_engine.saveGameData();

			GameData.Instance().setBackground(_index);
		}
	}

	@Override
	public void update(double deltaTime) {
		_selected = GameData.Instance().getCurrentBackground() == _index;
	}

	@Override
	public void render(Graphics graphics) {
		final int borderWidth = 5;

		_graphics.drawRoundedRect(_positionX - borderWidth, _positionY - borderWidth,
				_width + 2 * borderWidth, _height + 2 * borderWidth,
				_arc, _arc, _scale,
				Colors.colorValues.get(_selected ? Colors.ColorName.GREEN : Colors.ColorName.BLACK));
		_graphics.drawImage(_image, _positionX, _positionY, _width, _height, _scale);

		if (!_adquired) {
			final int textCoinSeparation = 10;
			final int coinSize = 20;

			float priceTagWidth = _graphics.getStringWidth(_text, _font) + textCoinSeparation + coinSize;
			_graphics.drawText(_text, _font, _positionX + _width / 2 - priceTagWidth / 2f,
					_positionY + _height + _priceGap / 2f + _graphics.getStringHeight(_text, _font) / 2,
					_scale, 0);
			_graphics.drawImage(_coin, _positionX + _width / 2 + priceTagWidth / 2f - coinSize,
					_positionY + _height + _priceGap / 2f - coinSize / 2f, coinSize, coinSize, _scale);
		}
	}
}
