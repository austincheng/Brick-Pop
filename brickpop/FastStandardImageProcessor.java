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
public class FastStandardImageProcessor extends JFrame{
    private static final String instruction = "Outline the board with a rectangle (Crop it)";
    private int left;
    private int width;
    private int top;
    private int height;
    private BufferedImage _img;

    public FastStandardImageProcessor(String title, BufferedImage img) {
        _img = img;

        setSize(img.getWidth() + 20, img.getHeight() + 80);
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* Main Pane for entire JFrame. */
        Container cp = getContentPane();
        BoxLayout layout = new BoxLayout(cp, BoxLayout.Y_AXIS);
        cp.setLayout(layout);

        /* Top Image pane. */
        ImagePanel imagePanel = new ImagePanel();
        cp.add(imagePanel);

        /* Bottom instructions pane. */
        JPanel bottom = new JPanel();
        BoxLayout layoutBottom = new BoxLayout(bottom, BoxLayout.X_AXIS);
        bottom.setLayout(layoutBottom);

        /* Instruction label inside bottom pane. */
        JLabel instructionLabel = new JLabel(instruction);
        bottom.add(instructionLabel);

        /* Confirm button inside bottom pane. */
        JButton confirm = new JButton("Confirm");
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FastStandardImageProcessor.this.setVisible(false);
                Board board = new Board(_img.getSubimage(left, top, width, height));
                Game game = new Game("Brick Pop", board);
            }
        });
        getRootPane().setDefaultButton(confirm);
        bottom.add(confirm);

        cp.add(bottom);

        setVisible(true);
    }

    private class ImagePanel extends JPanel {
        public ImagePanel() {
            left = 0;
            width = 0;
            top = 0;
            height = 0;
            addMouseListener(new MouseFollower());
            addMouseMotionListener(new MouseFollower());
        }
        @Override
        protected void paintComponent(Graphics g) {
            g.drawImage(_img, 0, 0, null);
            g.setColor(LINE_COLOR);
            g.drawRect(left, top, width, height);
        }

        private class MouseFollower extends MouseInputAdapter {
            @Override
            public void mousePressed(MouseEvent e) {
                left = e.getX();
                top = e.getY();
                width = 0;
                height = 0;
                ImagePanel.this.update(ImagePanel.this.getGraphics());
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                width = e.getX() - left;
                height = e.getY() - top;
                ImagePanel.this.update(ImagePanel.this.getGraphics());
            }
        }
    }
}
