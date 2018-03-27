package imagecolor;

import org.apache.commons.collections.Buffer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

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

        //img = crop(img);

        GUI display = new GUI("Image", img);
    }

    // Failed Method
    public static BufferedImage crop(BufferedImage img) throws IOException {
        System.out.println("xmax: " + img.getWidth() + ", y: " + img.getHeight());
        BufferedWriter file = new BufferedWriter(new FileWriter("loop.txt"));
        BufferedImage result = img;
        int x = 0;
        while (x < result.getWidth()) {
            boolean useless = true;
            for (int y = 0; y < result.getHeight(); y++) {
                int rgb = result.getRGB(x, y);
                if (rightColors(rgb)) {
                    useless = false;
                    break;
                }
            }
            if (useless) {
                if (x != 0) {
                    BufferedImage left = result.getSubimage(0, 0, x, result.getHeight());
                    BufferedImage right = result.getSubimage(x, 0, result.getWidth() - x - 1, result.getHeight());
                    result = concatH(left, right);
                } else {
                    result = result.getSubimage(1, 0, result.getWidth() - 1, result.getHeight());
                }
            } else {
                System.out.println("useful");
                x++;
            }
            file.write("x: " + x + "\n");
        }

        int y = 0;
        while (y < result.getHeight()) {
            boolean useless = true;
            for (x = 0; x < result.getWidth(); x++) {
                if (rightColors(result.getRGB(x, y))) {
                    useless = false;
                    break;
                }
            }
            if (useless) {
                if (y != 0) {
                    BufferedImage left = result.getSubimage(0, 0, result.getWidth(), y);
                    BufferedImage right = result.getSubimage(0, y, result.getWidth(), result.getHeight() - y - 1);
                    result = concatH(left, right);
                } else {
                    result = result.getSubimage(0, 1, result.getWidth(), result.getHeight() - 1);
                }
            }
            y++;
        }
        return result;
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

    public static BufferedImage concatH(BufferedImage l, BufferedImage r) {
        BufferedImage img = new BufferedImage(l.getWidth() + r.getWidth(), l.getHeight(), BufferedImage.TYPE_INT_RGB);
        img.createGraphics().drawImage(l, 0, 0, null);
        img.createGraphics().drawImage(r, l.getWidth(), 0, null);
        return img;
    }

    public static BufferedImage concatV(BufferedImage t, BufferedImage b) {
        BufferedImage img = new BufferedImage(t.getWidth(), t.getHeight() + b.getHeight(), BufferedImage.TYPE_INT_RGB);
        img.createGraphics().drawImage(t, 0, 0, null);
        img.createGraphics().drawImage(b, 0, t.getHeight(), null);
        return img;
    }
}
