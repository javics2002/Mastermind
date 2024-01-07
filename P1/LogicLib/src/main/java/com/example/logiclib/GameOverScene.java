package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;

import java.util.ArrayList;
import java.util.List;

public class GameOverScene implements Scene {
	private Text _resultText, _attemptsText, _attemptsNumberText, _codeText;
	private Button _playAgainButton;
	private Button _menuButton;
	private Button _shareButton;
	private List<ColorSlot> _resultCombination;
	private Image _coinImage;
	private Text _moneyText;
	private Button _adButton;
	private final int _coinSize = 40;
	private Transition _transition;

	Engine _engine;
	GameAttributes _gameAttributes;

	private final int _backgroundColor;

	public GameOverScene(Engine engine, final GameAttributes gameAttributes) {
		_engine = engine;
		final Graphics graphics = _engine.getGraphics();

		// Init GameAttributes
		_gameAttributes = gameAttributes;

		int buttonColor = Colors.colorValues.get(Colors.ColorName.BACKGROUNDORANGE);

		if (GameData.Instance().getCurrentTheme() < 0) {
			_backgroundColor = Colors.colorValues.get(Colors.ColorName.BACKGROUND);
		} else {
			final Theme theme = GameData.Instance().getThemes().get(GameData.Instance().getCurrentTheme());

			_backgroundColor = Colors.parseARGB(theme.backgroundColor);
			buttonColor = Colors.parseARGB(theme.buttonColor);
		}

		// Transition
		_transition = new Transition(_engine, graphics.getWidth(), graphics.getHeight());

		// Escena
		Font resultFont = graphics.newFont("Comfortaa-Regular.ttf", 40f),
				attemptsFont = graphics.newFont("Comfortaa-Regular.ttf", 18f),
				attemptsNumberFont = graphics.newFont("Comfortaa-Regular.ttf", 24f),
				codeFont = graphics.newFont("Comfortaa-Regular.ttf", 18f);
		String resultString, attemptsString, attemptsNumberString, codeString = "Código:";

		final int buttonWidth = 430;
		final int buttonHeight = 90;
		final int posY = graphics.getLogicHeight() / 2;
		Font buttonFont = graphics.newFont("Comfortaa-Regular.ttf", 35f);

		_resultCombination = new ArrayList<>();

		if (_gameAttributes.attemptsLeft != 0) {
			// Has ganado
			resultString = "ENHORABUENA!!";
			attemptsString = "Has averiguado el código en";
			attemptsNumberString = Integer.toString(_gameAttributes.activeLayout + 1) + " intentos";

			int scale = 40;
			int padding = 6;

			for (int i = 0; i < _gameAttributes.combinationLength; i++) {
				ColorSlot cSlotX = new ColorSlot(_engine,
						(int) ((graphics.getLogicWidth() / 2) + (i - _gameAttributes.combinationLength / 2f) * (scale + padding)),
						230, scale, scale, _gameAttributes);
				_resultCombination.add(cSlotX);
				cSlotX.setColor(_gameAttributes.resultCombination.getColors()[i], _gameAttributes.isEyeOpen);
			}

			_codeText = new Text(codeString, codeFont, _engine,
					graphics.getLogicWidth() / 2 - graphics.getStringWidth(codeString, codeFont) / 2, 200, 0, true);

			_adButton = null;

			_shareButton = new Button(buttonColor, "Compartir", buttonFont, _engine,
					graphics.getLogicWidth() / 2 - buttonWidth / 2, graphics.getLogicHeight() / 2 + 30,
					buttonWidth, buttonHeight) {
				@Override
				public void callback() {
					_engine.shareScreenshot(graphics.getWidth(), graphics.getHeight());
				}
			};

			// Si te encuentras en una partida rapida
			if (_gameAttributes.selectedWorld == -1) {
				_playAgainButton = new Button(buttonColor, "Volver a Jugar", buttonFont, _engine,
						graphics.getLogicWidth() / 2 - 400 / 2, graphics.getLogicHeight() - 200, 400, 50) {
					@Override
					public void callback() {
						GameData.Instance().resetCurrentLevelData();
						_engine.saveGameData();

						Scene scene = new GameScene(_engine, _gameAttributes.attemptsNumber, _gameAttributes.attemptsNumber, _gameAttributes.combinationLength,
								_gameAttributes.colorNumber, _gameAttributes.repeatedColors, _gameAttributes.returnScene,
								_gameAttributes.selectedWorld, _gameAttributes.selectedLevelID, _gameAttributes.reward, null);
						_engine.setCurrentScene(scene);
					}
				};
			}
			// Si te encuentras en un nivel de un mundo
			else {
				_playAgainButton = new Button(buttonColor, "Siguiente Nivel", buttonFont, _engine,
						graphics.getLogicWidth() / 2 - 400 / 2, graphics.getLogicHeight() - 200, 400, 50) {
					@Override
					public void callback() {
						GameData.Instance().resetCurrentLevelData();
						_engine.saveGameData();

						int levelID = _gameAttributes.selectedLevelID + 1;
						final WorldData data = GameData.Instance().getWorldDataByIndex(_gameAttributes.selectedWorld);

						if (levelID >= data.getLevelNumber()) {
							levelID = _gameAttributes.selectedLevelID;
						}

						String levelNumber = levelID >= 9 ? Integer.toString(levelID + 1) : "0" + Integer.toString(levelID + 1);
						String levelPath = "Levels/" + data.getWorldName() + "/level_"
								+ levelNumber + ".json";

						LevelData level = _engine.jsonToObject(levelPath, LevelData.class);

						Scene scene = new GameScene(_engine, level.attempts, level.attempts, level.codeSize, level.codeOpt,
								level.repeat, _gameAttributes.returnScene, _gameAttributes.selectedWorld,
								levelID, _gameAttributes.reward, null);
						_engine.setCurrentScene(scene);
					}
				};

				if (GameData.Instance().getWorldDataByIndex(_gameAttributes.selectedWorld).getLastLevelUnlocked() <= _gameAttributes.selectedLevelID) {
					_coinImage = graphics.loadImage("UI/coin.png");

					GameData.Instance().addMoney(_gameAttributes.reward);
					String moneyString = "+" + _gameAttributes.reward + " - Total: " + Integer.toString(GameData.Instance().getMoney());

					_moneyText = new Text(moneyString, resultFont, _engine,
							graphics.getLogicWidth() / 2 - (_coinSize + graphics.getStringWidth(moneyString, resultFont)) / 2 + _coinSize,
							graphics.getLogicHeight() / 2 + graphics.getStringHeight(moneyString, resultFont) / 2 - 30, 0, true);
				}
			}

			if (gameAttributes.selectedWorld != -1) {
				final WorldData data = GameData.Instance().getWorldDataByIndex(_gameAttributes.selectedWorld);

				if (data.getLastLevelUnlocked() < data.getLevelNumber() && data.getLastLevelUnlocked() == _gameAttributes.selectedLevelID) {
					data.completeLevel();
				}
			}
		} else {
			// Has perdido
			resultString = "GAME OVER";
			attemptsString = "Te has quedado sin intentos";
			attemptsNumberString = "";

			_adButton = new Button(buttonColor, "+2 Intentos", buttonFont, _engine,
					graphics.getLogicWidth() / 2 - buttonWidth / 2, graphics.getLogicHeight() / 3,
					buttonWidth, buttonHeight) {
				@Override
				public void callback() {
					_engine.showAd();
				}
			};

			_playAgainButton = new Button(buttonColor, "Volver a Intentar", buttonFont, _engine,
					graphics.getLogicWidth() / 2 - 400 / 2, graphics.getLogicHeight() - 200, 400, 50) {
				@Override
				public void callback() {
					GameData.Instance().resetCurrentLevelData();
					_engine.saveGameData();

					Scene scene = new GameScene(_engine, _gameAttributes.attemptsNumber, _gameAttributes.attemptsNumber,
							_gameAttributes.combinationLength, _gameAttributes.colorNumber, _gameAttributes.repeatedColors,
							_gameAttributes.returnScene, _gameAttributes.selectedWorld,
							_gameAttributes.selectedLevelID, _gameAttributes.reward, null);
					_engine.setCurrentScene(scene);
				}
			};
		}

		_resultText = new Text(resultString, resultFont, _engine,
				graphics.getLogicWidth() / 2 - graphics.getStringWidth(resultString, resultFont) / 2,
				70, 0, true);
		_attemptsText = new Text(attemptsString, attemptsFont, _engine,
				graphics.getLogicWidth() / 2 - graphics.getStringWidth(attemptsString, attemptsFont) / 2,
				110, 0, true);
		_attemptsNumberText = new Text(attemptsNumberString, attemptsNumberFont, _engine,
				graphics.getLogicWidth() / 2 - graphics.getStringWidth(attemptsNumberString, attemptsNumberFont) / 2,
				160, 0, true);

		_menuButton = new Button(buttonColor, "Volver al Menú", buttonFont, _engine,
				graphics.getLogicWidth() / 2 - 400 / 2, graphics.getLogicHeight() - 100, 400, 50) {
			@Override
			public void callback() {
				GameData.Instance().resetCurrentLevelData();
				_engine.saveGameData();

				if (_gameAttributes.selectedWorld == -1) {
					Scene scene = new InitialScene(_engine);
					_engine.setCurrentScene(scene);
				} else {
					WorldScene scene = new WorldScene(_engine, _gameAttributes.selectedWorld);
					_engine.setCurrentScene(scene);
				}
			}
		};

		_transition.PlayTransition(Transition.TransitionType.fadeIn, Colors.colorValues.get(Colors.ColorName.WHITE),
				0.2f, null);
	}

