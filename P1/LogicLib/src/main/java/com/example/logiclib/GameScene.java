package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GameScene implements Scene {
	private final Engine _engine;
	private final Graphics _graphics;
	private final GameAttributes _gameAttributes;
	private final Text _objectiveText, _attemptsText;
	private final Button _quitButton, _colorblindButton;
	private final List<CombinationLayout> _combinationLayouts;
	private final List<Combination> _combinations;
	private final List<ColorButton> _colorButtons;
	private final Image _backgroundImage;
	private final int _backgroundColor;
	private final Transition _transition;
	private boolean _gameFinished;

	private final int _visibleLayouts = 10;

	public GameScene(Engine engine, int tryNumber, int attemptsLeft, int combinationLength, int numberOfColors,
	                 boolean repeatedColors, final Scene returnScene, int worldId, int selectedLevelID, int reward, Combination cResult) {
		_engine = engine;
		_graphics = _engine.getGraphics();

		// Init GameAttributes
		_gameAttributes = new GameAttributes();

		_gameAttributes.reward = reward;
		_gameAttributes.attemptsNumber = tryNumber;
		_gameAttributes.attemptsLeft = attemptsLeft;
		_gameAttributes.combinationLength = combinationLength;
		_gameAttributes.colorNumber = numberOfColors;
		_gameAttributes.repeatedColors = repeatedColors;
		_gameAttributes.isEyeOpen = false;

		_gameFinished = false;

		// Transition
		_transition = new Transition(_engine, _graphics.getWidth(), _graphics.getHeight());

		if (cResult == null)
			_gameAttributes.resultCombination = new Combination(combinationLength, numberOfColors, repeatedColors);
		else _gameAttributes.resultCombination = cResult;

		_gameAttributes.returnScene = returnScene;
		_gameAttributes.selectedWorld = worldId;
		_gameAttributes.selectedLevelID = selectedLevelID;

		// Guarda datos del nivel
		if (GameData.Instance().getCurrentLevelData() == null) {
			_gameAttributes.activeLayout = 0;

			LevelData data = new LevelData();
			data.attempts = tryNumber;
			data.initialAttemptsNumber = tryNumber;
			data.codeSize = combinationLength;
			data.codeOpt = numberOfColors;
			data.repeat = repeatedColors;

			data.resultCombination = _gameAttributes.resultCombination;
			data.leftAttemptsNumber = _gameAttributes.attemptsLeft;
			data.combinations = new ArrayList<>();

			data.worldID = _gameAttributes.selectedWorld;
			data.levelID = _gameAttributes.selectedLevelID;
			data.reward = _gameAttributes.reward;

			GameData.Instance().setCurrentLevelData(data);
		} else {
			_gameAttributes.activeLayout = _gameAttributes.attemptsNumber - _gameAttributes.attemptsLeft;
			_gameAttributes.attemptsNumber = GameData.Instance().getCurrentLevelData().initialAttemptsNumber;
		}

		if (GameData.Instance().getCurrentTheme() < 0) {
			_backgroundColor = Colors.colorValues.get(Colors.ColorName.BACKGROUND);
		} else {
			final Theme theme = GameData.Instance().getThemes().get(GameData.Instance().getCurrentTheme());

			_backgroundColor = Colors.parseARGB(theme.backgroundColor);
		}

		// Titulo
		int buttonDimension = 50;
		int horizontalMargin = 5;
		int verticalMargin = 5;
		Font objetiveFont = _graphics.newFont("Comfortaa-Regular.ttf", 24f);
		String objectiveString = "Averigua el código";
		_objectiveText = new Text(objectiveString, objetiveFont, _engine, _graphics.getLogicWidth() / 2f,
				verticalMargin + buttonDimension / 4f, 0, true);

		// Intentos
		String attemptsString = "Te quedan " + _gameAttributes.attemptsLeft + " intentos.";
		Font attemptsFont = _graphics.newFont("Comfortaa-Regular.ttf", 16f);
		_attemptsText = new Text(attemptsString, attemptsFont, _engine, _graphics.getLogicWidth() / 2f,
				verticalMargin + 3 * buttonDimension / 4f, 0, true);

		// Boton de salir
		_quitButton = new Button("UI/close.png", _engine,
				horizontalMargin, verticalMargin, buttonDimension, buttonDimension) {
			@Override
			public void callback() {
				GameData.Instance().resetCurrentLevelData();
				// Guardar datos
				GameData.Instance().saveGameData(_engine);

				if (_gameAttributes.selectedWorld == -1) {
					Scene scene = new InitialScene(_engine);
					_transition.PlayTransition(Transition.TransitionType.fadeOut,
							Colors.colorValues.get(Colors.ColorName.WHITE), 0.2f, scene);
				} else {
					WorldScene scene = new WorldScene(_engine, _gameAttributes.selectedWorld, true);
					_transition.PlayTransition(Transition.TransitionType.fadeOut,
							Colors.colorValues.get(Colors.ColorName.WHITE), 0.2f, scene);
				}

				_gameFinished = true;
			}
		};

		// Botón daltónicos
		_colorblindButton = new Button("UI/eyeClosed.png", _engine,
				_graphics.getLogicWidth() - buttonDimension - horizontalMargin, verticalMargin,
				buttonDimension, buttonDimension) {
			@Override
			public void callback() {
				_gameAttributes.isEyeOpen = !_gameAttributes.isEyeOpen;
				_colorblindButton.setImage(_gameAttributes.isEyeOpen ? "UI/eyeOpened.png" : "UI/eyeClosed.png");
			}
		};

		// Combinación
		int initialHeight = 100;
		int verticalPadding = 15, scale = 40;
		_combinations = new ArrayList<>();
		_combinationLayouts = new ArrayList<>();
		int cont = 0;

		for (int i = 0; i < GameData.Instance().getCurrentLevelData().combinations.size(); i++) {
			Combination newCombination = new Combination(GameData.Instance().getCurrentLevelData().combinations.get(i).getColors());
			_combinations.add(newCombination);
			_combinationLayouts.add(new CombinationLayout(_engine, i, combinationLength,
					_graphics.getLogicWidth() / 2f, initialHeight + (verticalPadding + scale) * i,
					scale, _gameAttributes, newCombination));

			// Actualizar pistas
			Combination.HintEnum[] hints = _combinations.get(i).getHint(_gameAttributes.resultCombination);
			_combinationLayouts.get(i).setHints(hints);

			_combinationLayouts.get(i).updateCombination(_gameAttributes.isEyeOpen);

			cont++;
		}

		if (GameData.Instance().getCurrentLevelData().currentCombination != null) {
			Combination newCombination = new Combination(GameData.Instance().getCurrentLevelData().currentCombination.getColors());
			_combinations.add(newCombination);
			CombinationLayout newCombinationLayout = new CombinationLayout(_engine, cont, combinationLength,
					_graphics.getLogicWidth() / 2f, initialHeight + (verticalPadding + scale) * cont,
					scale, _gameAttributes, newCombination);
			_combinationLayouts.add(newCombinationLayout);

			newCombinationLayout.updateCombination(_gameAttributes.isEyeOpen);

			cont++;
		}

		for (int i = cont; i < tryNumber; i++) {
			Combination newCombination = new Combination(combinationLength);
			_combinations.add(newCombination);

			CombinationLayout newCombinationLayout =new CombinationLayout(_engine, i, combinationLength,
					_graphics.getLogicWidth() / 2f, initialHeight + (verticalPadding + scale) * i,
					scale, _gameAttributes, newCombination);
			_combinationLayouts.add(newCombinationLayout);

			newCombinationLayout.updateCombination(_gameAttributes.isEyeOpen);
		}

		// Botones de colores
		int horizontalPadding = 6;
		_colorButtons = new ArrayList<>();
		for (int i = 0; i < numberOfColors; i++) {
			_colorButtons.add(new ColorButton(_engine,
					(int) (_graphics.getLogicWidth() / 2 + (i - numberOfColors / 2f) * (scale + horizontalPadding)),
					_graphics.getLogicHeight() - 60, scale, scale, i + 1, _gameAttributes));
		}

		if (worldId != -1) {
			_backgroundImage =
					_graphics.loadImage(GameData.Instance().getWorldDataByIndex(worldId).getWorldName() + "/background.png");
		} else if (GameData.Instance().getCurrentBackground() >= 0) {
			final Background background = GameData.Instance().getBackgrounds().get(GameData.Instance().getCurrentBackground());

			_backgroundImage = _graphics.loadImage(background.image);
		} else {
			_backgroundImage = null;
		}

		_transition.PlayTransition(Transition.TransitionType.fadeIn,
				Colors.colorValues.get(Colors.ColorName.WHITE), 0.2f, null);
	}

	@Override
	public void update(double deltaTime) {
		_transition.update(deltaTime);

		if (_gameFinished) {
			return;
		}

		updateTriesText();

		CombinationLayout activeLayout = _combinationLayouts.get(_gameAttributes.activeLayout);
		GameData.Instance().getCurrentLevelData().currentCombination = _combinations.get(_gameAttributes.activeLayout);
		GameData.Instance().getCurrentLevelData().leftAttemptsNumber = _gameAttributes.attemptsLeft;

		if (activeLayout.isFull()) {
			if (_combinations.get(_gameAttributes.activeLayout).equals(_gameAttributes.resultCombination)) {
				// USER WON
				Scene gameOverScene = new GameOverScene(_engine, _gameAttributes);
				_transition.PlayTransition(Transition.TransitionType.fadeOut, Colors.colorValues.get(Colors.ColorName.WHITE),
						0.2f, gameOverScene);
				_gameFinished = true;
				return;
			}

			Combination.HintEnum[] hints = _combinations.get(_gameAttributes.activeLayout).getHint(_gameAttributes.resultCombination);
			activeLayout.setHints(hints);

			GameData.Instance().getCurrentLevelData().combinations.add(_combinations.get(_gameAttributes.activeLayout));

			_gameAttributes.attemptsLeft--;
			_gameAttributes.activeLayout++;

			if (_gameAttributes.attemptsLeft == 0) {
				// User LOST
				GameData.Instance().getCurrentLevelData().currentCombination = new Combination(_gameAttributes.combinationLength);

				Scene gameOverScene = new GameOverScene(_engine, _gameAttributes);
				_transition.PlayTransition(Transition.TransitionType.fadeOut, Colors.colorValues.get(Colors.ColorName.WHITE),
						0.2f, gameOverScene);
				_gameFinished = true;
			}
		}

		for (CombinationLayout combination : _combinationLayouts) {
			combination.update(deltaTime);
		}

		for (ColorButton colorButton : _colorButtons) {
			colorButton.update(deltaTime);
		}
	}

	@Override
	public void render(Graphics graphics) {
		_graphics.clear(_backgroundColor);

		if (_backgroundImage != null)
			_graphics.drawImage(_backgroundImage, 0, 60,
					_graphics.getLogicWidth(), _graphics.getLogicHeight() - 60, 1f);

		_objectiveText.render(graphics);
		_attemptsText.render(graphics);
		_quitButton.render(graphics);
		_colorblindButton.render(graphics);

		int firstCombination = _gameAttributes.activeLayout - _visibleLayouts + 1;
		if (firstCombination < 0)
			firstCombination = 0;

		for (int i = firstCombination; i < _combinationLayouts.size() && i < firstCombination + _visibleLayouts; i++) {
			_combinationLayouts.get(i).setPositionY(100 + (15 + 40) * (i - firstCombination));
			_combinationLayouts.get(i).render(graphics);
		}

		final int colorButtonBackgroundHeight = 80;
		graphics.drawRect(0, graphics.getLogicHeight() - colorButtonBackgroundHeight,
				graphics.getLogicWidth(), 1f,
				colorButtonBackgroundHeight, Colors.colorValues.get(Colors.ColorName.TRASPARENTBACKGROUND));

		for (ColorButton colorButton : _colorButtons)
			colorButton.render(graphics);

		_transition.render(graphics);
	}

	@Override
	public void handleEvents(Input.TouchEvent event) {
		if (_gameFinished) {
			return;
		}

		_colorblindButton.handleEvents(event);
		_quitButton.handleEvents(event);

		// Detectar click en colores ya colocados
		// Sirve para borrarlos
		if (_combinationLayouts.get(_gameAttributes.activeLayout).handleEvents(event)) {
			_combinationLayouts.get(_gameAttributes.activeLayout).updateCombination(_gameAttributes.isEyeOpen);
		}

		// Cuando detecta un click en un color, se coloca en el primer hueco posible.
		for (ColorButton colorButton : _colorButtons) {
			if (colorButton.handleEvents(event)) {
				int index = _combinations.get(_gameAttributes.activeLayout).setNextColor(colorButton._colorID);
				_combinationLayouts.get(_gameAttributes.activeLayout).updateCombination(_gameAttributes.isEyeOpen);
				_combinationLayouts.get(_gameAttributes.activeLayout).animateSlot(index);

				// Guardar datos
				GameData.Instance().saveGameData(_engine);
				break;
			}
		}
	}

	private void updateTriesText() {
		String attemptsString = "Te quedan " + _gameAttributes.attemptsLeft + " intentos.";
		if (_gameAttributes.attemptsLeft == 1) {
			attemptsString = "Este es tu último intento!";
		}

		_attemptsText.setText(attemptsString);
	}
	@Override
	public void recieveADMSG() {

	}
}
