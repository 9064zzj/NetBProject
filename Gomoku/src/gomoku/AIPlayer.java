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
import java.util.ArrayList;

public class AIPlayer {
	
	private int player;
	private List<Scoring> strats;
	
	public AIPlayer(int player) {
		this.player = player;
		this.strats = new ArrayList<Scoring>();
	}
    public void deleteAllRatingMechanism(){
        strats.clear();
    }

    public int getColor() {
        return player;
    }

    public void addStrat(Scoring strat) {
        strats.add(strat);
    }

    public Move makeMove(GomokuState state) {
        //resetAllRatings();
        Minimax mm = new Minimax();
        return mm.getBestMove(state, strats, player);
    }
}
