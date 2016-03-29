/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gomoku;

/**
 *
 * @author zhongjiezheng
 */
public class ConnectingStones extends AbstractScoring implements Scoring {
	
    private int value = 0;

    @Override
    public boolean isPresent(Move move, GomokuState state, int player) {
        
    	/*if (player == GomokuState.BLACK) {
    		player = GomokuState.WHITE;
    	}
    	else {
    		player = GomokuState.BLACK;
    	}*/
    	
    	boolean result = false;
        
        //streak of 4
    	if (super.nConnectHorizontal(4, state, move, player)) {
            int row[] = getCompleteRow(state, move.row);

            if (testIfSpace(row, move.col, player)) {
                result = true;
                value += ScoreValue.VERYHIGH;
            }
          //  System.out.println("STREAK OF 4 HORIZONTAL");
        }

        if (super.nConnectDiagonal(4, state, move, player)) {
            int RLDiagonal[] = getRightLeftDiagonal(state, move);
            int LRDiagonal[] = getLeftRightDiagonal(state, move);

            if (testIfSpace(RLDiagonal, move.col, player)
                    || testIfSpace(LRDiagonal, move.col, player)) {
                result = true;
                value += ScoreValue.VERYHIGH;
            }
          //  System.out.println("STREAK OF 4 DIAGONAL");
        }

        if (super.nConnectVertical(4, state, move, player)) {
            int column[] = getCompleteColumn(state, move.col);

            if (testIfSpace(column, move.row, player)) {
                result = true;
                value += ScoreValue.VERYHIGH;
            }
          //  System.out.println("STREAK OF 4 VERTICAL");
        }
    	
        //streak of 3
        if (super.nConnectHorizontal(3, state, move, player)) {
            int row[] = getCompleteRow(state, move.row);

            if (testIfSpace(row, move.col, player)) {
                result = true;
                value += ScoreValue.MEDIUM;
            }
          //  System.out.println("STREAK OF 3 HORIZONTAL");
        }

        if (super.nConnectDiagonal(3, state, move, player)) {
            int RLDiagonal[] = getRightLeftDiagonal(state, move);
            int LRDiagonal[] = getLeftRightDiagonal(state, move);

            if (testIfSpace(RLDiagonal, move.col, player)
                    || testIfSpace(LRDiagonal, move.col, player)) {
                result = true;
                value += ScoreValue.MEDIUM;
            }
          //  System.out.println("STREAK OF 3 DIAGONAL");
        }

        if (super.nConnectVertical(3, state, move, player)) {
            int column[] = getCompleteColumn(state, move.col);

            if (testIfSpace(column, move.row, player)) {
                result = true;
                value += ScoreValue.MEDIUM;
            }
          //  System.out.println("STREAK OF 3 VERTICAL");
        }
    	
        //streak of 2
        if (super.nConnectHorizontal(2, state, move, player)) {
            int row[] = getCompleteRow(state, move.row);
            
            if (testIfSpace(row, move.col, player)) {
                result = true;
                value += ScoreValue.MEDIUM;
            }
            //System.out.println("STREAK OF 2 HORIZONTAL");
        }

        if (super.nConnectDiagonal(2, state, move, player)) {
            int RLDiagonal[] = getRightLeftDiagonal(state, move);
            int LRDiagonal[] = getLeftRightDiagonal(state, move);

            if (testIfSpace(RLDiagonal, move.col, player)
                    || testIfSpace(LRDiagonal, move.col, player)) {
                result = true;
                value += ScoreValue.MEDIUM;
            }
            //System.out.println("STREAK OF 2 DIAGONAL");
        }

        if (super.nConnectVertical(2, state, move, player)) {
            int column[] = getCompleteColumn(state, move.col);

            if (testIfSpace(column, move.row, player)) {
                result = true;
                value += ScoreValue.MEDIUM;
            }
            //System.out.println("STREAK OF 2 VERTICAL");
        }
        /*
        //streak of 3
        if (super.nConnectHorizontal(3, state, move, player)) {
            int row[] = getCompleteRow(state, move.row);
            if (testIfSpace(row, move.col, player)) {
                result = true;
                value += ScoreValue.MEDIUM;
            }
            System.out.println("STREAK OF 3 HORIZONTAL");
        }
        if (super.nConnectDiagonal(3, state, move, player)) {
            int RLDiagonal[] = getRightLeftDiagonal(state, move);
            int LRDiagonal[] = getLeftRightDiagonal(state, move);
            if (testIfSpace(RLDiagonal, move.col, player)
                    || testIfSpace(LRDiagonal, move.col, player)) {
                result = true;
                value += ScoreValue.MEDIUM;
            }
            System.out.println("STREAK OF 3 DIAGONAL");
        }
        if (super.nConnectVertical(3, state, move, player)) {
            int column[] = getCompleteColumn(state, move.col);
            if (testIfSpace(column, move.row, player)) {
                result = true;
                value += ScoreValue.MEDIUM;
            }
            System.out.println("STREAK OF 3 VERTICAL");
        }
        
        //streak of 4
        if (super.nConnectHorizontal(4, state, move, player)) {
            int row[] = getCompleteRow(state, move.row);
            if (testIfSpace(row, move.col, player)) {
                result = true;
                value += ScoreValue.HIGH;
            }
            System.out.println("STREAK OF 4 HORIZONTAL");
        }
        if (super.nConnectDiagonal(4, state, move, player)) {
            int RLDiagonal[] = getRightLeftDiagonal(state, move);
            int LRDiagonal[] = getLeftRightDiagonal(state, move);
            if (testIfSpace(RLDiagonal, move.col, player)
                    || testIfSpace(LRDiagonal, move.col, player)) {
                result = true;
                value += ScoreValue.HIGH;
            }
            System.out.println("STREAK OF 4 DIAGONAL");
        }
        if (super.nConnectVertical(4, state, move, player)) {
            int column[] = getCompleteColumn(state, move.col);
            if (testIfSpace(column, move.row, player)) {
                result = true;
                value += ScoreValue.HIGH;
            }
            System.out.println("STREAK OF 4 VERTICAL");
        }*/
        return result;
    }

