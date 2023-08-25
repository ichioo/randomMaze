
public class PathFinder {
    
    private int rows, columns;

    private int startRow, startCol;
    private int endRow, endCol;

    private int currentCellNumber;
    private int currentRow, currentCol;
    private int lastCellRow, lastCellCol;

    private int[][] intMap;

    private RandomMaze randomMaze;

    public PathFinder (int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        intMap = new int[rows][columns];
    }

    public void findPath (int startRow, int startCol, int endRow, int endCol) {

        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
        
        insertNumbers();
        makePath();
        resetIntMap();
    }

    private void insertNumbers () {

        if (isSide(startRow, startCol)) {

            if (isUpOrDown(startRow, startCol)) {

                verticalFilling();
            } else if (isLeftOrRight(startRow, startCol)) {

                horizontalFilling();
            }
        } else {
            standardFilling();
        }

        deleteExcess();
    }

    private void deleteExcess () {

        if (endCol == startCol || endRow == startRow) {
            keepALine();
        } else {
            deleteExcessRows();
            deleteExcessColumns();
        }
    }

    private void makePath () {
        currentCellNumber = 0;
        currentRow = startRow;
        currentCol = startCol;

        findLastCell();

        while (!isOnLastCell(currentRow, currentCol)) {
            findNextCell();
        }

        System.out.println("done!");
    }

    private void resetIntMap () {

        intMap = new int[rows][columns];
    }

    //--
    private void findNextCell () {

        //chakes up
        if (currentRow - 1 > 0) {
            if (intMap[currentRow-1][currentCol] == currentCellNumber +1) {

                System.out.print("up, ");
                moveToNextCell(currentRow-1, currentCol);
            }
        }
        //checks down
        if (currentRow + 1 < rows-1) {
            if (intMap[currentRow+1][currentCol] == currentCellNumber +1) {

                System.out.print("down, ");
                moveToNextCell(currentRow+1, currentCol);
            }
        }
        //chacks left
        if (currentCol - 1 > 0) {
            if (intMap[currentRow][currentCol-1] == currentCellNumber +1) {

                System.out.print("left, ");
                moveToNextCell(currentRow, currentCol-1);
            }
        }
        //checks right
        if (currentCol + 1 < columns-1) {
            if (intMap[currentRow][currentCol+1] == currentCellNumber +1) {

                System.out.print("right, ");
                moveToNextCell(currentRow, currentCol+1);
            }
        }

    }

    private void moveToNextCell (int row, int column) {

        currentRow = row;
        currentCol = column;

        if (randomMaze != null) {
            setPathOnMaze();
        }

        currentCellNumber += 1;
    }

    private void setPathOnMaze () {

        randomMaze.setPathCell(currentRow, currentCol);
    }

    public boolean isOnLastCell (int row, int column) {

        if (row == lastCellRow && column == lastCellCol) {
            return true;
        }

        return false;
    }

    public void findLastCell () {

        int max = 0;

        for (int row=1; row<=rows-2; row++) {
            for (int column=1; column<=columns-2; column++) {

                if (intMap[row][column] > max) {

                    max = intMap[row][column];
                    lastCellRow = row;
                    lastCellCol = column;
                }
            }
        }
    }

    private void keepALine () {

        if (endCol == startCol) {
            keepColumnLine();
        } else if (endRow == startRow) {
            keepRowLine();
        }
    }

    private void keepColumnLine () {

        for (int row=1; row<=rows-2; row++) {

            for (int column=1; column<=columns-2; column++) {

                //when the start point is above the end point
                if (endRow > startRow) {

                    if (row > endRow) {
                        intMap[row][column] = 0;
                    } else if (column != endCol) {
                        intMap[row][column] = 0;
                    } else if (row < startRow) {
                        intMap[row][column] = 0;
                    }
                }
                //when the start point is below the end point
                if (endRow < startRow) {

                    if (row < endRow) {
                        intMap[row][column] = 0;
                    } else if (column != endCol) {
                        intMap[row][column] = 0;
                    } else if (row > startRow) {
                        intMap[row][column] = 0;
                    }
                }
            }
        }
    }

