import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
    private Board board;
    private Player[] players;
    private Rules rules;
    private int currentPlayerIndex;

    public Game(int rows, int cols, Player[] players, Rules rules) {
        this.board = new Board(rows, cols);
        this.players = players;
        this.rules = rules;
        this.currentPlayerIndex = 0;
    }

    public void playMove(int startRow, int startCol, int endRow, int endCol) {
        if (!rules.isMoveValid(startRow, startCol, endRow, endCol, board)) {
            System.out.println("Invalid move. Try again.");
            return;
        }

        boolean boxCompleted = false;
        if (startRow == endRow) {
            board.drawHorizontalLine(startRow, Math.min(startCol, endCol));
        } else {
            board.drawVerticalLine(Math.min(startRow, endRow), startCol);
        }

        for (int i = 0; i < board.getBoxes().length; i++) {
            for (int j = 0; j < board.getBoxes()[0].length; j++) {
                if (board.checkAndMarkBox(i, j)) {
                    players[currentPlayerIndex].incrementScore();
                    boxCompleted = true;
                }
            }
        }

        if (!boxCompleted) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
        }
    }

    public int getCurrentPlayerIndex() {
        return this.currentPlayerIndex;
    }

    public boolean isGameOver() {
        return rules.isGameOver(this.board);
    }

    public void printBoard() {
        boolean[][] hLines = board.getHorizontalLines();
        boolean[][] vLines = board.getVerticalLines();
        boolean[][] boxes = board.getBoxes();

        final String RESET = "\u001B[0m";
        final String BLUE = "\u001B[34m";
        final String RED = "\u001B[31m";
        final String GREEN = "\u001B[32m";

        for (int i = 0; i < hLines.length; i++) {
            for (int j = 0; j < hLines[0].length; j++) {
                System.out.print(BLUE + "+" + RESET);
                System.out.print(hLines[i][j] ? (RED + "---" + RESET) : "   ");
            }
            System.out.println(BLUE + "+" + RESET);

            if (i < vLines.length) {
                for (int j = 0; j < vLines[0].length; j++) {
                    System.out.print(vLines[i][j] ? (RED + "|" + RESET) : " ");
                    if (j < boxes[0].length) {
                        System.out.print(boxes[i][j] ? (GREEN + " X " + RESET) : "   ");
                    }
                }
                System.out.println();
            }
        }
    }

    public Board getBoard() {
        return this.board;
    }

    public int evaluate(Board board) {
        int computerScore = players[0].getScore();
        int humanScore = players[1].getScore();

        int potentialComputerBoxes = countPotentialBoxes(board, true);
        int potentialHumanBoxes = countPotentialBoxes(board, false);

        System.out.println("Computer Score: " + computerScore +
                ", Human Score: " + humanScore +
                ", Potential Computer Boxes: " + potentialComputerBoxes +
                ", Potential Human Boxes: " + potentialHumanBoxes);

        return (computerScore - humanScore) * 10 +
                (potentialComputerBoxes - potentialHumanBoxes) * 2;
    }

    private int countPotentialBoxes(Board board, boolean isComputer) {
        int count = 0;
        boolean[][] boxes = board.getBoxes();

        for (int i = 0; i < boxes.length; i++) {
            for (int j = 0; j < boxes[0].length; j++) {
                if (!boxes[i][j] && isBoxAlmostComplete(board, i, j)) {
                    count++;
                }
            }
        }
        System.out.println((isComputer ? "Computer" : "Human") +
                " Potential Boxes: " + count);
        return count;
    }

    private boolean isBoxAlmostComplete(Board board, int row, int col) {
        boolean[][] hLines = board.getHorizontalLines();
        boolean[][] vLines = board.getVerticalLines();

        int count = 0;

        if (hLines[row][col]) count++;
        if (hLines[row + 1][col]) count++;
        if (vLines[row][col]) count++;
        if (vLines[row][col + 1]) count++;

        return count == 3;
    }

    private int alphaBeta(Board board, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        if (depth == 0 || rules.isGameOver(board)) {
            return evaluate(board);
        }

        int currentScore = players[currentPlayerIndex].getScore();
        int opponentScore = players[(currentPlayerIndex + 1) % players.length].getScore();

        if (isMaximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;

            for (int[] move : rules.getPossibleMoves(board)) {
                Board clonedBoard = board.clone();
                clonedBoard.drawLine(move[0], move[1], move[2], move[3]);

                // Simulate box completions
                if (clonedBoard.checkAndMarkBox(move[0], move[1])) {
                    currentScore++;
                }

                int eval = alphaBeta(clonedBoard, depth - 1, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);

                if (beta <= alpha) break; // Prune the search tree
            }
            return maxEval;

        } else {
            int minEval = Integer.MAX_VALUE;

            for (int[] move : rules.getPossibleMoves(board)) {
                Board clonedBoard = board.clone();
                clonedBoard.drawLine(move[0], move[1], move[2], move[3]);

                // Simulate box completions
                if (clonedBoard.checkAndMarkBox(move[0], move[1])) {
                    opponentScore++;
                }

                int eval = alphaBeta(clonedBoard, depth - 1, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);

                if (beta <= alpha) break; // Prune the search tree
            }
            return minEval;
        }
    }

    public int[] findBestMove(int depth) {
        int bestValue = Integer.MIN_VALUE;
        int[] bestMove = null;

        for (int[] move : rules.getPossibleMoves(board)) {
            if (move != null && move.length == 4) {
                Board clonedBoard = board.clone();
                clonedBoard.drawLine(move[0], move[1], move[2], move[3]);

                int moveValue = alphaBeta(clonedBoard, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                System.out.println("Move: (" + move[0] + ", " + move[1] + ") to (" + move[2] + ", " + move[3] + "), Value: " + moveValue);

                if (moveValue > bestValue) {
                    bestValue = moveValue;
                    bestMove = move;
                }
            }
        }
        System.out.println("Best move: " + Arrays.toString(bestMove) + ", Value: " + bestValue);

        return bestMove;
    }
}
