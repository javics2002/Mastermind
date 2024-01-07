package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.Graphics;
import com.example.aninterface.Scene;

public class Transition {
    private Engine _engine;
    private Graphics _graphics;
    public enum TransitionType{ fadeIn, fadeOut}
    private TransitionType _transitionType;

    private int _width;
    private int _height;
    private float _currentOpacity;
    private int _color;
    private float _duration;
    private float _currentTime;
    private boolean _isPlaying;
    private Scene _sceneToLoad;

    public Transition(Engine engine, int width, int height) {
        _engine = engine;
        _graphics = _engine.getGraphics();

        _width = width;
        _height = height;
        _currentTime = 0f;

        _sceneToLoad = null;
        _isPlaying = false;
    }

    public void PlayTransition(TransitionType type, int color, float duration, Scene sceneToLoad) {
        if (_isPlaying)
            return;

        _transitionType = type;
        _color = color;
        _sceneToLoad = sceneToLoad;
        _duration = duration;

        if (_transitionType == TransitionType.fadeIn) {
            _currentOpacity = 1;
        }
        else if (_transitionType == TransitionType.fadeOut){
            _currentOpacity = 0;
        }

        _isPlaying = true;
    }

    public void update(double deltaTime) {
        if (!_isPlaying)
            return;

        _currentTime += deltaTime;

        if (_currentTime >= _duration){
            _isPlaying = false;
            _currentTime = 0f;

            if (_sceneToLoad != null){
                _engine.setCurrentScene(_sceneToLoad);
            }
        }
        else {
            _currentOpacity = _currentTime/_duration;

            if (_transitionType == TransitionType.fadeIn) {
                _currentOpacity = 1 - _currentOpacity;
            }
        }
    }

    public void render(Graphics graphics) {
        if (!_isPlaying)
            return;

        int newAlpha = (int) (_currentOpacity * 255);
        int colorARGB = combineAlphaAndColor(newAlpha, _color);

        _graphics.drawRealRect(0, 0, _width, _height, colorARGB);
    }

    // Combina el valor alpha y el color RGB para obtener el color ARGB
    private int combineAlphaAndColor(int alpha, int colorRGB) {
        return (alpha << 24) | (colorRGB & 0x00FFFFFF);
    }

    public boolean isTransitionPlaying() {
        return _isPlaying;
    }
}
