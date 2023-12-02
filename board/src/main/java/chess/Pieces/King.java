package chess.pieces;

import chess.Board;
import chess.PieceColor;
import chess.Position;

public class King extends Piece {

    public King(PieceColor color) {
        super(color);
    }

    @Override
    public boolean isValidMove(Board board, Position source, Position destination) {
        if (!destination.isDirectlyAround(source)) {
            return false;
        }

        return board.isSafeForKing(destination, this.getColor())
                && this.isEnemyOrNull(board.getPieceAt(destination));
    }

    @Override
    public String toString() {
        return this.getColor() == PieceColor.WHITE ? "\u2654" : "\u265A";
    }

}
