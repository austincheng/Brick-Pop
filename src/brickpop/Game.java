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
import static brickpop.Constants.DARK_COLOR;
import static brickpop.Constants.PIECE_RADIUS;


/** JFrame representing everything including the board and the score.
 *  @author Austin Cheng
 */
public class Game extends JFrame {
    /** Model being displayed. */
    private Board _model;
    /** Score label. */
    private JLabel score;
    /** Board panel. */
    private BoardPanel boardPanel;
    /** Series of pops in solution. */
    private Stack<Brick> _popSolution;
    /** Whether the stack contains the fully solved solution. */
    private boolean solved;

    /** A new widget displaying MODEL. */
    public Game(String title, Board model) {
        _model = model;
        _popSolution = new Stack<>();
        solved = false;

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
                if (!solved) {
                    ArrayList<Brick> way = _model.bestFinish();
                    if (way == null) {
                        JOptionPane.showMessageDialog(Game.this, "No solution possible");
                        return;
                    }
                    for (Brick b : way) {
                        _popSolution.push(b);
                    }
                    solved = true;
                    _popSolution.push(null);
                }
            }
        });
        menu.add(bestSolve);

        /* Fast Solve option. */
        JMenuItem fastSolve = new JMenuItem("Fast Solve");
        fastSolve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!solved) {
                    _popSolution.push(null);
                    ArrayList<Brick> way = _model.oneFinish();
                    if (way == null) {
                        JOptionPane.showMessageDialog(Game.this,"No solution possible");
                        return;
                    }
                    for (Brick b : way) {
                        _popSolution.push(b);
                    }
                    solved = true;
                }
            }
        });
        menu.add(fastSolve);

        /* Print Stack option. */
        JMenuItem printStack = new JMenuItem("Print Stack");
        printStack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Start Stack");
                for (Brick brick:_popSolution) {
                    if (brick == null) {
                        System.out.println("Solution called here.");
                    } else {
                        System.out.println("Pop " + brick.toString());
                    }
                }
                System.out.println("End Stack");
                System.out.println();
            }
        });
        menu.add(printStack);

        menuBar.add(menu);
        setJMenuBar(menuBar);

        setVisible(true);
    }

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
                if (!solved) {
                    _popSolution.push(b);
                }
                _model.pop(b);

                if (_model.isEmpty()) {
                    System.out.println("Start Solution");
                    for (Brick brick: _popSolution) {
                        if (brick != null) {
                            System.out.println("Pop " + brick.toString());
                        }
                    }
                    System.out.println("End Solution");
                    System.exit(0);
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
