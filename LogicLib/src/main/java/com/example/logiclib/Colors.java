package com.example.logiclib;

import java.util.HashMap;

public class Colors {
	public enum ColorName {
		RED, ORANGE, YELLOW, GREEN, CYAN, BLUE, MAGENTA, PINK, PURPLE, DARKGRAY,
		LIGHTGRAY, WHITE, BLACK, COMBINATIONLAYOUT, BACKGROUND, TRASPARENTBACKGROUND,
		BACKGROUNDRED, BACKGROUNDORANGE, BACKGROUNDYELLOW, BACKGROUNDGREEN, BACKGROUNDBLUE,
		LEVELCOMPLETED, LEVELUNCOMPLETED,
		CUSTOMCIRCLE1_1, CUSTOMCIRCLE1_2, CUSTOMCIRCLE1_3, CUSTOMCIRCLE1_4, CUSTOMCIRCLE1_5,
		CUSTOMCIRCLE1_6, CUSTOMCIRCLE1_7, CUSTOMCIRCLE1_8, CUSTOMCIRCLE1_9,
		CUSTOMCIRCLE2_1, CUSTOMCIRCLE2_2, CUSTOMCIRCLE2_3, CUSTOMCIRCLE2_4, CUSTOMCIRCLE2_5,
		CUSTOMCIRCLE2_6, CUSTOMCIRCLE2_7, CUSTOMCIRCLE2_8, CUSTOMCIRCLE2_9,
		CUSTOMCIRCLE3_1, CUSTOMCIRCLE3_2, CUSTOMCIRCLE3_3, CUSTOMCIRCLE3_4, CUSTOMCIRCLE3_5,
		CUSTOMCIRCLE3_6, CUSTOMCIRCLE3_7, CUSTOMCIRCLE3_8, CUSTOMCIRCLE3_9,
		CUSTOMTHEME1_1, CUSTOMTHEME1_2, CUSTOMTHEME2_1, CUSTOMTHEME2_2,
		CUSTOMTHEME3_1, CUSTOMTHEME3_2, CUSTOMTHEME4_1, CUSTOMTHEME4_2,
		CUSTOMTHEME5_1, CUSTOMTHEME5_2
	}

	public static HashMap<ColorName, Integer> colorValues;

