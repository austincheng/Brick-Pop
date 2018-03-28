package brickpop;

import java.awt.Color;

/** Describes the different colors of bricks on a Brick Pop board.
 *  @author Austin Cheng
 */
public enum Colors {
    RED, YELLOW, GREEN, BLUE, PURPLE, DARK, EMPTY;

    @Override
    public String toString() {
        if (this == RED) {
            return "r";
        } else if (this == YELLOW) {
            return "y";
        } else if (this == GREEN) {
            return "g";
        } else if (this == BLUE) {
            return "b";
        } else if (this == PURPLE) {
            return "p";
        } else if (this == DARK) {
            return "d";
        } else {
            return "-";
        }
    }

    /** Returns the color of a string S. */
    public static Colors color(String s) {
        if (s.equals("r")) {
            return RED;
        } else if (s.equals("y")) {
            return YELLOW;
        } else if (s.equals("g")) {
            return GREEN;
        } else if (s.equals("b")) {
            return BLUE;
        } else if (s.equals("p")) {
            return PURPLE;
        } else if (s.equals("d")) {
            return DARK;
        } else if (s.equals("-")) {
            return EMPTY;
        } else {
            throw new IllegalArgumentException("unsupported string");
        }
    }

    /** Returns the color with the given RGB. */
    public static Colors color(int rgb) {
        float[] hsb = new float[3];
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >>  8) & 0xFF;
        int b = (rgb      ) & 0xFF;
        Color.RGBtoHSB(r, g, b, hsb);

        float tone = hsb[0] * 360;

        if (hsb[1] < 0.1 && hsb[2] > 0.9) {
            return EMPTY;
        }
        if (0 <= tone && tone < 30 || tone >= 330) {
            return RED;
        } else if (30 <= tone && tone < 90) {
            float saturation = hsb[1];
            if (saturation < 0.5) {
                return DARK;
            } else {
                return YELLOW;
            }
        } else if (90 <= tone && tone < 200) {
            return GREEN;
        } else if (200 <= tone && tone < 250) {
            return BLUE;
        } else {
            return PURPLE;
        }
    }
}
