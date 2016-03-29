/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gomoku;

/**
 *
 * @author zhongjiezheng
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

public class Gomoku {
    
    
    
    public static void main(String[] args) {
        
        int size = 19;
        if (args.length > 0)
            size = Integer.parseInt(args[0]);
        
        JFrame frame = new JFrame();
        final int FRAME_WIDTH = 600;
        final int FRAME_HEIGHT = 650;
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setTitle("Gomoku");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        GomokuPanel panel = new GomokuPanel(size);
        frame.add(panel);
        frame.setVisible(true);
    }
}

@SuppressWarnings("serial")
class GomokuPanel extends JPanel {
    
    private final int MARGIN = 5;
    private final double PIECE_FRAC = 0.9;
    
    private int count=0;
    
    private int size = 19;
    private GomokuState state;
    
    public GomokuPanel() {
        this(19);
    }
    
    public GomokuState getState() {
        return state;
    }
    
    public GomokuPanel(int size) {
        super();
        this.size = size;
        state = new GomokuState(size);
        addMouseListener(new GomokuListener());
    }
    
    class GomokuListener extends MouseAdapter {
        public void mouseReleased(MouseEvent e) {
            double panelWidth = getWidth();
            double panelHeight = getHeight();
            double boardWidth = Math.min(panelWidth, panelHeight) - 2 * MARGIN;
            double squareWidth = boardWidth / size;
            double xLeft = (panelWidth - boardWidth) / 2 + MARGIN;
            double yTop = (panelHeight - boardWidth) / 2 + MARGIN;
            int col = (int) Math.round((e.getX() - xLeft) / squareWidth - 0.5);
            int row = (int) Math.round((e.getY() - yTop) / squareWidth - 0.5);
            Move move = new Move(row,col);
            if (row >= 0 && row < size && col >= 0 && col < size && state.getPiece(move) == GomokuState.NONE 
            		&& state.getWinner() == GomokuState.NONE) {
                
                
//                
//                if(count!=0){
//                    AIPlayer ai1 = new AIPlayer(GomokuState.BLACK);
//                ConnectingStones cs1 = new ConnectingStones();
//                ai1.addStrat(new Win());
//                ai1.addStrat(new BlockWin());
//                ai1.addStrat(new PositionMiddle());
//                ai1.addStrat(new Randomize());
//                ai1.addStrat(cs1);
//                Move aiMove1 = ai1.makeMove(state);
//                    state.playPiece(aiMove1, GomokuState.BLACK);
//                }
//                    
//                else{
//                    state.playPiece(new Move(9,9), GomokuState.BLACK);
//                    count++;
//                }
                
                state.playPiece(move, GomokuState.BLACK);
                
                repaint();
                int winner = state.getWinner();
                if (winner != GomokuState.NONE) {
                    JOptionPane.showMessageDialog(null, (winner == GomokuState.BLACK) ? "Black wins!" : "White wins!");
                } 
                else {
                	AIPlayer ai = new AIPlayer(GomokuState.WHITE);
                	ConnectingStones cs = new ConnectingStones();
                	ai.addStrat(new Win());
                	ai.addStrat(new BlockWin());
                	ai.addStrat(new PositionMiddle());
                	ai.addStrat(new Randomize());
                	ai.addStrat(cs);
                	Move aiMove = ai.makeMove(state);
                	//System.out.println(cs.isPresent(aiMove, state, GomokuState.WHITE));
                	//System.out.println(aiMove.toString());
                	state.playPiece(aiMove,GomokuState.WHITE);
                	repaint();
                	winner = state.getWinner();
                	if (winner != GomokuState.NONE)
                		JOptionPane.showMessageDialog(null, (winner == GomokuState.BLACK) ? "Black wins!" : "White wins!");
                }
            }
        }
    }
    
    
    public void paintComponent(Graphics g) {
        
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        
        double panelWidth = getWidth();
        double panelHeight = getHeight();
        
        g2D.setColor(new Color(0.925f, 0.670f, 0.34f));
        g2D.fill(new Rectangle2D.Double(0, 0, panelWidth, panelHeight));
        
        double boardWidth = Math.min(panelWidth, panelHeight) - 2 * MARGIN;
        double squareWidth = boardWidth / size;
        double gridWidth = (size - 1) * squareWidth;
        double pieceDiameter = PIECE_FRAC * squareWidth;
        boardWidth -= pieceDiameter;
        double xLeft = (panelWidth - boardWidth) / 2 + MARGIN;
        double yTop = (panelHeight - boardWidth) / 2 + MARGIN;
        
        g2D.setColor(Color.BLACK);
        for (int i = 0; i < size; i++) {
            double offset = i * squareWidth;
            g2D.draw(new Line2D.Double(xLeft, yTop + offset, xLeft + gridWidth, yTop + offset));
            g2D.draw(new Line2D.Double(xLeft + offset, yTop, xLeft + offset, yTop + gridWidth));
        }
        
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int piece = state.getPiece(new Move(row,col));
                if (piece != GomokuState.NONE) {
                    Color c = (piece == GomokuState.BLACK) ? Color.BLACK : Color.WHITE;
                    g2D.setColor(c);
                    double xCenter = xLeft + col * squareWidth;
                    double yCenter = yTop + row * squareWidth;
                    Ellipse2D.Double circle = 
                        new Ellipse2D.Double(xCenter - pieceDiameter / 2, yCenter - pieceDiameter / 2, pieceDiameter, pieceDiameter);
                    g2D.fill(circle);
                    g2D.setColor(Color.black);
                    g2D.draw(circle);
                }
            }
        }
    }
}
