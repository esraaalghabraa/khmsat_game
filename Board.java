public class Board {
    private boolean[][] horizontalLines;
    private boolean[][] verticalLines;
    private boolean[][] boxes;

    public Board(int rows, int cols) {
        this.horizontalLines = new boolean[rows + 1][cols];
        this.verticalLines = new boolean[rows][cols + 1];
        this.boxes = new boolean[rows][cols];
    }

    public boolean drawHorizontalLine(int row, int col) {
        if (row < 0 || row >= horizontalLines.length || col < 0 || col >= horizontalLines[0].length) {
            return false;
        }
        if (!horizontalLines[row][col]) {
            horizontalLines[row][col] = true;
            return true;
        }
        return false;
    }

    public boolean drawVerticalLine(int row, int col) {
        if (row < 0 || row >= verticalLines.length || col < 0 || col >= verticalLines[0].length) {
            return false;
        }
        if (!verticalLines[row][col]) {
            verticalLines[row][col] = true;
            return true;
        }
        return false;
    }

    public boolean drawLine(int startRow, int startCol, int endRow, int endCol) {
        if (startRow == endRow) {
            return drawHorizontalLine(startRow, Math.min(startCol, endCol));
        } else if (startCol == endCol) {
            return drawVerticalLine(Math.min(startRow, endRow), startCol);
        }
        return false;
    }

    public boolean checkAndMarkBox(int row, int col) {
        if (row < 0 || row >= boxes.length || col < 0 || col >= boxes[0].length) {
            return false;
        }

        if (!boxes[row][col] &&
                horizontalLines[row][col] && horizontalLines[row + 1][col] &&
                verticalLines[row][col] && verticalLines[row][col + 1]) {
            boxes[row][col] = true;
            return true;
        }
        return false;
    }

    public boolean[][] getHorizontalLines() {
        return horizontalLines;
    }

    public boolean[][] getVerticalLines() {
        return verticalLines;
    }

    public boolean[][] getBoxes() {
        return boxes;
    }

    public Board clone() {
        Board clonedBoard = new Board(horizontalLines.length - 1, verticalLines[0].length - 1);

        for (int i = 0; i < horizontalLines.length; i++) {
            System.arraycopy(horizontalLines[i], 0, clonedBoard.horizontalLines[i], 0, horizontalLines[i].length);
        }

        for (int i = 0; i < verticalLines.length; i++) {
            System.arraycopy(verticalLines[i], 0, clonedBoard.verticalLines[i], 0, verticalLines[i].length);
        }

        for (int i = 0; i < boxes.length; i++) {
            System.arraycopy(boxes[i], 0, clonedBoard.boxes[i], 0, boxes[i].length);
        }

        return clonedBoard;
    }
}
