package imagecolor;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

/**
 * GUI for analyzing image positioning and color.
 *  @author Austin Cheng
 */
public class GUI extends JFrame {
    private BufferedImage _img;

    public GUI(String title, BufferedImage img) {
        _img = img;
        setSize(img.getWidth() + 20, img.getHeight() + 80);
        setTitle(title);
        setContentPane(new ImagePanel());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /** Image panel inside GUI. */
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
            System.out.println("x: " + x + ", y: " + y);
            int rgb = _img.getRGB(x, y);
            float hsb[] = new float[3];
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >>  8) & 0xFF;
            int b = (rgb      ) & 0xFF;
            Color.RGBtoHSB(r, g, b, hsb);

            float tone = hsb[0] * 360;
            System.out.println("tone: " + tone + ", saturation: " + hsb[1] + ", brightness: " + hsb[2]);
            System.out.println("r: " + r + ", g: " + g + ", b: " + b);
            System.out.println();
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
