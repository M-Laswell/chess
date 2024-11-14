package ui;

import chess.ChessGame;
import chess.ChessPiece;
import static ui.EscapeSequences.*;

import java.util.Arrays;

public class GameplayClient implements Client{
    private final String serverUrl;
    private final Repl repl;
    private final ChessGame tempGame = new ChessGame();
    private Boolean flipBoard = false;


    public GameplayClient(String serverUrl, Repl repl) {
        this.serverUrl = serverUrl;
        this.repl = repl;
    }

    @Override
    public String eval(String command) {
        try {
            var tokens = command.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "help" -> help();
                case "quit", "q" -> "quit";
                default -> generateChessboard(flipBoard);
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String generateChessboard(boolean flipBoard){
        ChessPiece[][] board = this.tempGame.getBoard().getChessBoard();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++) {
                String pieceRepresentation = EMPTY;
                ChessPiece piece = board[i][j];
                if(j == 0 || j == board.length-1 || i == 0 || i == board.length-1){
                    stringBuilder.append(SET_BG_COLOR_DARK_GREY);
                } else if (j % 2 == 1 && i % 2 == 0) {
                    stringBuilder.append(SET_BG_COLOR_DARK_GREEN);
                } else if (j % 2 == 0 && i % 2 == 1) {
                    stringBuilder.append(SET_BG_COLOR_DARK_GREEN);
                } else {
                    stringBuilder.append(SET_BG_COLOR_LIGHT_GREY);
                }
                stringBuilder.append(SET_TEXT_COLOR_WHITE);
                if ((i == 0 || i == board.length - 1) && (j != 0 && j != board.length -1)){
                        pieceRepresentation = "\u200a\u200a\u200a" + "\u2001" + this.getLetter(j) + SKINNY_SPACE +"\u2004";
                }
                if ((j == 0) && (i != 0 && i != board.length -1)){
                        pieceRepresentation = "\u200a\u200a\u200a\u200a\u200a\u200a\u200a\u200a\u200a\u200a\u200a\u200a\u200a" + String.valueOf(i) + "\u200a\u200a\u200a\u200a\u200a\u200a\u200a\u200a\u200a\u200a\u200a\u200a";
                }
                if ((j == board.length - 1) && (i != 0 && i != board.length -1)){
                    pieceRepresentation = "\u200a\u200a\u200a\u200a\u200a\u200a\u200a\u200a\u200a\u200a\u200a\u200a" + String.valueOf(i) + "\u200a\u200a\u200a\u200a\u200a\u200a\u200a\u200a\u200a\u200a\u200a\u200a";
                }
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
                            default -> throw new RuntimeException("What the flip? ;)");
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
                            default -> throw new RuntimeException("What the flip? ;)");
                        }
                        ;
                    }
                }
                stringBuilder.append(pieceRepresentation);
            }
            stringBuilder.append(RESET_BG_COLOR);
            stringBuilder.append("\n");
        }

      return stringBuilder.toString();

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
            default -> throw new RuntimeException("What the flip? ;)");
        };
    }


    @Override
    public String help() {
        return """
                - help - lists all possible commands
                - quit - ends the client
                - anything else - prints chess board
                """;
    }
}
