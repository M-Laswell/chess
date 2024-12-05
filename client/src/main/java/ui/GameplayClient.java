package ui;

import chess.*;
import exception.ResponseException;
import model.GameData;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.*;

import java.util.Arrays;
import java.util.Collection;

public class GameplayClient implements Client{
    private final String serverUrl;
    private final Repl repl;
    public GameData chessGame;
    //private ChessBoard tempBoard = new ChessBoard();
    private Boolean flipBoard = true;
    private Boolean resConfirmation = false;
    private Boolean resConfirmed = false;
    private WebSocketFacade ws;


    public GameplayClient(String serverUrl, Repl repl) {
        this.serverUrl = serverUrl;
        this.repl = repl;
    }

    public void upgradeToWebsocket() {
        try {
            ws = new WebSocketFacade(serverUrl, repl);
            ws.connectToGame(chessGame.getGameID(), repl.getAuthData().getAuthToken());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public String eval(String command) {
        try {
            var tokens = command.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if(repl.getState() == State.OBSERVING) {
                return switch (cmd) {
                    case "help" -> help();
                    case "redraw", "r" -> printChessboard(null);
                    case "select", "s" -> select(params[0]);
                    case "leave", "b" -> leave();
                    case "quit", "q" -> quit();
                    default -> help();
                };
            } else if(resConfirmation) {
                return switch (cmd) {
                    case "y", "yes" -> confirmRes();
                    case "no", "n" -> declineRes();
                    case "quit", "q" -> quit();
                    default -> help();
                };
            }else {
                return switch (cmd) {
                    case "help" -> help();
                    case "redraw", "r" -> printChessboard(null);
                    case "select", "s" -> select(params[0]);
                    case "move", "m" -> makeMove(params[0], params[1]);
                    case "resign", "res" -> resign();
                    case "leave", "b" -> leave();
                    case "quit", "q" -> quit();
                    default -> help();
                };
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    private String confirmRes() throws ResponseException {
        resConfirmation = false;
        ws.resign(chessGame.getGameID(), repl.getAuthData().getAuthToken());
        if (!chessGame.getGame().isGameWon()) {
            return "You have resigned";
        }
        return"";
    }
    private String declineRes() throws ResponseException {
        resConfirmation = false;
        return "Resignation has been stopped";
    }
    private String resign(){
        try {
            if (chessGame.getBlackUsername() != null && chessGame.getWhiteUsername() != null
                    && chessGame.getBlackUsername().equals(repl.getAuthData().getUsername())) {
                if (!resConfirmation) {
                    resConfirmation = true;
                    return "Are you sure you want to resign?";
                }
                return "";

            } else if (!resConfirmation){
                resConfirmation = true;
                return "Are you sure you want to resign?";
            }
            return "";
        }catch (Exception e){
            System.out.println(e);
            return "An error has occured during your resignation";
        }
    }

    private String quit(){
        leave();
        return "quit";
    }

    public String printChessboard(ChessPosition selected) {
        if(chessGame.getBlackUsername() != null && chessGame.getBlackUsername().equals(repl.getAuthData().getUsername())) {
                return "In Game " + chessGame.getGameName() + "\n\n" + generateChessboard(flipBoard, selected);
        }
        return "In Game " + chessGame.getGameName() + "\n\n" + generateChessboard(!flipBoard, selected);
    }


    private String generateChessboard(boolean flipBoard, ChessPosition selected){
        ChessGame game = chessGame.getGame().clone();
        Collection<ChessMove> validMoves = null;
        ChessPiece[][] board = this.chessGame.getGame().getBoard().getChessBoard();
        StringBuilder stringBuilder = new StringBuilder();
        if(selected != null){
            validMoves = game.validMoves(selected);
        }
        if(flipBoard) {
            for (int i = 0; i < board.length; i++) {
                for (int j = board.length - 1; j >= 0; j--) {
                    String pieceRepresentation = EMPTY;
                    ChessPiece piece = board[i][j];
                    this.setBackGroundColor(stringBuilder, i, j, board, validMoves, selected);

                    stringBuilder.append(SET_TEXT_COLOR_WHITE);
                    pieceRepresentation = setSpacing(pieceRepresentation, i, j, board);
                    pieceRepresentation = setPiece(stringBuilder, piece, pieceRepresentation);
                    stringBuilder.append(pieceRepresentation);
                }
                stringBuilder.append(RESET_BG_COLOR);
                stringBuilder.append("\n");
            }
        } else {
            for (int i = board.length - 1; i >= 0; i--) {
                for (int j = 0; j < board.length; j++) {
                    String pieceRepresentation = EMPTY;
                    ChessPiece piece = board[i][j];
                    setBackGroundColor(stringBuilder, i, j, board, validMoves, selected);
                    stringBuilder.append(SET_TEXT_COLOR_WHITE);
                    pieceRepresentation = setSpacing(pieceRepresentation, i, j, board);
                    pieceRepresentation = setPiece(stringBuilder, piece, pieceRepresentation);
                    stringBuilder.append(pieceRepresentation);
                }
                stringBuilder.append(RESET_BG_COLOR);
                stringBuilder.append("\n");
            }
        }
      return stringBuilder.toString();

    }

    private String leave(){
        try {
            ws.leaveGame(chessGame.getGameID(), repl.getAuthData().getAuthToken());
            return backToMenu();
        } catch (Exception e){
            System.out.println(e);
        }
        return "You couldn't Leave for some unknown reason";
    }

    private String getLetter(int numberEquivalent){
        return switch (numberEquivalent) {
            case 1 -> "A";
            case 2 -> "B";
            case 3 -> "C";
            case 4 -> "D";
            case 5 -> "E";
            case 6 -> "F";
            case 7 -> "G";
            case 8 -> "H";
            default -> throw new RuntimeException(" ");
        };
    }

    private StringBuilder setBackGroundColor(StringBuilder stringBuilder, int i, int j, ChessPiece[][] board,
                                             Collection<ChessMove> validMoves, ChessPosition selected) {
        ChessMove move = new ChessMove(selected, new ChessPosition(i, j), null);
        if (validMoves != null && validMoves.contains(move)) {
            stringBuilder.append(SET_BG_COLOR_MAGENTA);
        } else if (selected != null && selected.getColumn() == j && selected.getRow() == i) {
            stringBuilder.append(SET_BG_COLOR_YELLOW);
        } else if(j == 0 || j == board.length-1 || i == 0 || i == board.length-1){
            stringBuilder.append(SET_BG_COLOR_DARK_GREY);
        } else if (j % 2 == 1 && i % 2 == 0) {
            stringBuilder.append(SET_BG_COLOR_LIGHT_GREY);
        } else if (j % 2 == 0 && i % 2 == 1) {
            stringBuilder.append(SET_BG_COLOR_LIGHT_GREY);
        } else {
            stringBuilder.append(SET_BG_COLOR_DARK_GREEN);
        }

        return stringBuilder;
    }

    private String setSpacing(String pieceRepresentation, int i, int j, ChessPiece[][]board){
        if ((i == 0 || i == board.length - 1) && (j != 0 && j != board.length -1)){
            pieceRepresentation = "\u2001" + this.getLetter(j) + SKINNY_SPACE +"\u2004";
        }
        if ((j == 0) && (i != 0 && i != board.length -1)){
            pieceRepresentation = "\u200a\u200a\u200a\u200a\u200a\u200a\u200a" +
                    String.valueOf(i) + "\u200a\u200a\u200a\u200a\u200a\u200a";
        }
        if ((j == board.length - 1) && (i != 0 && i != board.length -1)){
            pieceRepresentation = "\u200a\u200a\u200a\u200a\u200a\u200a" +
                    String.valueOf(i) + "\u200a\u200a\u200a\u200a\u200a\u200a";
        }
        return pieceRepresentation;
    }

    private String setPiece(StringBuilder stringBuilder, ChessPiece piece, String pieceRepresentation){
        if(piece != null) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                stringBuilder.append(SET_TEXT_COLOR_WHITE);
                switch (piece.getPieceType()) {
                    case ChessPiece.PieceType.KING -> pieceRepresentation = WHITE_KING;
                    case ChessPiece.PieceType.QUEEN -> pieceRepresentation = WHITE_QUEEN;
                    case ChessPiece.PieceType.BISHOP -> pieceRepresentation = WHITE_BISHOP;
                    case ChessPiece.PieceType.KNIGHT -> pieceRepresentation = WHITE_KNIGHT;
                    case ChessPiece.PieceType.ROOK -> pieceRepresentation = WHITE_ROOK;
                    case ChessPiece.PieceType.PAWN -> pieceRepresentation = WHITE_PAWN;
                    default -> throw new RuntimeException(" ");
                };
            } else {
                stringBuilder.append(SET_TEXT_COLOR_BLACK);
                switch (piece.getPieceType()) {
                    case ChessPiece.PieceType.KING -> pieceRepresentation = BLACK_KING;
                    case ChessPiece.PieceType.QUEEN -> pieceRepresentation = BLACK_QUEEN;
                    case ChessPiece.PieceType.BISHOP -> pieceRepresentation = BLACK_BISHOP;
                    case ChessPiece.PieceType.KNIGHT -> pieceRepresentation = BLACK_KNIGHT;
                    case ChessPiece.PieceType.ROOK -> pieceRepresentation = BLACK_ROOK;
                    case ChessPiece.PieceType.PAWN -> pieceRepresentation = BLACK_PAWN;
                    default -> throw new RuntimeException(" ");
                };
            }
        }
        return pieceRepresentation;
    }

    public void setChessGame(GameData chessGame) {
        this.chessGame = chessGame;
    }

    private String backToMenu(){
        this.repl.changeState(State.SIGNEDIN);
        return "Welcome Back";
    }

    private String select(String selection){
        try {
            int row = Character.getNumericValue(selection.charAt(1));
            int col = convertColToNumber(selection.charAt(0));
            return printChessboard(new ChessPosition(row,col));
        } catch (Exception e){
            return "Invalid Command";
        }
    }

    private Integer convertColToNumber(char letter){
        return switch(letter) {
            case 'A', 'a' -> 1;
            case 'B', 'b' -> 2;
            case 'C', 'c' -> 3;
            case 'D', 'd' -> 4;
            case 'E', 'e' -> 5;
            case 'F', 'f' -> 6;
            case 'G', 'g' -> 7;
            case 'H', 'h' -> 8;
            default -> throw new RuntimeException(" ");
        };
    }

    private String makeMove(String startingPosition, String endingPosition){
        try {
            int startRow = (Character.getNumericValue(startingPosition.charAt(1)));
            int startCol = convertColToNumber(startingPosition.charAt(0));
            int endRow = (Character.getNumericValue(endingPosition.charAt(1)));
            int endCol = convertColToNumber(endingPosition.charAt(0));
            ChessPosition startPos = new ChessPosition(startRow, startCol);
            ChessPosition endPos = new ChessPosition(endRow, endCol);
            ChessMove move = new ChessMove(startPos, endPos, null);
            ws.makeMove(chessGame.getGameID(), repl.getAuthData().getAuthToken(), move, startingPosition, endingPosition);

        } catch (Exception e){
            return "Invalid Command";
        }
        return "";
    }

    @Override
    public String help() {
        if (repl.getState().equals(State.OBSERVING)) {
            return """
                    - help - lists all possible commands
                    - redraw - draws the chessboard again
                    - leave - stop observing the game
                    - quit - ends the client
                    """;
        } else if (resConfirmation) {
            return """
                    - help - lists all possible commands
                    - yes - confirm resignation
                    - no - stop resignation
                    - quit - ends the client
                    """;
        } else {
            return """
                    - help - lists all possible commands
                    - redraw - draws the chessboard again
                    - select <A-H1-8> - selects a piece a the indicated row and column to see its moves
                    - move <A-H1-8> <A-H1-8>- moves the piece in the first row
                        column selection to the second row and column selection
                    - leave - removes player from the game leaving their spot open
                    - resign - forfeits the match to the other player
                    - quit - ends the client
                    """;
        }
    }
}
