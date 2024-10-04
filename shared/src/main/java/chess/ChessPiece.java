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

        return switch (type) {
            case PieceType.KING -> this.KingMove(board, myPosition);
            case PieceType.QUEEN -> this.QueenMove(board, myPosition);
            case PieceType.BISHOP -> this.BishopMove(board, myPosition);
            case PieceType.KNIGHT -> this.KnightMove(board, myPosition);
            case PieceType.ROOK -> this.RookMove(board, myPosition);
            case PieceType.PAWN -> this.PawnMove(board, myPosition);
            default -> throw new RuntimeException("What the flip? ;)");
        };

        //throw new RuntimeException("yah this is just out of bounds m8");

    }


    private Collection<ChessMove> KnightMove(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new HashSet<ChessMove>();
        ChessGame.TeamColor myColor = board.chessBoard[myPosition.getRow()][myPosition.getColumn()].pieceColor;
        //upright
        if ((myPosition.getRow() + 2 <= 8) && (myPosition.getColumn() + 1 <= 8) && board.chessBoard[myPosition.getRow()+2][myPosition.getColumn()+1] == null){
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn()+ 1), null));
        } else if ((myPosition.getRow() + 2 <= 8) && (myPosition.getColumn() + 1 <= 8) && board.chessBoard[myPosition.getRow()+2][myPosition.getColumn()+1].pieceColor != myColor){
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn()+ 1), null));
        }
        //rightup
        if ((myPosition.getRow() + 1 <= 8) && (myPosition.getColumn() + 2 <= 8) && board.chessBoard[myPosition.getRow()+1][myPosition.getColumn()+2] == null){
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()+ 2), null));
        } else if ((myPosition.getRow() + 1 <= 8) && (myPosition.getColumn() + 2 <= 8) && board.chessBoard[myPosition.getRow()+1][myPosition.getColumn()+2].pieceColor != myColor){
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()+ 2), null));
        }
        //upleft
        if ((myPosition.getRow() + 2 <= 8) && (myPosition.getColumn() - 1 >= 1) && board.chessBoard[myPosition.getRow()+2][myPosition.getColumn()-1] == null){
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn()- 1), null));
        } else if ((myPosition.getRow() + 2 <= 8) && (myPosition.getColumn() - 1 >= 1) && board.chessBoard[myPosition.getRow()+2][myPosition.getColumn()-1].pieceColor != myColor){
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn()- 1), null));
        }
        //leftup
        if ((myPosition.getRow() + 1 <= 8) && (myPosition.getColumn() - 2 >= 1) && board.chessBoard[myPosition.getRow()+1][myPosition.getColumn()-2] == null){
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2), null));
        } else if ((myPosition.getRow() + 1 <= 8) && (myPosition.getColumn() - 2 >= 1) && board.chessBoard[myPosition.getRow()+1][myPosition.getColumn()-2].pieceColor != myColor){
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2), null));
        }
        //downright
        if ((myPosition.getRow() - 2 >= 1) && (myPosition.getColumn() + 1 <= 8) && board.chessBoard[myPosition.getRow()-2][myPosition.getColumn()+1] == null){
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn()+ 1), null));
        } else if ((myPosition.getRow() - 2 >= 1) && (myPosition.getColumn() + 1 <= 8) && board.chessBoard[myPosition.getRow()-2][myPosition.getColumn()+1].pieceColor != myColor){
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn()+ 1), null));
        }
        //rightdown
        if ((myPosition.getRow() - 1 >= 1) && (myPosition.getColumn() + 2 <= 8) && board.chessBoard[myPosition.getRow()-1][myPosition.getColumn()+2] == null){
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()+ 2), null));
        } else if ((myPosition.getRow() - 1 >= 1) && (myPosition.getColumn() + 2 <= 8) && board.chessBoard[myPosition.getRow()-1][myPosition.getColumn()+2].pieceColor != myColor){
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()+ 2), null));
        }
        //downleft
        if ((myPosition.getRow() - 2 >= 1) && (myPosition.getColumn() - 1 >= 1) && board.chessBoard[myPosition.getRow()-2][myPosition.getColumn()-1] == null){
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn()- 1), null));
        } else if ((myPosition.getRow() - 2 >= 1) && (myPosition.getColumn() - 1 >= 1) && board.chessBoard[myPosition.getRow()-2][myPosition.getColumn()-1].pieceColor != myColor){
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn()- 1), null));
        }
        //leftdown
        if ((myPosition.getRow() - 1  >= 1) && (myPosition.getColumn() - 2 >= 1) && board.chessBoard[myPosition.getRow()-1][myPosition.getColumn()-2] == null){
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()-2), null));
        } else if ((myPosition.getRow() - 1  >= 1) && (myPosition.getColumn() - 2 >= 1) && board.chessBoard[myPosition.getRow()-1][myPosition.getColumn()-2].pieceColor != myColor){
            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2), null));
        }

        return moves;
    }


    private Collection<ChessMove> BishopMove(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new HashSet<ChessMove>();
        ChessPosition bishopPosition = myPosition;
        ChessGame.TeamColor myColor = board.chessBoard[myPosition.getRow()][myPosition.getColumn()].pieceColor;

        //upright
        while (bishopPosition.getRow()+1 <= 8 && bishopPosition.getColumn()+1 <= 8){
            if(board.chessBoard[bishopPosition.getRow()+1][bishopPosition.getColumn()+1] == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(bishopPosition.getRow()+1, bishopPosition.getColumn()+1),null));
                //BishopMove(board, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1));
            }else if (board.chessBoard[bishopPosition.getRow()+1][bishopPosition.getColumn()+1].pieceColor != myColor){
                moves.add(new ChessMove(myPosition, new ChessPosition(bishopPosition.getRow()+1, bishopPosition.getColumn()+1),null));
                break;
            } else if (board.chessBoard[bishopPosition.getRow()+1][bishopPosition.getColumn()+1].pieceColor == myColor) {
                break;

            }
            bishopPosition = new ChessPosition(bishopPosition.getRow()+1, bishopPosition.getColumn()+1);

        }
        bishopPosition = myPosition;
        //upleft
        while (bishopPosition.getRow()+1 <= 8 && bishopPosition.getColumn()-1 >= 1){
            if(board.chessBoard[bishopPosition.getRow()+1][bishopPosition.getColumn()-1] == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(bishopPosition.getRow()+1, bishopPosition.getColumn()-1),null));
                //BishopMove(board, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1));
            }else if (board.chessBoard[bishopPosition.getRow()+1][bishopPosition.getColumn()-1].pieceColor != myColor){
                moves.add(new ChessMove(myPosition, new ChessPosition(bishopPosition.getRow()+1, bishopPosition.getColumn()-1),null));
                break;
            } else if (board.chessBoard[bishopPosition.getRow()+1][bishopPosition.getColumn()-1].pieceColor == myColor) {
                break;
            }
            bishopPosition = new ChessPosition(bishopPosition.getRow()+1, bishopPosition.getColumn()-1);

        }
        bishopPosition = myPosition;

        //downright
        while (bishopPosition.getRow()-1 >= 1 && bishopPosition.getColumn()+1 <= 8){
            if(board.chessBoard[bishopPosition.getRow()-1][bishopPosition.getColumn()+1] == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(bishopPosition.getRow()-1, bishopPosition.getColumn()+1),null));
                //BishopMove(board, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1));
            }else if (board.chessBoard[bishopPosition.getRow()-1][bishopPosition.getColumn()+1].pieceColor != myColor){
                moves.add(new ChessMove(myPosition, new ChessPosition(bishopPosition.getRow()-1, bishopPosition.getColumn()+1),null));
                break;
            } else if (board.chessBoard[bishopPosition.getRow()-1][bishopPosition.getColumn()+1].pieceColor == myColor) {
                break;
            }

            bishopPosition = new ChessPosition(bishopPosition.getRow()-1, bishopPosition.getColumn()+1);

        }
        bishopPosition = myPosition;

        //downleft
        while (bishopPosition.getRow()-1 >= 1 && bishopPosition.getColumn()-1 >= 1){
            if(board.chessBoard[bishopPosition.getRow()-1][bishopPosition.getColumn()-1] == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(bishopPosition.getRow()-1, bishopPosition.getColumn()-1),null));
                //BishopMove(board, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1));
            }else if (board.chessBoard[bishopPosition.getRow()-1][bishopPosition.getColumn()-1].pieceColor != myColor){
                moves.add(new ChessMove(myPosition, new ChessPosition(bishopPosition.getRow()-1, bishopPosition.getColumn()-1),null));
                break;
            } else if (board.chessBoard[bishopPosition.getRow()-1][bishopPosition.getColumn()-1].pieceColor == myColor) {
                break;
            }

            bishopPosition = new ChessPosition(bishopPosition.getRow()-1, bishopPosition.getColumn()-1);

        }

        return moves;
    }

    private Collection<ChessMove> RookMove(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new HashSet<ChessMove>();
        ChessGame.TeamColor myColor = board.chessBoard[myPosition.getRow()][myPosition.getColumn()].pieceColor;
        ChessPosition rookPosition = myPosition;

        //up
        while (rookPosition.getRow()+1 <= 8){
            if(board.chessBoard[rookPosition.getRow()+1][rookPosition.getColumn()] == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(rookPosition.getRow()+1, rookPosition.getColumn()),null));
                //BishopMove(board, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1));
            }else if (board.chessBoard[rookPosition.getRow()+1][rookPosition.getColumn()].pieceColor != myColor){
                moves.add(new ChessMove(myPosition, new ChessPosition(rookPosition.getRow()+1, rookPosition.getColumn()),null));
                break;
            } else if (board.chessBoard[rookPosition.getRow()+1][rookPosition.getColumn()].pieceColor == myColor) {
                break;

            }
            rookPosition = new ChessPosition(rookPosition.getRow()+1, rookPosition.getColumn());

        }
        rookPosition = myPosition;
        //right
        while (rookPosition.getColumn()+ 1 <= 8){
            if(board.chessBoard[rookPosition.getRow()][rookPosition.getColumn()+1] == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(rookPosition.getRow(), rookPosition.getColumn()+1),null));
                //BishopMove(board, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1));
            }else if (board.chessBoard[rookPosition.getRow()][rookPosition.getColumn()+1].pieceColor != myColor){
                moves.add(new ChessMove(myPosition, new ChessPosition(rookPosition.getRow(), rookPosition.getColumn()+1),null));
                break;
            } else if (board.chessBoard[rookPosition.getRow()][rookPosition.getColumn()+1].pieceColor == myColor) {
                break;
            }
            rookPosition = new ChessPosition(rookPosition.getRow(), rookPosition.getColumn()+1);

        }
        rookPosition = myPosition;

        //left
        while (rookPosition.getColumn()-1 >= 1){
            if(board.chessBoard[rookPosition.getRow()][rookPosition.getColumn()-1] == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(rookPosition.getRow(), rookPosition.getColumn()-1),null));
                //BishopMove(board, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1));
            }else if (board.chessBoard[rookPosition.getRow()][rookPosition.getColumn()-1].pieceColor != myColor){
                moves.add(new ChessMove(myPosition, new ChessPosition(rookPosition.getRow(), rookPosition.getColumn()-1),null));
                break;
            } else if (board.chessBoard[rookPosition.getRow()][rookPosition.getColumn()-1].pieceColor == myColor) {
                break;
            }

            rookPosition = new ChessPosition(rookPosition.getRow(), rookPosition.getColumn()-1);

        }
        rookPosition = myPosition;

        //down
        while (rookPosition.getRow()-1 >= 1){
            if(board.chessBoard[rookPosition.getRow()-1][rookPosition.getColumn()] == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(rookPosition.getRow()-1, rookPosition.getColumn()),null));
                //BishopMove(board, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1));
            }else if (board.chessBoard[rookPosition.getRow()-1][rookPosition.getColumn()].pieceColor != myColor){
                moves.add(new ChessMove(myPosition, new ChessPosition(rookPosition.getRow()-1, rookPosition.getColumn()),null));
                break;
            } else if (board.chessBoard[rookPosition.getRow()-1][rookPosition.getColumn()].pieceColor == myColor) {
                break;
            }

            rookPosition = new ChessPosition(rookPosition.getRow()-1, rookPosition.getColumn());

        }

        return moves;
    }

    private Collection<ChessMove> QueenMove(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new HashSet<ChessMove>();
        ChessGame.TeamColor myColor = board.chessBoard[myPosition.getRow()][myPosition.getColumn()].pieceColor;
        ChessPosition queenPosition = myPosition;

        //up
        while (queenPosition.getRow()+1 <= 8){
            if(board.chessBoard[queenPosition.getRow()+1][queenPosition.getColumn()] == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(queenPosition.getRow()+1, queenPosition.getColumn()),null));
                //BishopMove(board, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1));
            }else if (board.chessBoard[queenPosition.getRow()+1][queenPosition.getColumn()].pieceColor != myColor){
                moves.add(new ChessMove(myPosition, new ChessPosition(queenPosition.getRow()+1, queenPosition.getColumn()),null));
                break;
            } else if (board.chessBoard[queenPosition.getRow()+1][queenPosition.getColumn()].pieceColor == myColor) {
                break;

            }
            queenPosition = new ChessPosition(queenPosition.getRow()+1, queenPosition.getColumn());

        }
        queenPosition = myPosition;
        //right
        while (queenPosition.getColumn()+ 1 <= 8){
            if(board.chessBoard[queenPosition.getRow()][queenPosition.getColumn()+1] == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(queenPosition.getRow(), queenPosition.getColumn()+1),null));
                //BishopMove(board, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1));
            }else if (board.chessBoard[queenPosition.getRow()][queenPosition.getColumn()+1].pieceColor != myColor){
                moves.add(new ChessMove(myPosition, new ChessPosition(queenPosition.getRow(), queenPosition.getColumn()+1),null));
                break;
            } else if (board.chessBoard[queenPosition.getRow()][queenPosition.getColumn()+1].pieceColor == myColor) {
                break;
            }
            queenPosition = new ChessPosition(queenPosition.getRow(), queenPosition.getColumn()+1);

        }
        queenPosition = myPosition;

        //left
        while (queenPosition.getColumn()-1 >= 1){
            if(board.chessBoard[queenPosition.getRow()][queenPosition.getColumn()-1] == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(queenPosition.getRow(), queenPosition.getColumn()-1),null));
                //BishopMove(board, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1));
            }else if (board.chessBoard[queenPosition.getRow()][queenPosition.getColumn()-1].pieceColor != myColor){
                moves.add(new ChessMove(myPosition, new ChessPosition(queenPosition.getRow(), queenPosition.getColumn()-1),null));
                break;
            } else if (board.chessBoard[queenPosition.getRow()][queenPosition.getColumn()-1].pieceColor == myColor) {
                break;
            }

            queenPosition = new ChessPosition(queenPosition.getRow(), queenPosition.getColumn()-1);

        }
        queenPosition = myPosition;

        //down
        while (queenPosition.getRow()-1 >= 1){
            if(board.chessBoard[queenPosition.getRow()-1][queenPosition.getColumn()] == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(queenPosition.getRow()-1, queenPosition.getColumn()),null));
                //BishopMove(board, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1));
            }else if (board.chessBoard[queenPosition.getRow()-1][queenPosition.getColumn()].pieceColor != myColor){
                moves.add(new ChessMove(myPosition, new ChessPosition(queenPosition.getRow()-1, queenPosition.getColumn()),null));
                break;
            } else if (board.chessBoard[queenPosition.getRow()-1][queenPosition.getColumn()].pieceColor == myColor) {
                break;
            }

            queenPosition = new ChessPosition(queenPosition.getRow()-1, queenPosition.getColumn());

        }
        queenPosition = myPosition;

        //upright
        while (queenPosition.getRow()+1 <= 8 && queenPosition.getColumn()+1 <= 8){
            if(board.chessBoard[queenPosition.getRow()+1][queenPosition.getColumn()+1] == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(queenPosition.getRow()+1, queenPosition.getColumn()+1),null));
                //BishopMove(board, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1));
            }else if (board.chessBoard[queenPosition.getRow()+1][queenPosition.getColumn()+1].pieceColor != myColor){
                moves.add(new ChessMove(myPosition, new ChessPosition(queenPosition.getRow()+1, queenPosition.getColumn()+1),null));
                break;
            } else if (board.chessBoard[queenPosition.getRow()+1][queenPosition.getColumn()+1].pieceColor == myColor) {
                break;

            }
            queenPosition = new ChessPosition(queenPosition.getRow()+1, queenPosition.getColumn()+1);

        }
        queenPosition = myPosition;
        //upleft
        while (queenPosition.getRow()+1 <= 8 && queenPosition.getColumn()-1 >= 1){
            if(board.chessBoard[queenPosition.getRow()+1][queenPosition.getColumn()-1] == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(queenPosition.getRow()+1, queenPosition.getColumn()-1),null));
                //BishopMove(board, new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1));
            }else if (board.chessBoard[queenPosition.getRow()+1][queenPosition.getColumn()-1].pieceColor != myColor){
                moves.add(new ChessMove(myPosition, new ChessPosition(queenPosition.getRow()+1, queenPosition.getColumn()-1),null));
                break;
            } else if (board.chessBoard[queenPosition.getRow()+1][queenPosition.getColumn()-1].pieceColor == myColor) {
                break;
            }
            queenPosition = new ChessPosition(queenPosition.getRow()+1, queenPosition.getColumn()-1);

        }
        queenPosition = myPosition;

        //downright
        while (queenPosition.getRow()-1 >= 1 && queenPosition.getColumn()+1 <= 8){
            if(board.chessBoard[queenPosition.getRow()-1][queenPosition.getColumn()+1] == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(queenPosition.getRow()-1, queenPosition.getColumn()+1),null));
                //BishopMove(board, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1));
            }else if (board.chessBoard[queenPosition.getRow()-1][queenPosition.getColumn()+1].pieceColor != myColor){
                moves.add(new ChessMove(myPosition, new ChessPosition(queenPosition.getRow()-1, queenPosition.getColumn()+1),null));
                break;
            } else if (board.chessBoard[queenPosition.getRow()-1][queenPosition.getColumn()+1].pieceColor == myColor) {
                break;
            }

            queenPosition = new ChessPosition(queenPosition.getRow()-1, queenPosition.getColumn()+1);

        }
        queenPosition = myPosition;

        //downleft
        while (queenPosition.getRow()-1 >= 1 && queenPosition.getColumn()-1 >= 1){
            if(board.chessBoard[queenPosition.getRow()-1][queenPosition.getColumn()-1] == null){
                moves.add(new ChessMove(myPosition, new ChessPosition(queenPosition.getRow()-1, queenPosition.getColumn()-1),null));
                //BishopMove(board, new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1));
            }else if (board.chessBoard[queenPosition.getRow()-1][queenPosition.getColumn()-1].pieceColor != myColor){
                moves.add(new ChessMove(myPosition, new ChessPosition(queenPosition.getRow()-1, queenPosition.getColumn()-1),null));
                break;
            } else if (board.chessBoard[queenPosition.getRow()-1][queenPosition.getColumn()-1].pieceColor == myColor) {
                break;
            }

            queenPosition = new ChessPosition(queenPosition.getRow()-1, queenPosition.getColumn()-1);

        }

        return moves;
    }

    private Collection<ChessMove> PawnMove(ChessBoard board, ChessPosition myPosition)  {
        Collection<ChessMove> moves = new HashSet<ChessMove>();
        ChessGame.TeamColor myColor = board.chessBoard[myPosition.getRow()][myPosition.getColumn()].pieceColor;
        if(myColor == ChessGame.TeamColor.WHITE) {
            //intial move bonus
            if (myPosition.getRow()  == 2 && board.chessBoard[myPosition.getRow() + 1][myPosition.getColumn()] == null){
                if ( board.chessBoard[myPosition.getRow() + 2][myPosition.getColumn()] == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() +2, myPosition.getColumn()), null));
                }

            }
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
            //intial move bonus
            if (myPosition.getRow()  == 7 && board.chessBoard[myPosition.getRow() - 1][myPosition.getColumn()] == null){
                if ( board.chessBoard[myPosition.getRow() - 2][myPosition.getColumn()] == null){
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn()), null));
                }
            }
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
