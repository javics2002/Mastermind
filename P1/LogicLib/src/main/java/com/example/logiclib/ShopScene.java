package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Font;
import com.example.aninterface.Graphics;
import com.example.aninterface.Input;
import com.example.aninterface.Scene;
import com.example.aninterface.Image;

public class ShopScene implements Scene {
    public final static int backgroundsNumber = 6, circlesNumber = 9, themesNumber = 5;
    public enum ShopType { BACKGROUNDS, CIRCLES, THEMES };
    public final ShopType _shopType;
    private final Button _backButton;
    private final Button _prevShopButton, _nextShopButton;
    private final Image _coinImage;
    private final int _coinSize = 20;
    private final Button _instantMoneyButton; //Hack
    private final Text _titleText, _moneyText;

    private final Graphics _graphics;
    Engine _engine;

    final int _barHeight = 80;
    final int _padding = 20;

    private CustomBackground[] _backgroundButtons;
    private CustomCircles[] _circlesButtons;
    private CustomTheme[] _themeButtons;

    private int _backgroundColor;

    public ShopScene(Engine engine, final ShopType shopType) {
        _engine = engine;
        _graphics = _engine.getGraphics();
        _shopType = shopType;

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

        if(GameData.Instance().getCurrentTheme() < 0){
            _backgroundColor = Colors.colorValues.get(Colors.ColorName.BACKGROUND);
        }
        else{
            final Theme theme = _engine.jsonToObject("Shop/Themes/themes_0"
                    + Integer.toString(GameData.Instance().getCurrentTheme() + 1) + ".json", Theme.class);

            _backgroundColor = Colors.parseARGB(theme.backgroundColor);
        }

        // Title
        Font font = _graphics.newFont("Comfortaa-Regular.ttf", 24f);
        String shopTitle = "";
        switch (_shopType){
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
                Scene scene = new ShopScene(_engine,
                        ShopType.values()[(shopType.ordinal() + shopType.values().length - 1) % shopType.values().length]);
                _engine.setCurrentScene(scene);
            }
        };

