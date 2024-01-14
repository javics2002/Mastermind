package com.example.logiclib;

import java.io.Serializable;

public class Theme implements Serializable {
	public String name;
	public String backgroundColor;
	public String buttonColor;
	public int price;
	public boolean acquired = false;

	@Override
	public String toString() {
		return "Theme[name_" + name + "backgroundColor_" + backgroundColor + "_buttonColor_" + buttonColor
				+ "_price_" + Integer.toString(price) + "_acquired_" + Boolean.toString(acquired) + "]";
	}
}
