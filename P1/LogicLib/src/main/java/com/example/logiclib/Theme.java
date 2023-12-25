package com.example.logiclib;

import java.io.Serializable;

public class Theme implements Serializable {
    public String name;
    String backgroundColor;
    String buttonColor;
    int price;
    boolean acquired = false;
    @Override
    public String toString() {
        return "Theme[name_" + name + "backgroundColor_" + backgroundColor + "_buttonColor_" + buttonColor
                + "_price_" + Integer.toString(price) + "_acquired_" + Boolean.toString(acquired) + "]";
    }
}
