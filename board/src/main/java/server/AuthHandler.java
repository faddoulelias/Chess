package server;

import java.io.IOException;
import java.util.Base64;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import database.structure.Authentication;

public class AuthHandler implements HttpHandler {
    private static void setHttpExchangeResponseHeaders(HttpExchange httpExchange) {
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Credentials-Header", "*");
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

    private void handleGet(HttpExchange t) {
        if (getAuth(t)) {
            try {
                sendResponse(t, "OK");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                sendResponse(t, "Unauthorized", 401);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handlePost(HttpExchange t) {

    }

    private boolean getAuth(HttpExchange t) {
        String authorizationHeader = t.getRequestHeaders().getFirst("Authorization");
        String[] credentials = new String(Base64.getDecoder().decode(authorizationHeader.substring(6))).split(":");
        if (credentials.length != 2) {
            return false;
        }

        return Authentication.login(credentials[0], credentials[1]);
    }
}