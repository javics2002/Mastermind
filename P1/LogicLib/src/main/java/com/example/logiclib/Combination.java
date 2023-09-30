package com.example.logiclib;

import java.util.Arrays;
import java.util.Random;

public class Combination {
    private int length;
    private int[] numbers;

    enum hint {EMPTY, WHITE, BLACK};

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

    public boolean equals(Combination otherComb) {
        return Arrays.equals(numbers, otherComb.numbers);
    }

    public hint[] getHint(Combination otherComb){
        hint[] hints = new hint[length];
        for(int i = 0; i < length; i++)
            hints[i] = hint.EMPTY;

        int black = 0;
        int white = 0;

        boolean[] found = new boolean[length];
        for(int i = 0; i < length; i++)
            found[i] = false;

        for(int i = 0; i < length; i++){
            for (int j = 0; j < length; j++){
                if(!found[j] && numbers[i] == otherComb.numbers[j]){
                    found[j] = true;
                    if(i == j){
                        black += 1;
                    }
                    else {
                        white += 1;
                    }
                }

            }
        }

        for(int i = 0; i < black; i++)
            hints[i] = hint.BLACK;

        for(int i = black; i < black + white; i++)
            hints[i] = hint.WHITE;

        return hints;
    }

    public void printHint(Combination otherComb) {
        hint[] hints = getHint(otherComb);
        for (int i = 0; i < length; i++){
            if(hints[i] == hint.BLACK)
                System.out.print("b ");
            else if(hints[i] == hint.WHITE)
                System.out.print("w ");
            else
                System.out.print(". ");
        }
    }
}