        _nextShopButton = new Button("UI/nextWorld.png", _engine,
                _graphics.getLogicWidth() / 2 + titleWidth / 2 + _padding, _barHeight / 2 - backbuttonScale / 2,
                backbuttonScale, backbuttonScale) {
            @Override
            public void callback() {
                Scene scene = new ShopScene(_engine, ShopType.values()[(shopType.ordinal() + 1) % shopType.values().length]);
                _engine.setCurrentScene(scene);
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

        // Product buttons
        Font buttonFont = _graphics.newFont("Comfortaa-Regular.ttf", 25f);

        final int priceGap = 50;

        switch (_shopType){
            case BACKGROUNDS:
                final int backgroundsPerRow = 4;
                final int backgroundWidth = (_graphics.getLogicWidth() - (backgroundsPerRow + 1) * _padding) / backgroundsPerRow;
                final int backgroundHeight = backgroundWidth * 3 / 2;

                _backgroundButtons = new CustomBackground[backgroundsNumber + 1];

                _backgroundButtons[0] = new CustomBackground(-1 == GameData.Instance().getCurrentBackground(),
                        -1, 0, buttonFont, "UI/defaultProduct.png", _engine,
                        _padding,_barHeight + _padding, backgroundWidth, backgroundHeight,
                        priceGap, _coinImage, _moneyText);

                for(int i = 1; i <= backgroundsNumber; i++){
                    int row = i / backgroundsPerRow;
                    int column = i % backgroundsPerRow;

                    final Background background = _engine.jsonToObject("Shop/Backgrounds/background_0"
                            + Integer.toString(i) + ".json", Background.class);

                    _backgroundButtons[i] = new CustomBackground(i - 1 == GameData.Instance().getCurrentBackground(),
                            i - 1, background.price, buttonFont, background.image, _engine,
                            _padding + column * (backgroundWidth + _padding),
                            _barHeight + _padding + row * (backgroundHeight + _padding + priceGap),
                            backgroundWidth, backgroundHeight, priceGap, _coinImage, _moneyText);
                }
                break;
            case CIRCLES:
                final int circlesPerRow = 4;
                final int circlesSize = (_graphics.getLogicWidth() - (circlesPerRow + 1) * _padding) / circlesPerRow;

                _circlesButtons = new CustomCircles[circlesNumber + 1];

                int[] defaultColors = new int[9];
                for (int i = 0; i < 9; i++){
                    defaultColors[i] = Colors.getColor(i);
                }
                _circlesButtons[0] = new CustomCircles(defaultColors, -1 == GameData.Instance().getCurrentCircle(),
                        -1, 0, buttonFont, _engine,
                        _padding, _barHeight + _padding,
                        circlesSize, circlesSize, priceGap, _coinImage, _moneyText);

                for(int i = 1; i <= circlesNumber; i++){
                    int row = i / circlesPerRow;
                    int column = i % circlesPerRow;

                    final Circles circles = _engine.jsonToObject("Shop/Circles/circles_0"
                            + Integer.toString(i) + ".json", Circles.class);

                    if(circles.skin) {
                        _circlesButtons[i] = new CustomCircles(circles.packPath, i - 1 == GameData.Instance().getCurrentCircle(),
                                i - 1, circles.price, buttonFont, _engine,
                                _padding + column * (circlesSize + _padding),
                                _barHeight + _padding + row * (circlesSize + _padding + priceGap),
                                circlesSize, circlesSize, priceGap, _coinImage, _moneyText);
                    }
                    else {
                        _circlesButtons[i] = new CustomCircles(circles.colors, i - 1 == GameData.Instance().getCurrentCircle(),
                                i - 1, circles.price, buttonFont, _engine,
                                _padding + column * (circlesSize + _padding),
                                _barHeight + _padding + row * (circlesSize + _padding + priceGap),
                                circlesSize, circlesSize, priceGap, _coinImage, _moneyText);
                    }
                }
                break;
            case THEMES:
                final int themesPerRow = 3;
                final int themesSize = (_graphics.getLogicWidth() - (themesPerRow + 1) * _padding) / themesPerRow;

                _themeButtons = new CustomTheme[themesNumber + 1];

                _themeButtons[0] = new CustomTheme(-1 == GameData.Instance().getCurrentTheme(),
                        -1, 0, Colors.ColorName.BACKGROUND, Colors.ColorName.BACKGROUNDBLUE, buttonFont, _engine,
                        _padding, _barHeight + _padding,
                        themesSize, themesSize, priceGap, _coinImage, _moneyText);

                for(int i = 1; i <= themesNumber; i++){
                    int row = i / themesPerRow;
                    int column = i % themesPerRow;

                    final Theme theme = _engine.jsonToObject("Shop/Themes/themes_0"
                            + Integer.toString(i) + ".json", Theme.class);

                    _themeButtons[i] = new CustomTheme(i - 1 == GameData.Instance().getCurrentTheme(),
                            i - 1, theme.price, theme.backgroundColor, theme.buttonColor, buttonFont, _engine,
                            _padding + column * (themesSize + _padding),
                            _barHeight + _padding + row * (themesSize + _padding + priceGap),
                            themesSize, themesSize, priceGap, _coinImage, _moneyText);
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void update(double deltaTime) {
        switch (_shopType){
            case BACKGROUNDS:
                for(int i = 0; i <= backgroundsNumber; i++)
                    _backgroundButtons[i].update(deltaTime);
                break;
            case CIRCLES:
                for(int i = 0; i <= circlesNumber; i++)
                    _circlesButtons[i].update(deltaTime);
                break;
            case THEMES:
                for(int i = 0; i <= themesNumber; i++)
                    _themeButtons[i].update(deltaTime);
                break;
            default:
                break;
        }

        if(GameData.Instance().getCurrentTheme() < 0){
            _backgroundColor = Colors.colorValues.get(Colors.ColorName.BACKGROUND);
        }
        else{
            final Theme theme = _engine.jsonToObject("Shop/Themes/themes_0"
                    + Integer.toString(GameData.Instance().getCurrentTheme() + 1) + ".json", Theme.class);

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
        graphics.drawImage(_coinImage, graphics.getLogicWidth() - _padding - _coinSize, _barHeight / 2 - _coinSize / 2,
                _coinSize, _coinSize);

        switch (_shopType){
            case BACKGROUNDS:
                for(int i = 0; i <= backgroundsNumber; i++)
                    _backgroundButtons[i].render(graphics);
                break;
            case CIRCLES:
                for(int i = 0; i <= circlesNumber; i++)
                    _circlesButtons[i].render(graphics);
                break;
            case THEMES:
                for(int i = 0; i <= themesNumber; i++)
                    _themeButtons[i].render(graphics);
                break;
            default:
                break;
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

            switch (_shopType){
                case BACKGROUNDS:
                    for(int i = 0; i <= backgroundsNumber; i ++)
                        _backgroundButtons[i].handleEvents(input.getTouchEvent().get(0));
                    break;
                case CIRCLES:
                    for(int i = 0; i <= circlesNumber; i++)
                        _circlesButtons[i].handleEvents(input.getTouchEvent().get(0));
                    break;
                case THEMES:
                    for(int i = 0; i <= themesNumber; i++)
                        _themeButtons[i].handleEvents(input.getTouchEvent().get(0));
                    break;
                default:
                    break;
            }
        }
    }
}
