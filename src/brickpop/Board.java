package brickpop;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/** A Brick Pop board.
 *  @author Austin Cheng
 */
public class Board {
    /** 2D representation of board. */
    private Brick[][] _board;
    /** Number of bricks on each side. */
    static final int SIDE = 10;
    /** Previous board states. */
    private Stack<Board> boardStates;
    /** Current score. */
    private int _score;
    /** Map of colors to count of color. */
    private HashMap<Colors, Integer> counts;

    /** New board with board contests as defined by BOARDSTRING.
     *  BOARDSTRING consists of 100 characters, each of which is
     *  r, y, g, b, p, d, or -, optionally interspersed with whitespace.
     *  These give the contents of the Board starting from top left moving right
     *  until the end of the row, then repeating for future rows.
     */
    public Board(String boardString) {
        boardString = boardString.toLowerCase().replaceAll("\\s", "");
        if (!boardString.matches("[rygbpd-]{100}")) {
            throw new IllegalArgumentException("bad board description");
        }
        _board = new Brick[SIDE][SIDE];
        boardStates = new Stack<>();
        counts = new HashMap<>();
        for (int r = 0; r < SIDE; r++) {
            for (int c = 0; c < SIDE; c++) {
                Colors color = Colors.color(boardString.substring(r * SIDE + c,
                        r * SIDE + c + 1));
                _board[r][c] = new Brick(r, c, color);
                if (counts.containsKey(color)) {
                    counts.put(color, counts.get(color) + 1);
                } else {
                    counts.put(color, 1);
                }
            }
        }
        boardStates.push(copyAndPushItself(this));
        _score = 0;
    }

    /** New board with board contests as defined by IMG and DIMENSIONS.
     *  DIMENSIONS[0] = board left-most x
     *  DIMENSIONS[1] = board right-most x
     *  DIMENSIONS[2] = board top-most y
     *  DIMENSIONS[3] = board bottom-most y
     *  DIMENSIONS[4] = brick left-most x
     *  DIMENSIONS[5] = brick right-most x (without shadow)
     *  DIMENSIONS[6] = brick right-most x (with shadow)
     *  DIMENSIONS[7] = next brick left-most x
     *  IMG is an image with the board contents.
     */
    public Board(BufferedImage img, int[] dimensions) {
        int brickRadius = (dimensions[5] - dimensions[4]) / 2;
        int brickDim = (dimensions[6] - dimensions[4]);
        int inBetween = dimensions[7] - dimensions[6];
        int left = dimensions[0];
        int top = dimensions[2];
        int width = dimensions[1] - dimensions[0];
        int height = dimensions[3] - dimensions[2];

        int rows = (int) Math.round((height + inBetween) / (double) (brickDim + inBetween));
        int cols = (int) Math.round((width + inBetween) / (double) (brickDim + inBetween));

        _board = new Brick[SIDE][SIDE];
        boardStates = new Stack<>();
        counts = new HashMap<>();
        for (int r = 0; r < SIDE - rows; r++) {
            for (int c = 0; c < SIDE; c++) {
                _board[r][c] = new Brick(r, c, Colors.EMPTY);
            }
        }

        for (int c = cols; c < SIDE; c++) {
            for (int r = 0; r < SIDE; r++) {
                _board[r][c] = new Brick(r, c, Colors.EMPTY);
            }
        }

        for (int r = SIDE - rows; r < SIDE; r++) {
            for (int c = 0; c < cols; c++) {
                int centerX = left + c * (brickDim + inBetween) + brickRadius;
                int centerY = top + (r - (SIDE - rows)) * (brickDim + inBetween) + brickRadius;
                Colors color = Colors.color(img.getRGB(centerX, centerY));
                _board[r][c] = new Brick(r, c, color);
                if (counts.containsKey(color)) {
                    counts.put(color, counts.get(color) + 1);
                } else {
                    counts.put(color, 1);
                }
            }
        }

        boardStates.push(copyAndPushItself(this));
        _score = 0;
    }

    /** A copy of B. */
    public Board(Board b) {
        internalCopy(b);
    }

    /** Copy B into me. */
    private void internalCopy(Board b) {
        _board = new Brick[SIDE][SIDE];
        _score = b.score();
        for (int r = 0; r < SIDE; r++) {
            for (int c = 0; c < SIDE; c++) {
                _board[r][c] = b.get(r, c);
            }
        }
        boardStates = new Stack<>();
        for (Board state: b.boardStates) {
            boardStates.push(state);
        }
        counts = new HashMap<>();
        for (Colors c: b.counts.keySet()) {
            counts.put(c, b.counts.get(c));
        }
    }

    /** Makes a copy of B and returns it with boardStates last element as B. */
    private Board copyAndPushItself(Board b) {
        Board b1 = new Board(b);
        b1.boardStates.push(b1);

        return b1;
    }

    /** Deals with all empty spaces (aside from empty columns). */
    public void fillEmpty() {
        int row = SIDE - 1;
        while (row >= 1) {
            for (Brick b: _board[row]) {
                if (b.color() == Colors.EMPTY) {
                    Brick top = _board[row - 1][b.col()];
                    while (top.color() == Colors.EMPTY && top.row() > 0) {
                        top = _board[top.row() - 1][b.col()];
                    }
                    if (top.color() != Colors.EMPTY) {
                        _board[row][b.col()] =
                                new Brick(row, b.col(), top.color());
                        _board[top.row()][b.col()] =
                                new Brick(top.row(), b.col(), Colors.EMPTY);
                    }
                }
            }
            row--;
        }
    }

