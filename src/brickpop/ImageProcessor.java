package brickpop;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class ImageProcessor extends JFrame{
    private static final String[] instructions = {
            "Click on the left-most edge of the brick pop board.",
            "Click on the right-most edge of the brick pop board. (Include the shadow)",
            "Click on the top-most edge of the brick pop board.",
            "Click on the left-most edge of any brick excluding right-most bricks.",
            "Click on the right-most edge of that brick. (Don't include the shadow)",
            "Click on the left-most edge of the brick immediately to the right of that brick."
    };
    private int[] dimensions;
    private BufferedImage _img;
    private int count;
    private JLabel instructionLabel;

    public ImageProcessor(String title, BufferedImage img) {
        dimensions = new int[6];
        _img = img;
        count = 0;

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

        /* Score label inside bottom pane. */
        instructionLabel = new JLabel(instructions[count]);
        bottom.add(instructionLabel);

        cp.add(bottom);

        /* Menu bar at top. */
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");

        /* Undo option. */
        JMenuItem undo = new JMenuItem("Undo");
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (count >= 1) {
                    count--;
                    instructionLabel.setText(instructions[count]);
                }
            }
        });
        menu.add(undo);

        menuBar.add(menu);
        setJMenuBar(menuBar);

        setVisible(true);
    }

    private class ImagePanel extends JPanel implements MouseListener {
        public ImagePanel() {
            addMouseListener(this);
        }
        @Override
        protected void paintComponent(Graphics g) {
            g.drawImage(_img, 0, 0, null);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            if (count == 2) {
                dimensions[count] = y;
            } else {
                dimensions[count] = x;
            }

            count++;
            if (count < 6) {
                instructionLabel.setText(instructions[count]);
            } else {
                ImageProcessor.this.setVisible(false);
                Board board = new Board(_img, dimensions[5] - dimensions[3], (dimensions[4] - dimensions[3]) / 2);
                Game game = new Game("Brick Pop", board);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }
    }
}
