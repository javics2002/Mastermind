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

    @Override
    public String toString() {
        return "LevelData[codeSize_" + codeSize + "_codeOpt_" + codeOpt + "_repeat_" + repeat + "_attempts_" + attempts +
                "_leftAttemptsNumber_" + leftAttemptsNumber + "_combinationsSize_" + combinations.size() +
                "_resultCombinationSize_" + resultCombination.getColors().length + "_currentCombinationSize_" + currentCombination.getColors().length + "]";
    }
}
