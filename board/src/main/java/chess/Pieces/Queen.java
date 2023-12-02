package chess.Pieces;

import chess.Board;
import chess.PieceColor;
import chess.Position;

public class Queen extends Piece {

    public Queen(PieceColor color) {
        super(color);
    }

    @Override
    public boolean isValidMove(Board board, Position source, Position destination) {
        if (source.equals(destination)) {
            return false;
        }

        if (destination.isOnSameDiagonalAs(source) || destination.isOnSameRankAs(source)
                || destination.isOnSameFileAs(source)) {
            Position[] positions = source.getPathTo(destination);
            for (Position position : positions) {
                if (board.getPieceAt(position) != null) {
                    return false;
                }
            }

            return this.isEnemyOrNull(board.getPieceAt(destination));
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.getColor() == PieceColor.WHITE ? "\u2655" : "\u265B";
    }

}
