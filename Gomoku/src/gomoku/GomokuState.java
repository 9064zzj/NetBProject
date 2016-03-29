/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gomoku;

/**
 *
 * @author zhongjiezheng
 */
public class GomokuState {
    
    public static final int NONE = 0;
    public static final int WHITE = 1;
    public static final int BLACK = 2;
    public static final int OVERFLOW = 100;
    
    private Move lastMove;
    private int length;
    private int[][] board;
    
    public GomokuState(int size) {
        length = size;
        board = new int[size][size];
        lastMove = new Move(0,0);
    }
    
    public GomokuState(GomokuState s) {
        this.lastMove = s.getLastMove();
        this.length = s.getLength();
        this.board = s.getBoard();
    }
    
    public int[][] getBoard() {
        return board;
    }
    
    public int getLength() {
        return length;
    }
    
    public int getPiece(Move move) {
    	if (inBounds(move)) {
    		return board[move.row][move.col];
    	}
        return OVERFLOW;
    }
    
    public Move getLastMove() {
        return lastMove;
    }
    
    public boolean isMoveAllowed(Move move) {
    	if (board[move.row][move.col] == NONE) {
    		return true;
    	}
    	return false;
    }
    
    public boolean inBounds(Move move) {
    	if (move.row > 18 || move.row < 0 || move.col > 18 || move.col < 0) {
    		return false;
    	}
    	return true;
    }
    
    public boolean playPiece(Move move, int player) {
        if (board[move.row][move.col] != NONE) {
            return false;
        } else {
            board[move.row][move.col] = player;
            lastMove = move;
            return true;
        }
    }
    
    public int count(int color, Move move, int dirY, int dirX){
        int numInRow = 1; 
        int r;  //row is y coordinate
        int c;  //col is x coordinate   
        
        r = move.row + dirY; 
        c = move.col + dirX;
        while (r >= 0 && r < length && c >= 0 && c < length && board[r][c] == color) {
            numInRow++;
            r += dirY;
            c += dirX;
        }
        
        r = move.row - dirY;  
        c = move.col - dirX;
        while (r >= 0 && r < length && c >= 0 && c < length && board[r][c] == color) {
            numInRow++;
            r -= dirY;   
            c -= dirX;
        }
        
        return numInRow;
    }
    
    //check for 5 in a row from the last piece played
    public int getWinner() {
        
        int row = lastMove.row;
        int col = lastMove.col;
        
        if (count(board[row][col], lastMove, 1, 0) >= 5)
            return board[row][col];
        if (count(board[row][col], lastMove, 0, 1) >= 5)
            return board[row][col];
        if (count(board[row][col], lastMove, 1, -1) >= 5)
            return board[row][col];
        if (count(board[row][col], lastMove, 1, 1) >= 5)
            return board[row][col];
        
        return NONE;
    }
    
    public String toString() {
        String s = "";
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                s = s + board[i][j] + " ";
            }
            s = s + "\n";
        }
        return s;
    }
}
