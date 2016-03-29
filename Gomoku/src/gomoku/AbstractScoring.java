/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gomoku;

/**
 *
 * @author zhongjiezheng
 */
/*package Connect4.AI.Controller;
import manager.interfaces.Token;
import manager.interfaces.ModelInterface;
import connect4.AI.Model.PossibleMove;*/

/**
 * This class contains general methods to check for the state of a field used
 * by more than one rating mechanism.
 * For example, it may check, whether or not a move leads to a given number
 * of stones in a row.
 */
public abstract class AbstractScoring{

    /**
     * checks whether or not there are n connecting stones.
     * If n is smaller than 2 or bigger than 4, false will be returned.
     * If the given move is not possible because the column is full, false
     * will be returned as well.
     *
     * @param n number of connecting stones
     * @param field the field to count on
     * @param move move to apply to field before checking (move will not be applied permanently)
     * @param color the color of the stones to check for
     * @param what determines if the method checks diagonally, vertically or horizontally
     * @return true, if n connecting stones are found, else false
     */
    private boolean nConnected(int n, GomokuState state, Move move, int color, BoardState bs) {
        if (n < 2 || n > 5) { //CHANGED TO 5
            return false;
        }

        if (!(state.isMoveAllowed(move))) {
            return false;
        }

        int countColor = 0;
        // check in the given row from n to the left to n to the right
        for (int i = -n; i <= n; i++) {          
	        if (bs.getBoardState(i, state, move.row, move.col) == color) {
	        	countColor++;
	        } 
	        else if (i == 0) {
	        	countColor++;
	        } 
	        else if (countColor != n) {
	        	countColor = 0;
	        }
	        // done
	        if (countColor == n) {
	        	break;
	        }
        }
        return countColor >= n;
    }

    /**
     * this method checks if there are n stones in a line.
     * In this case, in a line means that x stones may be extended
     * to 4 stones in a row (either at the end, or because there are 
     * still stones missing in the middle).
     * @param n number of connecting stones
     * @param holes the number of allowed holes
     * @param field the field to count on
     * @param move move to apply to field before checking (move will not be applied permanently)
     * @param color the color of the stones to check for
     * @param what determines if the method checks diagonally, vertically or horizontally
     * @return true, if n connecting stones are found, else false
     */
    private boolean nConnectedWithHoles(int n, int holes, GomokuState state, Move move, int player, BoardState bs) {
       
        if (n < 2 || n > 5) {
            return false;
        }

        if (holes < 0 || holes > 2) { //SHOULD I CHANGE THIS
            return false;
        }

        if (!(state.isMoveAllowed(move))) {
            return false;
        }
        
        int countHoles = 0;
        int countColor = 0;
        // check in the given row from n to the left to n to the right
        for (int i = -n; i <= n; i++) {
            
	        int currentColor = bs.getBoardState(i, state, move.row, move.col);
	
	        if (currentColor == player) {
	        	countColor++;
	        } 
	        else if (i == 0) {
	        	countColor++;
	        } 
	        else if (currentColor == GomokuState.NONE) {
	        	countHoles++;
	        } 
	        else if (countColor != n) {
	        	countColor = 0;
	        	countHoles = 0;
	        }
	        if (countHoles > holes) {
	        	countColor = 0;
	        	countHoles = 0;
	        }
	        // done
	        if (countColor + countHoles == n) {
	        	break;
	        }
        }
        return countColor + countHoles >= n;
    }

    /**
     * checks whether or not there are n stones in a horizontal row.
     * In these case a row means the n stones are vertical connected.
     * If n is smaller than 2 or bigger than 4, false will be returned.
     * If the given move is not possible because the column is full, false
     * will be returned as well.
     *
     * @param n number of connected stones in a row
     * @param field the field to check on
     * @param move move to apply to field before checking (move will not be applied permanently)
     * @param color the color of the stones to check for
     *
     * @return true if n stones are connected, else false
     */
    public boolean nConnectHorizontal(int n, GomokuState state, Move move, int player) {

        return nConnected(n, state, move, player, new BoardState() {

            @Override
            public int getBoardState(int counter, GomokuState state, int row, int col) {
                return state.getPiece(new Move(row,col+counter));
            }
        });


    }

    /**
     * checks whether or not there are n stones in a vertical row.
     * If n is smaller than 2 or bigger than 4, false will be returned.
     * If the given move is not possible because the column is full, false
     * will be returned as well.
     *
     * @param n number of stones in a column
     * @param field field the field to check on
     * @param move move to apply to field before checking (move will not be applied permanently)
     * @param color the color of the stones to check for
     *
     * @return true if n stones are connected, else false
     */
    public boolean nConnectVertical(int n, GomokuState state, Move move, int player) {

        return nConnected(n, state, move, player, new BoardState() {

            @Override
            public int getBoardState(int counter, GomokuState state, int row, int col) {
                return state.getPiece(new Move(row+counter,col));
            }
        });
    }

