/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gomoku;

/**
 *
 * @author zhongjiezheng
 */
public class PositionMiddle implements Scoring {

    /**
     * amount to multiply distance to edges of field by to get rating.
     */
    private int value = 0;

    @Override
    public int getValue() {
        int returnValue = value;
        value = 0; // reset value
        return returnValue;
    }


    @Override
    public boolean isPresent(final Move move, final GomokuState state, int player) {
        calculateValue(move, state);
        return true;
    }

    /**
     * calculates the value by determining the distance to the edge of the field
     * and multiplying a pre-defined value.
     * WARNING: this method only works with 7 rows!!
     * @param move the move to calc on
     * @param model the model to calc on
     */
    private void calculateValue(final Move move, final GomokuState state){
        if ((move.row >= 7 && move.row <= 11) && (move.col >= 7 && move.col <= 11)) {
            value = ScoreValue.MEDIUM;
        } else if ((move.row >= 5 && move.row <= 13) && (move.col >= 5 && move.col <= 13)) {
            value = ScoreValue.LOW;
        } else if ((move.row >= 3 && move.row <= 15) && (move.col >= 3 && move.col <= 15)) {
            value = ScoreValue.VERYLOW;
        } else {
            value = ScoreValue.ZERO;
        }
    }
}