    /**
     * test if enough space is in the given array to win in future.
     * @param array the array to test on
     * @param move the player move
     * @param color the player color
     * @return true if enough space, else false
     */
    private boolean testIfSpace(int[] array, int loc, int player) {

        int spaceRight = space(loc,1,array,player);
        int spaceLeft = space(loc,-1,array,player);

        return spaceRight > 4 || spaceLeft > 4 || (spaceLeft == 1 && spaceRight > 3) || (spaceRight == 1 && spaceLeft > 3) || 
        		(spaceLeft == 2 && spaceRight > 2) || (spaceRight == 2 && spaceLeft > 2);
    }

    private int space(int loc, int dir, int[] array, int player) {
    	int space = 0;
    	int i = loc + dir;
    	while (i > 0 && i < array.length) {
    		if (array[i] == GomokuState.NONE || array[i] == player) {
    			space++;
    		} else {
    			break;
    		}
    		i += dir;
    	}
    	return space;
    }
    
    /**
     * checks if in the given array from the startingpoint to the number of wide
     * are only the given token or empty token.
     * @param startingPoint the point to start checking from
     * @param wide the wide to check to
     * @param array the array to check
     * @param color the player color
     * @return true if only player token or empty token is in the interval
    private boolean isSpace(int startingPoint, int wide, int dir, int[] array, int player) {
        int space = 0;
        int i = 0;
        while(i < wide) {
            try {
                if (array[i] == GomokuState.NONE || array[i] == player) {
                    space++;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                return false;
            }
            startingPoint += dir;
        }
        for (int i = startingPoint; i < startingPoint + wide; i++) {
            try {
                if (array[i] == GomokuState.NONE || array[i] == player) {
                    space++;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                return false;
            }
        }
        return wide == space;
    }*/

    /**
     * returns a complete row of the given model as an array.
     * @param model the model to get the row from
     * @param currentRow the wished row
     * @return an array of <code> Token</code> from the row
     */
    private int[] getCompleteRow(GomokuState state, int currentRow) {
        int row[] = new int[19];
        for (int i = 0; i < 19; i++) {
            row[i] = state.getPiece(new Move(currentRow,i));
        }
        return row;
    }

