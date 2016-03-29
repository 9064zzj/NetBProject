/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gomoku;

/**
 *
 * @author zhongjiezheng
 */
public class BlockWin extends AbstractScoring implements Scoring {

    @Override
    public int getValue() {
        return ScoreValue.BLOCK;
    }   

    @Override
    public boolean isPresent(final Move move, final GomokuState state, int player) {
        int opponent = GomokuState.NONE;
        if (player == GomokuState.BLACK) {
            opponent = GomokuState.WHITE;
        } else {
            opponent = GomokuState.BLACK;
        }
        
        //CHANGED ALL VALUES TO 5
        return super.nConnectHorizontal(4, state, move, opponent) ||
                super.nConnectVertical(4, state, move, opponent) ||
                super.nConnectDiagonal(4, state, move, opponent) ||
        		super.nConnectHorizontal(5, state, move, opponent) ||
                super.nConnectVertical(5, state, move, opponent) ||
                super.nConnectDiagonal(5, state, move, opponent);
    }
}
