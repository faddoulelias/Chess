package chess.pieces;

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

    protected Piece(PieceColor color, boolean hasMoved) {
        this.color = color;
        this.hasMoved = hasMoved;
    }

    public PieceColor getColor() {
        return color;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
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
                    if (board.willLeaveKingInCheck(source, destination)) {
                        continue;
                    }
                    availableMoves.add(destination);
                }
            }
        }
        return availableMoves;
    }

    public String toJSON() {
        return String.format("{\"type\": \"%s\", \"color\": \"%s\", \"hasMoved\": %s}",
                this.getClass().getSimpleName().toLowerCase(),
                this.getColor().toString(), this.hasMoved());
    }

    public static Piece fromJSON(String json) {
        String type = json.replaceAll(".*\"type\": \"([a-z]+)\",.*", "$1");
        String color = json.replaceAll(".*\"color\": \"([A-Z]+)\",.*", "$1");
        boolean hasMoved = Boolean.parseBoolean(json.replaceAll(".*\"hasMoved\": ([a-z]+).*", "$1"));
        switch (type) {
            case "bishop":
                return new Bishop(PieceColor.valueOf(color), hasMoved);
            case "king":
                return new King(PieceColor.valueOf(color), hasMoved);
            case "knight":
                return new Knight(PieceColor.valueOf(color), hasMoved);
            case "pawn":
                return new Pawn(PieceColor.valueOf(color), hasMoved);
            case "queen":
                return new Queen(PieceColor.valueOf(color), hasMoved);
            case "rook":
                return new Rook(PieceColor.valueOf(color), hasMoved);
            default:
                return null;
        }
    }

    public abstract boolean isValidMove(Board board, Position source, Position destination);

    public abstract String toString();
}
