package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;

public class ShopScene implements Scene {
	private final Graphics _graphics;
	private final Engine _engine;

	public enum ShopType {BACKGROUNDS, CIRCLES, THEMES}

	public final ShopType _shopType;
	private final int _backgroundsNumber, _circlesNumber, _themesNumber;
	private final Button _backButton, _prevShopButton, _nextShopButton, _instantMoneyButton;
	private final Image _coinImage;
	private final int _coinSize = 20;
	private final Text _titleText, _moneyText;

	final int _barHeight = 80;
	final int _padding = 20;

	private CustomBackground[] _backgroundButtons;
	private CustomCircles[] _circlesButtons;
	private CustomTheme[] _themeButtons;
	private int _backgroundColor;
	private Transition _transition;

	public ShopScene(Engine engine, final ShopType shopType, boolean playInitialFadeIn) {
		_engine = engine;
		_graphics = _engine.getGraphics();
		_shopType = shopType;

		// Transition
		_transition = new Transition(_engine, _graphics.getWidth(), _graphics.getHeight());

		// Back button
		int backbuttonScale = 40;
		_backButton = new Button("UI/back.png", _engine,
				_padding, _barHeight / 2f - backbuttonScale / 2f,
				backbuttonScale, backbuttonScale) {
			@Override
			public void callback() {
				_engine.appeareanceBanner(true);
				Scene scene = new InitialScene(_engine);
				_transition.PlayTransition(Transition.TransitionType.fadeOut,
						Colors.colorValues.get(Colors.ColorName.WHITE), 0.2f, scene);
			}
		};

		if (GameData.Instance().getCurrentTheme() < 0) {
			_backgroundColor = Colors.colorValues.get(Colors.ColorName.BACKGROUND);
		} else {
			final Theme theme = GameData.Instance().getThemes().get(GameData.Instance().getCurrentTheme());

			_backgroundColor = Colors.parseARGB(theme.backgroundColor);
		}

		// Title
		Font font = _graphics.newFont("Comfortaa-Regular.ttf", 24f);
		String shopTitle = "";
		switch (_shopType) {
			case BACKGROUNDS:
				shopTitle = "Fondos";
				break;
			case CIRCLES:
				shopTitle = "Fichas";
				break;
			case THEMES:
				shopTitle = "Temas";
				break;
			default:
				break;
		}
		float titleWidth = _graphics.getStringWidth(shopTitle, font);
		_titleText = new Text(shopTitle, font, _engine,
				_graphics.getLogicWidth() / 2f, _barHeight / 2f, 0, true);

		final int worlds = _engine.filesInFolder("Levels");
		_prevShopButton = new Button("UI/prevWorld.png", _engine,
				_graphics.getLogicWidth() / 2f - titleWidth / 2 - _padding - backbuttonScale,
				_barHeight / 2f - backbuttonScale / 2f, backbuttonScale, backbuttonScale) {
			@Override
			public void callback() {
				Scene scene = new ShopScene(_engine,
						ShopType.values()[(shopType.ordinal() + ShopType.values().length - 1) % ShopType.values().length], false);
				_engine.setCurrentScene(scene);
			}
		};

		_nextShopButton = new Button("UI/nextWorld.png", _engine,
				_graphics.getLogicWidth() / 2f + titleWidth / 2 + _padding, _barHeight / 2f - backbuttonScale / 2f,
				backbuttonScale, backbuttonScale) {
			@Override
			public void callback() {
				Scene scene = new ShopScene(_engine, ShopType.values()[(shopType.ordinal() + 1) % ShopType.values().length], false);
				_engine.setCurrentScene(scene);
			}
		};

		_coinImage = _graphics.loadImage("UI/coin.png");

		String moneyString = Integer.toString(GameData.Instance().getMoney());
		_moneyText = new Text(moneyString, font, _engine,
				_graphics.getLogicWidth() - _graphics.getStringWidth(moneyString, font) - _coinSize - _padding - 5,
				_barHeight / 2f + _graphics.getStringHeight(moneyString, font) / 2, 0, false);

		_instantMoneyButton = new Button("UI/pijo.png", _engine,
				_graphics.getLogicWidth() / 2f + titleWidth / 2 + _padding + backbuttonScale,
				_barHeight / 2f - backbuttonScale / 2f, backbuttonScale, backbuttonScale) {
			@Override
			public void callback() {
				GameData.Instance().addMoney(100);

				Font font = _graphics.newFont("Comfortaa-Regular.ttf", 24f);

				String moneyString = Integer.toString(GameData.Instance().getMoney());
				_moneyText.setText(moneyString);
				_moneyText.setPosition(
						_graphics.getLogicWidth() - _graphics.getStringWidth(moneyString, font) - _coinSize - _padding - 5,
						_barHeight / 2f + _graphics.getStringHeight(moneyString, font) / 2);
			}
		};

		// Product buttons
		Font buttonFont = _graphics.newFont("Comfortaa-Regular.ttf", 25f);

		final int priceGap = 50;
		final float buttonArc = 10;

		switch (_shopType) {
			case BACKGROUNDS:
				_backgroundsNumber = GameData.Instance().getBackgrounds().size();
				_circlesNumber = 0;
				_themesNumber = 0;

				final int backgroundsPerRow = (int) Math.ceil(Math.sqrt((_backgroundsNumber + 1) * 3f / 2f));
				final int backgroundWidth = (_graphics.getLogicWidth() - (backgroundsPerRow + 1) * _padding) / backgroundsPerRow;
				final int backgroundHeight = backgroundWidth * 3 / 2;

				_backgroundButtons = new CustomBackground[_backgroundsNumber + 1];

				_backgroundButtons[0] = new CustomBackground(-1 == GameData.Instance().getCurrentBackground(),
						-1, 0, buttonFont, "UI/defaultProduct.png", _engine,
						_padding, _barHeight + _padding, backgroundWidth, backgroundHeight,
						buttonArc, priceGap, _coinImage, _moneyText);

				for (int i = 1; i <= _backgroundsNumber; i++) {
					int row = i / backgroundsPerRow;
					int column = i % backgroundsPerRow;

					final Background background = GameData.Instance().getBackgrounds().get(i - 1);

					_backgroundButtons[i] = new CustomBackground(i - 1 == GameData.Instance().getCurrentBackground(),
							i - 1, background.price, buttonFont, background.image, _engine,
							_padding + column * (backgroundWidth + _padding),
							_barHeight + _padding + row * (backgroundHeight + _padding + priceGap),
							backgroundWidth, backgroundHeight, buttonArc, priceGap, _coinImage, _moneyText);
				}
				break;
			case CIRCLES:
				_circlesNumber = GameData.Instance().getCircles().size();
				_backgroundsNumber = 0;
				_themesNumber = 0;

				//Tienen que caber en un cuadrado, teniendo el cuenta el boton para quitar skin
				final int circlesPerRow = (int) Math.ceil(Math.sqrt(_circlesNumber + 1));
				final int circlesSize = (_graphics.getLogicWidth() - (circlesPerRow + 1) * _padding) / circlesPerRow;

				_circlesButtons = new CustomCircles[_circlesNumber + 1];

				int[] defaultColors = new int[9];
				for (int i = 0; i < 9; i++) {
					defaultColors[i] = Colors.getColor(i);
				}
				_circlesButtons[0] = new CustomCircles(defaultColors, -1 == GameData.Instance().getCurrentCircles(),
						-1, 0, buttonFont, _engine, _padding, _barHeight + _padding,
						circlesSize, circlesSize, buttonArc, priceGap, _coinImage, _moneyText);

				for (int i = 1; i <= _circlesNumber; i++) {
					int row = i / circlesPerRow;
					int column = i % circlesPerRow;

					final Circles circles = GameData.Instance().getCircles().get(i - 1);

					if (circles.skin) {
						_circlesButtons[i] = new CustomCircles(circles.packPath,
								i - 1 == GameData.Instance().getCurrentCircles(),
								i - 1, circles.price, buttonFont, _engine,
								_padding + column * (circlesSize + _padding),
								_barHeight + _padding + row * (circlesSize + _padding + priceGap),
								circlesSize, circlesSize, buttonArc, priceGap, _coinImage, _moneyText);
					} else {
						_circlesButtons[i] = new CustomCircles(circles.colors, i - 1 == GameData.Instance().getCurrentCircles(),
								i - 1, circles.price, buttonFont, _engine,
								_padding + column * (circlesSize + _padding),
								_barHeight + _padding + row * (circlesSize + _padding + priceGap),
								circlesSize, circlesSize, buttonArc, priceGap, _coinImage, _moneyText);
					}
				}
				break;
			case THEMES:
				_themesNumber = GameData.Instance().getThemes().size();
				_circlesNumber = 0;
				_backgroundsNumber = 0;

				final int themesPerRow = (int) Math.ceil(Math.sqrt(_themesNumber + 1));
				final int themesSize = (_graphics.getLogicWidth() - (themesPerRow + 1) * _padding) / themesPerRow;

				_themeButtons = new CustomTheme[_themesNumber + 1];

				_themeButtons[0] = new CustomTheme(-1 == GameData.Instance().getCurrentTheme(),
						-1, 0, Colors.ColorName.BACKGROUND, Colors.ColorName.BACKGROUNDBLUE,
						buttonFont, _engine, _padding, _barHeight + _padding,
						themesSize, themesSize, buttonArc, priceGap, _coinImage, _moneyText);

				for (int i = 1; i <= _themesNumber; i++) {
					int row = i / themesPerRow;
					int column = i % themesPerRow;

					final Theme theme = GameData.Instance().getThemes().get(i - 1);

					_themeButtons[i] = new CustomTheme(i - 1 == GameData.Instance().getCurrentTheme(),
							i - 1, theme.price, theme.backgroundColor, theme.buttonColor, buttonFont, _engine,
							_padding + column * (themesSize + _padding),
							_barHeight + _padding + row * (themesSize + _padding + priceGap),
							themesSize, themesSize, buttonArc, priceGap, _coinImage, _moneyText);
				}
				break;
			default:
				_backgroundsNumber = 0;
				_circlesNumber = 0;
				_themesNumber = 0;
				break;
		}

		if (playInitialFadeIn){
			_transition.PlayTransition(Transition.TransitionType.fadeIn,
					Colors.colorValues.get(Colors.ColorName.WHITE), 0.2f, null);
		}
	}

