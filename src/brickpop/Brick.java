package brickpop;

import java.util.ArrayList;

/** A Brick Pop Brick.
 * @author Austin Cheng
 */
public class Brick {
    /** Row of brick. */
    private int _row;
    /** Column of brick. */
    private int _column;
    /** Color of brick. */
    private Colors _color;

    /** New Brick with row ROW, column COLUMN, and color COLOR. */
    public Brick(int row, int column, Colors color) {
        _row = row;
        _column = column;
        _color = color;
    }

    /** Returns a list of all bricks adjacent
     *  on board BOARD that are the same color. */
    public ArrayList<Brick> sameAdjacent(Board board) {
        Brick[][] model = board.getBoard();
        ArrayList<Brick> adjacents = new ArrayList<>();
        if (_row < Board.SIDE - 1) {
            Brick down = model[_row + 1][_column];
            if (down.color() == _color) {
                adjacents.add(down);
            }
        }

        if (_row > 0) {
            Brick up = model[_row - 1][_column];
            if (up.color() == _color) {
                adjacents.add(up);
            }
        }

        if (_column < Board.SIDE - 1) {
            Brick right = model[_row][_column + 1];
            if (right.color() == _color) {
                adjacents.add(right);
            }
        }

        if (_column > 0) {
            Brick left = model[_row][_column - 1];
            if (left.color() == _color) {
                adjacents.add(left);
            }
        }

        return adjacents;
    }

    /** Returns a list of bricks in the block on board BOARD. */
    public ArrayList<Brick> block(Board board) {
        ArrayList<Brick> block = new ArrayList<>();
        if (_color == Colors.EMPTY) {
            return block;
        }

        block.add(this);
        int i = 0;
        while (!hasAllBlock(board, block)) {
            for (Brick adjacent: block.get(i).sameAdjacent(board)) {
                if (!block.contains(adjacent)) {
                    block.add(adjacent);
                }
            }
            i++;
        }

        return block;
    }

    /** Returns whether BRICKS contains a complete block on BOARD. */
    public static boolean hasAllBlock(Board board, ArrayList<Brick> bricks) {
        for (Brick b: bricks) {
            for (Brick adjacent: b.sameAdjacent(board)) {
                if (!bricks.contains(adjacent)) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Returns the color. */
    public Colors color() {
        return _color;
    }

    /** Returns the row. */
    public int row() {
        return _row;
    }

    /** Returns the column. */
    public int col() {
        return _column;
    }

    @Override
    public String toString() {
        return "(" + _row + ", " + _column + ", " + _color.toString() + ")";
    }

    @Override
    public boolean equals(Object other) {
        Brick b = (Brick) other;
        return b.row() == _row && b.col() == _column && b.color() == _color;
    }

    @Override
    public int hashCode() {
        return ((_row << 5) | _column) & (_color.toString().charAt(0));
    }
}
