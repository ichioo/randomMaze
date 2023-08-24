
import java.util.Random;

public class RandomMaze {
    
    private int rows;
    private int minRows = 7;
    private int maxRows = 10;
    private int columns;
    private int minColumns = 7;
    private int maxColumns = 10;
    private String[][] maze;

    private char WALL = 'W';
    private char PATH = ' ';
    private char START = 'S';
    private char END = 'E'; 

    private int startPosRow;
    private int startPosCol;
    private int endPosRow;
    private int endPosCol;

    public RandomMaze () {
        createMaze();
    }
    
    private void createMaze () {

        createSize();
        fillMaze();
        createStartAndEnd();
        createRanCorrectPath();
    }

    private void createSize () {

        Random random = new Random();

        rows = random.nextInt((maxRows + 1) - minRows) + minRows;
        columns = random.nextInt((maxColumns + 1) - minColumns) + minColumns;
        maze = new String[rows+1][columns+1];

        random = null;
    }

    private void fillMaze () {

        for (int row=0; row<rows; row++) {
            for (int column=0; column<columns; column++) {

                maze[row][column] = intoBrackets(WALL);
            }
        }
    }

    private void createStartAndEnd () {
        //corners excluded

        Random random = new Random();

        int endSide;
        int startSide = random.nextInt(4);
        ranInsertOnSide(startSide, START);

        endSide = random.nextInt(4);
        while (endSide == startSide) {
            endSide = random.nextInt(4);
        }
        ranInsertOnSide(endSide, END);

        findStartAndEndPos();
        random = null;
    }

    private void createRanCorrectPath () {
        
    } 

    //--
    private boolean isEnd (int row, int column) {

        if (row == endPosRow && column == endPosCol) {
            return true;
        }

        return false;
    } 

    private boolean isValidPos (int row, int column) {

        if (isOut(row, column) || isCorner(row, column) || isStart(row, column) || isSide(row, column) || isPath(row, column)) {
            return false;
        }

        return true;
    }

    private boolean isPath (int row, int column) {

        if (maze[row][column].equals(intoBrackets(PATH))) {
            return true;
        }

        return false;
    }

    private boolean isOut (int row, int column) {

        if (row < 0 || row == rows || column < 0 || column == columns) {
            return true;
        }

        return false;
    }

    private boolean isStart (int row, int column) {

        if (row == startPosRow && column == startPosCol) {
            return true;
        }

        return false;
    }

    private boolean isSide (int row, int column) {

        if ((column == 0 && !isEnd(row, column)) || column == columns || row == rows || (row == 0 && !isEnd(row, column))) {
            return true;
        }

        return false;
    }

    private boolean isCorner (int row, int column) {

        //checks down and up
        if (row + 1 == rows || row - 1 < 0) {
            return true;
        }

        return false;
    }

    private void findStartAndEndPos () {

        for (int row=0; row<rows; row++) {
            for (int column=0; column<columns; column++) {

                if (maze[row][column].equals("[" + START + "]")) {
                    startPosRow = row;
                    startPosCol = column;

                } else if (maze[row][column].equals("[" + END + "]")) {
                    endPosRow = row;
                    endPosCol = column;

                }
            }
        }

    }

    private void ranInsertOnSide (int side, char character) {
        Random random = new Random();

        switch (side) {
            case 0,2:     //above and below
                int column = random.nextInt(columns);

                //excludes angles
                if (column == 0) {
                    column = 1;
                } else if (column == (columns-1)) {
                    column = columns-2;
                }

                if (side == 0) {
                    maze[0][column] = intoBrackets(character);
                } else {
                    maze[rows-1][column] = intoBrackets(character);
                }
                break;
            case 1,3:      //left and right
                int row = random.nextInt(rows);

                //excludes angle
                if (row == 0) {
                    row = 1;
                } else if (row == (rows-1)) {
                    row = rows - 2;
                }

                if (side == 1) {
                    maze[row][0] = intoBrackets(character);

                } else {
                    maze[row][columns-1] = intoBrackets(character);
                }
        }

        random = null;
    }

    private String intoBrackets (char character) {
        return "[" + character + "]";
    }

    public String[][] getMaze () {
        return maze;
    }

    public String toString () {

        String outString = "";

        for (int row=0; row<rows; row++) {
            for (int column=0; column<columns; column++) {

                outString += maze[row][column];
            }
            outString += "\n";
        }

        return outString;
    }
}
