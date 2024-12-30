import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Player[] players = {
                new Player("Computer"),
                new Player("Human"),
        };

        Rules rules = new Rules();
        Game game = new Game(3, 3, players, rules);
        Scanner scanner = new Scanner(System.in);
        while (!game.isGameOver()) {
            System.out.println("Current board:");
            game.printBoard();
            if (players[game.getCurrentPlayerIndex()].getName().equals("Computer")) {
                System.out.println("Computer's turn...");
                int[] bestMove = game.findBestMove(10);
                System.out.println("Computer plays: (" + bestMove[0] + ", " + bestMove[1] + ") to (" + bestMove[2] + ", " + bestMove[3] + ")");
                game.playMove(bestMove[0], bestMove[1], bestMove[2], bestMove[3]);
            } else {
                System.out.println(players[game.getCurrentPlayerIndex()].getName() + "'s turn.");
                System.out.print("Enter start dot (row col): ");
                int startRow = scanner.nextInt();
                int startCol = scanner.nextInt();
                System.out.print("Enter end dot (row col): ");
                int endRow = scanner.nextInt();
                int endCol = scanner.nextInt();

                game.playMove(startRow, startCol, endRow, endCol);
            }
        }

        scanner.close();
        System.out.println("Game over! Final scores:");
        for (Player player : players) {
            System.out.println(player.getName() + ": " + player.getScore());
        }
    }
}
