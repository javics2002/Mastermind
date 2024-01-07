package com.example.logiclib;

import java.io.Serializable;

public class Circles implements Serializable {
	public String name;
	boolean skin; // True si los circulos son imágenes, false si son colores
	String packPath = "";
	String[] colors = null;
	int price;
	boolean acquired = false;

	@Override
	public String toString() {
		return "Circles[name_" + name + "skin_" + Boolean.toString(skin) + "_packPath_" + packPath
				+ "_price_" + Integer.toString(price) + "_acquired_" + Boolean.toString(acquired) + "]";
	}
}
