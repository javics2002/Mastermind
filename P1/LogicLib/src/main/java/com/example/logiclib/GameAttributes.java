package com.example.logiclib;

public class GameAttributes {
    static GameAttributes _instance;
    public int _attemptsLeft;
    public int _attemptsNumber;
    public int _combinationLength;
    public int _colorNumber;
    public int _activeLayout;
    public boolean _isEyeOpen;
    public Combination _resultCombination;

    private GameAttributes(){
    }

    public static GameAttributes Instance(){
        if (_instance == null)
            _instance = new GameAttributes();

        return _instance;
    }
}
