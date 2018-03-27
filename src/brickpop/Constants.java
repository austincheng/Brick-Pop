package brickpop;

import java.awt.Color;

public class Constants {
    /** Radius of circle representing a brick. */
    public static final int PIECE_RADIUS = 20;
    /** Font size of numbers during answer panel. */
    public static final int FONT_SIZE = 20;
    /** Height of current drawing surface in pixels. */
    public static final int HEIGHT = Board.SIDE * (PIECE_RADIUS * 2);
    /** Width of current drawing surface in pixels. */
    public static final int WIDTH = Board.SIDE * (PIECE_RADIUS * 2);

    /** Color of red bricks. */
    public static final Color RED_COLOR = Color.RED;
    /** Color of yellow bricks. */
    public static final Color YELLOW_COLOR = Color.YELLOW;
    /** Color of green bricks. */
    public static final Color GREEN_COLOR = Color.GREEN;
    /** Color of blue bricks. */
    public static final Color BLUE_COLOR = Color.BLUE;
    /** Color of magenta bricks. */
    public static final Color PURPLE_COLOR = Color.MAGENTA;
    /** Color of gray bricks. */
    public static final Color DARK_COLOR = Color.GRAY;
    /** Color of background/empty bricks. */
    public static final Color BACKGROUND_COLOR = Color.WHITE;

}
