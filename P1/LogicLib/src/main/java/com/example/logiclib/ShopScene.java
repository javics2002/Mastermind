package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;
import com.example.aninterface.Image;

public class ShopScene implements Scene {
    public final static int backgroundsNumber = 6, circlesNumber = 9, themesNumber = 5;
    private final Button _backButton;
    private final Button _prevShopButton, _nextShopButton;
    private final Image _coinImage;
    private final int _coinSize = 20;
    private final Button _instantMoneyButton; //Hack
    private final CustomBackground[] _backgroundButtons;
    private final Text _titleText, _moneyText;
    final int _padding = 20;
    private final Graphics _graphics;
    Engine _engine;

    final int _barHeight = 80;

    public ShopScene(Engine engine) {
        _engine = engine;
        _graphics = _engine.getGraphics();

        // Back button
        int backbuttonScale = 40;
        _backButton = new Button("UI/back.png", _engine,
                _padding, _barHeight / 2 - backbuttonScale / 2,
                backbuttonScale, backbuttonScale) {
            @Override
            public void callback() {
                Scene scene = new InitialScene(_engine);
                _engine.setCurrentScene(scene);
            }
        };

        // Title
        Font font = _graphics.newFont("Comfortaa-Regular.ttf", 24f);
        String shopTitle = "Fondos";
        int titleWidth = _graphics.getStringWidth(shopTitle, font);
        _titleText = new Text(shopTitle, font, _engine,
                _graphics.getLogicWidth() / 2 - titleWidth / 2,
                _barHeight / 2 + _graphics.getStringHeight(shopTitle, font) / 2, 0);

        final int worlds = _engine.filesInFolder("Levels");
        _prevShopButton = new Button("UI/prevWorld.png", _engine,
                _graphics.getLogicWidth() / 2 - titleWidth / 2 - _padding - backbuttonScale,
                _barHeight / 2 - backbuttonScale / 2, backbuttonScale, backbuttonScale) {
            @Override
            public void callback() {
            }
        };

        _nextShopButton = new Button("UI/nextWorld.png", _engine,
                _graphics.getLogicWidth() / 2 + titleWidth / 2 + _padding, _barHeight / 2 - backbuttonScale / 2,
                backbuttonScale, backbuttonScale) {
            @Override
            public void callback() {
            }
        };

        _coinImage = _graphics.loadImage("UI/coin.png");

        String moneyString = Integer.toString(GameData.Instance().getMoney());
        _moneyText = new Text(moneyString, font, _engine,
                _graphics.getLogicWidth() - _graphics.getStringWidth(moneyString, font) - _coinSize - _padding - 5,
                _barHeight / 2 + _graphics.getStringHeight(moneyString, font) / 2, 0);

        _instantMoneyButton = new Button("UI/pijo.png", _engine,
                _graphics.getLogicWidth() / 2 + titleWidth / 2 + _padding + backbuttonScale,
                _barHeight / 2 - backbuttonScale / 2, backbuttonScale, backbuttonScale) {
            @Override
            public void callback() {
                GameData.Instance().addMoney(100);

                String moneyString = Integer.toString(GameData.Instance().getMoney());
                Font font = _graphics.newFont("Comfortaa-Regular.ttf", 24f);

                _moneyText.setText(moneyString);
                _moneyText.setPos(_graphics.getLogicWidth() - _graphics.getStringWidth(moneyString, font) - _coinSize - _padding - 5,
                        _barHeight / 2 + _graphics.getStringHeight(moneyString, font) / 2);
            }
        };

        // Game buttons
        final int backgroundsPerRow = 3;
        final int backgroundWidth = (_graphics.getLogicWidth() - (backgroundsPerRow + 1) * _padding) / backgroundsPerRow;
        final int backgroundHeight = backgroundWidth * 3 / 2;
        final int priceGap = 50;

        Font buttonFont = _graphics.newFont("Comfortaa-Regular.ttf", 25f);

        _backgroundButtons = new CustomBackground[backgroundsNumber];

        for(int i = 0; i < backgroundsNumber; i++){
            int row = i / backgroundsPerRow;
            int column = i % backgroundsPerRow;

            final Background level = _engine.jsonToObject("Shop/Backgrounds/background_0"
                    + Integer.toString(i + 1) + ".json", Background.class);

            _backgroundButtons[i] = new CustomBackground(i == GameData.Instance().getCurrentBackground(),
                    i,level.price, buttonFont, level.image, _engine,
                    _padding + column * (backgroundWidth + _padding),
                    _barHeight + _padding + row * (backgroundHeight + _padding + priceGap),
                    backgroundWidth, backgroundHeight, priceGap, _coinImage, _moneyText);
        }
    }

    @Override
    public void update(double deltaTime) {
        for(int i = 0; i < backgroundsNumber; i++) {
            _backgroundButtons[i].update(deltaTime);
        }
    }

    @Override
    public void render(Graphics graphics) {
        _backButton.render(graphics);
        _titleText.render(graphics);
        _prevShopButton.render(graphics);
        _nextShopButton.render(graphics);
        _moneyText.render(graphics);
        _instantMoneyButton.render(graphics);
        graphics.drawImage(_coinImage, graphics.getLogicWidth() - _padding - _coinSize, _barHeight / 2 - _coinSize / 2,
                _coinSize, _coinSize);

        for(int i = 0; i < backgroundsNumber; i ++){
            _backgroundButtons[i].render(graphics);
        }
    }

    @Override
    public void handleEvents(Input input) {
        int numEvents = input.getTouchEvent().size();
        if (numEvents > 0) {
            _backButton.handleEvents(input.getTouchEvent().get(0));
            _prevShopButton.handleEvents(input.getTouchEvent().get(0));
            _nextShopButton.handleEvents(input.getTouchEvent().get(0));
            _moneyText.handleEvents(input.getTouchEvent().get(0));
            _instantMoneyButton.handleEvents(input.getTouchEvent().get(0));

            for(int i = 0; i < backgroundsNumber; i ++){
                _backgroundButtons[i].handleEvents(input.getTouchEvent().get(0));
            }
        }
    }
}
