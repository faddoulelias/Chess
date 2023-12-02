package chess;

import chess.Pieces.Bishop;
import chess.Pieces.King;
import chess.Pieces.Knight;
import chess.Pieces.Pawn;
import chess.Pieces.Piece;
import chess.Pieces.Queen;
import chess.Pieces.Rook;

public class Board {
    private Piece[][] board = new Piece[8][8];

    public Board() {
        board = new Piece[8][8];

        // Set all the pieces
        this.setPiece(new Position("a1"), new Rook(PieceColor.WHITE));
        this.setPiece(new Position("b1"), new Knight(PieceColor.WHITE));
        this.setPiece(new Position("c1"), new Bishop(PieceColor.WHITE));
        this.setPiece(new Position("d1"), new Queen(PieceColor.WHITE));
        this.setPiece(new Position("e1"), new King(PieceColor.WHITE));
        this.setPiece(new Position("f1"), new Bishop(PieceColor.WHITE));
        this.setPiece(new Position("g1"), new Knight(PieceColor.WHITE));
        this.setPiece(new Position("h1"), new Rook(PieceColor.WHITE));

        this.setPiece(new Position("a2"), new Pawn(PieceColor.WHITE));
        this.setPiece(new Position("b2"), new Pawn(PieceColor.WHITE));
        this.setPiece(new Position("c2"), new Pawn(PieceColor.WHITE));
        this.setPiece(new Position("d2"), new Pawn(PieceColor.WHITE));
        this.setPiece(new Position("e2"), new Pawn(PieceColor.WHITE));
        this.setPiece(new Position("f2"), new Pawn(PieceColor.WHITE));
        this.setPiece(new Position("g2"), new Pawn(PieceColor.WHITE));
        this.setPiece(new Position("h2"), new Pawn(PieceColor.WHITE));

        this.setPiece(new Position("a7"), new Pawn(PieceColor.BLACK));
        this.setPiece(new Position("b7"), new Pawn(PieceColor.BLACK));
        this.setPiece(new Position("c7"), new Pawn(PieceColor.BLACK));
        this.setPiece(new Position("d7"), new Pawn(PieceColor.BLACK));
        this.setPiece(new Position("e7"), new Pawn(PieceColor.BLACK));
        this.setPiece(new Position("f7"), new Pawn(PieceColor.BLACK));
        this.setPiece(new Position("g7"), new Pawn(PieceColor.BLACK));
        this.setPiece(new Position("h7"), new Pawn(PieceColor.BLACK));

        this.setPiece(new Position("a8"), new Rook(PieceColor.BLACK));
        this.setPiece(new Position("b8"), new Knight(PieceColor.BLACK));
        this.setPiece(new Position("c8"), new Bishop(PieceColor.BLACK));
        this.setPiece(new Position("d8"), new Queen(PieceColor.BLACK));
        this.setPiece(new Position("e8"), new King(PieceColor.BLACK));
        this.setPiece(new Position("f8"), new Bishop(PieceColor.BLACK));
        this.setPiece(new Position("g8"), new Knight(PieceColor.BLACK));
        this.setPiece(new Position("h8"), new Rook(PieceColor.BLACK));

    }

    public void setPiece(Position position, Piece piece) {
        board[position.getRank()][position.getFile()] = piece;
    }

    public Piece getPieceAt(Position position) {
        return board[position.getRank()][position.getFile()];
    }

    private Position findKing(PieceColor color) {
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Piece piece = board[rank][file];
                if (piece != null && piece instanceof King && piece.getColor() == color) {
                    return new Position(rank, file);
                }
            }
        }
        return null;
    }

    public boolean isCheck(PieceColor color) {
        Position kingPosition = findKing(color);
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Piece piece = board[rank][file];
                if (piece != null && piece.getColor() != color
                        && piece.isValidMove(this, new Position(rank, file), kingPosition)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isSafeForKing(Position position, PieceColor color) {
        Board clone = null;
        try {
            clone = (Board) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        clone.setPiece(findKing(color), null);
        clone.setPiece(position, new King(color));
        return !clone.isCheck(color);
    }

    public boolean hasPieceAt(Position position) {
        return board[position.getRank()][position.getFile()] != null;
    }

    public String toString() {
        String result = "";
        for (int rank = 0; rank < 8; rank++) {
            result += (8 - rank) + " ";
            for (int file = 0; file < 8; file++) {
                Piece piece = this.getPieceAt(new Position(rank, file));
                if (piece == null) {
                    result += "  ";
                } else {
                    result += piece + " ";
                }
            }
            result += "\n";
        }
        result += "  a b c d e f g h\n";
        return result;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Board clone = new Board();
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                clone.board[rank][file] = this.board[rank][file];
            }
        }
        return clone;
    }
}
