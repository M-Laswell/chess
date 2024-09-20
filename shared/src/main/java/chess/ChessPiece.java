package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    ChessGame.TeamColor pieceColor;
    ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;


    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {

        return pieceColor;

    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {

        return type;

    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        //pieceMoves: This method is similar to ChessGame.validMoves, except it does not honor whose turn it is or check if the king is being attacked.
        // This method does account for enemy and friendly pieces blocking movement paths.
        // The pieceMoves method will need to take into account the type of piece, and the location of other pieces on the board.

        switch(type){
            case PieceType.KING:
                return this.KingMove(board,myPosition);
            case PieceType.QUEEN:
                break;
            case PieceType.BISHOP:
                break;
            case PieceType.KNIGHT:
                break;
            case PieceType.ROOK:
                break;
            case PieceType.PAWN:
                return this.PawnMove(board,myPosition);
            default:
                throw new RuntimeException("What the flip? ;)" );
        }

        throw new RuntimeException("yah this is just out of bounds m8");

    }

    private Collection<ChessMove> PawnMove(ChessBoard board, ChessPosition myPosition)  {
        Collection<ChessMove> moves = new HashSet<ChessMove>();
        ChessGame.TeamColor myColor = board.chessBoard[myPosition.getRow()][myPosition.getColumn()].pieceColor;
        if(myColor == ChessGame.TeamColor.WHITE) {
            //upleft
            if ((myPosition.getRow() + 1 <= 8) && (myPosition.getColumn() - 1 >= 1) && board.chessBoard[myPosition.getRow() + 1][myPosition.getColumn() - 1] != null && board.chessBoard[myPosition.getRow() + 1][myPosition.getColumn() - 1].pieceColor != myColor) {
                if(myPosition.getRow() + 1 == 8){
                    moves.addAll(this.PawnPromoting(myPosition, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1)));
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), null));
                }
            }
            //up
            if (board.chessBoard[myPosition.getRow() + 1][myPosition.getColumn()] == null && (myPosition.getRow() + 1 <= 8)) {
                if(myPosition.getRow() + 1 == 8){
                    moves.addAll(this.PawnPromoting(myPosition, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn())));
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() ), null));
                }
            }
            //upright
            if ((myPosition.getRow() + 1 <= 8) && (myPosition.getColumn() + 1 <= 8) && board.chessBoard[myPosition.getRow() + 1][myPosition.getColumn() + 1] != null && board.chessBoard[myPosition.getRow() + 1][myPosition.getColumn() + 1].pieceColor != myColor) {
                if(myPosition.getRow() + 1 == 8){
                    moves.addAll(this.PawnPromoting(myPosition, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1)));
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), null));
                }            }
        } else {
            //downleft
            if ((myPosition.getRow() - 1 >= 1) && (myPosition.getColumn() - 1 >= 1) && board.chessBoard[myPosition.getRow() - 1][myPosition.getColumn() - 1] != null && board.chessBoard[myPosition.getRow() - 1][myPosition.getColumn() - 1].pieceColor != myColor) {
                if(myPosition.getRow() - 1 == 1){
                    moves.addAll(this.PawnPromoting(myPosition, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1)));
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), null));
                }
            }
            //down
            if (board.chessBoard[myPosition.getRow() - 1][myPosition.getColumn()] == null && (myPosition.getRow() - 1 >= 1)) {
                if(myPosition.getRow() - 1 == 1){
                    moves.addAll(this.PawnPromoting(myPosition, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn())));
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), null));
                }
            }
            //downright
            if ((myPosition.getRow() - 1 >= 1) && (myPosition.getColumn() + 1 <= 8) && board.chessBoard[myPosition.getRow() - 1][myPosition.getColumn() + 1] != null && board.chessBoard[myPosition.getRow() - 1][myPosition.getColumn() + 1].pieceColor != myColor) {
                if(myPosition.getRow() - 1 == 1){
                    moves.addAll(this.PawnPromoting(myPosition, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1)));
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), null));
                }
            }
        }
        System.out.println(moves);
        return moves;

    }
    private Collection<ChessMove> PawnPromoting(ChessPosition startPosition, ChessPosition endPosition){
        Collection<ChessMove> allThePromotes = new HashSet<ChessMove>();
        allThePromotes.add(new ChessMove(startPosition,endPosition,PieceType.KNIGHT));
        allThePromotes.add(new ChessMove(startPosition,endPosition,PieceType.QUEEN));
        allThePromotes.add(new ChessMove(startPosition,endPosition,PieceType.ROOK));
        allThePromotes.add(new ChessMove(startPosition,endPosition,PieceType.BISHOP));


        return allThePromotes;
    }

    private Collection<ChessMove> KingMove(ChessBoard board, ChessPosition myPosition)  {
        Collection<ChessMove> moves = new HashSet<ChessMove>();
        ChessGame.TeamColor myColor = board.chessBoard[myPosition.getRow()][myPosition.getColumn()].pieceColor;
        //upleft
        if(board.chessBoard[myPosition.getRow()+1][myPosition.getColumn()-1] == null && (myPosition.getRow()+1 <= 8) && (myPosition.getColumn() -1 >= 1)){
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() +1, myPosition.getColumn()-1), null));
        } else if ((myPosition.getRow()+1 <= 8) && (myPosition.getColumn() -1 >= 1) && board.chessBoard[myPosition.getRow()+1][myPosition.getColumn()-1].pieceColor != myColor ) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() +1, myPosition.getColumn()-1), null));
        }
        //up
        if(board.chessBoard[myPosition.getRow()+1][myPosition.getColumn()] == null && (myPosition.getRow()+1 <= 8)){
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() +1, myPosition.getColumn()), null));
        }else if ((myPosition.getRow()+1 <= 8) && board.chessBoard[myPosition.getRow()+1][myPosition.getColumn()].pieceColor != myColor) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() +1, myPosition.getColumn()), null));
        }
        //upright
        if(board.chessBoard[myPosition.getRow()+1][myPosition.getColumn()+1] == null && (myPosition.getRow()+1 <= 8) && (myPosition.getColumn() +1 <= 8)){
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() +1, myPosition.getColumn()+1), null));
        }else if ((myPosition.getRow()+1 <= 8) && (myPosition.getColumn() +1 <= 8) && board.chessBoard[myPosition.getRow()+1][myPosition.getColumn()+1].pieceColor != myColor) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() +1, myPosition.getColumn()+1), null));
        }
        //left
        if(board.chessBoard[myPosition.getRow()][myPosition.getColumn()-1] == null && (myPosition.getColumn() -1 >= 1)){
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1), null));
        }else if ((myPosition.getColumn() -1 >= 1) && board.chessBoard[myPosition.getRow()][myPosition.getColumn()-1].pieceColor != myColor) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() , myPosition.getColumn()-1), null));
        }
        //right
        if(board.chessBoard[myPosition.getRow()][myPosition.getColumn()+1] == null && (myPosition.getColumn() +1 <= 8)){
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() , myPosition.getColumn()+1), null));
        }else if ((myPosition.getColumn() +1 <= 8) && board.chessBoard[myPosition.getRow()][myPosition.getColumn()+1].pieceColor != myColor) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() , myPosition.getColumn()+1), null));
        }
        //downleft
        if(board.chessBoard[myPosition.getRow()-1][myPosition.getColumn()-1] == null && (myPosition.getRow()-1 >= 1) && (myPosition.getColumn() -1 >= 1)){
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() -1, myPosition.getColumn()-1), null));
        }else if ((myPosition.getRow()-1 >= 1) && (myPosition.getColumn() -1 >= 1) && board.chessBoard[myPosition.getRow()-1][myPosition.getColumn()-1].pieceColor != myColor) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() -1, myPosition.getColumn()-1), null));
        }
        //down
        if(board.chessBoard[myPosition.getRow()-1][myPosition.getColumn()] == null && (myPosition.getRow()-1 >= 1) ){
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() -1, myPosition.getColumn()), null));
        }else if ((myPosition.getRow()-1 >= 1) && board.chessBoard[myPosition.getRow()-1][myPosition.getColumn()].pieceColor != myColor) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() -1, myPosition.getColumn()), null));
        }
        //downright
        if(board.chessBoard[myPosition.getRow()-1][myPosition.getColumn()+1] == null && (myPosition.getRow()-1 >= 1) && (myPosition.getColumn() +1 <= 8)){
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() -1, myPosition.getColumn()+1), null));
        }else if ((myPosition.getRow()-1 >= 1) && (myPosition.getColumn() +1 <= 8) && board.chessBoard[myPosition.getRow()-1][myPosition.getColumn()+1].pieceColor != myColor) {
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() -1, myPosition.getColumn()+1), null));
        }
        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
