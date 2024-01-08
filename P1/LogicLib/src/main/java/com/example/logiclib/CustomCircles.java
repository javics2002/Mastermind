package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;
import com.example.aninterface.Sound;

public class CustomCircles extends Button {
	private final int borderWidth = 5, _textCoinSeparation = 10, _coinSize = 20, _numberOfCircles = 4;

	private final boolean _skin; //True si los circulos son imágenes, false si son colores
	private boolean _selected, _adquired;
	private final int _index, _price;
	private final int _priceGap;
	private final Sound _purchaseSound;
	private final Image _coin;
	private final Text _moneyText;
	private final int[] _colors;
	private final String _packPath;
	private final Image[] _circleImages;

	//Color constructor
	CustomCircles(String[] colors, boolean selected, int index, int price, Font font, Engine engine,
	              int positionX, int positionY, int width, int height, int priceGap, Image coin, Text moneyText) {
		super(Colors.colorValues.get(selected ? Colors.ColorName.GREEN : Colors.ColorName.BLACK), Integer.toString(price), font,
				engine, positionX, positionY, width, height);

		_skin = false;
		_colors = new int[colors.length];
		for (int i = 0; i < _colors.length; i++)
			_colors[i] = Colors.parseARGB(colors[i]);

		_packPath = "";
		_circleImages = null;

		_index = index;
		_price = price;
		_selected = selected;
		_adquired = GameData.Instance().hasCircle(_index);
		_priceGap = priceGap;

		_purchaseSound = _engine.getAudio().loadSound("buy.mp3", false);
		_purchaseSound.setVolume(.5f);

		_coin = coin;
		_moneyText = moneyText;
	}

	CustomCircles(int[] colors, boolean selected, int index, int price, Font font, Engine engine,
	              int positionX, int positionY, int width, int height, int priceGap, Image coin, Text moneyText) {
		super(Colors.colorValues.get(selected ? Colors.ColorName.GREEN : Colors.ColorName.BLACK), Integer.toString(price), font,
				engine, positionX, positionY, width, height);

		_skin = false;
		_colors = new int[colors.length];
		for (int i = 0; i < _colors.length; i++)
			_colors[i] = colors[i];

		_packPath = "";
		_circleImages = null;

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

	//Skin constructor
	CustomCircles(String packPath, boolean selected, int index, int price, Font font, Engine engine,
	              int positionX, int positionY, int width, int height, int priceGap, Image coin, Text moneyText) {
		super(Colors.colorValues.get(selected ? Colors.ColorName.GREEN : Colors.ColorName.BLACK), Integer.toString(price), font,
				engine, positionX, positionY, width, height);

		_skin = true;
		_colors = null;
		_packPath = packPath;

		_circleImages = new Image[_numberOfCircles];
		for (int i = 0; i < _numberOfCircles; i++) {
			_circleImages[i] = _graphics.loadImage(packPath + "/icon" + Integer.toString(i + 1) + ".png");
		}

		_index = index;
		_price = price;
		_selected = selected;
		_adquired = GameData.Instance().hasCircle(_index);
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
		if (GameData.Instance().hasCircle(_index)) {
			_engine.getAudio().playSound(_clickSound);

			GameData.Instance().setCircles(_index);
		} else if (GameData.Instance().purchaseCircle(_index, _price)) {
			_engine.getAudio().playSound(_purchaseSound);

			_moneyText.setText(Integer.toString(GameData.Instance().getMoney()));
			_adquired = true;

			_engine.saveGameData();

			GameData.Instance().setCircles(_index);
		}
	}

	@Override
	public void update(double deltaTime) {
		_selected = GameData.Instance().getCurrentCircles() == _index;
	}

	@Override
	public void render(Graphics graphics) {
		_graphics.drawRoundedRect(_positionX - borderWidth, _positionY - borderWidth,
				_width + 2 * borderWidth, _height + 2 * borderWidth,
				_arc, _arc, _scale,
				Colors.colorValues.get(_selected ? Colors.ColorName.GREEN : Colors.ColorName.BLACK));
		_graphics.drawRoundedRect(_positionX, _positionY, _width, _height,
				_arc, _arc, _scale, Colors.colorValues.get(Colors.ColorName.BACKGROUNDORANGE));

		if (_skin) {
			for (int i = 0; i < _numberOfCircles; i++)
				_graphics.drawImage(_circleImages[i], _positionX + (i % 2) * _width / 2,
						_positionY + i / 2 * _height / 2, _width / 2, _height / 2, _scale);
		} else {
			for (int i = 0; i < _numberOfCircles; i++)
				_graphics.drawCircle(_positionX + _width / 4 + (i % 2) * _width / 2,
						_positionY + _height / 4 + i / 2 * _height / 2, _width / 4, _scale, _colors[i]);
		}

		if (!_adquired) {
			float priceTagWidth = _graphics.getStringWidth(_text, _font) + _textCoinSeparation + _coinSize;
			_graphics.drawText(_text, _font, _positionX + _width / 2 - priceTagWidth / 2,
					_positionY + _height + _priceGap / 2 + _graphics.getStringHeight(_text, _font) / 2,
					_scale, 0);
			_graphics.drawImage(_coin, _positionX + _width / 2 + priceTagWidth / 2 - _coinSize,
					_positionY + _height + _priceGap / 2 - _coinSize / 2, _coinSize, _coinSize, _scale);
		}
	}
}