	static {
		colorValues = new HashMap<>();
		colorValues.put(ColorName.RED, 0xFFFF0000);
		colorValues.put(ColorName.ORANGE, 0xFFFFA500);
		colorValues.put(ColorName.YELLOW, 0xFFFFFF00);
		colorValues.put(ColorName.GREEN, 0xFF00FF00);
		colorValues.put(ColorName.CYAN, 0xFF00FFFF);
		colorValues.put(ColorName.BLUE, 0xFF0000FF);
		colorValues.put(ColorName.MAGENTA, 0xFFFF00FF);
		colorValues.put(ColorName.PINK, 0xFFFF69B4);
		colorValues.put(ColorName.PURPLE, 0xFF4B0082);
		colorValues.put(ColorName.DARKGRAY, 0xFF5A5A5A);
		colorValues.put(ColorName.LIGHTGRAY, 0xFF9A9A9A);
		colorValues.put(ColorName.WHITE, 0xFFFFFFFF);
		colorValues.put(ColorName.BLACK, 0xFF000000);
		colorValues.put(ColorName.COMBINATIONLAYOUT, 0xFFF8F4ED);
		colorValues.put(ColorName.BACKGROUND, 0xFFE7D6BD);
		colorValues.put(ColorName.TRASPARENTBACKGROUND, 0xCCF8F4ED);
		colorValues.put(ColorName.BACKGROUNDRED, 0xFFF76E6E);
		colorValues.put(ColorName.BACKGROUNDORANGE, 0xFFEB9A53);
		colorValues.put(ColorName.BACKGROUNDYELLOW, 0xFFEBD753);
		colorValues.put(ColorName.BACKGROUNDGREEN, 0xFF5DEB53);
		colorValues.put(ColorName.BACKGROUNDBLUE, 0xFF53D4EB);
		colorValues.put(ColorName.LEVELCOMPLETED, 0xAAE7D6BD);
		colorValues.put(ColorName.LEVELUNCOMPLETED, 0xFFE7D6BD);

		//Tema barbie
		colorValues.put(ColorName.CUSTOMTHEME1_1, 0xFFFFE7EE);
		colorValues.put(ColorName.CUSTOMTHEME1_2, 0xFFFF6D94);

		//Circulos barbie
		colorValues.put(ColorName.CUSTOMCIRCLE1_1, 0xFFDEA9BD);
		colorValues.put(ColorName.CUSTOMCIRCLE1_2, 0xFFB80542);
		colorValues.put(ColorName.CUSTOMCIRCLE1_3, 0xFF92123A);
		colorValues.put(ColorName.CUSTOMCIRCLE1_4, 0xFF9B058B);
		colorValues.put(ColorName.CUSTOMCIRCLE1_5, 0xFFFF9CE9);
		colorValues.put(ColorName.CUSTOMCIRCLE1_6, 0xFFFF8ABA);
		colorValues.put(ColorName.CUSTOMCIRCLE1_7, 0xFFFE9BCA);
		colorValues.put(ColorName.CUSTOMCIRCLE1_8, 0xFFFDC2C2);
		colorValues.put(ColorName.CUSTOMCIRCLE1_9, 0xFFE96AAA);

		//Tema desayuno
		colorValues.put(ColorName.CUSTOMTHEME2_1, 0xFFE1B879);
		colorValues.put(ColorName.CUSTOMTHEME2_2, 0xFFDD9C4C);

		//Circulos desayuno
		colorValues.put(ColorName.CUSTOMCIRCLE2_1, 0xFFFFF3C3);
		colorValues.put(ColorName.CUSTOMCIRCLE2_2, 0xFFFF7E6F);
		colorValues.put(ColorName.CUSTOMCIRCLE2_3, 0xFFFFEA94);
		colorValues.put(ColorName.CUSTOMCIRCLE2_4, 0xFFC1905E);
		colorValues.put(ColorName.CUSTOMCIRCLE2_5, 0xFF724E2C);
		colorValues.put(ColorName.CUSTOMCIRCLE2_6, 0xFF271300);
		colorValues.put(ColorName.CUSTOMCIRCLE2_7, 0xFF9C6F44);
		colorValues.put(ColorName.CUSTOMCIRCLE2_8, 0xFF563517);
		colorValues.put(ColorName.CUSTOMCIRCLE2_9, 0xFF6F4827);

		//Tema pikachu
		colorValues.put(ColorName.CUSTOMTHEME3_1, 0xFFF3D245);
		colorValues.put(ColorName.CUSTOMTHEME3_2, 0xFFDE6145);

		//Circulos eeveelutions
		colorValues.put(ColorName.CUSTOMCIRCLE3_1, 0xFFC3915C);
		colorValues.put(ColorName.CUSTOMCIRCLE3_2, 0xFF347190);
		colorValues.put(ColorName.CUSTOMCIRCLE3_3, 0xFFF3CF6F);
		colorValues.put(ColorName.CUSTOMCIRCLE3_4, 0xFFEB804C);
		colorValues.put(ColorName.CUSTOMCIRCLE3_5, 0xFFE4C9DC);
		colorValues.put(ColorName.CUSTOMCIRCLE3_6, 0xFF363F44);
		colorValues.put(ColorName.CUSTOMCIRCLE3_7, 0xFF679D7D);
		colorValues.put(ColorName.CUSTOMCIRCLE3_8, 0xFF68BBC9);
		colorValues.put(ColorName.CUSTOMCIRCLE3_9, 0xFFE07D8F);

		//Tema invierno
		colorValues.put(ColorName.CUSTOMTHEME5_1, 0xFFEEFFFF);
		colorValues.put(ColorName.CUSTOMTHEME5_2, 0xFF9CFEFF);

		//Tema oscuro
		colorValues.put(ColorName.CUSTOMTHEME4_1, 0xFF131313);
		colorValues.put(ColorName.CUSTOMTHEME4_2, 0xFF3E3E3E);
	}

	public static int getColor(int colorID) {
		return Colors.colorValues.get(Colors.ColorName.values()[colorID]);
	}

	public static int parseARGB(String hexadecimalARGBColor) {
		// Eliminar el carácter '#' o los caracteres "Ox" si están presentes
		if (hexadecimalARGBColor.startsWith("#")) {
			hexadecimalARGBColor = hexadecimalARGBColor.substring(1);
		} else if (hexadecimalARGBColor.startsWith("0x")) {
			hexadecimalARGBColor = hexadecimalARGBColor.substring(2);
		}

		int argb = (int) Long.parseLong(hexadecimalARGBColor, 16);

		// Si la cadena no tiene el canal alfa (A), agregar el valor predeterminado (255)
		if (hexadecimalARGBColor.length() <= 6) {
			argb |= 0xFF000000;
		}

		return argb;
	}
}
