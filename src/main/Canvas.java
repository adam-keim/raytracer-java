package main;

import java.io.*;

public class Canvas {
    private final int width;
    private final int height;
    private final Color[][] pixels;

    public Canvas(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new Color[width][height];
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                pixels[x][y] = new Color(0, 0, 0);

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color getPixel(int x, int y) {
        return pixels[x][y];
    }

    public void writePixel(int x, int y, Color c) {
        pixels[x][y] = c;
    }

    private int clamp(double value) {
        if (value <= 0) {
            return 0;
        } else if (value >= 1) {
            return 255;
        } else {
            return (int) (value * 256);
        }
    }

    private String toPPMPixel(Color c) {
        return String.format("%d %d %d", clamp(c.red), clamp(c.green), clamp(c.blue));
    }

    private String addLineBreaks(String input) {
        int lengthLimit = 70;
        StringBuilder output = new StringBuilder();
        int currentIndex = 0;
        int lastIndex = input.length();
        while (lastIndex - currentIndex > lengthLimit) {
            int spaceIndex = currentIndex + lengthLimit;
            while (input.charAt(spaceIndex) != ' ') {
                spaceIndex--;
            }
            output.append(input, currentIndex, spaceIndex);
            output.append("\n");
            currentIndex = spaceIndex + 1;
        }
        output.append(input.substring(currentIndex));
        return output.toString();
    }

    public void writePPMStream(OutputStream outStream) {
        PrintStream out = new PrintStream(outStream);
        out.println("P3");
        out.printf("%d %d%n", this.width, this.height);
        out.println("255");
        for (int y = 0; y < height; y++) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < width; x++) {
                line.append(toPPMPixel(getPixel(x, y)));
                if (x < width - 1) {
                    line.append(" ");
                }
            }
            out.println(addLineBreaks(line.toString()));
        }
    }

    public void writeFile(String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            this.writePPMStream(fos);
            fos.close();
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }
}
