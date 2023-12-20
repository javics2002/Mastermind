package com.example.logiclib;

import com.example.aninterface.Engine;
import com.example.aninterface.IFile;

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

    //Si tienes el producto o no
    private boolean _backgrounds[], _circles[], _themes[];

    private GameData(Engine engine) {
        _worldsData = new ArrayList<>();
        _money = 0;
        _engine = engine;

        _backgrounds = new boolean[ShopScene.backgroundsNumber];
        _circles = new boolean[ShopScene.circlesNumber];
        _themes = new boolean[ShopScene.themesNumber];

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

            for(int i = 0; i < ShopScene.backgroundsNumber; i++)
                _backgrounds[i] = false;

            for(int i = 0; i < ShopScene.circlesNumber; i++)
                _circles[i] = false;

            for(int i = 0; i < ShopScene.themesNumber; i++)
                _themes[i] = false;
        }
        // If there is file, append world data from assets if it does not exist in file
        else {
            //String saveData = saveFile.getContent();

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

    public boolean hasBackground(int index){
        //No es valido
        if(index < 0 || index >= ShopScene.backgroundsNumber)
            return false;

        return _backgrounds[index];
    }

    public boolean hasCircle(int index){
        //No es valido
        if(index < 0 || index >= ShopScene.circlesNumber)
            return false;

        return _circles[index];
    }

    public boolean hasTheme(int index){
        //No es valido
        if(index < 0 || index >= ShopScene.themesNumber)
            return false;

        return _themes[index];
    }

    public boolean purchaseBackground(int index, int price){
        //No es valido
        if(index < 0 || index >= ShopScene.backgroundsNumber)
            return false;

        //Ya lo tienes
        if(_backgrounds[index])
            return false;

        //Compramos fondo
        if(_money >= price){
            _money -= price;
            _backgrounds[index] = true;
        }

        return _backgrounds[index];
    }

    public boolean purchaseCircle(int index, int price){
        //No es valido
        if(index < 0 || index >= ShopScene.circlesNumber)
            return false;

        //Ya lo tienes
        if(_circles[index])
            return false;

        //Compramos fondo
        if(_money >= price){
            _money -= price;
            _circles[index] = true;
        }

        return _circles[index];
    }

    public boolean purchaseTheme(int index, int price){
        //No es valido
        if(index < 0 || index >= ShopScene.themesNumber)
            return false;

        //Ya lo tienes
        if(_themes[index])
            return false;

        //Compramos fondo
        if(_money >= price){
            _money -= price;
            _themes[index] = true;
        }

        return _themes[index];
    }
}
