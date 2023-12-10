package database.Beans;

import chess.ChessGame;
import chess.PieceColor;
import java.sql.Timestamp;

public class GameData {
    private ChessGame game;
    private int whitePlayerId;
    private int blackPlayerId;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    public ChessGame getGame() {
        return game;
    }

    public int getWhitePlayerId() {
        return whitePlayerId;
    }

    public int getBlackPlayerId() {
        return blackPlayerId;
    }

    public int getTurnPlayerId() {
        if (game.getTurn().equals(PieceColor.WHITE)) {
            return whitePlayerId;
        } else {
            return blackPlayerId;
        }
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public GameData(ChessGame game, int whitePlayerId, int blackPlayerId, Timestamp createdAt, Timestamp updatedAt) {
        this.game = game;
        this.whitePlayerId = whitePlayerId;
        this.blackPlayerId = blackPlayerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
