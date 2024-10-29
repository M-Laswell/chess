package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    TeamColor teamTurn;
    ChessBoard chessBoard = new ChessBoard();

    public ChessGame() {
        this.teamTurn = TeamColor.WHITE;
        this.chessBoard.resetBoard();



    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        ChessPiece currPiece = chessBoard.getPiece(startPosition);
        if (currPiece == null){
            return null;
        }
        Collection<ChessMove> possibleMoves = currPiece.pieceMoves(chessBoard, startPosition);
        Collection<ChessMove> validMoves = new HashSet<>();


        for (ChessMove move : possibleMoves) {
            ChessBoard tempBoard = new ChessBoard(chessBoard);
            chessBoard.movePiece(startPosition, move.getEndPosition(), move.promotionPiece);
            if (!isInCheck(currPiece.pieceColor)){
                validMoves.add(move);
            }

            chessBoard = tempBoard;
        }

        return validMoves;


    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = chessBoard.getPiece(move.getStartPosition());


        if ( validMoves(move.startPosition) == null || !validMoves(move.startPosition).contains(move) || piece.getTeamColor() != teamTurn ) {
            throw new InvalidMoveException("Invalid move attempted.");
        }
        //ChessPiece piece = chessBoard.getPiece(move.getStartPosition());
        chessBoard.movePiece(move.getStartPosition(), move.getEndPosition(), move.promotionPiece);
        setTeamTurn(teamTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    //try using.contains on the collection of moves after finding the king
    public boolean isInCheck(TeamColor teamColor) {

        ChessPosition kingPos = new ChessPosition(0,0);
        Collection<ChessMove> possibleMoves = new HashSet<>();

        for(int row = 0; row < this.getBoard().chessBoard.length ;row++){
            for(int col = 0; col < this.getBoard().chessBoard.length ;col++) {
                ChessPiece piece = this.getBoard().chessBoard[row][col];
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.pieceColor == teamColor) {
                    kingPos = new ChessPosition(row, col);
                }
            }

        }

        for(int row = 0; row < this.getBoard().chessBoard.length ;row++){
            for(int col = 0; col < this.getBoard().chessBoard.length ;col++) {
                ChessPiece piece = this.getBoard().chessBoard[row][col];
                ChessPosition position = new ChessPosition(row, col);
                if(piece != null && piece.pieceColor != teamColor){
                    possibleMoves.addAll(piece.pieceMoves(this.getBoard(), position));

                }
            }

        }
        for (ChessMove move : possibleMoves) {
            if (move.endPosition.getRow() == kingPos.getRow() && move.endPosition.getColumn()  == kingPos.getColumn()) {
                return true;
            }
        }




        return false;
    }

    private Collection<ChessMove> cycleThroughMoves (TeamColor teamColor){

        ChessPosition piecePos = new ChessPosition(0,0);
        Collection<ChessMove> possibleMoves = new HashSet<>();

        for(int row = 0; row < this.getBoard().chessBoard.length ;row++){
            for(int col = 0; col < this.getBoard().chessBoard.length ;col++) {
                ChessPiece piece = this.getBoard().chessBoard[row][col];
                if (piece != null && piece.pieceColor == teamColor) {
                    piecePos = new ChessPosition(row, col);
                    possibleMoves.addAll(validMoves(piecePos));


                }
            }

        }
        return possibleMoves;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        Collection<ChessMove> possibleMoves = new HashSet<>();

        possibleMoves = cycleThroughMoves(teamColor);

        if (possibleMoves.isEmpty() && isInCheck(teamColor)){
            return true;
        }

        return false;
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> possibleMoves = new HashSet<>();

        possibleMoves = cycleThroughMoves(teamColor);

        if (possibleMoves.isEmpty() && !isInCheckmate(teamColor)){
            return true;
        }

        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {

        this.chessBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return chessBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(chessBoard, chessGame.chessBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, chessBoard);
    }
}
