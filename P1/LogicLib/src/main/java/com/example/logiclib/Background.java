package com.example.logiclib;

import java.io.Serializable;

public class Background implements Serializable {
    public String name;

    String image;
    int price;

    boolean acquired = false;

    @Override
    public String toString() {
        return "Background[name_" + name + "_image_" + image + "_price_" + Integer.toString(price)
                + "_acquired_" + Boolean.toString(acquired) + "]";
    }
}
