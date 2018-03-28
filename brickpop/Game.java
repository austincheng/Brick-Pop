package brickpop;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Container;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Stack;

import static brickpop.Constants.*;


/** JFrame representing the experiment board and the score.
 *  @author Austin Cheng
 */
public class Game extends JFrame {
    /** Board that the Game experiments with. */
    private Board _model;
    /** Original board. */
    private Board original;
    /** Series of pops in solution. */
    private Stack<Brick> _popSolution;
    /** Board panel. */
    private BoardPanel boardPanel;
    /** Score label. */
    private JLabel score;

    public Game(String title, Board model) {
        _model = model;
        original = new Board(model);
        _popSolution = new Stack<>();

        setSize(Constants.WIDTH + 20, Constants.HEIGHT + 80);
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* Main Pane for entire JFrame. */
        Container cp = getContentPane();
        BoxLayout layout = new BoxLayout(cp, BoxLayout.Y_AXIS);
        cp.setLayout(layout);

        /* Top Board pane. */
        boardPanel = new BoardPanel();
        cp.add(boardPanel);

        /* Bottom Score pane. */
        JPanel bottom = new JPanel();
        BoxLayout layoutBottom = new BoxLayout(bottom, BoxLayout.X_AXIS);
        bottom.setLayout(layoutBottom);

        /* Score label inside bottom pane. */
        score = new JLabel("Score: 0");
        bottom.add(score);

        cp.add(bottom);

        /* Menu bar at top. */
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Game");

        /* Undo option. */
        JMenuItem undo = new JMenuItem("Undo");
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (_popSolution.empty()) {
                    return;
                }
                _model.undo();
                _popSolution.pop();
                score.setText("Score: " + _model.score());
                boardPanel.update(boardPanel.getGraphics());
            }
        });
        menu.add(undo);

        /* Best Solve option. */
        JMenuItem bestSolve = new JMenuItem("Best Solve");
        bestSolve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Brick> way = _model.bestFinish();
                if (way == null) {
                    JOptionPane.showMessageDialog(Game.this, "No solution possible");
                    return;
                }
                for (Brick b : way) {
                    _popSolution.push(b);
                }

                ArrayList<Brick> solution = new ArrayList<>();
                while (!_popSolution.empty()) {
                    solution.add(0, _popSolution.pop());
                }
                Game.this.setVisible(false);
                new AnswerFrame(original, solution);
            }
        });
        menu.add(bestSolve);

        /* Fast Solve option. */
        JMenuItem fastSolve = new JMenuItem("Fast Solve");
        fastSolve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Brick> way = _model.oneFinish();
                if (way == null) {
                    JOptionPane.showMessageDialog(Game.this,"No solution possible");
                    return;
                }
                for (Brick b : way) {
                    _popSolution.push(b);
                }

                ArrayList<Brick> solution = new ArrayList<>();
                while (!_popSolution.empty()) {
                    solution.add(0, _popSolution.pop());
                }
                Game.this.setVisible(false);
                new AnswerFrame(original, solution);
            }
        });
        menu.add(fastSolve);

        menuBar.add(menu);
        setJMenuBar(menuBar);

        setVisible(true);
    }

    /** Board within the Game. */
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
        }

        @Override
        public void mouseClicked(MouseEvent where) {
            int x = where.getX(), y = where.getY();
            int mouseCol = x / (PIECE_RADIUS * 2);
            int mouseRow = y / (PIECE_RADIUS * 2);
            if (mouseCol <= 9 && mouseRow <= 9) {
                Brick b = _model.get(mouseRow, mouseCol);
                _popSolution.push(b);
                _model.pop(b);

                if (_model.isEmpty()) {
                    ArrayList<Brick> solution = new ArrayList<>();
                    while (!_popSolution.empty()) {
                        solution.add(0, _popSolution.pop());
                    }
                    Game.this.setVisible(false);
                    new AnswerFrame(original, solution);
                }
                update(getGraphics());
                score.setText("Score: " + _model.score());
            }
        }

        @Override public void mousePressed(MouseEvent evt) { }
        @Override public void mouseReleased(MouseEvent evt) { }
        @Override public void mouseEntered(MouseEvent evt) { }
        @Override public void mouseExited(MouseEvent evt) { }
    }
}
