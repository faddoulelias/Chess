package chess.Pieces;

import chess.Board;
import chess.PieceColor;
import chess.Position;

public class Bishop extends Piece {

    public Bishop(PieceColor color) {
        super(color);
    }

    @Override
    public boolean isValidMove(Board board, Position source, Position destination) {
        if (source.equals(destination)) {
            return false;
        }

        if (destination.isOnSameDiagonalAs(source)) {
            Position[] positions = source.getDiagonalPathTo(destination);
            for (Position position : positions) {
                if (board.getPieceAt(position) != null) {
                    return false;
                }
            }

            return this.isEnemyOrNull(board.getPieceAt(destination));
        }

        return false;
    }

    @Override
    public String toString() {
        return this.getColor() == PieceColor.WHITE ? "\u2657" : "\u265D";
    }

}
