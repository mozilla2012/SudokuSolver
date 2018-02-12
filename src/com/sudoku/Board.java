package com.sudoku;

import java.util.ArrayList;

public class Board {
    private int size;
    private int boxSize;
    private Cell[][] board;

    Board(int boxSize) {
        this.boxSize = boxSize;
        size = boxSize * boxSize;
        board = new Cell[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                board[x][y] = new Cell(size);
            }
        }
    }

    public void setValue(int x, int y, int value) {
        board[x][y].setValue(value);
    }

    public void solve() {
        updatePossibilities(); // Should theoretically only need to run the first time.
        checkForUniques(); // Checks for cells with only 1 possibility and updates it - rerun often
        checkMatches(); // Checks for 2 cells with matching double possibilities - removes possibilities
        checkForPointingPairs(); // Checks within boxes to see if certain numbers can only exist - removes possibilities
    }

    /**
     * If multiple cells in the same row contain a common value that shows up in no other row in the same box, then
     * you can assume that value *must* be in that row in that box, and thus it can be removed from the rest of the
     * row outside of that box.
     */
    private void checkForPointingPairs() {
        checkPointingRows();
        checkPointingCols();
    }

    private void checkPointingCols() {
        for (int xBox = 0; xBox < boxSize; xBox++) {
            for (int yBox = 0; yBox < boxSize; yBox++) {
                for (int subX = 0; subX < boxSize; subX++) {
                    int superXCord = xBox * boxSize + subX;
                    for (int subY = 0; subY < boxSize; subY++) {
                        int superYCord = yBox * boxSize + subY;
                        ArrayList<Integer> possibilities = board[superXCord][superYCord].getPossibilitiesNumeric();
                        if (possibilities.size() > 0) {
//                            System.out.println("Checking for " + Arrays.toString(possibilities.toArray())
//                                    + " in col " + superXCord
//                                    + " of box " + xBox * boxSize + "," + yBox * boxSize);
                            possibilitiesLoop:
                            for (int possibility : possibilities) {
                                for (int subX2 = 0; subX2 < boxSize; subX2++) {
                                    int superXCord2 = xBox * boxSize + subX2;
                                    if (superXCord2 == superXCord) {
                                        continue;
                                    }
                                    for (int subY2 = 0; subY2 < boxSize; subY2++) {
                                        int superYCord2 = yBox * boxSize + subY2;
//                                        System.out.println("Checking if " + possibility
//                                                + " is in " + superXCord2 + "," + superYCord2 + ": "
//                                                + board[superXCord2][superYCord2].printPossibilities());
                                        if (board[superXCord2][superYCord2].getPossibilitiesNumeric()
                                                .contains(possibility)) {
//                                            System.out.println("Yo, " + superXCord2 + "," + superYCord2 + " contains " +
//                                                    possibility);
                                            continue possibilitiesLoop;
                                        }
                                    }
                                }
                                //If we get to this point and don't continue, then we can remove the number from the
                                // other cells in this same row!
//                                System.out.println(String.format("Hey, %d found in %d,%d was not found in another " +
//                                                "cell in the same box. Removing from col %d", possibility, superXCord,
//                                        superYCord, superXCord));
                                //Loop to remove from cells before this box
                                for(int y = 0; y < yBox * boxSize; y++ ) {
//                                    System.out.println("Before Removing possibilities from " + x);
                                    board[superXCord][y].removePossibility(possibility);
                                }

                                //Loop to remove from cells after this box
                                for(int y = (yBox+1) * boxSize; y < size; y++ ) {
//                                    System.out.println("After Removing possibilities from " + x);
                                    board[superXCord][y].removePossibility(possibility);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkPointingRows() {
        for (int yBox = 0; yBox < boxSize; yBox++) {
            for (int xBox = 0; xBox < boxSize; xBox++) {
                for (int subY = 0; subY < boxSize; subY++) {
                    int superYCord = yBox * boxSize + subY;
                    for (int subX = 0; subX < boxSize; subX++) {
                        int superXCord = xBox * boxSize + subX;
                        ArrayList<Integer> possibilities = board[superXCord][superYCord].getPossibilitiesNumeric();
                        if (possibilities.size() > 0) {
//                            System.out.println("Checking for " + Arrays.toString(possibilities.toArray())
//                                    + " in row " + superYCord
//                                    + " of box " + xBox * boxSize + "," + yBox * boxSize);
                            possibilitiesLoop:
                            for (int possibility : possibilities) {
                                for (int subY2 = 0; subY2 < boxSize; subY2++) {
                                    int superYCord2 = yBox * boxSize + subY2;
                                    if (superYCord2 == superYCord) {
                                        continue;
                                    }
                                    for (int subX2 = 0; subX2 < boxSize; subX2++) {
                                        int superXCord2 = xBox * boxSize + subX2;
//                                        System.out.println("Checking if " + possibility
//                                                + " is in " + superXCord2 + "," + superYCord2 + ": "
//                                                + board[superXCord2][superYCord2].printPossibilities());
                                        if (board[superXCord2][superYCord2].getPossibilitiesNumeric()
                                                .contains(possibility)) {
//                                            System.out.println("Yo, " + superXCord2 + "," + superYCord2 + " contains " +
//                                                    possibility);
                                            continue possibilitiesLoop;
                                        }
                                    }
                                }
                                //If we get to this point and don't continue, then we can remove the number from the
                                // other cells in this same row!
//                                System.out.println(String.format("Hey, %d found in %d,%d was not found in another " +
//                                        "cell in the same box. Removing from row %d", possibility, superXCord,
//                                        superYCord, superYCord));
                                //Loop to remove from cells before this box
                                for(int x = 0; x < xBox * boxSize; x++ ) {
//                                    System.out.println("Before Removing possibilities from " + x);
                                    board[x][superYCord].removePossibility(possibility);
                                }

                                //Loop to remove from cells after this box
                                for(int x = (xBox+1) * boxSize; x < size; x++ ) {
//                                    System.out.println("After Removing possibilities from " + x);
                                    board[x][superYCord].removePossibility(possibility);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Sets the value i to the cell at x,y. Then updates the possibilities of all cells in the same row, col, and box.
     */
    private void setAndUpdate(int x, int y, int i) {
        board[x][y].setValue(i);
        updateRow(y, i);
        updateCol(x, i);
        updateBox(x, y, i);
    }

    private void updateRow(int y, int i) {
        for (int x = 0; x < size; x++) {
            board[x][y].removePossibility(i);
        }
    }

    private void updateCol(int x, int i) {
        for (int y = 0; y < size; y++) {
            board[x][y].removePossibility(i);
        }
    }

    private void updateBox(int x, int y, int i) {
        int boxXMin = Math.floorDiv(x, boxSize) * boxSize;
        int boxXMax = Math.floorDiv(x, boxSize) * boxSize + boxSize;
        int boxYMin = Math.floorDiv(y, boxSize) * boxSize;
        int boxYMax = Math.floorDiv(y, boxSize) * boxSize + boxSize;
        for (int xBox = boxXMin; xBox < boxXMax; xBox++) {
            for (int yBox = boxYMin; yBox < boxYMax; yBox++) {
                board[xBox][yBox].removePossibility(i);
            }
        }
    }

    /**
     * Look through each row, column, and box. Update the possibilities list.
     */
    private void updatePossibilities() {
        for (int solveY = 0; solveY < size; solveY++) {
            outerloop:
            for (int solveX = 0; solveX < size; solveX++) {
                if (board[solveX][solveY].value != 0) {
                    continue;
                }
//                System.out.println("Cell " + solveX + "," + solveY +":");

                // Remove possibilities based on row
                for (int x = 0; x < size; x++) {
                    if (x != solveX) { // Don't waste time comparing to itself
                        int valueToRemove = board[x][solveY].value;
                        if (0 != valueToRemove) {
                            board[solveX][solveY].removePossibility(valueToRemove);
                        }
//                        System.out.println("x Removing " + valueToRemove
//                                + ". Possibilities: " + board[solveX][solveY].printPossibilities());
                        // If the value was zero, don't bother checking if we solved it.
                        if (valueToRemove > 0) {
                            int value = board[solveX][solveY].solveCell();
                            if (value > 0) {
//                                System.out.println("The cell " +solveX+","+solveY+ " only has one possibility: " + value);
                                setAndUpdate(solveX, solveY, value);
                                continue outerloop;
                            }
                        }
                    }
                }
                // Remove possibilities based on col
                for (int y = 0; y < size; y++) {
                    if (y != solveY) {
                        int valueToRemove = board[solveX][y].value;
                        if (0 != valueToRemove) {
                            board[solveX][solveY].removePossibility(valueToRemove);
                        }
//                        System.out.println("y Removing " + valueToRemove
//                                + ". Possibilities: " + board[solveX][solveY].printPossibilities());
                        if (valueToRemove > 0) {
                            int value = board[solveX][solveY].solveCell();
                            if (value > 0) {
//                                System.out.println("The cell " +solveX+","+solveY+ " only has one possibility: " + value);
                                setAndUpdate(solveX, solveY, value);
                                continue outerloop;
                            }
                        }
                    }
                }

                // Remove possibilities based on box
                int boxXMin = Math.floorDiv(solveX, boxSize) * boxSize;
                int boxXMax = Math.floorDiv(solveX, boxSize) * boxSize + boxSize;
                int boxYMin = Math.floorDiv(solveY, boxSize) * boxSize;
                int boxYMax = Math.floorDiv(solveY, boxSize) * boxSize + boxSize;

//                System.out.println("Looking at cell " + solveX + "," + solveY + ". Box is " +
//                boxXMin + "," + boxYMin + " to " + boxXMax + "," + boxYMax);

                for (int y = boxYMin; y < boxYMax; y++) {
                    for (int x = boxXMin; x < boxXMax; x++) {
                        if (!(x == solveX && y == solveY)) {
                            int valueToRemove = board[x][y].value;
                            if (0 != valueToRemove) {
                                board[solveX][solveY].removePossibility(valueToRemove);
                            }
//                            System.out.println("Removing " + valueToRemove
//                                    + ". Possibilities: " + board[solveX][solveY].printPossibilities());
                            if (valueToRemove > 0) {
                                int value = board[solveX][solveY].solveCell();
                                if (value > 0) {
//                                    System.out.println("The cell " +solveX+","+solveY+ " only has one possibility: " + value);
                                    setAndUpdate(solveX, solveY, value);
                                    continue outerloop;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Any cells with only one value? If so, solve them.
     */
    private void checkForUniques() {
        checkRowUniques();
        checkColUniques();
        checkBoxUniques();
    }

    private void checkRowUniques() {
        for (int y = 0; y < size; y++) {
            numberLoop:
            for (int i = 1; i <= size; i ++) {
//                System.out.println("Checking row "+y+" for " + i);
                for (int x = 0; x < size; x++) {
                    if (board[x][y].value == i) {
//                        System.out.println(i + " was already set; moving on...");
                        continue numberLoop;
                    }
                }

                int numberOfPossibleLocations = 0;
                int winner = 0;
                for (int x = 0; x < size; x++) {
//                    System.out.println("Checking " + x + "," + y);
                    if(board[x][y].isPossible(i)) {
//                        System.out.println(x + "," + y + " could be " + i);
                        numberOfPossibleLocations++;
                        winner = x;
                        if (numberOfPossibleLocations > 1) {
//                            System.out.println("Two cells could be " + i + ", so moving on");
                            continue numberLoop;
                        }
                    }
                }
                if (numberOfPossibleLocations == 1) {
//                    System.out.println("Only one " + i + " possible! " + winner + "," + y + " wins!");
                    setAndUpdate(winner,y,i);
                }
            }
        }
    }

    private void checkColUniques() {
        for (int x = 0; x < size; x++) {
            numberLoop:
            for (int i = 1; i <= size; i ++) {
//                System.out.println("Checking column "+x+" for " + i);
                for (int y = 0; y < size; y++) {
                    if (board[x][y].value == i) {
//                        System.out.println(i + " was already set; moving on...");
                        continue numberLoop;
                    }
                }

                int numberOfPossibleLocations = 0;
                int winner = 0;
                for (int y = 0; y < size; y++) {
//                    System.out.println("Checking " + x + "," + y);
                    if(board[x][y].isPossible(i)) {
//                        System.out.println(x + "," + y + " could be " + i);
                        numberOfPossibleLocations++;
                        winner = y;
                        if (numberOfPossibleLocations > 1) {
//                            System.out.println("Two cells could be " + i + ", so moving on");
                            continue numberLoop;
                        }
                    }
                }
                if (numberOfPossibleLocations == 1) {
//                    System.out.println("Only one " + i + " possible! " + winner + " wins!");
                    setAndUpdate(x, winner, i);
                }
            }
        }
    }

    private void checkBoxUniques() {
        for (int yBox = 0; yBox < boxSize; yBox++) {
            for (int xBox = 0; xBox < boxSize; xBox++) {
//                System.out.println("Checking box starting at " + xBox*boxSize + "," + yBox*boxSize);
                // Check unique values within the box.
                numberLoop:
                for (int i = 1; i <= size; i ++) {
                    // Check to see if i was already set in this box:
                    for (int subX = 0; subX < boxSize; subX++) {
                        for (int subY = 0; subY < boxSize; subY++) {
                            int superXCord = xBox * boxSize + subX;
                            int superYCord = yBox * boxSize + subY;
                            if(board[superXCord][superYCord].value == i){
//                                System.out.println(i + " was already set; moving on...");
                                continue numberLoop;
                            }
                        }
                    }

                    int numberOfPossibleLocations = 0;
                    int winnerX = 0;
                    int winnerY = 0;
                    for (int subX = 0; subX < boxSize; subX++) {
                        for (int subY = 0; subY < boxSize; subY++) {
                            int superXCord = xBox * boxSize + subX;
                            int superYCord = yBox * boxSize + subY;
//                            System.out.println("Checking " + (superXCord) + "," + (superYCord) + ": " +
//                                    board[superXCord][superYCord].printPossibilities());
                            if (board[superXCord][superYCord].isPossible(i)) {
//                                System.out.println((superXCord) + "," + (superYCord) + " could be " + i);
                                numberOfPossibleLocations++;
                                winnerX = superXCord;
                                winnerY = superYCord;
                                if (numberOfPossibleLocations > 1) {
//                                    System.out.println("Two cells could be " + i + ", so moving on");
                                    continue numberLoop;
                                }
                            }
                        }
                    }
                    if (numberOfPossibleLocations == 1) {
//                        System.out.println("Only one " + i + " possible! " + winnerX + "," + winnerY + " wins!");
                        setAndUpdate(winnerX, winnerY, i);
                    }
                }
            }
        }
    }

    /**
     * Are there any pairs cells that have the same two possibilities? If so, remove those possibilities from other
     * cells.
     */
    private void checkMatches() {
        checkRowMatches();
        checkColMatches();
        checkBoxMatches();
    }

    private void checkRowMatches() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                String possibilities = board[x][y].printPossibilities();
                if (possibilities.length() != 2) { //For now, only look for cells with two possibilities.
                    continue;
                }
                for (int x2 = x + 1; x2 < size; x2++){
                    String possibilitiesToCompare = board[x2][y].printPossibilities();
                    if(possibilities.equals(possibilitiesToCompare)) {
                        System.out.println("Shit, we have an X match! " + x + "," + y + " and " + x2 + "," + y + " = " +
                                possibilities);
                        // Remove the possibilities from all other spots in row
//                        int[] values = new int[possibilities.length()];
//                        for (int i = 0; i < values.length; i++) {
//                            values[i] = Integer.parseInt(String.valueOf(possibilities.charAt(i)));
//                        }
                        ArrayList<Integer> values = board[x][y].getPossibilitiesNumeric();
                        for (int x3 = 0; x3 < size; x3++){
                            if (x3 != x && x3 != x2) {
                                for (int value : values) {
                                    board[x3][y].removePossibility(value);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkColMatches() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                String possibilities = board[x][y].printPossibilities();
                if (possibilities.length() != 2) { //For now, only look for cells with two possibilities.
                    continue;
                }
                for (int y2 = y + 1; y2 < size; y2++){
                    String possibilitiesToCompare = board[x][y2].printPossibilities();
                    if(possibilities.equals(possibilitiesToCompare)) {
//                        System.out.println("Shit, we have a Y match! " + x + "," + y + " and " + x + "," + y2 + " = " +
//                                possibilities);
                        // Remove the possibilities from all other spots in col
//                        int[] values = new int[possibilities.length()];
//                        for (int i = 0; i < values.length; i++) {
//                            values[i] = Integer.parseInt(String.valueOf(possibilities.charAt(i)));
//                        }
                        ArrayList<Integer> values = board[x][y].getPossibilitiesNumeric();
                        for (int y3 = 0; y3 < size; y3++){
                            if (y3 != y && y3 != y2) {
                                for (int value : values) {
                                    board[x][y3].removePossibility(value);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkBoxMatches() {
        for (int yBox = 0; yBox < boxSize; yBox++) {
            for (int xBox = 0; xBox < boxSize; xBox++) {
                for (int subY = 0; subY < boxSize; subY++) {
                    for (int subX = 0; subX < boxSize; subX++) {
                        int superXCord = xBox * boxSize + subX;
                        int superYCord = yBox * boxSize + subY;
                        String possibilities = board[superXCord][superYCord].printPossibilities();
                        if (possibilities.length() != 2) { //For now, only look for cells with two possibilities.
                            continue;
                        }
//                        System.out.println("Looking at " + superXCord + "," + superYCord + ": " + possibilities);
                        for (int subY2 = subY; subY2 < boxSize; subY2++) {
                            for (int subX2 = subX + 1; subX2 < boxSize; subX2++) {
                                int superXCord2 = xBox * boxSize + subX2;
                                int superYCord2 = yBox * boxSize + subY2;
                                String possibilitiesToCompare = board[superXCord2][superYCord2].printPossibilities();
//                                System.out.println("  Comparing to " + superXCord2 + "," + superYCord2 + ": " +
//                                        possibilitiesToCompare);
                                if(possibilities.equals(possibilitiesToCompare)) {
                                    System.out.println("Shit, we have an X match! " + superXCord + "," + superYCord + " " +
                                            "and " + superXCord2 + "," + superYCord2 + " = " + possibilities);
                                    // Remove the possibilities from all other spots in box
//                                    int[] values = new int[possibilities.length()];
//                                    for (int i = 0; i < values.length; i++) {
//                                        values[i] = Integer.parseInt(String.valueOf(possibilities.charAt(i)));
//                                    }
                                    ArrayList<Integer> values = board[superXCord][superYCord].getPossibilitiesNumeric();
                                    for (int subY3 = 0; subY3 < boxSize; subY3++) {
                                        for (int subX3 = 0; subX3 < boxSize; subX3++) {
                                            int superXCord3 = xBox * boxSize + subX3;
                                            int superYCord3 = yBox * boxSize + subY3;
                                            if (!(superXCord3 == superXCord && superYCord3 == superYCord) &&
                                                    !(superXCord3 == superXCord2 && superYCord3 == superYCord2)) {
                                                for (int value : values) {
                                                    board[superXCord3][superYCord3].removePossibility(value);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public int countTotalPossibilities() {
        int totalPossibilities = 0;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                totalPossibilities += board[x][y].countPossibilites();
            }
        }
        return totalPossibilities;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        int xBoxCounter = 0;
        int yBoxCounter = 0;
        for (int y = 0 ; y < size; y++){

            for (int x = 0 ; x < size; x++) {
                output.append(board[x][y]);
                if(++xBoxCounter == boxSize) {
                    if (x != size - 1) {
                        output.append(" | ");
                    }
                    xBoxCounter = 0;
                }
            }

            output.append("\n");
            if(++yBoxCounter == boxSize) {
                if (y != size - 1) {
                    // Determine how many dashes to write. There are `boxSize - 1` dashes, taking up 3 spaces each.
                    int printedLength = 3 * (boxSize - 1) + size;
                    for (int i = 0; i < printedLength; i++) {
                        output.append("-");
                    }
                }
                output.append("\n");
                yBoxCounter = 0;
            }
        }
        return output.toString();
    }

    public String printPossibilities() {
        StringBuilder output = new StringBuilder();
        int xBoxCounter = 0;
        int yBoxCounter = 0;
        for (int y = 0 ; y < size; y++){

            for (int x = 0 ; x < size; x++) {
                String formatString = "%-" + size + "s ";
                output.append(String.format(formatString, board[x][y].printPossibilities()));
                if(++xBoxCounter == boxSize) {
                    if (x != size - 1) {
                        output.append(" | ");
                    }
                    xBoxCounter = 0;
                }
            }

            output.append("\n");
            if(++yBoxCounter == boxSize) {
                if (y != size - 1) {
                    // Determine how many dashes to write. There are `boxSize - 1` dashes, taking up 3 spaces each.
                    // Also, each cell has size*size possibilities
                    int printedLength = (3 * (boxSize - 1)) + size*(size+1);
                    for (int i = 0; i < printedLength; i++) {
                        output.append("-");
                    }
                }
                output.append("\n");
                yBoxCounter = 0;
            }
        }
        return output.toString();
    }
}
