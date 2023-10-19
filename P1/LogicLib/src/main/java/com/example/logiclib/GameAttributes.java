package com.example.logiclib;

public class GameAttributes {
    static GameAttributes instance;

    public int attemptsLeft;
    public int attemptsNumber;
    public int combinationLength;
    public int colorNumber;

    private GameAttributes(){

    }

    public static GameAttributes Instance(){
        if (instance == null)
            instance = new GameAttributes();

        return instance;
    }
}
