package chess;

import java.util.ArrayList;

import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Piece;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class Board {
    private Piece[][] board = new Piece[8][8];

    public Board() {
        board = new Piece[8][8];
    }

    public void setStartingPosition() {
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

    public boolean willLeaveKingInCheck(Position from, Position to) {
        Board clone = null;
        try {
            clone = (Board) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        Piece piece = clone.getPieceAt(from);
        clone.setPiece(from, null);
        clone.setPiece(to, piece);
        return clone.isCheck(piece.getColor());
    }

    public boolean movePiece(Position from, Position to) {
        Piece piece = this.getPieceAt(from);
        if (piece == null) {
            return false;
        }

        if (this.willLeaveKingInCheck(from, to)) {
            return false;
        }

        if (piece.isValidMove(this, from, to)) {
            this.setPiece(to, piece);
            this.setPiece(from, null);
            piece.setHasMoved(true);
            return true;
        }

        return false;
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

    public boolean isStalemate(PieceColor color) {
        if (this.isCheck(color)) {
            return false;
        }

        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Piece piece = board[rank][file];
                if (piece != null && piece.getColor() == color) {
                    ArrayList<Position> availableMoves = piece.getAllAvailableMoves(this,
                            new Position(rank, file));
                    if (availableMoves.size() > 0) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public boolean isCheckmate(PieceColor color) {
        if (!this.isCheck(color)) {
            return false;
        }

        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Piece piece = board[rank][file];
                if (piece != null && piece.getColor() == color) {
                    ArrayList<Position> availableMoves = piece.getAllAvailableMoves(this,
                            new Position(rank, file));
                    for (Position availableMove : availableMoves) {
                        if (!this.willLeaveKingInCheck(new Position(rank, file), availableMove)) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Board clone = new Board();
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                clone.board[rank][file] = this.board[rank][file];
            }
        }
        return clone;
    }

    public String toJSON() {
        String result = "[";
        for (int rank = 0; rank < 8; rank++) {
            result += "[";
            for (int file = 0; file < 8; file++) {
                Piece piece = this.getPieceAt(new Position(rank, file));
                if (piece == null) {
                    result += "null";
                } else {
                    result += piece.toJSON();
                }
                if (file < 7) {
                    result += ",";
                }
            }
            result += "]";
            if (rank < 7) {
                result += ",";
            }
        }
        result += "]";
        return result;
    }

    public static String toJSONArray(ArrayList<Board> json) {
        String result = "[";
        for (int i = 0; i < json.size(); i++) {
            result += json.get(i).toJSON();
            if (i < json.size() - 1) {
                result += ",";
            }
        }
        result += "]";
        return result;
    }

    public static Board fromJSON(String json) {
        Board result = new Board();
        json = json.substring(2, json.length() - 2);
        String[] rows = json.split("\\],\\[");
        for (int rank = 0; rank < 8; rank++) {
            String[] pieces = rows[rank].split("(?<=\\}),|(?<=null),");

            for (int file = 0; file < 8; file++) {
                if (!pieces[file].equals("null")) {
                    result.setPiece(new Position(rank, file), Piece.fromJSON(pieces[file]));
                }
            }
        }
        return result;
    }

    public static ArrayList<Board> fromJSONArray(String json) {
        return new ArrayList<Board>(); // result;
    }
}
