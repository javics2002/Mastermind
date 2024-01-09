package com.example.aninterface;

public interface Engine {
	Graphics getGraphics();

	void setCurrentScene(Scene _currentScene);

	Scene getScene();

	Input getInput();

	void resume();

	void pause();

	Audio getAudio();

	void showAd();

	void shareScreenshot(int width, int height);

	<T> T jsonToObject(String fileName, Class<T> classOfT);

	String objectToJson(Object object);

	int filesInFolder(String folderPath);

	String[] getFileNames(String folderPath);

	void loadGameData();

	void saveGameData();
	void appeareanceBanner(boolean hasTo);
}
