package com.example.libengineandroid;

import com.example.aninterface.Input;

import java.util.ArrayList;

public class InputAndroid implements Input {
	private final TouchHandlerAndroid _handler; // Controlador de eventos táctiles

	InputAndroid() {
		_handler = new TouchHandlerAndroid(); // Inicializa un controlador de eventos táctiles
	}

	public ArrayList<TouchEvent> getTouchEvents() {
		return _handler.getTouchEvents(); // Obtiene la lista de eventos táctiles del controlador
	}

	public void clearEvents() {
		synchronized (_handler) {
			_handler.clearEvents(); // Limpia (borra) todos los eventos táctiles registrados en el controlador
		}
	}

	public TouchHandlerAndroid getTouchHandler() {
		return _handler; // Obtiene el controlador de eventos táctiles
	}
}