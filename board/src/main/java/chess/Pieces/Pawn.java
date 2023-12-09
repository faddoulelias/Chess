package chess.pieces;

import chess.Board;
import chess.PieceColor;
import chess.Position;

public class Pawn extends Piece {

    public Pawn(PieceColor color) {
        super(color);
    }

    protected Pawn(PieceColor color, boolean hasMoved) {
        super(color, hasMoved);
    }

    @Override
    public boolean isValidMove(Board board, Position source, Position destination) {
        if (destination.isDirectlyInFrontOf(source, this.getColor()) && !board.hasPieceAt(destination)) {
            return true;
        }

        if (destination.isInFrontByTwoOf(source, getColor()) && !this.hasMoved()
                && !board.hasPieceAt(destination)
                && !board.hasPieceAt(source.getPositionInFront(getColor()))) {
            return true;
        }

        if (destination.isDiagonallyDirectlyInFrontOf(source, this.getColor())
                && board.hasPieceAt(destination)
                && this.isEnemyOrNull(board.getPieceAt(destination))) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return this.getColor() == PieceColor.WHITE ? "\u2659" : "\u265F";
    }
}
