package database.Beans;

import chess.ChessGame;
import chess.PieceColor;

public class GameData {
    private ChessGame game;
    private int whitePlayerId;
    private int blackPlayerId;

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

    public GameData(ChessGame game, int whitePlayerId, int blackPlayerId) {
        this.game = game;
        this.whitePlayerId = whitePlayerId;
        this.blackPlayerId = blackPlayerId;
    }
}
