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
		assert (_instance != null);
		_instance = null;
	}

	public void reset() {
		for (WorldData worldData : _worldsData)
			worldData.setLastLevelUnlocked(0);

		_money = 0;

		for (Background background : _backgrounds)
			background.acquired = false;

		for (Circles circles : _circles)
			circles.acquired = false;

		for (Theme theme : _themes)
			theme.acquired = false;

		_currentBackground = -1;
		_currentCircles = -1;
		_currentTheme = -1;

		_currentLevelData = null;
	}

	public void loadGameData(Engine engine){
		boolean doesSaveExist = false;

		try {
			ObjectInputStream saveStream = engine.openSaveFileForReading("GameData.json");

			String currentFileContent = "";

			int worldNumber = saveStream.readInt();
			currentFileContent += worldNumber;

			for (int i = 0; i < worldNumber; i++) {
				WorldData worldData = (WorldData) saveStream.readObject();
				addWorld(worldData);
				currentFileContent += worldData.toString();
			}

			int money = saveStream.readInt();
			addMoney(money);
			currentFileContent += money;

			int backgroundNumber = saveStream.readInt();
			currentFileContent += backgroundNumber;

			for (int i = 0; i < backgroundNumber; i++) {
				Background background = (Background) saveStream.readObject();
				currentFileContent += background.toString();
				addBackground(background);
			}

			int currentBackground = saveStream.readInt();
			setBackground(currentBackground);
			currentFileContent += currentBackground;

			int circlesNumber = saveStream.readInt();
			currentFileContent += circlesNumber;

			for (int i = 0; i < circlesNumber; i++) {
				Circles circles = (Circles) saveStream.readObject();
				currentFileContent += circles.toString();
				addCircles(circles);
			}

			int currentCircle = saveStream.readInt();
			setCircles(currentCircle);
			currentFileContent += currentCircle;

			int themesNumber = saveStream.readInt();
			currentFileContent += themesNumber;

			for (int i = 0; i < themesNumber; i++) {
				Theme theme = (Theme) saveStream.readObject();
				currentFileContent += theme;
				addTheme(theme);
			}

			int currentTheme = saveStream.readInt();
			setTheme(currentTheme);
			currentFileContent += currentTheme;

			// Load LevelData
			LevelData newData = (LevelData) saveStream.readObject();
			setCurrentLevelData(newData);

			if (newData != null) {
				currentFileContent += newData.toString();
			}

			// HASH
			String saveHash = (String) saveStream.readObject();

			String salt = "contrasenya";

			String hash = engine.hashJson(currentFileContent);
			String finalHash =  engine.hashJson(hash + currentFileContent + salt);

			doesSaveExist = saveHash.equals(finalHash);

			saveStream.close();
			engine.closeSaveFile();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String[] worldFolderNames = engine.getFileNames("Levels");
		int numberOfWorlds = _worldsData.size();
		boolean[] loadedWorldSaveData = new boolean[numberOfWorlds];

		if (doesSaveExist) {
			for (int i = 0; i < worldFolderNames.length; i++) {
				boolean loadedFolder = false;

				for (int j = 0; j < numberOfWorlds; j++) {
					WorldData worldData = getWorldDataByIndex(j);
					String saveWorldName = worldData.getWorldName();

					if (saveWorldName.equals(worldFolderNames[i])) {
						// World exists in save file
						loadedFolder = true;
						loadedWorldSaveData[j] = true;

						// Check number of levels and overwrite last level unlocked
						// in case we need to
						String[] levels = engine.getFileNames("Levels/" + worldFolderNames[i]);

						worldData.setLevelNumber(levels.length);
						if (worldData.getLastLevelUnlocked() > levels.length) {
							worldData.setLastLevelUnlocked(levels.length);
						}

						break;
					}
				}

				if (!loadedFolder) {
					// World does not exist in save file
					WorldData newWorld = new WorldData();
					newWorld.setWorldName(worldFolderNames[i]);

					String[] levels = engine.getFileNames("Levels/" + worldFolderNames[i]);
					newWorld.setLevelNumber(levels.length);

					addWorld(newWorld);
				}
			}

			int erasedFiles = 0;
			for (int i = 0; i < numberOfWorlds; i++) {
				if (!loadedWorldSaveData[i]) {
					// File does exist in save data, but does not exist in folder.
					// We have to delete the info of the world.
					deleteWorldByIndex(i - erasedFiles);
					erasedFiles++;
				}
			}
		} else { // File does not exist, we need to create world data from assets
			for (int i = 0; i < worldFolderNames.length; i++) {
				WorldData newWorld = new WorldData();
				newWorld.setWorldName(worldFolderNames[i]);

				String[] levels = engine.getFileNames("Levels/" + worldFolderNames[i]);
				newWorld.setLevelNumber(levels.length);

				addWorld(newWorld);
			}
		}

		String[] backgroundsFolderNames = engine.getFileNames("Shop/Backgrounds");
		String[] circlesFolderNames = engine.getFileNames("Shop/Circles");
		String[] themesFolderNames = engine.getFileNames("Shop/Themes");
		int numberOfBackgrounds = _backgrounds.size();
		int numberOfCircles = _circles.size();
		int numberOfThemes = _themes.size();
		boolean[] loadedBackgroundSaveData = new boolean[numberOfBackgrounds];
		boolean[] loadedCirclesSaveData = new boolean[numberOfCircles];
		boolean[] loadedThemesSaveData = new boolean[numberOfThemes];

		if (doesSaveExist) {
			for (int i = 0; i < backgroundsFolderNames.length; i++) {
				boolean loadedBackground = false;

				for (int j = 0; j < numberOfBackgrounds; j++) {
					Background savedBackground = _backgrounds.get(j);

					if (savedBackground.name.equals(backgroundsFolderNames[i])) {
						// Background exists in save file
						loadedBackground = true;
						loadedBackgroundSaveData[j] = true;

						// Update background
						final Background backgroundAsset = engine.jsonToObject("Shop/Backgrounds/" + backgroundsFolderNames[i], Background.class);
						savedBackground.image = backgroundAsset.image;
						savedBackground.price = backgroundAsset.price;

						break;
					}
				}

				if (!loadedBackground) {
					// Background does not exist in save file
					final Background background = engine.jsonToObject("Shop/Backgrounds/" + backgroundsFolderNames[i], Background.class);
					background.name = backgroundsFolderNames[i];

					addBackground(background);
				}
			}

			int erasedFiles = 0;
			for (int i = 0; i < numberOfBackgrounds; i++) {
				if (!loadedBackgroundSaveData[i]) {
					// File does exist in save data, but does not exist in folder.
					// We have to delete the info of the world.
					removeBackgroundByIndex(i - erasedFiles);

					if (_currentBackground > i - erasedFiles)
						setBackground(_currentBackground - 1);
					else if (_currentBackground == i - erasedFiles)
						setBackground(-1);

					erasedFiles++;
				}
			}

			for (int i = 0; i < circlesFolderNames.length; i++) {
				boolean loadedCircles = false;

				for (int j = 0; j < numberOfCircles; j++) {
					Circles savedCircles = _circles.get(j);

					if (savedCircles.name.equals(circlesFolderNames[i])) {
						// Circles exist in save file
						loadedCircles = true;
						loadedCirclesSaveData[j] = true;

						//Update circles
						final Circles circlesAsset = engine.jsonToObject("Shop/Circles/" + circlesFolderNames[i], Circles.class);
						savedCircles.skin = circlesAsset.skin;
						if(savedCircles.skin)
							savedCircles.packPath = circlesAsset.packPath;
						else
							savedCircles.colors = circlesAsset.colors.clone();
						savedCircles.price = circlesAsset.price;

						break;
					}
				}

				if (!loadedCircles) {
					// Background does not exist in save file
					final Circles circles = engine.jsonToObject("Shop/Circles/" + circlesFolderNames[i], Circles.class);
					circles.name = circlesFolderNames[i];

					addCircles(circles);
				}
			}

			erasedFiles = 0;
			for (int i = 0; i < numberOfCircles; i++) {
				if (!loadedCirclesSaveData[i]) {
					// File does exist in save data, but does not exist in folder.
					// We have to delete the info of the world.
					removeCirclesByIndex(i - erasedFiles);

					if (_currentCircles > i - erasedFiles)
						setCircles(_currentCircles - 1);
					else if (_currentCircles == i - erasedFiles)
						setCircles(-1);

					erasedFiles++;
				}
			}

			for (int i = 0; i < themesFolderNames.length; i++) {
				boolean loadedTheme = false;

				for (int j = 0; j < numberOfThemes; j++) {
					Theme savedTheme = _themes.get(j);

					if (savedTheme.name.equals(themesFolderNames[i])) {
						// Theme exists in save file
						loadedTheme = true;
						loadedThemesSaveData[j] = true;

						//Update theme
						final Theme themeAsset = engine.jsonToObject("Shop/Themes/" + themesFolderNames[i], Theme.class);
						savedTheme.backgroundColor = themeAsset.backgroundColor;
						savedTheme.buttonColor = themeAsset.buttonColor;
						savedTheme.price = themeAsset.price;

						break;
					}
				}

				if (!loadedTheme) {
					// Background does not exist in save file
					final Theme theme = engine.jsonToObject("Shop/Themes/" + themesFolderNames[i], Theme.class);
					theme.name = themesFolderNames[i];

					addTheme(theme);
				}
			}

			erasedFiles = 0;
			for (int i = 0; i < numberOfThemes; i++) {
				if (!loadedThemesSaveData[i]) {
					// File does exist in save data, but does not exist in folder.
					// We have to delete the info of the world.
					removeThemesByIndex(i - erasedFiles);

					if (_currentTheme > i - erasedFiles)
						setTheme(_currentTheme - 1);
					else if (_currentTheme == i - erasedFiles)
						setTheme(-1);

					erasedFiles++;
				}
			}
		} else { // File does not exist, we need to create world data from assets
			for (int i = 0; i < backgroundsFolderNames.length; i++) {
				final Background background = engine.jsonToObject("Shop/Backgrounds/" + backgroundsFolderNames[i], Background.class);
				background.name = backgroundsFolderNames[i];

				addBackground(background);
			}

			for (int i = 0; i < circlesFolderNames.length; i++) {
				final Circles circles = engine.jsonToObject("Shop/Circles/" + circlesFolderNames[i], Circles.class);
				circles.name = circlesFolderNames[i];

				addCircles(circles);
			}

			for (int i = 0; i < themesFolderNames.length; i++) {
				final Theme theme = engine.jsonToObject("Shop/Themes/" + themesFolderNames[i], Theme.class);
				theme.name = themesFolderNames[i];

				addTheme(theme);
			}
		}
	}

	public void saveGameData(Engine engine) {
		try {
			ObjectOutputStream saveStream = engine.openSaveFileForWriting("GameData.json");

			String currentFileContent = "";

			int numberOfWorlds = _worldsData.size();
			saveStream.writeInt(numberOfWorlds);
			currentFileContent += numberOfWorlds;

			for (int i = 0; i < numberOfWorlds; i++) {
				WorldData world = _worldsData.get(i);
				saveStream.writeObject(world);
				currentFileContent += world.toString();
			}

			int money = _money;
			saveStream.writeInt(money);
			currentFileContent += money;

			int backgroundsNumber = _backgrounds.size();
			saveStream.writeInt(backgroundsNumber);
			currentFileContent += backgroundsNumber;

			for (int i = 0; i < backgroundsNumber; i++) {
				Background background = _backgrounds.get(i);
				saveStream.writeObject(background);
				currentFileContent += background.toString();
			}

			int currentBackground = _currentBackground;
			saveStream.writeInt(currentBackground);
			currentFileContent += currentBackground;

			int circlesNumber = _circles.size();
			saveStream.writeInt(circlesNumber);
			currentFileContent += circlesNumber;

			for (int i = 0; i < circlesNumber; i++) {
				Circles circles = _circles.get(i);
				saveStream.writeObject(circles);
				currentFileContent += circles.toString();
			}

			int currentCircle = _currentCircles;
			saveStream.writeInt(currentCircle);
			currentFileContent += currentCircle;

			int themesNumber = _themes.size();
			saveStream.writeInt(themesNumber);
			currentFileContent += themesNumber;

			for (int i = 0; i < themesNumber; i++) {
				Theme theme = _themes.get(i);
				saveStream.writeObject(theme);
				currentFileContent += theme.toString();
			}

			int currentTheme = _currentTheme;
			saveStream.writeInt(currentTheme);
			currentFileContent += currentTheme;

			// Save LevelData
			LevelData data = _currentLevelData;
			saveStream.writeObject(data);

			if (data != null) {
				currentFileContent += data.toString();
			}

			// HASH
			String salt = "contrasenya";

			String hash = engine.hashJson(currentFileContent);
			String finalHash =  engine.hashJson(hash + currentFileContent + salt);

			saveStream.writeObject(finalHash);
			saveStream.close();
			engine.closeSaveFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public WorldData getWorldDataByIndex(int index) {
		return _worldsData.get(index);
	}

	public void addWorld(WorldData newWorld) {
		_worldsData.add(newWorld);
	}

	public void deleteWorldByIndex(int index) {
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

	public ArrayList<Background> getBackgrounds() {
		return _backgrounds;
	}

	public ArrayList<Circles> getCircles() {
		return _circles;
	}

	public ArrayList<Theme> getThemes() {
		return _themes;
	}

	public void addBackground(Background background) {
		_backgrounds.add(background);
	}

	public void addCircles(Circles circles) {
		_circles.add(circles);
	}

	public void addTheme(Theme theme) {
		_themes.add(theme);
	}

	public void removeBackgroundByIndex(int index) {
		_backgrounds.remove(index);
	}

	public void removeCirclesByIndex(int index) {
		_circles.remove(index);
	}

	public void removeThemesByIndex(int index) {
		_themes.remove(index);
	}

	public boolean hasBackground(int index) {
		//Default background
		if (index == -1)
			return true;

		//No es valido
		if (index < 0 || index >= _backgrounds.size())
			return false;

		return _backgrounds.get(index).acquired;
	}

	public boolean hasCircle(int index) {
		//Default circles
		if (index == -1)
			return true;

		//No es valido
		if (index < 0 || index >= _circles.size())
			return false;

		return _circles.get(index).acquired;
	}

	public boolean hasTheme(int index) {
		//Default theme
		if (index == -1)
			return true;

		//No es valido
		if (index < 0 || index >= _themes.size())
			return false;

		return _themes.get(index).acquired;
	}

	public boolean purchaseBackground(int index, int price) {
		//No es valido
		if (index < 0 || index >= _backgrounds.size())
			return false;

		//Ya lo tienes
		if (_backgrounds.get(index).acquired)
			return false;

		//Compramos fondo
		if (_money >= price) {
			_money -= price;
			_backgrounds.get(index).acquired = true;
		}

		return _backgrounds.get(index).acquired;
	}

	public boolean setBackground(int index) {
		//Erase selection
		if (index == -1) {
			_currentBackground = -1;
			return true;
		}

		//No es valido
		if (index < 0 || index >= _backgrounds.size())
			return false;

		//No lo tienes
		if (!_backgrounds.get(index).acquired)
			return false;

		_currentBackground = index;
		return true;
	}

	public boolean purchaseCircle(int index, int price) {
		//No es valido
		if (index < 0 || index >= _circles.size())
			return false;

		//Ya lo tienes
		if (_circles.get(index).acquired)
			return false;

		//Compramos fondo
		if (_money >= price) {
			_money -= price;
			_circles.get(index).acquired = true;
		}

		return _circles.get(index).acquired;
	}

	public boolean setCircles(int index) {
		//Erase selection
		if (index == -1) {
			_currentCircles = -1;
			return true;
		}

		//No es valido
		if (index < 0 || index >= _circles.size())
			return false;

		//No lo tienes
		if (!_circles.get(index).acquired)
			return false;

		_currentCircles = index;
		return true;
	}

	public boolean purchaseTheme(int index, int price) {
		//No es valido
		if (index < 0 || index >= _themes.size())
			return false;

		//Ya lo tienes
		if (_themes.get(index).acquired)
			return false;

		//Compramos fondo
		if (_money >= price) {
			_money -= price;
			_themes.get(index).acquired = true;
		}

		return _themes.get(index).acquired;
	}

	public boolean setTheme(int index) {
		//Erase selection
		if (index == -1) {
			_currentTheme = -1;
			return true;
		}

		//No es valido
		if (index < 0 || index >= _themes.size())
			return false;

		//No lo tienes
		if (!_themes.get(index).acquired)
			return false;

		_currentTheme = index;
		return true;
	}

	public int getCurrentBackground() {
		return _currentBackground;
	}

	public int getCurrentCircles() {
		return _currentCircles;
	}

	public int getCurrentTheme() {
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