    /**
     * returns a complete column of the given model as an array.
     * @param model the model to get the row from
     * @param currentColumn the wished column
     * @return an array of <code> Token</code> from the column
     */
    private int[] getCompleteColumn(GomokuState state, int currentCol) {
        int column[] = new int[19];
        for (int i = 0; i < 19; i++) {
            column[i] = state.getPiece(new Move(i,currentCol));
        }
        return column;
    }

    /**
     * returns an array of the diagonl, which goes from
     * upper left to lower right
     * @param model the model to geht the diagonal from
     * @param currentColumn the wished colum
     * @param currentRow the wished row
     * @return an array of <code>Token</code>
     */
//    private int[] getLeftRightDiagonal(GomokuState state, Move current) {
//        int diagonal[] = new int[19];
//        for (int i = 0; i <= 9; i++) {
//            int scanningColumn = current.col - i;
//            int scanningRow = current.row - i;
//            if (scanningColumn < 0 || scanningRow < 0) {
//                diagonal[i] = GomokuState.NONE;
//            } else {
//                diagonal[i] = state.getPiece(new Move(scanningRow,scanningColumn));
//
//            }
//        }
//
//        for (int i = 0; i <= 9; i++) {
//            int scanningColumn = current.col + i;
//            int scanningRow = current.row + i;
//            if (scanningColumn > state.getLength() - 1
//                    || scanningRow > state.getLength() - 1) {
//                diagonal[i + 3] = GomokuState.NONE;
//            } else {
//                diagonal[i + 3] = state.getPiece(new Move(scanningRow, scanningColumn));
//
//            }
//        }
//        return diagonal;
//    }

    private int[] getLeftRightDiagonal(GomokuState state, Move current) {
        int diagonal[] = new int[19];
        int startRow, startCol;
        
        startRow = current.row - current.col;
        startCol=0;
        
        for(int i = 0; i<19; i++){
            int scanningColumn = startCol + i;
            int scanningRow = startRow + i;
            
            diagonal[i] = state.getPiece(new Move(scanningRow,scanningColumn));
        } 
        return diagonal;
    }
    /**
     * returns an array of the diagonl, which goes from
     * upper right to lower left
     * @param model the model to geht the diagonal from
     * @param currentColumn the wished colum
     * @param currentRow the wished row
     * @return an array of <code>Token</code>
     */
//    private int[] getRightLeftDiagonal(GomokuState state, Move current) {
//        int diagonal[] = new int[19];
//        for (int i = 0; i <= 9; i++) {
//            int scanningColumn = current.col + i;
//            int scanningRow = current.row - i;
//            if (scanningColumn < 0 || scanningRow < 0) {
//                diagonal[i] = GomokuState.NONE;
//            } else {
//                diagonal[i] = state.getPiece(new Move(scanningColumn, scanningRow));
//
//            }
//        }
//        for (int i = 0; i <= 9; i++) {
//            int scanningColumn = current.col - i;
//            int scanningRow = current.row + i;
//            if (scanningColumn > state.getLength() - 1
//                    || scanningRow > state.getLength() - 1) {
//                diagonal[i + 3] = GomokuState.NONE;
//            } else {
//                diagonal[i + 3] = state.getPiece(new Move(scanningColumn, scanningRow));
//
//            }
//        }
//        return diagonal;
//    }

    private int[] getRightLeftDiagonal(GomokuState state, Move current) {
        int diagonal[] = new int[19];

        int startRow, startCol;
        
        startRow = current.row + current.col;
        startCol=0;
        
        for(int i = 0; i<19; i++){
            int scanningColumn = startCol + i;
            int scanningRow = startRow - i;
            
            diagonal[i] = state.getPiece(new Move(scanningRow,scanningColumn));
        } 
        
        
        return diagonal;
    }
    
    @Override
    public int getValue() {
        int returnValue = value;
        value = 0; // reset value
        return returnValue;
    }
}
