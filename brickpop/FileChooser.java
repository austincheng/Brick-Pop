package brickpop;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/** JFrame representing initial file choosing option buttons.
 *  @author Austin Cheng
 */
public class FileChooser extends JFrame {
    public FileChooser(String title) {
        setTitle(title);
        setSize(500, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* Main Pane for entire JFrame. */
        Container cp = getContentPane();
        BoxLayout layout = new BoxLayout(cp, BoxLayout.Y_AXIS);
        cp.setLayout(layout);

        /* Standard Button. */
        JButton standard = new JButton("Perfectly Cropped Full Brick Pop Board");
        standard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedImage img = chooseImage();
                FileChooser.this.setVisible(false);
                Board board = new Board(img);
                Game game = new Game("Brick Pop", board);
            }
        });
        cp.add(standard);

        /* Standard Fast Cropping Button. */
        JButton standardNoisyFast = new JButton("Uncropped Full Brick Pop Board (fast cropping)");
        standardNoisyFast.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedImage img = chooseImage();
                FileChooser.this.setVisible(false);
                FastStandardImageProcessor processor = new FastStandardImageProcessor("Image Processing", img);
            }
        });
        cp.add(standardNoisyFast);

        /* Standard Precise Cropping Button. */
        JButton standardNoisyPrecise = new JButton("Uncropped Full Brick Pop Board (precise cropping)");
        standardNoisyPrecise.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedImage img = chooseImage();
                FileChooser.this.setVisible(false);
                PreciseStandardImageProcessor processor = new PreciseStandardImageProcessor("Image Processing", img);
            }
        });
        cp.add(standardNoisyPrecise);

        /* Custom Button. */
        JButton custom = new JButton("Custom Brick Pop Board");
        custom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedImage img = chooseImage();
                FileChooser.this.setVisible(false);
                CustomImageProcessor processor = new CustomImageProcessor("Image Processing", img);
            }
        });
        cp.add(custom);

        setVisible(true);
    }

    /** Prompts the user to select an image and returns that image. */
    private BufferedImage chooseImage() {
        BufferedImage img = null;

        while (img == null) {
            JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
            int returnValue = fileChooser.showOpenDialog(null);
            File selectedFile = null;
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
            } else {
                System.exit(0);
            }
            try {
                img = ImageIO.read(selectedFile);
            } catch (IOException e) {

            }
            if (img == null) {
                JOptionPane.showMessageDialog(this, "Cannot read image. Please select another file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        return img;
    }
}
