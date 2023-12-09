package database.structure;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import chess.Board;
import chess.ChessGame;
import chess.PieceColor;
import database.Beans.GameData;

public class Database {
    private static Database instance;
    private Connection connection;
    private final String URL = "jdbc:mariadb://localhost:3306";
    private final String USER = "root";
    private final String PASSWORD = "root";
    private final String DATABASE_NAME = "chess";

    private Database() throws SQLException {
        connection = DriverManager.getConnection(URL + "/" + DATABASE_NAME, USER, PASSWORD);
    }

    public static Database getInstance() throws SQLException {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private static String generateRandomSalt() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int r = random.nextInt(64);
            if (r < 10) {
                sb.append((char) (r + 48));
            } else if (r < 36) {
                sb.append((char) (r + 55));
            } else if (r < 62) {
                sb.append((char) (r + 61));
            } else {
                sb.append((char) (r - 62));
            }
        }

        return sb.toString();
    }

    private static String generateHash(String salt, String password) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((salt + password).getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isUsernameTaken(String username) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM player WHERE username = ?");
        statement.setString(1, username);
        ResultSet rs = statement.executeQuery();
        return rs.next();
    }

    public boolean isEmailTaken(String email) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM credentials WHERE email = ?");
        statement.setString(1, email);
        ResultSet rs = statement.executeQuery();
        return rs.next();
    }

    protected int getPlayerId(String username) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT id FROM player WHERE username = ?");
        statement.setString(1, username);
        ResultSet rs = statement.executeQuery();
        if (!rs.next()) {
            return -1;
        }
        return rs.getInt("id");
    }

    public String getUsername(int playerId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT username FROM player WHERE id = ?");
        statement.setInt(1, playerId);
        ResultSet rs = statement.executeQuery();
        if (!rs.next()) {
            return null;
        }
        return rs.getString("username");
    }

    public boolean createUserAndCredentials(String username, String email, String password) throws SQLException {
        if (isUsernameTaken(username)) {
            return false;
        }
        if (isEmailTaken(email)) {
            return false;
        }
        String salt = generateRandomSalt();
        String hash = generateHash(salt, password);
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO player (username) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, username);
        int result = statement.executeUpdate();
        if (result == 0) {
            return false;
        }

        ResultSet rs = statement.getGeneratedKeys();
        rs.next();
        int playerId = rs.getInt(1);
        statement = connection.prepareStatement(
                "INSERT INTO credentials (player_id, email, salt, sha256) VALUES (?, ?, ?, ?)");
        statement.setInt(1, playerId);

        statement.setString(2, email);
        statement.setString(3, salt);
        statement.setString(4, hash);
        result = statement.executeUpdate();
        return result != 0;
    }

    public boolean authenticate(String username, String password) throws SQLException, NoSuchAlgorithmException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT salt, sha256 FROM credentials WHERE player_id = (SELECT id FROM player WHERE username = ?)");
        statement.setString(1, username);
        ResultSet rs = statement.executeQuery();
        if (!rs.next()) {
            return false;
        }
        String salt = rs.getString("salt");
        String hash = rs.getString("sha256");
        return hash.equals(generateHash(salt, password));
    }

    public int createGame(String playerWhite, String playerBlack) throws SQLException {
        ChessGame game = new ChessGame();
        int playerWhiteId = getPlayerId(playerWhite);
        int playerBlackId = getPlayerId(playerBlack);

        if (playerWhiteId == playerBlackId || playerWhiteId == -1 || playerBlackId == -1) {
            return -1;
        }

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO game (playerWhite, playerBlack, board, history, isWhiteTurn, turnCount) VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, getPlayerId(playerWhite));
        statement.setInt(2, getPlayerId(playerBlack));
        statement.setString(3, game.getBoard().toJSON());
        statement.setString(4, Board.toJSONArray(game.getHistory()));
        statement.setBoolean(5, game.getTurn() == PieceColor.WHITE);
        statement.setInt(6, game.getTurnCount());
        int result = statement.executeUpdate();
        if (result == 0) {
            return -1;
        }

        ResultSet rs = statement.getGeneratedKeys();
        rs.next();
        return rs.getInt(1);
    }

    public GameData loadGame(int gameId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM game WHERE id = ?");
        statement.setInt(1, gameId);
        ResultSet rs = statement.executeQuery();
        if (!rs.next()) {
            return null;
        }

        String historyJSON = rs.getString("history");
        historyJSON = historyJSON == null ? "[]" : historyJSON;
        ChessGame game = ChessGame.create(
                Board.fromJSON(rs.getString("board")),
                Board.fromJSONArray(historyJSON),
                rs.getBoolean("isWhiteTurn") ? PieceColor.WHITE : PieceColor.BLACK,
                rs.getInt("turnCount"));

        return new GameData(game, rs.getInt("playerWhite"), rs.getInt("playerBlack"));
    }

    public boolean updateGame(int gameId, ChessGame game) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "UPDATE game SET board = ?, history = ?, isWhiteTurn = ?, turnCount = ? WHERE id = ?");
        statement.setString(1, game.getBoard().toJSON());
        statement.setString(2, Board.toJSONArray(game.getHistory()));
        statement.setBoolean(3, game.getTurn() == PieceColor.WHITE);
        statement.setInt(4, game.getTurnCount());
        statement.setInt(5, gameId);
        int result = statement.executeUpdate();
        return result != 0;
    }
}
