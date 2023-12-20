package com.example.logiclib;

import com.example.aninterface.Scene;

public class GameAttributes {
    public int attemptsLeft, attemptsNumber;
    public int combinationLength, colorNumber;
    public boolean repeatedColors;
    public int activeLayout;
    public boolean isEyeOpen;
    public Combination resultCombination;
    public Scene returnScene;
    public int skin;
    public int backGroundSkinId;
    public int selectedWorld;

    public GameAttributes() {
        skin = -1;
        backGroundSkinId = -1;
        selectedWorld = -1;
    }


}
