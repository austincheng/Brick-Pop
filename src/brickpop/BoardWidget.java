package brickpop;

import ucb.gui2.Pad;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Observer;
import java.util.Observable;

import java.awt.event.MouseEvent;

/** Widget for displaying a Brick Pop board.
 *  @author Austin Cheng
 */
public class BoardWidget extends Pad implements Observer {
    /** Radius of circle representing a brick. */
    static final int PIECE_RADIUS = 20;
    /** Dimension of current drawing surface in pixels. */
    private int _dim;

    /** Color of red bricks. */
    private static final Color RED_COLOR = Color.RED;
    /** Color of yellow bricks. */
    private static final Color YELLOW_COLOR = Color.YELLOW;
    /** Color of green bricks. */
    private static final Color GREEN_COLOR = Color.GREEN;
    /** Color of blue bricks. */
    private static final Color BLUE_COLOR = Color.BLUE;
    /** Color of magenta bricks. */
    private static final Color PURPLE_COLOR = Color.MAGENTA;
    /** Color of gray bricks. */
    private static final Color DARK_COLOR = Color.GRAY;
    /** Color of empty bricks. */
    private static final Color BACKGROUND_COLOR = Color.WHITE;

    /** Model being displayed. */
    private static Board _model;

    /** A new widget displaying MODEL. */
    BoardWidget(Board model) {
        _model = model;
        setMouseHandler("click", this::readPop);
        _model.addObserver(this);
        _dim = Board.SIDE * (PIECE_RADIUS * 2);
        setPreferredSize(_dim, _dim);
    }

    @Override
    public synchronized void paintComponent(Graphics2D g) {
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, _dim, _dim);

        for (int r = 0; r < Board.SIDE; r++) {
            for (int c = 0; c < Board.SIDE; c++) {
                render(g, _model.get(r, c));
            }
        }
    }

    /** Draw circle at row ROW column COLUMN on G with
     *  color COLOR and radius RADIUS and filled according
     *  to FILLED. */
    public void circle(Graphics2D g, Color color, int row, int column,
                       int radius, boolean filled) {
        g.setColor(color);

        int centerX = column * (PIECE_RADIUS * 2) + PIECE_RADIUS;
        int centerY = row * (PIECE_RADIUS * 2) + PIECE_RADIUS;

        if (filled) {
            g.fillOval(centerX - radius, centerY - radius,
                    radius * 2, radius * 2);
        } else {
            g.drawOval(centerX - radius, centerY - radius,
                    radius * 2, radius * 2);
        }
    }

    /** Renders brick B on G. */
    public void render(Graphics2D g, Brick b) {
        Color color;
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
        } else {
            return;
        }

        circle(g, color, b.row(), b.col(), PIECE_RADIUS, true);
    }

    /** Notify observers of mouse's current position
     *  from click event WHERE.*/
    private void readPop(String unused, MouseEvent where) {
        int x = where.getX(), y = where.getY();
        int mouseCol = x / (PIECE_RADIUS * 2);
        int mouseRow = y / (PIECE_RADIUS * 2);
        if (where.getButton() == MouseEvent.BUTTON1) {
            if (mouseCol <= 9 && mouseRow <= 9) {
                setChanged();
                notifyObservers("" + mouseCol + mouseRow);
            }
        }
    }

    @Override
    public synchronized void update(Observable model, Object arg) {
        repaint();
    }
}
