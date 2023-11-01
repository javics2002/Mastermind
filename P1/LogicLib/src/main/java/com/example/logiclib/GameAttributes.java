package com.example.logiclib;

public class GameAttributes {
    static GameAttributes _Instance;
    public int attemptsLeft, attemptsNumber;
    public int combinationLength, colorNumber;
    public boolean repeatedColors;
    public int activeLayout;
    public boolean isEyeOpen;
    public Combination resultCombination;

    private GameAttributes() {
    }

    public static GameAttributes Instance() {
        if (_Instance == null)
            _Instance = new GameAttributes();

        return _Instance;
    }
}
