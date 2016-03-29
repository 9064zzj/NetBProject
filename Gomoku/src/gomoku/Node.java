/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gomoku;

/**
 *
 * @author zhongjiezheng
 */
import java.util.*;

public class Node {
    
    private GomokuState state;
    private List<Node> children;
    private Node parent;
    private int owner; //owner of move
    public Move move; //possible move
    public boolean isLeaf;
    
    public Node(Move move, GomokuState state) {
        this.state = state;
        children = new ArrayList<Node>();
        this.move = move;
    }
    
    /* layer owns this node, all children get opposite player
     * move will be rated with scoring algorithm
     */
    public ArrayList<Node> generateChildren(List<Scoring> strats) { //int player
        ArrayList<Node> children = new ArrayList<Node>();
        if(!isLeaf) {
	        for (int y = 0; y < state.getLength(); y++) {
	            for (int x = 0; x < state.getLength(); x++) {
	                if ((state.getBoard()[y][x] == GomokuState.NONE) && checkSurrounding(y,x)) {
	                    GomokuState temp = cloneBoard(state);
	                    Node newChild = new Node(new Move(y,x),temp);
	                    newChild.setParent(this);
	                    newChild.setOwner(owner);
	                    newChild.switchOwner();
	                    newChild.calcScore(strats); // player
	                    newChild.executeMove();
	                    children.add(newChild);
	                }
	            }
	        }
        }
        this.children = children;
        return children;
    }
    
    public void calcScore(List<Scoring> strats) { //int player
        //System.out.println("CHECKING IF PRESENT FOR " + (owner == GomokuState.BLACK ? "BLACK" : "WHITE"));
    	for (Scoring strat : strats) {
            if (strat instanceof Win && strat.isPresent(move, state, owner)) { //switched owner to player
                move.addScore(strat.getValue());
                setLeaf();
                return;
            }

            if (strat.isPresent(move, state, owner)) { //switched owner to player
                move.addScore(strat.getValue());
            }
        }
    }
    
    public GomokuState cloneBoard(GomokuState state) {
        GomokuState clone = new GomokuState(19);
        for (int i = 0; i < state.getLength(); i++) {
            for (int j = 0; j < state.getLength(); j++) {
                clone.getBoard()[i][j] = state.getBoard()[i][j];
            }
        }
        return clone;
    }
    
    public boolean checkSurrounding(int row, int col) {      
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int r = row + i;
                int c = col + j;
                if (!(i == 0 && j == 0) && r >= 0 && r < state.getLength() && c >= 0 && c < state.getLength() 
                        && (state.getBoard()[r][c] != GomokuState.NONE)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void executeMove() {
        state.playPiece(move,owner);
    }
    
    public void setParent(Node parent) {
        this.parent = parent;
    }
    
    public void setOwner(int owner) {
        this.owner = owner;
    }
    
    public void setLeaf() {
    	isLeaf = true;
    }
    
    public void switchOwner() {
        if (owner == GomokuState.BLACK) {
            owner = GomokuState.WHITE;
        } else {
            owner = GomokuState.BLACK;
        }
    }
    
    public boolean isLeaf() {
    	return isLeaf;
    }
    
    public GomokuState getState() {
        return state;
    }
    
    public List<Node> getChildren() {
        return children;
    }
    
    public Node getParent() {
        return parent;
    }
    
    public int getOwner() {
        return owner;
    }
}
