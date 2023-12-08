package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.IFile;
import com.example.aninterface.Scene;

import java.util.ArrayList;

public class GameData {
    // TODO: Esta clase debería guardar gestionar todos los datos relacionados con
    // el guardado, como el dinero o los ultimos niveles desbloqueados de cada mundo.

    private static GameData _instance;

    private ArrayList<WorldData> _worldsData;

    private Engine _engine;
    private int _money;

    private GameData(Engine engine) {
        _worldsData = new ArrayList<>();
        _money = 0;
        _engine = engine;

        // If there's no file, create world data from assets
        IFile saveFile = _engine.getSaveData();
        if (saveFile == null) {
            String worldPath = "Levels";
            int numWorlds = _engine.filesInFolder(worldPath);

            for (int i = 0; i < numWorlds; i++) {
                // TODO: Get Info of World With style.json?

                //WorldData worldData = new WorldData();
                //_worldsData.add(worldData);
            }

            _money = 0;
        }
        // If there is file, append world data from assets if it does not exist in file
        else {
            String saveData = saveFile.getContent();

            // TODO: Build worlds

            // TODO: Add money from save
        }
    }

    public static void Init(Engine engine) {
        assert (_instance == null);
        _instance = new GameData(engine);
    }

    public static GameData Instance() {
        assert (_instance != null);
        return _instance;
    }

    public WorldData getWorldDataByIndex(int index){
        return _worldsData.get(index);
    }

    public int getMoney() {
        return _money;
    }

    public void addMoney(int money) {
        _money += money;
    }
}