package com.example.logiclib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Combination {
    private final int _length;
    private final int[] _numbers;

    enum HintEnum {EMPTY, WHITE, BLACK};

    Combination(int length, int colorNumber, boolean repeatColors) {
        _length = length;
        _numbers = new int[_length];
        Random rand = new Random();

        if (!repeatColors) {
            List<Integer> noRepeatedNumbers = new ArrayList<>();
            for (int i = 0; i < colorNumber; i++) {
                noRepeatedNumbers.add(i + 1);
            }

            for (int i = 0; i < _length; i++) {
                int randomNum = rand.nextInt(colorNumber - i);
                _numbers[i] = noRepeatedNumbers.get(randomNum);
                noRepeatedNumbers.remove(randomNum);
            }
        } else {
            for (int i = 0; i < _length; i++) {
                int randomNum = rand.nextInt(colorNumber);
                _numbers[i] = randomNum + 1;
            }
        }
    }

    // CONSTRUCTORA USUARIO (Predicciones)
    Combination(int length) {
        _length = length;
        _numbers = new int[_length];

        for (int i = 0; i < _length; i++) {
            _numbers[i] = -1;
        }
    }

    public void deleteColor(int index) {
        _numbers[index] = -1;
    }

    // Coloca el color en el primer hueco libre de la combinación
    public void setNextColor(int colorID) {
        for (int i = 0; i < _numbers.length; i++) {
            if (_numbers[i] == -1) {
                _numbers[i] = colorID;
                break;
            }
        }
    }

    public void printCombination() {
        for (int i = 0; i < _length; i++) {
            System.out.print(_numbers[i] + " ");
        }
        System.out.println();
    }

    // Comprueba si la combinación pasada a la función es igual que la combinación generada aleatoriamente
    // Si es igual, devuelve true.
    public boolean equals(Combination otherComb) {
        return Arrays.equals(_numbers, otherComb._numbers);
    }

    // Se encarga de crear el array de pistas, recorre la combinación generada aleatoriamente con la pasada por
    // la función, si un número se encuentra, y además se encuentra en la misma posición, la pista será "negra".
    // Si un número se encuentra, pero no está en la misma posición, la pista será "blanca".
    // En el array de las pistas, primero se colocarán el número de pistas negras, luego se colocarán las blancas, y por último
    // se quedará vacío (si el jugador no ha acertado el numero y tampoco se encuentra en el array).
    public HintEnum[] getHint(Combination otherComb) {
        HintEnum[] hints = new HintEnum[_length];
        for (int i = 0; i < _length; i++)
            hints[i] = HintEnum.EMPTY;

        int black = 0;
        int white = 0;
        int whitePosition = 0;
        boolean addWhite = false;

        boolean[] found = new boolean[_length];
        boolean[] isWhite = new boolean[_length];

        for (int i = 0; i < _length; i++) {
            if (found[i])
                continue;
            for (int j = 0; j < _length; j++) {
                if (found[j] || _numbers[i] != otherComb._numbers[j])
                    continue;

                if (j < i) {
                    if (!isWhite[j]) {
                        addWhite = true;
                        whitePosition = j;
                        isWhite[whitePosition] = true;
                    }
                } else if (i == j) {
                    black++;
                    found[j] = true;

                    if (addWhite) {
                        addWhite = false;
                        isWhite[whitePosition] = false;
                    }
                    break;
                } else { // j > i
                    if (_numbers[j] == otherComb._numbers[j]) {
                        black++;
                        found[j] = true;
                    } else if (!addWhite) {
                        addWhite = true;

                        whitePosition = j;
                        isWhite[whitePosition] = true;
                    }
                }
            }
            if (addWhite) {
                white++;
                addWhite = false;
                whitePosition = 0;
            }
        }

        for (int i = 0; i < black; i++)
            hints[i] = HintEnum.BLACK;

        for (int i = black; i < black + white; i++)
            hints[i] = HintEnum.WHITE;

        return hints;
    }

    public int[] getColors() {
        return _numbers;
    }
}
