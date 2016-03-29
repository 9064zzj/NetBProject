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
import connect4.AI.Model.PossibleMove;
import manager.interfaces.Token;
import manager.interfaces.ModelInterface;*?
/**
 * An interface describing a rating mechanism.
 * A move may be tested by a rating mechanism, as to whether or not it
 * satisfies the rule defined by the concrete mechanism.
 * In addition, it holds a value which represents the value of the rule
 * the mechanism contains.
 * <p>
 * The rating mechanism itself should NOT change the value of the move,
 * nor should it change the ModelInterface.
 */
public interface Scoring {

    /**
     * returns whether or not the given move satisfies this mechanism.
     *
     * @param move the move to be tested
     * @param model model
     * @param color the color of the ki
     *
     * @return
     */
    boolean isPresent(final Move move, final GomokuState state, int player);

    /**
     * returns the value of this mechanism.
     *
     * @return value of this mechanism
     * @see Rating
     */
    int getValue();
}
