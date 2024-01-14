package com.example.logiclib;

import java.io.Serializable;

public class Circles implements Serializable {
	public String name;
	public boolean skin; // True si los circulos son imágenes, false si son colores
	public String packPath = "";
	public String[] colors = null;
	public int price;
	public boolean acquired = false;

	@Override
	public String toString() {
		return "Circles[name_" + name + "skin_" + Boolean.toString(skin) + "_packPath_" + packPath
				+ "_price_" + Integer.toString(price) + "_acquired_" + Boolean.toString(acquired) + "]";
	}
}
