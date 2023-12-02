package chess;

public class ChessGame {
    Board board;
    PieceColor turn;

    public ChessGame() {
        board = new Board();
        turn = PieceColor.WHITE;
    }

    public PieceColor getTurn() {
        return turn;
    }

    public boolean play(Position from, Position to) {
        if (board.getPieceAt(from) == null) {
            return false;
        }

        if (board.getPieceAt(from).getColor() != turn) {
            return false;
        }

        boolean success = board.movePiece(from, to);
        if (!success) {
            return false;
        }

        turn = (turn == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
        return true;
    }

    @Override
    public String toString() {
        return board.toString();
    }
}
