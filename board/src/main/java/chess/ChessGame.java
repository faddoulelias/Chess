package chess;

import java.util.ArrayList;

public class ChessGame {
    Board board;
    ArrayList<Board> history;
    PieceColor turn;
    PieceColor winner;
    int turnCount;

    public ChessGame() {
        board = new Board();
        board.setStartingPosition();
        history = new ArrayList<Board>();
        winner = null;
        turn = PieceColor.WHITE;
        turnCount = 0;
    }

    public static ChessGame create(Board board, ArrayList<Board> history, PieceColor turn, int turnCount) {
        ChessGame game = new ChessGame();
        game.board = board;
        game.history = history;
        game.turn = turn;
        game.turnCount = turnCount;
        game.checkIfGameIsOver();
        return game;
    }

    public PieceColor getTurn() {
        return turn;
    }

    public ArrayList<Board> getHistory() {
        return history;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public Board getBoard() {
        return board;
    }

    public PieceColor getWinner() {
        return winner;
    }

    public boolean isGameOver() {
        return this.checkIfGameIsOver();
    }

    public boolean checkIfGameIsOver() {
        if (board.isCheckmate(PieceColor.WHITE)) {
            winner = PieceColor.BLACK;
            return true;
        }

        if (board.isCheckmate(PieceColor.BLACK)) {
            winner = PieceColor.WHITE;
            return true;
        }

        if (board.isStalemate(PieceColor.WHITE) || board.isStalemate(PieceColor.BLACK)) {
            return true;
        }

        return false;
    }

    public boolean play(Position from, Position to) {
        if (this.checkIfGameIsOver()
                || board.getPieceAt(from) == null
                || board.getPieceAt(from).getColor() != turn) {
            return false;
        }

        try {
            history.add((Board) board.clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        boolean success = board.movePiece(from, to);
        turnCount++;
        if (!success) {
            return false;
        }

        turn = (turn == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
        checkIfGameIsOver();
        return true;
    }

    @Override
    public String toString() {
        return board.toString();
    }
}