	@Override
	public void render(Graphics graphics) {
		graphics.clear(_backgroundColor);

		_resultText.render(graphics);
		_attemptsText.render(graphics);
		_attemptsNumberText.render(graphics);

		if (_codeText != null) {
			_codeText.render(graphics);
		}

		if (_adButton != null) {
			_adButton.render(graphics);
		}

		if (_shareButton != null) {
			_shareButton.render(graphics);
		}

		for (int i = 0; i < _resultCombination.size(); i++) {
			_resultCombination.get(i).render(graphics);
		}

		if (_coinImage != null) {
			final int padding = 10;
			graphics.drawImage(_coinImage, graphics.getLogicWidth() / 2f
							- (_coinSize + graphics.getStringWidth(_moneyText.getText(), _moneyText.getFont())) / 2 - padding,
					graphics.getLogicHeight() / 2f - _coinSize / 2f - 30, _coinSize, _coinSize, 1f);
		}

		if (_moneyText != null) {
			_moneyText.render(graphics);
		}

		_playAgainButton.render(graphics);
		_menuButton.render(graphics);

		_transition.render(graphics);
	}

	@Override
	public void handleEvents(Input.TouchEvent event) {
		_playAgainButton.handleEvents(event);
		_menuButton.handleEvents(event);

		if (_adButton != null) {
			_adButton.handleEvents(event);
		}
		if (_shareButton != null) {
			_shareButton.handleEvents(event);
		}
	}

	public GameAttributes getGameAttributtes() {
		return _gameAttributes;
	}

	@Override
	public void update(double deltaTime) {
		_transition.update(deltaTime);
	}
}