    /**
     * checks whether or not there are n stones in a diagonal row.
     * If n is smaller than 2 or bigger than 4, false will be returned.
     * If the given move is not possible because the column is full, false
     * will be returned as well.
     *
     * @param n number of stones in a diagonal
     * @param field the field to check on
     * @param move move to apply to field before checking (move will not be applied permanently)
     * @param color the color of the stones to check for
     *
     * @return true if n stones are connected, else false
     */
    public boolean nConnectDiagonal(int n, GomokuState state, Move move, int player) {

        BoardState lowerLeftToUpperRight = new BoardState() { //changed name

            @Override
            public int getBoardState(int counter, GomokuState state, int row, int col) {
                return state.getPiece(new Move(row+counter,col+counter));
                
            }
        };

        BoardState upperLeftToLowerRight = new BoardState() { //changed name

            @Override
            public int getBoardState(int counter, GomokuState state, int row, int col) {
                return state.getPiece(new Move(row-counter,col+counter));
            }
        };

        return nConnected(n, state, move, player, upperLeftToLowerRight)
                || nConnected(n, state, move, player, lowerLeftToUpperRight);
    }

    /** checks whether or not there are n stones in a vertical row.
    * If n is smaller than 2 or bigger than 4, false will be returned.
    * If the given move is not possible because the column is full, false
    * will be returned as well. The number of holes will be counted as well.
    * If this number is greater than 3 or lower than 0 false will be returned.
    *
    * @param n number of stones in a diagonal
    * @param holes the number of allowed holes in a line
    * @param field the field to check on
    * @param move move to apply to field before checking (move will not be applied permanently)
    * @param color the color of the stones to check for
    * @return true if n stones are connected, else false
    */
   public boolean nConncetedHorizontalWithHoles(int n, int holes, GomokuState state, Move move, int player) {
       return nConnectedWithHoles(n, holes, state, move, player, new BoardState() {

           @Override
           public int getBoardState(int counter, GomokuState state, int row, int col) {
               return state.getPiece(new Move(row,col+counter));
           }
       });
   }
    
    /** checks whether or not there are n stones in a vertical row.
    * If n is smaller than 2 or bigger than 4, false will be returned.
    * If the given move is not possible because the column is full, false
    * will be returned as well. The number of holes will be counted as well.
    * If this number is greater than 3 or lower than 0 false will be returned.
    *
    * @param n number of stones in a diagonal
    * @param holes the number of allowed holes in a line
    * @param field the field to check on
    * @param move move to apply to field before checking (move will not be applied permanently)
    * @param color the color of the stones to check for
    * @return true if n stones are connected, else false
    */
   public boolean nConncetedVerticalWithHoles(int n, int holes, GomokuState state, Move move, int player) {
       return nConnectedWithHoles(n, holes, state, move, player, new BoardState() {

           @Override
           public int getBoardState(int counter, GomokuState state, int row, int col) {
               return state.getPiece(new Move(row+counter,col));
           }
       });
   }
   
    /** checks whether or not there are n stones in a diagonal row.
     * If n is smaller than 2 or bigger than 4, false will be returned.
     * If the given move is not possible because the column is full, false
     * will be returned as well. The number of holes will be counted as well.
     * If this number is greater than 3 or lower than 0 false will be returned.
     *
     * @param n number of stones in a diagonal
     * @param holes the number of allowed holes in a line
     * @param field the field to check on
     * @param move move to apply to field before checking (move will not be applied permanently)
     * @param color the color of the stones to check for
     * @return true if n stones are connected, else false
     */
    public boolean nConnectDiagonalWithHoles(int n, int holes, GomokuState state, Move move, int player) {
        BoardState upperLeftToLowerRight = new BoardState() {

            @Override
            public int getBoardState(int counter, GomokuState state, int row, int col) {
                return state.getPiece(new Move(row+counter,col+counter));
            }
        };

        BoardState lowerLeftToUpperRight = new BoardState() {

            @Override
            public int getBoardState(int counter, GomokuState state, int row, int col) {
                return state.getPiece(new Move(row-counter,col+counter));
            }
        };

        return nConnectedWithHoles(n, holes, state, move, player, upperLeftToLowerRight)
                || nConnectedWithHoles(n, holes, state, move, player, lowerLeftToUpperRight);
    }

    /**
     * This interface is used to avoid copy-pasting for the methods above.
     */
    private interface BoardState {

        /**
         * returns the relevant field status depending on the currently passed
         * loopCounter as well as the placed row and column.
         *
         * @param loopCounter
         * @param field
         * @return
         */
        int getBoardState(int counter, GomokuState state, int row, int column);
    }
}
