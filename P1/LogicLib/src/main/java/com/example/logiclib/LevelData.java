package com.example.logiclib;

import java.io.Serializable;
import java.util.ArrayList;

public class LevelData implements Serializable {
    public int codeSize;
    public int codeOpt;
    public boolean repeat;
    public int attempts;

    public ArrayList<Combination> combinations;
    public Combination currentCombination;
    public Combination resultCombination;
    public int leftAttemptsNumber;
}
