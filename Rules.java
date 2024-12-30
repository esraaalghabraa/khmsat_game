import java.util.ArrayList;
import java.util.List;

public class Rules {
    public boolean isMoveValid(int startRow, int startCol, int endRow, int endCol, Board board) {
        if (Math.abs(startRow - endRow) + Math.abs(startCol - endCol) != 1) {
            return false;
        }
        if (startRow == endRow) {
            return !board.getHorizontalLines()[startRow][Math.min(startCol, endCol)];
        } else {
            return !board.getVerticalLines()[Math.min(startRow, endRow)][startCol];
        }
    }

    public boolean isGameOver(Board board) {
        for (boolean[] row : board.getBoxes()) {
            for (boolean box : row) {
                if (!box) return false;
            }
        }
        return true;
    }

    public List<int[]> getPossibleMoves(Board board) {
        List<int[]> moves = new ArrayList<>();
        boolean[][] hLines = board.getHorizontalLines();
        boolean[][] vLines = board.getVerticalLines();

        for (int i = 0; i < hLines.length; i++) {
            for (int j = 0; j < hLines[0].length; j++) {
                if (!hLines[i][j] && j + 1 < hLines[0].length) {
                    moves.add(new int[]{i, j, i, j + 1});
                }
            }
        }

        for (int i = 0; i < vLines.length; i++) {
            for (int j = 0; j < vLines[0].length; j++) {
                if (!vLines[i][j] && i + 1 < vLines.length) {
                    moves.add(new int[]{i, j, i + 1, j});
                }
            }
        }

        return moves;
    }


}