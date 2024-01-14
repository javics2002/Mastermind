package com.example.aninterface;

public interface Scene {
	void handleEvents(Input.TouchEvent event);

	void update(double deltaTime);

	void render(Graphics graphics);
	void recieveADMSG();
}