    private void keepRowLine () {
        
        for (int row=1; row<=rows-2; row++) {

            for (int column=1; column<=columns-2; column++) {

                //when the start point is right of the end point
                if (endCol > startCol) {

                    if (column > endCol) {
                        intMap[row][column] = 0;
                    } else if (row != endRow) {
                        intMap[row][column] = 0;
                    } else if (column < startCol) {
                        intMap[row][column] = 0;
                    }
                }
                //when the start point is left of the end point
                if (endCol < startCol) {

                    if (column < endCol) {
                        intMap[row][column] = 0;
                    } else if (row != endRow) {
                        intMap[row][column] = 0;
                    } else if (column > startCol) {
                        intMap[row][column] = 0;
                    }
                }
            }
        }
    }

    private void deleteExcessColumns () {

        for (int row=1; row<=rows-2; row++) {

            for (int column=1; column<=columns-2; column++) {

                //when the start point is left of the end point
                if (endCol > startCol) {
                    if (column < startCol || column > endCol) {
                        intMap[row][column] = 0;
                    }
                }
                //when the start point is right of the end point
                if (endCol < startCol) {
                    if (column > startCol || column < endCol) {
                        intMap[row][column] = 0;
                    }
                }
            }
        }
    }

    private void deleteExcessRows () {

        for (int row=1; row<=rows-2; row++) {

            for (int column=1; column<=columns-2; column++) {

                //when the start point is above the end point
                if (endRow > startRow) {
                    if (row < startRow || row > endRow) {
                        intMap[row][column] = 0;
                    }
                }
                //when the start point is below the end point
                if (endRow < startRow) {
                    if (row > startRow || row < endRow) {
                        intMap[row][column] = 0;
                    }
                }
                
            }
        }
    }   

    private void standardFilling () {
        downLine();
        upLine();
        fillFromVertical();
    }

    private void verticalFilling () {

        if (startRow == 0) {
            downLine();

        } else if (startRow == rows-1) {
            upLine();
        }

        fillFromVertical();
    }

    private void horizontalFilling () {

        if (startCol == 0) {
            rightLine();
        } else if (startCol == columns-1) {
            leftLine();
        }

        fillFromHorizontal();
    } 

    private void fillFromHorizontal () {
        fillHorizUp();
        fillHorizDown();
    }

    private void fillFromVertical () {
        fillVertLeft();
        fillVertRight();
    }

    private void fillHorizUp () {

        for (int row=startRow-1; row>=1; row--) {

            for (int column=1; column<=columns-2; column++) {

                intMap[row][column] = intMap[row+1][column] + 1;
            }
        }
    }

    private void fillHorizDown () {

        for (int row=startRow+1; row<=rows-2; row++) {

            for (int column=1; column<=columns-2; column++) {

                intMap[row][column] = intMap[row-1][column] + 1;
            }
        }
    }

    private void fillVertLeft () {

        for (int column=startCol-1; column>=1; column--) {

            for (int row=1; row<=rows-2; row++) {

                intMap[row][column] = intMap[row][column+1] + 1; 
            }
        }
    }

    private void fillVertRight () {

        for (int column=startCol+1; column<=columns-2; column++) {

            for (int row=1; row<=rows-2; row++) {

                intMap[row][column] = intMap[row][column-1] + 1; 
            }
        }
    }

    private void leftLine () {

        int counter = 1;

        for (int column=startCol-1; column>=1; column--) {

            intMap[startRow][column] = counter;
            counter++;
        } 

    }

    private void rightLine () {

        int counter = 1;

        for (int column=startCol+1; column<=columns-2; column++) {

            intMap[startRow][column] = counter;
            counter++;
        } 
    }

    private void downLine () {

        int counter = 1;

        for (int row = startRow+1; row<=rows-2; row++) {

            intMap[row][startCol] = counter;
            counter++;
        }
    }

    private void upLine() {

        int counter = 1;

        for (int row = startRow-1; row>=1; row--) {

            intMap[row][startCol] = counter;
            counter++;
        }

    }

    private boolean isSide (int row, int column) {

        if (row == 0 || column == 0 || row == rows-1 || column == columns-1) {
            return true;
        }

        return false;
    }

    private boolean isUpOrDown (int row, int column) {

        if (row == 0 || row == rows - 1) {
            return true;
        }

        return false;
    }

    private boolean isLeftOrRight (int row, int column) {

        if (column == 0 || column == columns - 1) {
            return true;
        }

        return false;
    }       

    public String toString () {

        String outString = "";

        for (int row=0; row<rows; row++) {
            for (int column=0; column<columns; column++) {

                outString += ("[" + intMap[row][column] + "]");

            }
            outString += "\n";
        }

        return outString;
    }

    public void setRandomMaze (RandomMaze randomMaze) {
        this.randomMaze = randomMaze;
    }
}
