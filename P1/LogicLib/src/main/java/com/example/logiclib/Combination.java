package com.example.logiclib;

import java.util.Arrays;
import java.util.Random;

public class Combination {
    private int length;
    private int[] numbers;

    // CONSTRUCTORA ALEATORIA (Resultado)
    Combination(int _length, int _colorNumber){
        length = _length;
        numbers = new int[length];
        Random rand = new Random();

        for (int i = 0; i < length; i++){
            int randomNum = rand.nextInt(_colorNumber + 1);
            numbers[i] = randomNum;
        }
    }

    // CONSTRUCTORA USUARIO (Predicciones)
    Combination(int _length) {
        length = _length;
        numbers = new int[length];

        for (int i = 0; i < length; i++){
            numbers[i] = -1;
        }
    }

    public int getLength() {
        return length;
    }

    public int[] getNumbers() {
        return numbers;
    }

    public void setColor(int index, int color) {
        numbers[index] = color;
    }

    public int getColor(int index) {
        return numbers[index];
    }

    public void printCombination() {
        for (int i = 0; i < length; i++){
            System.out.print(numbers[i] + " ");
        }
    }

    public boolean equals(Combination otherComb){
        return Arrays.equals(numbers, otherComb.numbers);
    }



}
