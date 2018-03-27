package brickpop;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileChooser extends JFrame {
    public FileChooser() {
        BufferedImage img = null;
        while (img == null) {
            JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
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

        ImageProcessor processor = new ImageProcessor("Image Processing", img);
    }
}
