package server;

import java.util.ArrayList;

import chess.Board;
import chess.ChessGame;
import chess.PieceColor;
import chess.Position;
import chess.pieces.Piece;

public class GameSerializer {
    public static String serializePiece(Piece piece) {
        if (piece == null) {
            return "0";
        }

        int type = 0;
        int color = piece.getColor() == chess.PieceColor.WHITE ? 1 : -1;
        switch (piece.getClass().getSimpleName()) {
            case "Bishop":
                type = 1;
                break;
            case "King":
                type = 2;
                break;
            case "Knight":
                type = 3;
                break;
            case "Pawn":
                type = 4;
                break;
            case "Queen":
                type = 5;
                break;
            case "Rook":
                type = 6;
                break;
            default:
                break;
        }

        int data = color * type;
        return String.valueOf(data);
    }

    public static Integer[] serializeBoard(Board board) {
        Integer[] resultArray = new Integer[64];
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Piece piece = board.getPieceAt(new Position(rank, file));
                resultArray[rank * 8 + file] = Integer.parseInt(serializePiece(piece));
            }
        }

        return resultArray;
    }

    protected static String toJSONString(String key, String value) {
        if (value == null) {
            return "\"" + key + "\":null";
        }
        return "\"" + key + "\": \"" + value + "\"";
    }

    protected static String toJSONString(String key, Object[] value) {
        if (value == null) {
            return "\"" + key + "\":null";
        }
        String output = "\"" + key + "\": [";
        for (int i = 0; i < value.length; i++) {
            output += "\"" + value[i] + "\"";
            if (i != value.length - 1) {
                output += ",";
            }
        }
        output += "]";
        return output;
    }

    protected static String toJSONString(String key, boolean value) {
        return "\"" + key + "\": " + value;
    }

    protected static String toJSONString(String key, ArrayList<?> value) {
        if (value == null) {
            return "\"" + key + "\":null,";
        }
        return "\"" + key + "\": " + value.toString();
    }

    public static String serializeGame(ChessGame game) {
        String winner = game.getWinner() == null ? null : game.getWinner().toString().toLowerCase();

        String output = "";
        output += "{";
        output += toJSONString("turn", game.getTurn().toString().toLowerCase()) + ",";
        output += toJSONString("check", game.getBoard().isCheck(game.getTurn())) + ",";
        output += toJSONString("winner", winner) + ",";
        output += toJSONString("draw", game.isGameOver() && winner == null) + ",";
        output += toJSONString("board", serializeBoard(game.getBoard()));
        output += "}";
        return output;
    }

    public static String serializeAvailableMoves(ChessGame game, Position position) {
        String output = "";

        Piece piece = game.getBoard().getPieceAt(position);
        ArrayList<Position> availableMoves;
        if (piece == null) {
            availableMoves = new ArrayList<Position>();
        } else {
            availableMoves = piece.getAllAvailableMoves(game.getBoard(), position);
        }

        output += "{";
        output += toJSONString("moves", availableMoves.toArray());
        output += "}";
        return output;
    }

}