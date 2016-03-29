/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gomoku;

/**
 *
 * @author zhongjiezheng
 */
import java.util.Random;

/**
 * This mechanism randomly returns either ZERO, VERYLOW or LOW
 */
public class Randomize implements Scoring {

    @Override
    public int getValue() {
        Random generator = new Random();
        int r = generator.nextInt(3);
        if (r == 0) {
            return ScoreValue.ZERO;
        } else if (r == 1) {
            return ScoreValue.VERYLOW;
        } else {
            return ScoreValue.LOW;
        }

    }

    @Override
    public boolean isPresent(Move move, GomokuState model, int player) {
        return true;
    }
}
