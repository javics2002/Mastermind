package com.example.logiclib;
import java.io.Serializable;
import java.util.ArrayList;

public class WorldData implements Serializable {
    // TODO: En esta clase se deberían guardar la información de cada mundo:
    // los niveles, el ultimo nivel desbloqueado, el nombre del mundo...
    private String _worldName;
    private ArrayList<LevelData> _levelsData;
    private int _lastLevelUnlocked;
    private int _levelNumber;

    public WorldData(String worldName, int levelNumber) {
        _worldName = worldName;
        _levelNumber = levelNumber;

        _levelsData  = new ArrayList<>();
        for (int i = 0; i < _levelNumber; i++){
            LevelData newLevelData = new LevelData();
            _levelsData.add(newLevelData);
        }
    }

    public WorldData() {
        _lastLevelUnlocked = 0;
        _levelNumber = 0;
        _worldName = "defaultName";
        _levelsData  = new ArrayList<>();
    }

    public String getWorldName() {
        return _worldName;
    }
    public int getLastLevelUnlocked() {
        return _lastLevelUnlocked;
    }
    public void setWorldName(String worldName) {
        _worldName = worldName;
    }
    public void completeLevel(){
        _lastLevelUnlocked++;
    }
    public int getLevelNumber() {
        return _levelNumber;
    }
    public void setLevelNumber(int newLevelNumber) {
        _levelNumber = newLevelNumber;
    }
    public void addLevelData(LevelData newLevelData) {
        _levelsData.add(newLevelData);
    }
    public LevelData getLevelDataByIndex(int index){
        return _levelsData.get(index);
    }
}
