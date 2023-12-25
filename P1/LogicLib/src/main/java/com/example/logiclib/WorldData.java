package com.example.logiclib;
import java.io.Serializable;

// Esta clase contiene la información que se guardará de un mundo especifico.
// La informacion de utilidad que se guardará es el nombre del mundo, el ultimo nivel desbloqueado y el numero de niveles.

public class WorldData implements Serializable {
    private String _worldName;
    private int _lastLevelUnlocked;
    private int _levelNumber;

    public WorldData() {
        _worldName = "defaultName";
        _levelNumber = 0;
        _lastLevelUnlocked = 0;
    }

    public String getWorldName() {
        return _worldName;
    }
    public int getLastLevelUnlocked() {
        return _lastLevelUnlocked;
    }
    public void setLastLevelUnlocked(int lastLevelUnlocked) {
        _lastLevelUnlocked = lastLevelUnlocked;
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

    @Override
    public String toString() {
        return "WorldData[Name_" + _worldName + "_LastLevelUnlocked_" + _lastLevelUnlocked + "_LevelNumber_" + _levelNumber + "]";
    }
}
