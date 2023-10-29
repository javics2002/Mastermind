package com.example.logiclib;

import java.util.Arrays;
import java.util.Random;

public class Combination {
    private int length;
    private int[] numbers;
    enum HintEnum {EMPTY, WHITE, BLACK};

    // CONSTRUCTORA ALEATORIA (Resultado)
    Combination(int _length, int _colorNumber){
        length = _length;
        numbers = new int[length];
        Random rand = new Random();

        for (int i = 0; i < length; i++){
            int randomNum = rand.nextInt(_colorNumber);
            numbers[i] = randomNum + 1;
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

    // Coloca el color en el primer hueco libre de la combinación
    public void setNextColor(int colorID) {
        for (int i = 0; i < numbers.length; i++){
            if (numbers[i] == -1){
                numbers[i] = colorID;
                break;
            }
        }
    }

    public void printCombination() {
        for (int i = 0; i < length; i++){
            System.out.print(numbers[i] + " ");
        }
        System.out.println();
    }

    // Comprueba si la combinación pasada a la función es igual que la combinación generada aleatoriamente
    // Si es igual, devuelve true.
    public boolean equals(Combination otherComb) {
        return Arrays.equals(numbers, otherComb.numbers);
    }

    // Se encarga de crear el array de pistas, recorre la combinación generada aleatoriamente con la pasada por
    // la función, si un número se encuentra, y además se encuentra en la misma posición, la pista será "negra".
    // Si un número se encuentra, pero no está en la misma posición, la pista será "blanca".
    // En el array de las pistas, primero se colocarán el número de pistas negras, luego se colocarán las blancas, y por último
    // se quedará vacío (si el jugador no ha acertado el numero y tampoco se encuentra en el array).
    public HintEnum[] getHint(Combination otherComb){
        HintEnum[] hints = new HintEnum[length];
        for(int i = 0; i < length; i++)
            hints[i] = HintEnum.EMPTY;

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
            hints[i] = HintEnum.BLACK;

        for(int i = black; i < black + white; i++)
            hints[i] = HintEnum.WHITE;

        return hints;
    }
}
