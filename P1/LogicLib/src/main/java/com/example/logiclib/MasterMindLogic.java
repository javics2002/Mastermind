package com.example.logiclib;

import java.util.Random;
import java.util.Scanner;

public class MasterMindLogic {
    public static void main(String[] args) {
        //System.out.println("Enter the length of the combination");
        Random rand = new Random();

        int combLength = 5;  // Read user input
        int tryNumber = 5;
        int colorNumber = 5;

        Combination resultCombination = new Combination(combLength, colorNumber);
        System.out.println("Solution: ");
        resultCombination.printCombination();

        Combination[] attempts = new Combination[tryNumber];

        for (int i = 0; i < tryNumber; i++)
            attempts[i] = new Combination(combLength);

        for (int i = 0; i < tryNumber; i++) {
            //System.out.println("Attempt " + i +  " :");

            for (int j = 0; j < combLength; j++) {
                if(i == tryNumber - 1){
                    attempts[i].setColor(j, resultCombination.getColor(j));
                }
                else{
                    int randomNum = rand.nextInt(colorNumber + 1);
                    attempts[i].setColor(j, randomNum);
                }
            }

            attempts[i].printCombination();
            if (resultCombination.equals(attempts[i])) {
                //System.out.println("Correcto");
                resultCombination.printHint(attempts[i]);
                break;
            }
            else {
                resultCombination.printHint(attempts[i]);
                //System.out.println("Incorrecto");
            }
        }
//        Combination[] attempts = new Combination[1];
//        attempts[0] = new Combination(combLength);
//        for(int i = 0; i < combLength; i++){
//            attempts[0].setColor(i, resultCombination.getColor(i));
//        }
    }
}