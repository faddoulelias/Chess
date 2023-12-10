package server;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import chess.ChessGame;
import chess.Position;
import database.Beans.GameData;
import database.structure.Game;

public class GameHandler implements HttpHandler {
    private static GameData loadGame(int id) {
        return Game.load(id);
    }

    private boolean sendResponse(HttpExchange t, String response, int code) throws IOException {
        t.sendResponseHeaders(code, response.length());
        t.getResponseBody().write(response.getBytes());
        t.getResponseBody().close();
        return true;
    }

    private boolean sendResponse(HttpExchange t, String response) throws IOException {
        return sendResponse(t, response, 200);
    }

    private static void setHttpExchangeResponseHeaders(HttpExchange httpExchange) {
        // Set common response headers
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Credentials-Header", "*");
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        setHttpExchangeResponseHeaders(t);
        switch (t.getRequestMethod()) {
            case "GET":
                handleGet(t);
                break;
            case "POST":
                handlePost(t);
                break;
            case "OPTIONS":
                sendResponse(t, "OK");
                break;
            default:
                sendResponse(t, "Invalid request method", 405);
                break;
        }

        t.getResponseBody().close();
    }

    public void handleGet(HttpExchange t) throws IOException {
        switch (t.getRequestURI().getPath()) {
            case "/chess/board":
                getBoard(t);
                break;
            case "/chess/status":
                getStatus(t);
                break;
            case "/chess/moves":
                getAvailableMoves(t);
                break;
            case "/chess/update":
                getHasUpdate(t);
                break;
            default:
                sendResponse(t, "Invalid request path", 404);
                break;
        }
    }

    public void handlePost(HttpExchange t) throws IOException {
        switch (t.getRequestURI().getPath()) {
            case "/chess/move":
                postMove(t);
                break;
            case "/chess/promotion":
                postPromotion(t);
                break;
            default:
                sendResponse(t, "Invalid request path", 404);
                break;
        }
    }

    public boolean getBoard(HttpExchange t) throws IOException {
        ChessGame game = loadGame(Integer.parseInt(t.getRequestURI().getQuery())).getGame();
        String response = Arrays.toString(GameSerializer.serializeBoard(game.getBoard()));
        return sendResponse(t, response);
    }

    public boolean getStatus(HttpExchange t) throws IOException {
        GameData game = loadGame(Integer.parseInt(t.getRequestURI().getQuery()));
        return sendResponse(t, GameSerializer.serializeGame(game));
    }

    public boolean getHasUpdate(HttpExchange t) throws IOException {
        String[] query = t.getRequestURI().getQuery().split("&");
        int gameId = Integer.parseInt(query[0]);
        long lastUpdate = Long.parseLong(query[1]);
        GameData game = loadGame(gameId);
        return sendResponse(t, (game.getUpdatedAt().getTime() > lastUpdate) ? "true" : "false");
    }

    public boolean getAvailableMoves(HttpExchange t) throws IOException {
        String[] query = t.getRequestURI().getQuery().split("&");
        ChessGame game = loadGame(Integer.parseInt(query[0])).getGame();
        try {
            Position position = new Position(query[1]);
            String response = GameSerializer.serializeAvailableMoves(game, position);

            return sendResponse(t, (response != null) ? response : "[]");
        } catch (Exception e) {
            return sendResponse(t, "Invalid position", 400);
        }
    }

    public boolean postMove(HttpExchange t) throws IOException {
        String[] data = new String(t.getRequestBody().readAllBytes()).split(",");
        String authorizationHeader = t.getRequestHeaders().getFirst("Authorization");
        String[] credentials = new String(Base64.getDecoder().decode(authorizationHeader.substring(6))).split(":");

        String username = credentials[0];
        String password = credentials[1];
        int gameId = Integer.parseInt(data[0]);
        Position from = new Position(data[1]);
        Position to = new Position(data[2]);
        boolean success = Game.play(gameId, username, from, to);

        System.out.println("username: " + username);
        System.out.println("password: " + password);
        System.out.println("gameId: " + gameId);
        System.out.println("from: " + from);
        System.out.println("to: " + to);

        if (success) {
            return sendResponse(t, "Move played successfully");
        } else {
            return sendResponse(t, "Failed to play move", 400);
        }
    }

    public boolean postPromotion(HttpExchange t) throws IOException {
        return sendResponse(t, "This is the postPromotion() method");
    }
}
