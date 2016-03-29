/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gomoku;

/**
 *
 * @author zhongjiezheng
 */
import java.util.List;

public class Minimax {
    
    public static final int MAXINT = Integer.MAX_VALUE / 2;
    public static final int MININT = Integer.MIN_VALUE / 2;
    
    private Tree tree;

    public Move getBestMove(GomokuState state, List<Scoring> strats, int owner) {
        tree = new Tree(state, strats, owner);
        // minimize or maximize each level
        
//        System.out.print("BEFORE MINIMAX");
//        System.out.println(tree.getLevel(1).get(0).getOwner());
//        for (int i = 0; i < tree.getLevel(1).size(); i++) {
//        	System.out.println(tree.getLevel(1).get(i).move.toString());
//        }
//        System.out.println("NOW EXECUTING MINIMAX");
        
        /*for (int i = Tree.MAXDEPTH-1; i > 0; i--) {
            List<Node> curLevel = tree.getLevel(i);
            if (!curLevel.isEmpty()) {
                if (curLevel.get(0).getOwner() == owner) {
                    for (Node node : curLevel) {
                        minimize(node); //SWITCHED MAX AND MIN
                    }  
                } else {
                    for (Node node : curLevel) {
                        maximize(node);
                    }
                }
            }
        }*/
        
        // debug
//        System.out.println(tree.getLevel(1).get(0).getOwner());
//        for (int i = 0; i < tree.getLevel(1).size(); i++) {
//        	System.out.println(tree.getLevel(1).get(i).move.toString());
//        }
        //System.out.println(tree.getLevel(2).get(0).getOwner());
        /*for (int i = 0; i < tree.getLevel(1).get(0).getChildren().size(); i++) {
        	System.out.println(tree.getLevel(2).get(i).move.toString());
        }*/
        
        int minNode = 0;
        Move minMove = tree.getLevel(1).get(0).move;
        for (int i = 0; i < tree.getLevel(1).size(); i++) {
            if (tree.getLevel(1).get(i).move.score > minMove.score) { //CHANGED TO MAXIMUM
                minMove = tree.getLevel(1).get(i).move;
                minNode = i;
            }
        }
        
//        System.out.println("CHILDREN");
//        for (int i = 0; i < tree.getLevel(1).get(minNode).getChildren().size(); i++) {
//        	System.out.println(tree.getLevel(1).get(minNode).getChildren().get(i).move.toString());
//    	}
        
        return minMove;
        //return tree.getLevel(0).get(0).move;
    }
    
    private void maximize(Node node) {
        int max = MININT;
        List<Node> children = node.getChildren();
        
        for (Node child : children) {
            if (child.move.score > max) {
                max = child.move.score;
            }
        }
        node.move.setScore(max);
    }
    
    private void minimize(Node node) {
        int min = MAXINT;
        List<Node> children = node.getChildren();
        
        for (Node child : children) {
            if (child.move.score < min) {
                min = child.move.score;
            }
        }
        node.move.setScore(min);
    }
    
}
