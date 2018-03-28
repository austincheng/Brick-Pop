package brickpop;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import static brickpop.Constants.*;

/**
 * JFrame representing the solution board and walkthrough solver.
 * @author Austin Cheng
 */
public class AnswerFrame extends JFrame {
    /** Board that the AnswerFrame answers to. */
    private Board _model;
    /** Sequence of bricks to pop for the solution. */
    private ArrayList<Brick> _solution;

    public AnswerFrame(Board model, ArrayList<Brick> solution) {
        _model = model;
        _solution = solution;

        setSize(Constants.WIDTH + 20, Constants.HEIGHT + 80);
        setTitle("Solution");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new BoardPanel());
        setVisible(true);
    }

    /** Board within the AnswerFrame. */
    private class BoardPanel extends JPanel implements MouseListener {
        public BoardPanel() {
            addMouseListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(BACKGROUND_COLOR);
            g.fillRect(0, 0, Constants.WIDTH, Constants.HEIGHT);
            for (int r = 0; r < Board.SIDE; r++) {
                for (int c = 0; c < Board.SIDE; c++) {
                    render(g, _model.get(r, c));
                }
            }
        }

        /** Draw circle at row ROW column COLUMN on G with color COLOR. */
        private void circle(Graphics g, Color color, int row, int column) {
            g.setColor(color);

            int centerX = column * (PIECE_RADIUS * 2) + PIECE_RADIUS;
            int centerY = row * (PIECE_RADIUS * 2) + PIECE_RADIUS;

            g.fillOval(centerX - PIECE_RADIUS, centerY - PIECE_RADIUS,
                    PIECE_RADIUS * 2, PIECE_RADIUS * 2);
        }

        /** Renders brick B on G. */
        private void render(Graphics g, Brick b) {
            Color color = BACKGROUND_COLOR;
            Colors piece = b.color();
            if (piece == Colors.RED) {
                color = RED_COLOR;
            } else if (piece == Colors.YELLOW) {
                color = YELLOW_COLOR;
            } else if (piece == Colors.GREEN) {
                color = GREEN_COLOR;
            } else if (piece == Colors.BLUE) {
                color = BLUE_COLOR;
            } else if (piece == Colors.PURPLE) {
                color = PURPLE_COLOR;
            } else if (piece == Colors.DARK) {
                color = DARK_COLOR;
            }

            circle(g, color, b.row(), b.col());
            for (int i = 0; i < _solution.size(); i++) {
                if (b.row() == _solution.get(i).row() && b.col() == _solution.get(i).col()) {
                    g.setColor(Color.BLACK);
                    g.setFont(new Font(g.getFont().getName(), Font.BOLD, FONT_SIZE));
                    int position = i + 1;
                    g.drawString(position + "",
                            b.col() * (PIECE_RADIUS * 2) + PIECE_RADIUS - g.getFontMetrics().stringWidth(position + "") / 2
                            ,b.row() * (PIECE_RADIUS * 2) + PIECE_RADIUS + FONT_SIZE / 2);
                    break;
                }
            }
        }

        @Override
        public void mouseClicked(MouseEvent where) {
            int x = where.getX(), y = where.getY();
            int mouseCol = x / (PIECE_RADIUS * 2);
            int mouseRow = y / (PIECE_RADIUS * 2);
            Brick correct = _solution.get(0);
            if (mouseCol == correct.col() && mouseRow == correct.row()) {
                _model.pop(correct);

                if (_model.isEmpty()) {
                    System.exit(0);
                }
                _solution.remove(0);
                update(getGraphics());
            }
        }

        @Override public void mousePressed(MouseEvent evt) { }
        @Override public void mouseReleased(MouseEvent evt) { }
        @Override public void mouseEntered(MouseEvent evt) { }
        @Override public void mouseExited(MouseEvent evt) { }
    }
}
