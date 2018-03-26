package brickpop;

import ucb.gui2.TopLevel;
import ucb.gui2.LayoutSpec;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;

/** The GUI for the Brick Pop game.
 *  @author Austin Cheng
 */
public class GUI extends TopLevel implements Observer {
    /** Minimum size of board in pixels. */
    private static final int MIN_SIZE = 500;
    /** Contains the drawing logic for the Brick Pop model. */
    private BoardWidget _widget;
    /** The model of the game. */
    private Board _model;
    /** Series of pops in solution. */
    private Stack<Brick> _popSolution;
    /** Whether the stack contains the fully solved solution. */
    private boolean solved;

    /** A new display observing MODEL, with TITLE as its window title. */
    public GUI(String title, Board model) {
        super(title, true);
        addMenuButton("Game->Undo", this::undo);
        addMenuButton("Game->Best Solve", this::bestSolve);
        addMenuButton("Game->Fast Solve", this::fastSolve);
        addMenuButton("Game->Print Stack", this::printStack);
        _model = model;
        _widget = new BoardWidget(model);
        _popSolution = new Stack<>();
        solved = false;
        addLabel("Score: 0", "Score", new LayoutSpec("x", 0, "y", 1));
        add(_widget,
                new LayoutSpec("height", "1",
                        "width", "REMAINDER",
                        "ileft", 5, "itop", 5, "iright", 5,
                        "ibottom", 5));
        setMinimumSize(MIN_SIZE, MIN_SIZE);
        _widget.addObserver(this);
        _model.addObserver(this);
    }

    /** Undoes the last move. */
    private synchronized void undo(String unused) {
        _model.undo();
        _popSolution.pop();
        _widget.repaint();
    }

    /** Adds the best solution to the solution stack. */
    private synchronized void bestSolve(String unused) {
        if (!solved) {
            ArrayList<Brick> way = _model.bestFinish();
            if (way == null) {
                System.out.println("No solution possible");
                return;
            }
            for (Brick b : way) {
                _popSolution.push(b);
            }
            solved = true;
            _popSolution.push(null);
        }
    }

    /** Adds the fastest solution to the solution stack. */
    private synchronized void fastSolve(String unused) {
        if (!solved) {
            _popSolution.push(null);
            ArrayList<Brick> way = _model.oneFinish();
            if (way == null) {
                System.out.println("No solution possible");
                return;
            }
            for (Brick b : way) {
                _popSolution.push(b);
            }
            solved = true;
        }
    }

    /** Prints the solution stack. */
    private synchronized void printStack(String unused) {
        System.out.println("Start Stack");
        for (Brick brick: _popSolution) {
            if (brick == null) {
                System.out.println("Solution called here.");
            } else {
                System.out.println("Pop " + brick.toString());
            }
        }
        System.out.println("End Stack");
        System.out.println();
    }


    @Override
    public void update(Observable obs, Object arg) {
        if (obs == _model) {
            _widget.update(_model, arg);
        } else if (obs == _widget) {
            makePop((String) arg);
        }
    }

    /** Respond to a click on SQ. */
    private void makePop(String sq) {
        int column = Integer.parseInt(sq.substring(0, 1));
        int row = Integer.parseInt(sq.substring(1));

        Brick b = _model.get(row, column);
        if (!solved) {
            _popSolution.push(b);
        }
        _model.pop(b);
        _widget.repaint();

        setLabel("Score", "Score: " + _model.score());
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
    }
}