    /** Shifts columns such that COLUMN is an empty column. */
    public void fillEmptyColumn(int column) {
        assert isEmptyColumn(column);

        while (column != 9) {
            for (int r = 0; r < SIDE; r++) {
                _board[r][column] =
                        new Brick(r, column, _board[r][column + 1].color());
            }
            column++;

            if (column == 9) {
                for (int r = 0; r < SIDE; r++) {
                    _board[r][column] = new Brick(r, column, Colors.EMPTY);
                }
            }
        }
    }

    /** Returns whether COLUMN is an empty column. */
    public boolean isEmptyColumn(int column) {
        for (int r = 0; r < SIDE; r++) {
            if (_board[r][column].color() != Colors.EMPTY) {
                return false;
            }
        }
        return true;
    }

    /** Returns whether every column to the right of COLUMN
     *  including itself is empty. */
    public boolean isEmptyRightColumns(int column) {
        for (int c = column; c < SIDE; c++) {
            for (int r = 0; r < SIDE; r++) {
                if (_board[r][c].color() != Colors.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Pops brick B. */
    public void pop(Brick b) {
        ArrayList<Brick> block = b.block(this);
        _score += popScore(b);
        if (block.size() <= 1) {
            return;
        }

        for (Brick[] row: _board) {
            for (Brick item: row) {
                if (block.contains(item)) {
                    _board[item.row()][item.col()] =
                            new Brick(item.row(), item.col(), Colors.EMPTY);
                }
            }
        }

        counts.put(b.color(), counts.get(b.color()) - block.size());

        fillEmpty();
        for (int c = 0; c < SIDE; c++) {
            while (isEmptyColumn(c) && !isEmptyRightColumns(c)) {
                fillEmptyColumn(c);
            }
        }

        boardStates.push(copyAndPushItself(this));
    }

    /** Returns the score resulting from popping B. */
    public int popScore(Brick b) {
        int numBlocks = b.block(this).size();
        return numBlocks * (numBlocks - 1);
    }

    /** Returns the score resulting from popping bricks in WAY in order. */
    public int gameScore(ArrayList<Brick> way) {
        Board trial = new Board(this);
        int score = 0;
        for (Brick b: way) {
            score += trial.popScore(b);
            trial.pop(b);
        }
        return score;
    }

    /** Returns a list of all unique pops. */
    public ArrayList<Brick> uniquePops() {
        ArrayList<Brick> uniquePops = new ArrayList<>();
        for (Brick[] row: _board) {
            for (Brick b: row) {
                ArrayList<Brick> block = b.block(this);
                if (block.size() > 1 && !hasAny(uniquePops, block)) {
                    uniquePops.add(b);
                }
            }
        }
        return uniquePops;
    }

    /** Returns whether any item in CONTAINS is in LST. */
    private static boolean hasAny(ArrayList<Brick> lst,
                                  ArrayList<Brick> contains) {
        for (Brick b: contains) {
            if (lst.contains(b)) {
                return true;
            }
        }
        return false;
    }

    /** Returns all possible ways to finish the game. */
    public ArrayList<ArrayList<Brick>> finishGame() {
        ArrayList<Brick> uniquePops = uniquePops();
        ArrayList<ArrayList<Brick>> ways = new ArrayList<>();

        if (isEmpty()) {
            ways.add(new ArrayList<>());
        }

        if (counts.containsValue(1)) {
            return ways;
        }

        for (Brick b: uniquePops) {
            Board trial = new Board(this);
            trial.pop(b);
            for (ArrayList<Brick> way: trial.finishGame()) {
                ArrayList<Brick> realWay = new ArrayList<>();
                realWay.add(b);
                realWay.addAll(way);
                ways.add(realWay);
            }
        }

        return ways;
    }

    /** Returns an ordered list of bricks to pop to win. */
    public ArrayList<Brick> oneFinish() {
        ArrayList<Brick> uniquePops = uniquePops();
        ArrayList<Brick> way = new ArrayList<>();

        if (isEmpty()) {
            return way;
        }

        if (counts.containsValue(1)) {
            return null;
        }

        for (Brick b: uniquePops) {
            Board trial = new Board(this);
            trial.pop(b);
            ArrayList<Brick> rest = trial.oneFinish();
            if (rest != null) {
                way.add(b);
                way.addAll(rest);
                return way;
            }
        }

        return null;
    }

    /** Returns an ordered list of bricks to pop needed to achieve maximum score. */
    public ArrayList<Brick> bestFinish() {
        ArrayList<ArrayList<Brick>> ways = finishGame();
        if (ways.size() == 0) {
            return null;
        } else {
            ArrayList<Brick> bestWay = ways.get(0);
            for (ArrayList<Brick> way: ways) {
                if (gameScore(way) > gameScore(bestWay)) {
                    bestWay = way;
                }
            }

            return bestWay;
        }
    }

    /** Undo the last move, if any. */
    public void undo() {
        if (boardStates.size() <= 1) {
            return;
        }
        Board previous = boardStates.pop();
        _score = _score - previous.score();
        internalCopy(boardStates.peek());
    }

    /** Returns whether the board is completely empty. */
    public boolean isEmpty() {
        for (Brick[] row: _board) {
            for (Brick b: row) {
                if (b.color() != Colors.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String result = "";
        for (int r = 0; r < SIDE; r++) {
            for (int c = 0; c < SIDE; c++) {
                result += _board[r][c].color() + " ";
            }
            result += "\n";
        }
        return result;
    }

    /** Returns the 2D representation of the board. */
    public Brick[][] getBoard() {
        return _board;
    }

    /** Returns the brick with row R and column C. */
    public Brick get(int r, int c) {
        return _board[r][c];
    }

    /** Returns the current score. */
    public int score() {
        return _score;
    }
}
