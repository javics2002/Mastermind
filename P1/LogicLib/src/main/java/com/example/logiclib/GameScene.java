package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;

import java.util.ArrayList;
import java.util.List;

public class GameScene implements Scene {
	private final Engine _engine;
	private final GameAttributes _gameAttributes;
	private final Text _objectiveText, _attemptsText;
	private final Button _quitButton, _colorblindButton;
	private final List<CombinationLayout> _combinationLayouts;
	private final List<Combination> _combinations;
	private final List<ColorButton> _colorButtons;
	private final Transition _transition;
	private boolean _gameFinished;
	private final int _visibleLayouts = 10;

	public GameScene(Engine engine, int tryNumber, int combinationLength, int numberOfColors, boolean repeatedColors) {
		_engine = engine;
		Graphics graphics = _engine.getGraphics();

		// Init GameAttributes
		_gameAttributes = new GameAttributes();

		_gameAttributes.attemptsNumber = tryNumber;
		_gameAttributes.attemptsLeft = tryNumber;
		_gameAttributes.combinationLength = combinationLength;
		_gameAttributes.colorNumber = numberOfColors;
		_gameAttributes.repeatedColors = repeatedColors;
		_gameAttributes.activeLayout = 0;
		_gameAttributes.isEyeOpen = false;
		_gameAttributes.resultCombination = new Combination(combinationLength, numberOfColors, repeatedColors);
		_gameFinished = false;

		// Transition
		_transition = new Transition(_engine, graphics.getWidth(), graphics.getHeight());

		// Title
		final int verticalMargin = 5;
		Font objetiveFont = graphics.newFont("Comfortaa-Regular.ttf", 24f);
		String objectiveString = "Averigua el código";
		_objectiveText = new Text(objectiveString, objetiveFont, _engine, graphics.getLogicWidth() / 2,
				verticalMargin + 40, 0, true);

		// Attempts
		String attemptsString = "Te quedan " + _gameAttributes.attemptsLeft + " intentos.";
		Font attemptsFont = graphics.newFont("Comfortaa-Regular.ttf", 16f);
		_attemptsText = new Text(attemptsString, attemptsFont, _engine, graphics.getLogicWidth() / 2,
				verticalMargin + 60,
				0, true);

		// Quit button
		int buttonDimension = 50;
		int horizontalMargin = 5;
		_quitButton = new Button("UI/close.png", _engine,
				horizontalMargin, verticalMargin, buttonDimension, buttonDimension) {
			@Override
			public void callback() {
				Scene scene = new DifficultyScene(_engine);
				_transition.PlayTransition(Transition.TransitionType.fadeOut, Colors.colorValues.get(Colors.ColorName.WHITE), 0.2f, scene);
			}
		};

		// ColorBlind button
		_colorblindButton = new Button("UI/eyeClosed.png", _engine,
				graphics.getLogicWidth() - buttonDimension - horizontalMargin, verticalMargin,
				buttonDimension, buttonDimension) {
			@Override
			public void callback() {
				_gameAttributes.isEyeOpen = !_gameAttributes.isEyeOpen;
				_colorblindButton.setImage(_gameAttributes.isEyeOpen ? "UI/eyeOpened.png" : "UI/eyeClosed.png");
			}
		};

		// Combinations
		int initialHeight = 100;
		int verticalPadding = 15, scale = 40;
		_combinations = new ArrayList<>();
		_combinationLayouts = new ArrayList<>();
		for (int i = 0; i < tryNumber; i++) {
			_combinations.add(new Combination(combinationLength));
			_combinationLayouts.add(new CombinationLayout(_engine, i, combinationLength,
					graphics.getLogicWidth() / 2, initialHeight + (verticalPadding + scale) * i,
					scale, _gameAttributes, _combinations.get(i)));
		}

		// Color buttons
		int horizontalPadding = 6;
		_colorButtons = new ArrayList<>();
		for (int i = 0; i < numberOfColors; i++) {
			_colorButtons.add(new ColorButton(_engine,
					(int) (graphics.getLogicWidth() / 2 + (i - numberOfColors / 2f) * (scale + horizontalPadding)),
					graphics.getLogicHeight() - 60, scale, scale, i + 1, _gameAttributes));
		}

		_transition.PlayTransition(Transition.TransitionType.fadeIn, Colors.colorValues.get(Colors.ColorName.WHITE), 0.2f, null);
	}

	@Override
	public void update(double deltaTime) {
		_transition.update(deltaTime);

		if (_gameFinished) {
			return;
		}

		updateTriesText();

		int firstCombination = _gameAttributes.activeLayout - _visibleLayouts + 1;
		if (firstCombination < 0)
			firstCombination = 0;

		for (int i = firstCombination; i < _combinationLayouts.size() && i < firstCombination + _visibleLayouts; i++) {
			_combinationLayouts.get(i).setPositionY(100 + 55 * (i - firstCombination));
		}

		// _combinationLayouts.get(gameAttributes._activeLayout).getCurrentCombination().printCombination();
		CombinationLayout activeLayout = _combinationLayouts.get(_gameAttributes.activeLayout);
		if (activeLayout.isFull()) {
			if (_combinations.get(_gameAttributes.activeLayout).equals(_gameAttributes.resultCombination)) {
				// USER WON
				Scene gameOverScene = new GameOverScene(_engine, _gameAttributes);
				_transition.PlayTransition(Transition.TransitionType.fadeOut, Colors.colorValues.get(Colors.ColorName.WHITE), 0.2f, gameOverScene);
				_gameFinished = true;
				return;
			}

			// Hints
			Combination.HintEnum[] hints = _combinations.get(_gameAttributes.activeLayout).getHint(_gameAttributes.resultCombination);
			activeLayout.setHints(hints);

			_gameAttributes.attemptsLeft--;
			_gameAttributes.activeLayout++;

			if (_gameAttributes.attemptsLeft == 0) {
				// User LOST
				Scene gameOverScene = new GameOverScene(_engine, _gameAttributes);
				_transition.PlayTransition(Transition.TransitionType.fadeOut, Colors.colorValues.get(Colors.ColorName.WHITE), 0.2f, gameOverScene);
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
		graphics.clear(Colors.colorValues.get(Colors.ColorName.BACKGROUND));

		_objectiveText.render(graphics);
		_attemptsText.render(graphics);
		_quitButton.render(graphics);
		_colorblindButton.render(graphics);

		int firstCombination = _gameAttributes.activeLayout - _visibleLayouts + 1;
		if (firstCombination < 0)
			firstCombination = 0;

		for (int i = firstCombination; i < _combinationLayouts.size() && i < firstCombination + _visibleLayouts; i++) {
			_combinationLayouts.get(i).render(graphics);
		}

		final int colorButtonBackgroundHeight = 80;
		graphics.drawRect(0, graphics.getLogicHeight() - colorButtonBackgroundHeight,
				graphics.getLogicWidth(), colorButtonBackgroundHeight, 1f, 0xFFFAFAFA);

		for (ColorButton colorButton : _colorButtons) {
			colorButton.render(graphics);
		}

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
}
