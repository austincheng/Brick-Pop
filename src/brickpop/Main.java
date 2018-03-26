package brickpop;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/** The main program for Brick Pop.
 *  @author Austin Cheng */
public class Main {
    /** Displays the Brick Pop to allow interaction. ARGS unused. */
    public static void main(String[] args) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("Current.png"));
        } catch (IOException e) {
            System.exit(1);
        }

        Board board = new Board(img);
        GUI display = new GUI("Brick Pop", board);
        display.display(true);
    }
}
