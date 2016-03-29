/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gomoku;

/**
 *
 * @author zhongjiezheng
 */
public class Win extends AbstractScoring implements Scoring {

    @Override
    public int getValue() {
        return ScoreValue.WIN;
    }

    @Override
    public boolean isPresent(final Move move, final GomokuState state, int player) {
        return super.nConnectHorizontal(5, state, move, player) ||
                super.nConnectVertical(5, state, move, player)||
                super.nConnectDiagonal(5, state, move, player);
    }
}
