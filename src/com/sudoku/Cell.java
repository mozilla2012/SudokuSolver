package com.sudoku;

import java.util.ArrayList;

public class Cell {
    public int value;
    private boolean[] possibilities;

    Cell(int size) {
        this.value = 0;
        this.possibilities = new boolean[size];
        for (int val = 0; val < size; val++) {
            possibilities[val] = true;
        }
    }

    public void setValue(int value) {
        this.value = value;
        if (value > 0) {
            removeAllPossibilities();
        }
    }

    public int countPossibilites() {
        int possibilityCount = 0;
        for (boolean p : possibilities) {
            if (p) {
                possibilityCount++;
            }
        }
        return possibilityCount;
    }

    public void removePossibility(int newPossibility) {
        if (0 == newPossibility) {
            throw new IllegalStateException("You can't remove zero from the list of possibilities.");
        }
        possibilities[newPossibility - 1] = false;
    }

    public boolean isPossible(int question) {
        if (0 == question) {
            throw new IllegalStateException("You can't ask if 0 is available.");
        }
        return possibilities[question - 1];
    }

    private void removeAllPossibilities() {
        for (int val = 1; val <= possibilities.length; val++) {
            removePossibility(val);
        }
    }

    /**
     * Checks to see if this cell is solvable. If so, return the only possibility. If not, return 0.
     * @return only possibility, else 0.
     */
    public int solveCell() {
        int possibilityCount = countPossibilites();
        if (possibilityCount <= 0) {
            throw new IllegalStateException("There should never be zero possibilities if the cell is unsolved.");
        } else if (possibilityCount > 1) {
            return 0;
        } else {
            for (int i = 0; i < possibilities.length; i++) {
                if (possibilities[i]) {
                    return i+1;
                }
            }
            // If we didn't find the only possibility in the list, something is wrong.
            throw new IllegalStateException("There was one possibility, but we didn't find it.");
        }
    }

    public String printPossibilities() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < possibilities.length; i++) {
            if (possibilities[i]) {
                b.append(i + 1);
            }
        }
        return b.toString();
    }

    public ArrayList<Integer> getPossibilitiesNumeric() {
        ArrayList<Integer> possibleNumbers = new ArrayList<>();
        for (int i = 0; i < possibilities.length; i++) {
            if (possibilities[i]) {
                possibleNumbers.add(i+1);
            }
        }
        return possibleNumbers;
    }

    @Override
    public String toString() {
        return "" + this.value;
    }

}
