package chess.pieces;

import chess.Board;
import chess.PieceColor;
import chess.Position;

public class Knight extends Piece {

    public Knight(PieceColor color) {
        super(color);
    }

    protected Knight(PieceColor color, boolean hasMoved) {
        super(color, hasMoved);
    }

    @Override
    public boolean isValidMove(Board board, Position source, Position destination) {
        return destination.isLShapedFrom(source) && this.isEnemyOrNull(board.getPieceAt(destination));
    }

    @Override
    public String toString() {
        return this.getColor() == PieceColor.WHITE ? "\u2658" : "\u265E";
    }
}
