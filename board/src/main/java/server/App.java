package server;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class App {
    public static void main(String[] args) throws IOException {
        System.setProperty("sun.net.httpserver.allowRestrictedHeaders", "true");
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/chess", new GameHandler());
        server.setExecutor(null);
        server.start();
    }
}