	@Override
	public void update(double deltaTime) {
		_transition.update(deltaTime);
		switch (_shopType) {
			case BACKGROUNDS:
				for (int i = 0; i <= _backgroundsNumber; i++)
					_backgroundButtons[i].update(deltaTime);
				break;
			case CIRCLES:
				for (int i = 0; i <= _circlesNumber; i++)
					_circlesButtons[i].update(deltaTime);
				break;
			case THEMES:
				for (int i = 0; i <= _themesNumber; i++)
					_themeButtons[i].update(deltaTime);
				break;
			default:
				break;
		}

		if (GameData.Instance().getCurrentTheme() < 0) {
			_backgroundColor = Colors.colorValues.get(Colors.ColorName.BACKGROUND);
		} else {
			final Theme theme = GameData.Instance().getThemes().get(GameData.Instance().getCurrentTheme());

			_backgroundColor = Colors.parseARGB(theme.backgroundColor);
		}
	}

	@Override
	public void render(Graphics graphics) {
		_graphics.clear(_backgroundColor);

		_backButton.render(graphics);
		_titleText.render(graphics);
		_prevShopButton.render(graphics);
		_nextShopButton.render(graphics);
		_moneyText.render(graphics);
		_instantMoneyButton.render(graphics);
		graphics.drawImage(_coinImage, graphics.getLogicWidth() - _padding - _coinSize,
				_barHeight / 2f - _coinSize / 2f, _coinSize, _coinSize, 1f);

		switch (_shopType) {
			case BACKGROUNDS:
				for (int i = 0; i <= _backgroundsNumber; i++)
					_backgroundButtons[i].render(graphics);
				break;
			case CIRCLES:
				for (int i = 0; i <= _circlesNumber; i++)
					_circlesButtons[i].render(graphics);
				break;
			case THEMES:
				for (int i = 0; i <= _themesNumber; i++)
					_themeButtons[i].render(graphics);
				break;
			default:
				break;
		}

		_transition.render(graphics);
	}

	@Override
	public void handleEvents(Input.TouchEvent event) {
		_backButton.handleEvents(event);
		_prevShopButton.handleEvents(event);
		_nextShopButton.handleEvents(event);
		_moneyText.handleEvents(event);
		_instantMoneyButton.handleEvents(event);

		switch (_shopType) {
			case BACKGROUNDS:
				for (int i = 0; i <= _backgroundsNumber; i++)
					_backgroundButtons[i].handleEvents(event);
				break;
			case CIRCLES:
				for (int i = 0; i <= _circlesNumber; i++)
					_circlesButtons[i].handleEvents(event);
				break;
			case THEMES:
				for (int i = 0; i <= _themesNumber; i++)
					_themeButtons[i].handleEvents(event);
				break;
			default:
				break;
		}
	}
	@Override
	public void recieveADMSG() {

	}
}