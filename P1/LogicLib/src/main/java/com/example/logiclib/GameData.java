package com.example.logiclib;

import com.example.aninterface.Engine;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

// Clase Singleton que guarda datos útiles para la ejecución y persistencia del juego.
// Contiene información de todos los mundos y de toda la tienda, además de información
// del nivel jugado por el jugador en ese momento.

// Cuando se ejecuta el juego, se inicializa el singleton y se completa usando la información
// leida del guardado de datos.
public class GameData {
    private static GameData _instance;

    private ArrayList<Integer> _numberOfCompletions;

    private GameData() {
        _numberOfCompletions = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            _numberOfCompletions.add(0);
        }
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
        assert (_instance != null);
        _instance = null;
    }

    public void reset() {
        _numberOfCompletions = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            _numberOfCompletions.add(0);
        }
    }

    public void loadGameData(Engine engine){
        boolean doesSaveExist = false;

        try {
            ObjectInputStream saveStream = engine.openSaveFileForReading("GameData.txt");

            for (int i = 0; i < 4; i++) {
                _numberOfCompletions.set(i, saveStream.readInt());

                saveStream.readChar();
            }

            doesSaveExist = true;

            saveStream.close();
            engine.closeSaveFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!doesSaveExist){
            reset();
        }
    }

    public void saveGameData(Engine engine) {
        try {
            ObjectOutputStream saveStream = engine.openSaveFileForWriting("GameData.txt");

            for (int i = 0; i < 4; i++) {
                saveStream.writeInt(_numberOfCompletions.get(i));
                saveStream.writeChar(';');
            }

            saveStream.close();
            engine.closeSaveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void winLevel(int difficultyIndex){
        _numberOfCompletions.set(difficultyIndex, _numberOfCompletions.get(difficultyIndex) + 1);
    }

    public Integer getNumberOfCompletions(int difficultyIndex){
        return _numberOfCompletions.get(difficultyIndex);
    }
}
