/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gomoku;

/**
 *
 * @author zhongjiezheng
 */
import java.util.ArrayList;
import java.util.List;

public class Tree {
    
    public static final int MAXDEPTH = 1;
    
    private Node root; //will contain best move
    private List<List<Node>> levels; //levels of tree, each level represents a turn
    //private int player; //color of current turn
    private List<Scoring> strats; //score of player
    
    //FOR TESTING
    /*public static void main(String[] args) {
        GomokuState test = new GomokuState(19);
        test.playPiece(new Move(4,3),GomokuState.BLACK);
        System.out.println(test.toString());
        test.playPiece(new Move(9,9),GomokuState.BLACK);
        //new Tree(test,GomokuState.BLACK,0);
    }*/
    
    public Tree(GomokuState state, List<Scoring> strats, int player) {
        this.strats = strats;
    	//this.player = player;
        root = new Node(state.getLastMove(),state);
        root.setOwner(player);
        root.switchOwner();  //first level of moves should be for ki
        levels = new ArrayList<List<Node>>();
        for (int i = 0; i <= MAXDEPTH; i++) {
            levels.add(new ArrayList<Node>());
        }
        getLevel(0).add(root);
        buildTree(MAXDEPTH);
    }
    
    public List<Node> getLevel(int level) {
        return levels.get(level);
    }
    
    public void buildTree(int depth) {
        for (int i = 0; i < depth; i++) {
            List<Node> curLevel = getLevel(i);
            List<Node> nextLevel = getLevel(i+1);
            for (Node node : curLevel)
                nextLevel.addAll(node.generateChildren(strats)); //player
        }
    }
}
