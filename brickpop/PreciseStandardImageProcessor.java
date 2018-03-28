package brickpop;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import static brickpop.Constants.LINE_COLOR;

/**
 * JFrame for standard pre-processing for the given image.
 *  @author Austin Cheng
 */
public class PreciseStandardImageProcessor extends JFrame{
    private static final String[] instructions = {
            "Align the line with the left-most edge of the brick pop board.",
            "Align the line with the right-most edge of the brick pop board. (Include the shadow) (Exclude empty columns)",
            "Align the line with the top-most edge of the brick pop board. (Exclude empty rows)",
            "Align the line with the bottom-most edge of the brick pop board. (Include the shadow)",
    };
    private int[] dimensions;
    private int[] mouseDimensions;
    private int count;
    private BufferedImage _img;
    private JLabel instructionLabel;
    private ImagePanel imagePanel;

    public PreciseStandardImageProcessor(String title, BufferedImage img) {
        dimensions = new int[4];
        mouseDimensions = new int[4];
        count = 0;
        _img = img;

        setSize(img.getWidth() + 20, img.getHeight() + 80);
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* Main Pane for entire JFrame. */
        Container cp = getContentPane();
        BoxLayout layout = new BoxLayout(cp, BoxLayout.Y_AXIS);
        cp.setLayout(layout);

        /* Top Image pane. */
        imagePanel = new ImagePanel();
        cp.add(imagePanel);

        /* Bottom instructions pane. */
        JPanel bottom = new JPanel();
        BoxLayout layoutBottom = new BoxLayout(bottom, BoxLayout.X_AXIS);
        bottom.setLayout(layoutBottom);

        /* Instruction label inside bottom pane. */
        instructionLabel = new JLabel(instructions[0]);
        bottom.add(instructionLabel);

        /* Confirm button inside bottom pane. */
        JButton confirm = new JButton("Confirm");
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dimensions[count] = mouseDimensions[count];
                count++;
                if (count < 4) {
                    instructionLabel.setText(instructions[count]);
                    imagePanel.update(imagePanel.getGraphics());
                } else {
                    PreciseStandardImageProcessor.this.setVisible(false);
                    Board board = new Board(_img.getSubimage(dimensions[0], dimensions[2], dimensions[1] - dimensions[0], dimensions[3] - dimensions[2]));
                    Game game = new Game("Brick Pop", board);
                }
            }
        });
        getRootPane().setDefaultButton(confirm);
        bottom.add(confirm);

        cp.add(bottom);

        setVisible(true);
    }

    private class ImagePanel extends JPanel {
        public ImagePanel() {
            for (int i = 0; i < mouseDimensions.length; i++) {
                if (i == 2 || i == 3) {
                    mouseDimensions[i] = _img.getHeight() / 2;
                } else {
                    mouseDimensions[i] = _img.getWidth() / 2;
                }
            }
            addMouseListener(new MouseFollower());
            addMouseMotionListener(new MouseFollower());
        }
        @Override
        protected void paintComponent(Graphics g) {
            g.drawImage(_img, 0, 0, null);
            for (int i = 0; i <= count; i++) {
                g.setColor(LINE_COLOR);
                if (i == 2 || i == 3) {
                    g.drawLine(0, mouseDimensions[i], _img.getWidth(), mouseDimensions[i]);
                } else {
                    g.drawLine(mouseDimensions[i], 0, mouseDimensions[i], _img.getHeight());
                }
            }
        }

        private class MouseFollower extends MouseInputAdapter {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                if (count == 2 || count == 3) {
                    mouseDimensions[count] = y;
                } else {
                    mouseDimensions[count] = x;
                }
                ImagePanel.this.update(ImagePanel.this.getGraphics());
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                if (count == 2 || count == 3) {
                    mouseDimensions[count] = y;
                } else {
                    mouseDimensions[count] = x;
                }
                ImagePanel.this.update(ImagePanel.this.getGraphics());
            }
        }
    }
}
