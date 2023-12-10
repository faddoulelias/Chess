package database.structure;

import java.sql.SQLException;
import chess.ChessGame;
import chess.Position;
import database.Beans.GameData;

public class Game {
    public static int create(String whitePlayerUsername, String blackPlayerUsername) {
        try {
            return Database.getInstance().createGame(whitePlayerUsername, blackPlayerUsername);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static GameData load(int gameId) {
        try {
            return Database.getInstance().loadGame(gameId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String whitePlayerUsername(GameData gameData) {
        try {
            return Database.getInstance().getUsername(gameData.getWhitePlayerId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String blackPlayerUsername(GameData gameData) {
        try {
            return Database.getInstance().getUsername(gameData.getBlackPlayerId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean play(int gameId, String playerUsername, Position from, Position to) {
        Database db;
        try {
            db = Database.getInstance();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        GameData game = load(gameId);
        if (game == null) {
            return false;
        }

        int currentPlayerId = game.getTurnPlayerId();
        int thisPlayerId;
        try {
            thisPlayerId = db.getPlayerId(playerUsername);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        if (currentPlayerId != thisPlayerId) {
            return false;
        }

        ChessGame chessGame = game.getGame();
        boolean result = chessGame.play(from, to);

        if (result) {
            try {
                db.updateGame(gameId, chessGame);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        return result;
    }
}
