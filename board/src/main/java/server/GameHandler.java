package server;

import java.io.IOException;
import java.util.Arrays;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import chess.ChessGame;
import chess.Position;

public class GameHandler implements HttpHandler {
    ChessGame game;

    public GameHandler() {
        this.game = new ChessGame();
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

    @Override
    public void handle(HttpExchange t) throws IOException {
        t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        switch (t.getRequestMethod()) {
            case "GET":
                handleGet(t);
                break;
            case "POST":
                handlePost(t);
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
        String response = Arrays.toString(GameSerializer.serializeBoard(game.getBoard()));
        return sendResponse(t, response);
    }

    public boolean getStatus(HttpExchange t) throws IOException {
        return sendResponse(t, GameSerializer.serializeGame(game));
    }

    public boolean getAvailableMoves(HttpExchange t) throws IOException {
        try {
            Position position = new Position(t.getRequestURI().getQuery());
            String response = GameSerializer.serializeAvailableMoves(game, position);

            return sendResponse(t, (response != null) ? response : "[]");
        } catch (Exception e) {
            return sendResponse(t, "Invalid position", 400);
        }
    }

    public boolean postMove(HttpExchange t) throws IOException {
        try {
            String data = new String(t.getRequestBody().readAllBytes());
            String[] parts = data.split(",");
            Position from = new Position(parts[0]);
            Position to = new Position(parts[1]);

            System.out.println("From: " + from);
            System.out.println("To: " + to);

            boolean success = game.play(from, to);
            if (success) {
                return sendResponse(t, "Move successful");
            } else {
                return sendResponse(t, "Move failed", 400);
            }
        } catch (Exception e) {
            return sendResponse(t, "Invalid Request Body", 400);
        }
    }

    public boolean postPromotion(HttpExchange t) throws IOException {
        return sendResponse(t, "This is the postPromotion() method");
    }
}
