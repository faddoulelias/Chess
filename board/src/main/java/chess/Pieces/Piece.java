package chess.Pieces;

import java.util.ArrayList;

import chess.Board;
import chess.PieceColor;
import chess.Position;

public abstract class Piece {
    private PieceColor color;
    private boolean hasMoved;

    public Piece(PieceColor color) {
        this.color = color;
        this.hasMoved = false;
    }

    public PieceColor getColor() {
        return color;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public boolean isEnemyOrNull(Piece other) {
        if (other == null) {
            return true;
        }
        return this.getColor() != other.getColor();
    }

    public ArrayList<Position> getAllAvailableMoves(Board board, Position source) {
        ArrayList<Position> availableMoves = new ArrayList<Position>();
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Position destination = new Position(rank, file);
                if (isValidMove(board, source, destination)) {
                    availableMoves.add(destination);
                }
            }
        }
        return availableMoves;
    }

    public abstract boolean isValidMove(Board board, Position source, Position destination);

    public abstract String toString();
}
