package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.IFile;
import com.example.aninterface.Scene;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.naming.Context;

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

    public void addWorld(WorldData newWorld){
        _worldsData.add(newWorld);
    }

    public void deleteWorldByIndex(int index){
        _worldsData.remove(index);
    }

    public int numberOfWorlds() {
        return _worldsData.size();
    }

    public int getMoney() {
        return _money;
    }

    public void addMoney(int money) {
        _money += money;
    }
}
