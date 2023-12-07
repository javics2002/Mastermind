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

    final int _barHeight = 80;

    public WorldScene(Engine engine, final int worldId) {
        _engine = engine;

        _graphics = _engine.getGraphics();

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
        String worldTitle = "Mundo " + Integer.toString(worldId);
        int titleWidth = _graphics.getStringWidth(worldTitle, font);
        _titleText = new Text(worldTitle, font, _engine,
                _graphics.getLogicWidth() / 2 - titleWidth / 2,
                _barHeight / 2 + _graphics.getStringHeight(worldTitle, font) / 2, 0);

        final int worlds = _engine.filesInFolder("Levels");
        _prevWorldButton = new Button("UI/prevWorld.png", _engine,
                _graphics.getLogicWidth() / 2 - titleWidth / 2 - padding - backbuttonScale,
                _barHeight / 2 - backbuttonScale / 2, backbuttonScale, backbuttonScale) {
            @Override
            public void callback() {
                int prevWorldId = ((worldId - 2) % worlds) + 1;
                if(prevWorldId < 1)
                    prevWorldId += worlds;
                Scene scene = new WorldScene(_engine, prevWorldId);
                _engine.setCurrentScene(scene);
            }
        };

        _nextWorldButton = new Button("UI/nextWorld.png", _engine,
                _graphics.getLogicWidth() / 2 + titleWidth / 2 + padding, _barHeight / 2 - backbuttonScale / 2,
                backbuttonScale, backbuttonScale) {
            @Override
            public void callback() {
                int nextWorldId = (worldId % worlds) + 1;
                Scene scene = new WorldScene(_engine, nextWorldId);
                _engine.setCurrentScene(scene);
            }
        };

        // Read saved data (progress)
        _worldData = _engine.jsonToObject("RayitoPonLaRutaDeGuardadoAqui", WorldData.class);
        if(_worldData == null){
            _worldData = new WorldData();
        }
        final int lastLevelUnlocked = _worldData.getLastLevelUnlocked();

        // Game buttons
        final int levelsPerRow = 3;
        final int gameButtonsSize = (_graphics.getLogicWidth() - (levelsPerRow + 1) * padding) / levelsPerRow;

        Font buttonFont = _graphics.newFont("Comfortaa-Regular.ttf", 35f);

        String worldPath = "Levels/world" + Integer.toString(worldId);
        _numLevels = _engine.filesInFolder(worldPath);
        _levelButtons = new Level[_numLevels];
        final Scene returnScene = this;

        for(int i = 0; i < _numLevels; i++){
            int row = i / levelsPerRow;
            int column = i % levelsPerRow;

            String levelNumber = i >= 9 ? Integer.toString(i + 1) : "0" + Integer.toString(i + 1);

            final LevelData level = _engine.jsonToObject(worldPath + "/level" + Integer.toString(worldId)
                    + "_" + levelNumber + ".json", LevelData.class);
            _levelButtons[i] = new Level(i > lastLevelUnlocked, i < lastLevelUnlocked,
                    Integer.toString(i + 1), buttonFont, _engine,
                    padding + column * (gameButtonsSize + padding),
                    _barHeight + padding + row * (gameButtonsSize + padding),
                    gameButtonsSize, gameButtonsSize) {
                @Override
                public void callback() {
                    Scene scene = new GameScene(_engine, level.attempts, level.codeSize, level.codeOpt,
                            level.repeat, returnScene, worldId, worldId);
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

        _backgroundImage = _graphics.loadImage("world"
                + Integer.toString(worldId) + "/background.png");
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render(Graphics gr) {
        _graphics.drawImage(_backgroundImage, 0, _barHeight,
                _graphics.getLogicWidth(), _graphics.getLogicHeight() - _barHeight);

        _backButton.render();
        _titleText.render();
        _prevWorldButton.render();
        _nextWorldButton.render();
        _unlockNextLevelButton.render();

        for(int i = 0; i < _numLevels; i ++){
            _levelButtons[i].render();
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




