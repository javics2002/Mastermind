package com.example.logiclib;

import java.util.ArrayList;

// Clase Singleton que guarda datos útiles para la ejecución y persistencia del juego.
// Contiene información de todos los mundos y de toda la tienda, además de información
// del nivel jugado por el jugador en ese momento.

// Cuando se ejecuta el juego, se inicializa el singleton y se completa usando la información
// leida del guardado de datos.
public class GameData {
    private static GameData _instance;
    private ArrayList<WorldData> _worldsData;
    private int _money;

    private ArrayList<Background> _backgrounds;
    private ArrayList<Circles> _circles;
    private ArrayList<Theme> _themes;
    private int _currentBackground, _currentCircles, _currentTheme;

    private LevelData _currentLevelData;

    private GameData() {
        _worldsData = new ArrayList<>();
        _money = 0;

        _backgrounds = new ArrayList<>();
        _circles = new ArrayList<>();
        _themes = new ArrayList<>();

        _currentBackground = -1;
        _currentCircles = -1;
        _currentTheme = -1;

        _currentLevelData = null;
    }

    public static void Init() {
        assert (_instance == null);
        _instance = new GameData();
    }

    public static GameData Instance() {
        assert (_instance != null);
        return _instance;
    }

    public static void Release() {
        assert(_instance != null);
        _instance = null;
    }

    public void reset(){
        for(WorldData worldData : _worldsData)
            worldData.setLastLevelUnlocked(0);

        _money = 0;

        for(Background background : _backgrounds)
            background.acquired = false;

        for(Circles circles : _circles)
            circles.acquired = false;

        for(Theme theme : _themes)
            theme.acquired = false;

        _currentBackground = -1;
        _currentCircles = -1;
        _currentTheme = -1;

        _currentLevelData = null;
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

    public ArrayList<Background> getBackgrounds(){
        return _backgrounds;
    }

    public ArrayList<Circles> getCircles() {
        return _circles;
    }

    public ArrayList<Theme> getThemes(){
        return _themes;
    }

    public void addBackground(Background background){
        _backgrounds.add(background);
    }

    public void addCircles(Circles circles){
        _circles.add(circles);
    }

    public void addTheme(Theme theme){
        _themes.add(theme);
    }

    public void removeBackgroundByIndex(int index){
        _backgrounds.remove(index);
    }

    public void removeCirclesByIndex(int index){
        _circles.remove(index);
    }

    public void removeThemesByIndex(int index){
        _themes.remove(index);
    }

    public boolean hasBackground(int index){
        //Default background
        if(index == -1)
            return true;

        //No es valido
        if(index < 0 || index >= _backgrounds.size())
            return false;

        return _backgrounds.get(index).acquired;
    }

    public boolean hasCircle(int index){
        //Default circles
        if(index == -1)
            return true;

        //No es valido
        if(index < 0 || index >= _circles.size())
            return false;

        return _circles.get(index).acquired;
    }

    public boolean hasTheme(int index){
        //Default theme
        if(index == -1)
            return true;

        //No es valido
        if(index < 0 || index >= _themes.size())
            return false;

        return _themes.get(index).acquired;
    }

    public boolean purchaseBackground(int index, int price){
        //No es valido
        if(index < 0 || index >= _backgrounds.size())
            return false;

        //Ya lo tienes
        if(_backgrounds.get(index).acquired)
            return false;

        //Compramos fondo
        if(_money >= price){
            _money -= price;
            _backgrounds.get(index).acquired = true;
        }

        return _backgrounds.get(index).acquired;
    }

    public boolean setBackground(int index){
        //Erase selection
        if(index == -1){
            _currentBackground = -1;
            return true;
        }

        //No es valido
        if(index < 0 || index >= _backgrounds.size())
            return false;

        //No lo tienes
        if(!_backgrounds.get(index).acquired)
            return false;

        _currentBackground = index;
        return true;
    }

    public boolean purchaseCircle(int index, int price){
        //No es valido
        if(index < 0 || index >= _circles.size())
            return false;

        //Ya lo tienes
        if(_circles.get(index).acquired)
            return false;

        //Compramos fondo
        if(_money >= price){
            _money -= price;
            _circles.get(index).acquired = true;
        }

        return _circles.get(index).acquired;
    }

    public boolean setCircles(int index){
        //Erase selection
        if(index == -1){
            _currentCircles = -1;
            return true;
        }

        //No es valido
        if(index < 0 || index >= _circles.size())
            return false;

        //No lo tienes
        if(!_circles.get(index).acquired)
            return false;

        _currentCircles = index;
        return true;
    }

    public boolean purchaseTheme(int index, int price){
        //No es valido
        if(index < 0 || index >= _themes.size())
            return false;

        //Ya lo tienes
        if(_themes.get(index).acquired)
            return false;

        //Compramos fondo
        if(_money >= price){
            _money -= price;
            _themes.get(index).acquired = true;
        }

        return _themes.get(index).acquired;
    }

    public boolean setTheme(int index){
        //Erase selection
        if(index == -1){
            _currentTheme = -1;
            return true;
        }

        //No es valido
        if(index < 0 || index >= _themes.size())
            return false;

        //No lo tienes
        if(!_themes.get(index).acquired)
            return false;

        _currentTheme = index;
        return true;
    }

    public int getCurrentBackground(){
        return _currentBackground;
    }


    public int getCurrentCircles(){
        return _currentCircles;
    }

    public int getCurrentTheme(){
        return _currentTheme;
    }

    public LevelData getCurrentLevelData() {
        return _currentLevelData;
    }

    public void setCurrentLevelData(LevelData data) {
        _currentLevelData = data;
    }

    public void resetCurrentLevelData() {
        _currentLevelData = null;
    }
}
