package com.example.logiclib;

import java.util.ArrayList;

public class WorldData {
    // TODO: En esta clase se deberían guardar la información de cada mundo:
    // los niveles, el ultimo nivel desbloqueado, el nombre del mundo...
    private String _worldName;
    private ArrayList<LevelData> _levels;
    private int _lastLevelUnlocked;
    private int _levelNumber;

    public WorldData(String worldName, int levelNumber) {
        _worldName = worldName;
        _levelNumber = levelNumber;

        //_levels  = new ArrayList<>();
        //for (int i = 0; i < _levelNumber; i++){
            //_levels.add();
        //}
    }

    public String getWorldName() {
        return _worldName;
    }
    public int getLastLevelUnlocked() {
        return _lastLevelUnlocked;
    }

    public int getLevelNumber() {
        return _levelNumber;
    }
}
