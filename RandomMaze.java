
import java.lang.Math;
import java.util.Random;

public class RandomMaze {
    
    private int rows;
    private int minRows;
    private int maxRows;
    private int columns;
    private int minColumns;
    private int maxColumns;

    private char[][] maze;

    private char WALL = 'W';
    private char PATH = ' ';
    private char START = 'S';
    private char END = 'E'; 
    private char MIDDLE_POINT = 'P';

    private int startPosRow, startPosCol;
    private int endPosRow, endPosCol;
    private int pointRow, pointCol;

    //for reaching middle points 
    private int currentRow;
    private int currentCol;

    public RandomMaze (int minRows, int maxRows, int minColumns, int maxColumns) {
        this.minRows = minRows;
        this.maxRows = maxRows;
        this.minColumns = minColumns;
        this.maxColumns = maxColumns;

        createMaze();
    }
    
    private void createMaze () {

        createSize();
        fillMaze();
        createStartAndEnd();
        createMiddlePoint();
        goToMiddlePoint();
        goToEnd();
        createRandomPaths();
        
    }

    private void createSize () {

        Random random = new Random();

        rows = random.nextInt((maxRows + 1) - minRows) + minRows;
        columns = random.nextInt((maxColumns + 1) - minColumns) + minColumns;
        maze = new char[rows+1][columns+1];

        random = null;
    }

