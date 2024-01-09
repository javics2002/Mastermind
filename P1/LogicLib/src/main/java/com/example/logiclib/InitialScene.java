package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;

public class InitialScene implements Scene {
	private final Engine _engine;

	private final Button _quickGameButton, _worldsButton, _customizeButton, _eraseProgressButton;
	private final Text _titleText;
	private final int _backgroundColor;

	public InitialScene(Engine engine) {
		_engine = engine;

		final Graphics graphics = _engine.getGraphics();

		int buttonColor = Colors.colorValues.get(Colors.ColorName.BACKGROUNDBLUE);

		if (GameData.Instance().getCurrentTheme() < 0) {
			_backgroundColor = Colors.colorValues.get(Colors.ColorName.BACKGROUND);
		} else {
			final Theme theme = GameData.Instance().getThemes().get(GameData.Instance().getCurrentTheme());

			_backgroundColor = Colors.parseARGB(theme.backgroundColor);
			buttonColor = Colors.parseARGB(theme.buttonColor);
		}

		Font _titleFont = graphics.newFont("Comfortaa-Regular.ttf", 48f);
		String title = "Master Mind";

		_titleText = new Text(title, _titleFont, _engine,
				graphics.getLogicWidth() / 2f, 200, 0, true);

		final int buttonWidth = 330;
		final int buttonHeight = 90;
		final float buttonArc = 20;
		final int quickGameButtonPositionY = graphics.getLogicHeight() / 2;
		final int paddingY = 30;

		final Scene returnScene = this;

		Font buttonFont = graphics.newFont("Comfortaa-Regular.ttf", 35f);
		_quickGameButton = new Button(buttonColor, "Partida rápida", buttonFont, _engine,
				graphics.getLogicWidth() / 2f - buttonWidth / 2f, quickGameButtonPositionY,
				buttonWidth, buttonHeight, buttonArc) {
			@Override
			public void callback() {
				_engine.appeareanceBanner(false);
				Scene scene = new DifficultyScene(_engine);
				_engine.setCurrentScene(scene);
			}
		};

		_worldsButton = new Button(buttonColor, "Explorar mundos", buttonFont, _engine,
				graphics.getLogicWidth() / 2f - buttonWidth / 2f,
				quickGameButtonPositionY + buttonHeight + paddingY,
				buttonWidth, buttonHeight, buttonArc) {
			@Override
			public void callback() {
				_engine.appeareanceBanner(false);
				Scene scene = new WorldScene(_engine, 0);
				_engine.setCurrentScene(scene);
			}
		};

		_customizeButton = new Button("UI/customize.png", _engine,
				graphics.getLogicWidth() - 60, 10,
				50, 50) {
			@Override
			public void callback() {
				_engine.appeareanceBanner(false);
				Scene scene = new ShopScene(_engine, ShopScene.ShopType.BACKGROUNDS);
				_engine.setCurrentScene(scene);
			}
		};

		Font eraseButtonFont = graphics.newFont("Comfortaa-Regular.ttf", 12f);
		_eraseProgressButton = new Button(Colors.colorValues.get(Colors.ColorName.BACKGROUNDRED),
				"Borrar progreso", eraseButtonFont, _engine, 10, 10,
				graphics.getStringWidth("Borrar progreso", eraseButtonFont) + 20, 50, 10) {
			@Override
			public void callback() {
				GameData.Instance().reset();
			}
		};
	}

	@Override
	public void render(Graphics graphics) {
		graphics.clear(_backgroundColor);

		_titleText.render(graphics);
		_quickGameButton.render(graphics);
		_worldsButton.render(graphics);
		_customizeButton.render(graphics);

		_eraseProgressButton.render(graphics);
	}

	@Override
	public void handleEvents(Input.TouchEvent event) {
		_quickGameButton.handleEvents(event);
		_worldsButton.handleEvents(event);
		_customizeButton.handleEvents(event);

		_eraseProgressButton.handleEvents(event);
	}

	@Override
	public void update(double deltaTime) {
	}
	@Override
	public void recieveADMSG() {

	}
}




