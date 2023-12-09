package database;

import database.structure.Authentication;
import database.structure.Game;

import java.sql.SQLException;

import chess.ChessGame;

public class App {
    public static void main(String[] args) throws SQLException {
        boolean player1LogedIn = Authentication.login("efaddoul", "1Qwerty@");
        boolean player2LogedIn = Authentication.login("karlmouss", "1Qwerty@");

        if (!player1LogedIn || !player2LogedIn) {
            System.out.println("Failed to login");
            return;
        }

        int gameCreated = Game.create("efaddoul", "karlmouss");
        if (gameCreated == -1) {
            return;
        }
        System.out.println("Game created with id: " + gameCreated);

        ChessGame game = Game.load(gameCreated).getGame();
        if (game == null) {
            return;
        }

        boolean played1 = Game.play(gameCreated, "efaddoul", new chess.Position("a2"), new chess.Position("a3"));
        System.out.println("Played 1: " + played1);
        boolean played2 = Game.play(gameCreated, "karlmouss", new chess.Position("a7"), new chess.Position("a6"));
        System.out.println("Played 2: " + played2);
        boolean played3 = Game.play(gameCreated, "efaddoul", new chess.Position("a3"), new chess.Position("a4"));
        System.out.println("Played 3: " + played3);

        ChessGame game2 = Game.load(gameCreated).getGame();
        if (game2 == null) {
            return;
        }
        System.out.println(game2);
    }
}
