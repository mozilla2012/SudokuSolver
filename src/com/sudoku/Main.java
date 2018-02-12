package com.sudoku;

import java.io.BufferedReader;
import java.io.FileReader ;
import java.io.IOException;
import java.util.Date;

public class Main {

    public static void main(String[] args) {

        Board board = readBoard();
        if (null == board) {
            System.out.println("Could not read board.");
            return;
        }
        System.out.println(board);
        long startTime = new Date().getTime();

        int totalPossibilities = board.countTotalPossibilities();
        int oldTotal;
        while (totalPossibilities > 0) {
            board.solve();
            oldTotal = totalPossibilities;
            totalPossibilities = board.countTotalPossibilities();
            System.out.println("Total possibilities: " + totalPossibilities);
            if (oldTotal == totalPossibilities) {
                System.out.println("Uh oh, we're stuck!");
                System.out.println(board.printPossibilities());
                break;
            }
        }
        printResults(board, startTime);
    }

    private static void printResults(Board board, long startTime) {
        long elapsedTime = new Date().getTime() - startTime;
        String solvedBoard = board.toString();
        System.out.println(solvedBoard);
        System.out.println("Took " + elapsedTime + " ms");
        String originalBoard = readBoard().toString();
        int originalBlanks = originalBoard.length() - originalBoard.replace("0","").length();
        int solvedBlanks = solvedBoard.length() - solvedBoard.replace("0","").length();
        int diffInBlanks = originalBlanks - solvedBlanks;
        System.out.println("You solved " + diffInBlanks);
    }

    private static Board readBoard() {
        FileReader test;
        int boxSize;
        Board board = null;
        try {
            test = new FileReader("resources/testBoard.txt");
            BufferedReader reader = new BufferedReader(test);
            boxSize = Integer.valueOf(reader.readLine());
            board = new Board(boxSize);
            int sideLength = boxSize * boxSize;
            int totalNums = sideLength * sideLength;
            int xCounter = 0;
            int yCounter = 0;
            int nextVal;
            for (int i = 0; i < totalNums; i++) {
                nextVal = Integer.valueOf(String.valueOf(Character.toChars(reader.read())));

//                System.out.println("Setting " + nextVal + " to " + xCounter + "," + yCounter);
                board.setValue(xCounter++, yCounter, nextVal);
                if (xCounter >= sideLength) {
                    xCounter = 0;
                    yCounter++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return board;
    }
}
