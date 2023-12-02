package chess;

public class App {

    public static void main(String[] args) {
        ChessGame game = new ChessGame();
        System.out.println(game);

        while (true) {
            System.out.println("Turn: " + game.getTurn());
            System.out.println("Enter move: ");
            String move = System.console().readLine();
            if (move.equals("exit")) {
                break;
            }
            String[] moveParts = move.split(" ");
            Position from = new Position(moveParts[0]);
            Position to = new Position(moveParts[1]);
            if (!game.play(from, to)) {
                System.out.println("Invalid move");
                System.out.println(game.board.getPieceAt(from).getAllAvailableMoves(game.board, from));
            }
            System.out.println(game);
        }
    }
}
