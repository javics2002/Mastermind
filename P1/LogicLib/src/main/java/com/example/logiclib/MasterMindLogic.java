package com.example.logiclib;

import java.util.Scanner;

public class MasterMindLogic {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        //System.out.println("Enter the length of the combination");

        int combLength = 5;  // Read user input
        int tryNumber = 5;
        int colorNumber = 5;

        Combination resultCombination = new Combination(combLength, colorNumber);
        System.out.println("Solution:");
        resultCombination.printCombination();

        Combination[] attempts = new Combination[tryNumber];

        while (!scanner.hasNext())
            scanner.nextInt();

        for(int i = 0; i< tryNumber; i++) {
            System.out.println("Attempt " + i +  " :");

            for(int j = 0; j < combLength; j++){
                attempts[i].setColor(j, scanner.nextInt());
            }

            //if(resultCombination.equals(attempts[0]))
            //System.out.println();
        }


//        Combination[] attempts = new Combination[1];
//        attempts[0] = new Combination(combLength);
//        for(int i = 0; i < combLength; i++){
//            attempts[0].setColor(i, resultCombination.getColor(i));
//        }



    }
}