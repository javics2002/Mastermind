package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;

public class DifficultyScene implements Scene {
	private final Button _backButton;
	private final Button _easyDifficultyButton, _mediumDifficultyButton,
			_difficultDifficultyButton, _impossibleDifficultyButton;

	private final Text _titleText;
	private Transition _transition;

	Engine _engine;

	private final int _backgroundColor;

	public DifficultyScene(Engine engine) {
		_engine = engine;

		Graphics graphics = _engine.getGraphics();

		// Transition
		_transition = new Transition(_engine, graphics.getWidth(), graphics.getHeight());

		if (GameData.Instance().getCurrentTheme() < 0) {
			_backgroundColor = Colors.colorValues.get(Colors.ColorName.BACKGROUND);
		} else {
			final Theme theme = GameData.Instance().getThemes().get(GameData.Instance().getCurrentTheme());

			_backgroundColor = Colors.parseARGB(theme.backgroundColor);
		}

		// Back button
		int backbuttonScale = 40;
		_backButton = new Button("UI/back.png", _engine,
				backbuttonScale / 2f, backbuttonScale / 2f,
				backbuttonScale, backbuttonScale) {
			@Override
			public void callback() {
				_engine.appeareanceBanner(true);
				Scene scene = new InitialScene(_engine);
				_transition.PlayTransition(Transition.TransitionType.fadeOut, Colors.colorValues.get(Colors.ColorName.WHITE),
						0.2f, scene);
			}
		};

		// Title
		Font font = graphics.newFont("Comfortaa-Regular.ttf", 24f);
		String question = "¿En qué dificultad quieres jugar?";
		_titleText = new Text(question, font, _engine,
				graphics.getLogicWidth() / 2f, graphics.getLogicHeight() / 4f, 0, true);

		// Game buttons
		int gameButtonsWidth = 330;
		int gameButtonsHeight = 90;
		float gameButtonsArc = 20;
		int startingGameButtonsHeight = graphics.getLogicHeight() / 3;
		int padding = 20;
		Font buttonFont = graphics.newFont("Comfortaa-Regular.ttf", 35f);
		final Scene returnScene = this;

		_easyDifficultyButton = new Button(Colors.colorValues.get(Colors.ColorName.BACKGROUNDGREEN),
				"Fácil", buttonFont, _engine, graphics.getLogicWidth() / 2f - gameButtonsWidth / 2f,
				startingGameButtonsHeight, gameButtonsWidth, gameButtonsHeight, gameButtonsArc) {
			@Override
			public void callback() {
				Scene scene = new GameScene(_engine, 6, 6, 4,
						4, false, returnScene, -1, -1,
						0, null);
				_transition.PlayTransition(Transition.TransitionType.fadeOut,
						Colors.colorValues.get(Colors.ColorName.WHITE), 0.2f, scene);
			}
		};
		_mediumDifficultyButton = new Button(Colors.colorValues.get(Colors.ColorName.BACKGROUNDYELLOW),
				"Medio", buttonFont, _engine, graphics.getLogicWidth() / 2f - gameButtonsWidth / 2f,
				startingGameButtonsHeight + gameButtonsHeight + padding,
				gameButtonsWidth, gameButtonsHeight, gameButtonsArc) {
			@Override
			public void callback() {
				Scene scene = new GameScene(_engine, 8, 8, 4,
						6, false, returnScene, -1, -1,
						0, null);
				_transition.PlayTransition(Transition.TransitionType.fadeOut,
						Colors.colorValues.get(Colors.ColorName.WHITE), 0.2f, scene);
			}
		};
		_difficultDifficultyButton = new Button(Colors.colorValues.get(Colors.ColorName.BACKGROUNDORANGE),
				"Difícil", buttonFont, _engine, graphics.getLogicWidth() / 2f - gameButtonsWidth / 2f,
				startingGameButtonsHeight + (gameButtonsHeight + padding) * 2,
				gameButtonsWidth, gameButtonsHeight, gameButtonsArc) {
			@Override
			public void callback() {
				Scene scene = new GameScene(_engine, 10, 10, 5,
						8, true, returnScene, -1, -1,
						0, null);
				_transition.PlayTransition(Transition.TransitionType.fadeOut,
						Colors.colorValues.get(Colors.ColorName.WHITE), 0.2f, scene);
			}
		};
		_impossibleDifficultyButton = new Button(Colors.colorValues.get(Colors.ColorName.BACKGROUNDRED),
				"Imposible", buttonFont, _engine, graphics.getLogicWidth() / 2f - gameButtonsWidth / 2f,
				startingGameButtonsHeight + (gameButtonsHeight + padding) * 3,
				gameButtonsWidth, gameButtonsHeight, gameButtonsArc) {
			@Override
			public void callback() {
				Scene scene = new GameScene(_engine, 15, 15, 6,
						9, true, returnScene, -1, -1,
						0, null);
				_transition.PlayTransition(Transition.TransitionType.fadeOut,
						Colors.colorValues.get(Colors.ColorName.WHITE), 0.2f, scene);
			}
		};

		_transition.PlayTransition(Transition.TransitionType.fadeIn,
				Colors.colorValues.get(Colors.ColorName.WHITE), 0.2f, null);
	}

	@Override
	public void update(double deltaTime) {
		_transition.update(deltaTime);
	}

	@Override
	public void render(Graphics graphics) {
		graphics.clear(_backgroundColor);

		_backButton.render(graphics);
		_titleText.render(graphics);
		_easyDifficultyButton.render(graphics);
		_mediumDifficultyButton.render(graphics);
		_difficultDifficultyButton.render(graphics);
		_impossibleDifficultyButton.render(graphics);
		_transition.render(graphics);
	}

	@Override
	public void handleEvents(Input.TouchEvent event) {
		_backButton.handleEvents(event);
		_easyDifficultyButton.handleEvents(event);
		_mediumDifficultyButton.handleEvents(event);
		_difficultDifficultyButton.handleEvents(event);
		_impossibleDifficultyButton.handleEvents(event);
	}

	@Override
	public void recieveADMSG() {

	}
}




