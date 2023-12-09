package chess.pieces;

import chess.Board;
import chess.PieceColor;
import chess.Position;

public class Rook extends Piece {

    public Rook(PieceColor color) {
        super(color);
    }

    protected Rook(PieceColor color, boolean hasMoved) {
        super(color, hasMoved);
    }

    @Override
    public boolean isValidMove(Board board, Position source, Position destination) {
        if (source.equals(destination)) {
            return false;
        }

        if (destination.isOnSameRankAs(source) || destination.isOnSameFileAs(source)) {
            Position[] positions = source.getLinearPathTo(destination);
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
        return this.getColor() == PieceColor.WHITE ? "\u2656" : "\u265C";
    }

}
