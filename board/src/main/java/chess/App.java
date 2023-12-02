package chess;

import chess.Pieces.Piece;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        Board board = new Board();
        System.out.println(board);
        System.out.println(board.isCheck(PieceColor.BLACK));

        // Ask the user to input a position
        System.out.print("Enter a position: ");
        String input = System.console().readLine();

        // Create a position object from the input
        Position position = new Position(input);

        // Get the piece at the position
        Piece piece = board.getPieceAt(position);

        // Print out the piece
        System.out.println(piece);
        System.out.println(piece.getAllAvailableMoves(board, position));
    }
}
