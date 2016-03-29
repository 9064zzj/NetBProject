/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gomoku;

/**
 *
 * @author zhongjiezheng
 */
public class Move {
    
    public int row; //row
    public int col; //col
    public int score;
    
    public Move(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    public Move(int row, int col, int score) {
        this.row = row;
        this.col = col;
        this.score = score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public void resetScore() {
    	score = ScoreValue.ZERO;
    }
    
    public void addScore(int s) {
    	score += s;
    }
    
    public String toString() {
    	return "row = " + row + ", col = " + col + ", score = " + score;
    }
}
