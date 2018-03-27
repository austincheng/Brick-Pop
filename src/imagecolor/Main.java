package imagecolor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * Main program for image processing GUI.
 * @author Austin Cheng
 */
public class Main {
    public static void main(String[] args) {
        int[] x = new int[10];
        for (int i = 0; i < x.length; i++) {
            x[i] = 26 + i * 59;
        }
        System.out.println(Arrays.toString(x));
        BufferedImage img;
        try {
            img = ImageIO.read(new File("Current.png"));
        } catch (IOException e) {
            return;
        }

        GUI display = new GUI("Image", img);
    }

    public static boolean rightColors(int rgb) {
        float[] hsb = new float[3];
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >>  8) & 0xFF;
        int b = (rgb      ) & 0xFF;
        Color.RGBtoHSB(r, g, b, hsb);

        float tone = hsb[0] * 360;
        float saturation = hsb[1];
        float brightness = hsb[2];

        if (357 < tone && tone < 358 && 0.57 < saturation && 0.58 < saturation && brightness > 0.99) {
            return true;
        } else if (37 < tone && tone < 38 && 0.78 < saturation && saturation < 0.79 && brightness > 0.99) {
            return true;
        } else if (172 < tone && tone < 173 && 0.92 < saturation && saturation < 0.93 && 0.69 < brightness && brightness < 0.7) {
            return true;
        } else if (213 < tone && tone < 214 && 0.67 < saturation && saturation < 0.68 && 0.94 < brightness && brightness < 0.95) {
            return true;
        } else if (272 < tone && tone < 273 && 0.53 < saturation && saturation < 0.54 && 0.93 < brightness && brightness < 0.94) {
            return true;
        } else if (34 < tone && tone < 35 && 0.23 < saturation && saturation < 0.24 && 0.59 < brightness && brightness < 0.6) {
            return true;
        } else {
            return false;
        }
    }
}
