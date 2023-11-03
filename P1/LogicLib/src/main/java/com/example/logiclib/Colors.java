package com.example.logiclib;
import java.util.HashMap;

public class Colors {
    public enum ColorName { RED, ORANGE, YELLOW, GREEN, CYAN, BLUE, MAGENTA, PINK, PURPLE, DARKGRAY, LIGHTGRAY, WHITE, BLACK, BACKGROUNDRED, BACKGROUNDORANGE, BACKGROUNDYELLOW, BACKGROUNDGREEN, BACKGROUNDBLUE}
    public static HashMap<ColorName, Integer> colorValues;
    static{
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
        colorValues.put(ColorName.BACKGROUNDRED, 0xFFF76E6E);
        colorValues.put(ColorName.BACKGROUNDORANGE, 0xFFEB9A53);
        colorValues.put(ColorName.BACKGROUNDYELLOW, 0xFFEBD753);
        colorValues.put(ColorName.BACKGROUNDGREEN, 0xFF5DEB53);
        colorValues.put(ColorName.BACKGROUNDBLUE, 0xFF53D4EB);

    }
    public static int getColor(int colorID){
        return Colors.colorValues.get(Colors.ColorName.values()[colorID]);
    }
}
