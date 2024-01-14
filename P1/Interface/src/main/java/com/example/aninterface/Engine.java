package com.example.aninterface;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface Engine {
	Graphics getGraphics();

	void setCurrentScene(Scene _currentScene);

	Scene getScene();

	Input getInput();

	Audio getAudio();

	ObjectInputStream openSaveFileForReading(String fileName);
	ObjectOutputStream openSaveFileForWriting(String fileName);
	void closeSaveFile();
}