    private void fillMaze () {

        for (int row=0; row<rows; row++) {
            for (int column=0; column<columns; column++) {

                maze[row][column] = WALL;
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

    private void createMiddlePoint () {

        int row = Math.abs(startPosRow - endPosRow) / 2;
        int column = Math.abs(startPosCol - endPosCol) / 2;

        if (row == 1 && column == 1) {
            row = rows / 2;
            column = columns / 2;
        }
        if (column == 0) {
            column = columns / 2;
        }
        if (row == 0) {
            row = rows / 2;
        }

        pointRow = row;
        pointCol = column;
        maze[pointRow][pointCol] = MIDDLE_POINT;
    }

    private void goToMiddlePoint () {

        currentCol = startPosCol;
        currentRow = startPosRow;
        
        //conditions to try to make the path better
        if (startPosRow > endPosRow) {
            //avoids going into sides
            if (startPosCol == 0) {
                currentCol++;
                setPathCell(currentRow, currentCol);
            } else if (startPosCol == columns - 1) {
                currentCol--;
                setPathCell(currentRow, currentCol);
            }

            goToRow(pointRow);
            goToColumn(pointCol);
        } else if (startPosRow <= endPosRow) {
            //avoids going into sides
            if (startPosRow == 0) {
                currentRow++;
                setPathCell(currentRow, currentCol);
            } else if (startPosRow == rows - 1) {
                currentRow--;
                setPathCell(currentRow, currentCol);
            }

            goToColumn(pointCol);
            goToRow(pointRow);
        }
        
    }

    private void goToEnd () {

        //conditions to try to make the path better
        if (currentRow <= endPosRow) {

            goToEndCol();
            goToEndRow();
        } else if (currentRow > endPosRow) {
            goToEndRow();
            goToEndCol();

        } 

    }

    private void createRandomPaths () {

        for (int repeats=0; repeats<rows*columns; repeats++) {

            for (int row=1; row<rows-1; row++) {
                for (int column=1; column<columns-1; column++) {

                    if (maze[row][column] == PATH) {
                        createRandomFrom(row, column);
                    }   

                }
            }
        }

    }

    //--
    private void createRandomFrom (int row, int column) {

        Random random = new Random();
        int prevRow = row;
        int prevCol = column;
        int nextRow = prevRow;
        int nextCol = prevCol;

        int direction;

        direction = random.nextInt(4);

        switch (direction) {
            //up
            case 0:
                nextRow = prevRow - 1;
                if (canCreatePath(nextRow, nextCol, prevRow, prevCol)) {
                    maze[nextRow][nextCol] = PATH;
                } else {
                    nextRow = prevRow;
                }
                break;
            //down
            case 1:
                nextRow = prevRow + 1;
                if (canCreatePath(nextRow, nextCol, prevRow, prevCol)) {
                    maze[nextRow][nextCol] = PATH;
                } else {
                    nextRow = prevRow;
                }
                break;
            //left
            case 2:
                nextCol = prevCol - 1;
                if (canCreatePath(nextRow, nextCol, prevRow, prevCol)) {
                    maze[nextRow][nextCol] = PATH;
                } else {
                    nextCol = prevCol;
                }
                break;
            //right
            case 3:
                nextCol = prevCol + 1;
                if (canCreatePath(nextRow, nextCol, prevRow, prevCol)) {
                    maze[nextRow][nextCol] = PATH;
                } else {
                    nextCol = prevCol;
                }
                break;
        }
    }

    private boolean canCreatePath (int row, int column, int prevRow, int prevCol) {

        if (!hasPathsAround(row, column, prevRow, prevCol) && !isASide(row, column)) {
            return true;
        }

        return false;
    }

    private boolean hasPathsAround (int row, int column, int prevRow, int prevCol) {

        //up
        try {
            if (maze[row-1][column] == PATH && row-1 != prevRow) {
                return true;
            }
        } catch (Exception e) { }
        //down
        try {
            if (maze[row+1][column] == PATH && row+1 != prevRow) {
                return true;
            }
        } catch (Exception e) { }
        //left
        try {
            if (maze[row][column-1] == PATH && column-1 != prevCol) {
                return true;
            }
        } catch (Exception e) { }
        //right
        try {
            if (maze[row][column+1] == PATH && column+1 != prevCol) {
                return true;
            }
        } catch (Exception e) { }

        return false;
    }

    private boolean isASide (int row, int column) {

        if (row == 0 || row == rows - 1 || column == 0 || column == columns - 1) {
            return true;
        }

        return false;
    }

    private void goToEndCol () {
        if (endPosCol == 0) {
                goToColumn(endPosCol+1);
        } else if (endPosCol == columns - 1) {
            goToColumn(endPosCol-1);
        } else {
            goToColumn(endPosCol);
        }
    }
 
    private void goToEndRow () {
        if (endPosRow == 0) {
            goToRow(endPosRow+1);
        } else if (endPosRow == rows -1) {
            goToRow(endPosRow-1);
        } else {
            goToRow(endPosRow);
        }
    }

    private void goToColumn (int column) {

        while (currentCol != column) {

            if (currentCol < column) {

                currentCol++;
                setPathCell(currentRow, currentCol);
            } else if (currentCol > column) {

                currentCol--;
                setPathCell(currentRow, currentCol);
            }

        }
    }

    private void goToRow (int row) {

        while (currentRow != row) {

            if (currentRow < row) {

                currentRow++;
                setPathCell(currentRow, currentCol);
            } else if (currentRow > row) {

                currentRow--;
                setPathCell(currentRow, currentCol);
            }

        }
    }

    private boolean isEnd (int row, int column) {

        if (row == endPosRow && column == endPosCol) {
            return true;
        }

        return false;
    } 

    private void findStartAndEndPos () {

        for (int row=0; row<rows; row++) {
            for (int column=0; column<columns; column++) {

                if (maze[row][column] == START) {
                    startPosRow = row;
                    startPosCol = column;

                } else if (maze[row][column] == END) {
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
                if (column == 0 || column == 1) {
                    column = 2;
                } else if (column == (columns-1) || column == (columns-2)) {
                    column = columns-3;
                }

                if (side == 0) {
                    maze[0][column] = character;
                } else {
                    maze[rows-1][column] = character;
                }
                break;
            case 1,3:      //left and right
                int row = random.nextInt(rows);

                //excludes angle
                if (row == 0 || row == 1) {
                    row = 2;
                } else if (row == (rows-1) || row == (rows-2)) {
                    row = rows - 3;
                }

                if (side == 1) {
                    maze[row][0] = character;

                } else {
                    maze[row][columns-1] = character;
                }
        }

        random = null;
    }

    public char[][] getMaze () {
        return maze;
    }

    public String toString () {

        String outString = "";

        for (int row=0; row<rows; row++) {
            for (int column=0; column<columns; column++) {

                outString += ("["+maze[row][column]+"]");
            }
            outString += "\n";
        }

        return outString;
    }

    public void setPathCell (int row, int column) {

        if (!isEnd(row, column)) {
            maze[row][column] = PATH;
        }
        
        // if (!isEnd(row, column) && maze[row][column] != MIDDLE_POINT) {
        //     maze[row][column] = PATH;
        // }
    }
}
