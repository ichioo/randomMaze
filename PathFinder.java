
public class PathFinder {
    
    private int rows;
    private int columns;

    private int startRow;
    private int startCol;
    private int endRow;
    private int endCol;

    private int[][] intMap;

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
    }

    private void insertNumbers () {
        
        if (startCol > 0) {
            leftNumbers();
        }
        if (startCol < columns) {
            rightNumbers();
        }
        if (startRow < rows) {
            downNumbers();
        }
        if (startRow > 0) {
            upNumbers();
        }
    }


    private void leftNumbers () {
        int counter = 1;

        for (int column=startCol-1; column>=1; column--) {

            if (startRow == 0) {
                intMap[startRow+1][column] = counter;
            } else if (startRow == rows-1) {
                intMap[startRow-1][column] = counter;
            } else {
                intMap[startRow][column] = counter;
            }
            counter++;
        }
    }

    private void rightNumbers () {
        int counter = 1;

        for (int column=startCol+1; column<=columns-2; column++) {

            if (startRow == 0) {
                intMap[startRow+1][column] = counter;
            } else if (startRow == rows-1) {
                intMap[startRow-1][column] = counter;
            } else {
                intMap[startRow][column] = counter;
            }
            counter++;
        }
    }

    private void downNumbers () {

        for (int row=startRow+1; row<=rows-2; row++) {

            for (int column=1; column<=columns-2; column++) {

                intMap[row][column] = intMap[row-1][column] + 1; 
            }
        }

    }

    private void upNumbers () {

        for (int row=startRow-1; row>=1; row--) {

            for (int column=1; column<=columns-2; column++) {

                intMap[row][column] = intMap[row+1][column] + 1;
            }
        }
    }

    //--
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
}
