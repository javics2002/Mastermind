package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Image;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;

public class WorldScene implements Scene {
    private final Button _backButton;
    private final Button _prevWorldButton, _nextWorldButton;
    private final Button _unlockNextLevelButton; //Hack
    private final Level[] _levelButtons;
    private final Text _titleText;
    final int _numLevels;
    private WorldData _worldData;
    private final Graphics _graphics;
    private final Image _backgroundImage;
    Engine _engine;

    private final int _backgroundColor;

    final int _barHeight = 80;

    public WorldScene(Engine engine, final int worldId) {
        _engine = engine;

        _graphics = _engine.getGraphics();

        if(GameData.Instance().getCurrentTheme() < 0){
            _backgroundColor = Colors.colorValues.get(Colors.ColorName.BACKGROUND);
        }
        else{
            final Theme theme = _engine.jsonToObject("Shop/Themes/themes_0"
                    + Integer.toString(GameData.Instance().getCurrentTheme() + 1) + ".json", Theme.class);

            _backgroundColor = Colors.parseARGB(theme.backgroundColor);
        }

        final int padding = 20;
        // Back button
        int backbuttonScale = 40;
        _backButton = new Button("UI/back.png", _engine,
                padding, _barHeight / 2 - backbuttonScale / 2,
                backbuttonScale, backbuttonScale) {
            @Override
            public void callback() {
                Scene scene = new InitialScene(_engine);
                _engine.setCurrentScene(scene);
            }
        };

        // Title
        Font font = _graphics.newFont("Comfortaa-Regular.ttf", 24f);
        String worldTitle = GameData.Instance().getWorldDataByIndex(worldId).getWorldName();
        int titleWidth = _graphics.getStringWidth(worldTitle, font);
        _titleText = new Text(worldTitle, font, _engine,
                _graphics.getLogicWidth() / 2 - titleWidth / 2,
                _barHeight / 2 + _graphics.getStringHeight(worldTitle, font) / 2, 0);

        // Worlds
        final int numberOfWorlds = GameData.Instance().numberOfWorlds();

        _prevWorldButton = new Button("UI/prevWorld.png", _engine,
                _graphics.getLogicWidth() / 2 - titleWidth / 2 - padding - backbuttonScale,
                _barHeight / 2 - backbuttonScale / 2, backbuttonScale, backbuttonScale) {
            @Override
            public void callback() {
                int prevWorldId = (worldId - 1) % numberOfWorlds;
                if(prevWorldId < 0)
                    prevWorldId += numberOfWorlds;

                Scene scene = new WorldScene(_engine, prevWorldId);
                _engine.setCurrentScene(scene);
            }
        };

        _nextWorldButton = new Button("UI/nextWorld.png", _engine,
                _graphics.getLogicWidth() / 2 + titleWidth / 2 + padding, _barHeight / 2 - backbuttonScale / 2,
                backbuttonScale, backbuttonScale) {
            @Override
            public void callback() {
                int nextWorldId = (worldId % numberOfWorlds) + 1;
                if (nextWorldId >= numberOfWorlds) {
                    nextWorldId = 0;
                }
                Scene scene = new WorldScene(_engine, nextWorldId);
                _engine.setCurrentScene(scene);
            }
        };

        _worldData = GameData.Instance().getWorldDataByIndex(worldId);

        final int lastLevelUnlocked = _worldData.getLastLevelUnlocked();

        // Game buttons
        final int levelsPerRow = 3;
        final int gameButtonsSize = (_graphics.getLogicWidth() - (levelsPerRow + 1) * padding) / levelsPerRow;

        Font buttonFont = _graphics.newFont("Comfortaa-Regular.ttf", 35f);

        _numLevels = _worldData.getLevelNumber();

        _levelButtons = new Level[_numLevels];
        final Scene returnScene = this;

        for(int i = 0; i < _numLevels; i++){
            int row = i / levelsPerRow;
            int column = i % levelsPerRow;

            String levelNumber = i >= 9 ? Integer.toString(i + 1) : "0" + Integer.toString(i + 1);
            final LevelData level = _engine.jsonToObject("Levels/" + _worldData.getWorldName() + "/level_"
                    + levelNumber + ".json", LevelData.class);

            _levelButtons[i] = new Level(i > lastLevelUnlocked, i < lastLevelUnlocked,
                    Integer.toString(i + 1), buttonFont, _engine,
                    padding + column * (gameButtonsSize + padding),
                    _barHeight + padding + row * (gameButtonsSize + padding),
                    gameButtonsSize, gameButtonsSize) {

                @Override
                public void callback() {
                    Scene scene = new GameScene(_engine, level.attempts, level.attempts, level.codeSize, level.codeOpt,
                            level.repeat, returnScene, worldId, null);
                    _engine.setCurrentScene(scene);
                }
            };
        }

        _unlockNextLevelButton = new Button("UI/nerd.png", _engine,
                _graphics.getLogicWidth() - padding - backbuttonScale, _barHeight / 2 - backbuttonScale / 2,
                backbuttonScale, backbuttonScale) {
            @Override
            public void callback() {
                if(_worldData.getLastLevelUnlocked() >= _numLevels)
                    return;

                _levelButtons[_worldData.getLastLevelUnlocked()].completeLevel();
                _worldData.completeLevel();
                if(_worldData.getLastLevelUnlocked() < _numLevels)
                    _levelButtons[_worldData.getLastLevelUnlocked()].unlockLevel();
            }
        };

        _backgroundImage =
                _graphics.loadImage(GameData.Instance().getWorldDataByIndex(worldId).getWorldName() + "/background.png");
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render(Graphics graphics) {
        _graphics.clear(_backgroundColor);

        _graphics.drawImage(_backgroundImage, 0, _barHeight,
                _graphics.getLogicWidth(), _graphics.getLogicHeight() - _barHeight);

        _backButton.render(graphics);
        _titleText.render(graphics);
        _prevWorldButton.render(graphics);
        _nextWorldButton.render(graphics);
        _unlockNextLevelButton.render(graphics);

        for(int i = 0; i < _numLevels; i ++){
            _levelButtons[i].render(graphics);
        }
    }

    @Override
    public void handleEvents(Input a) {
        int numEvents = a.getTouchEvent().size();
        if (numEvents > 0) {
            _backButton.handleEvents(a.getTouchEvent().get(0));
            _prevWorldButton.handleEvents(a.getTouchEvent().get(0));
            _nextWorldButton.handleEvents(a.getTouchEvent().get(0));
            _unlockNextLevelButton.handleEvents(a.getTouchEvent().get(0));

            for(int i = 0; i < _numLevels; i ++){
                _levelButtons[i].handleEvents(a.getTouchEvent().get(0));
            }
        }
    }
}




