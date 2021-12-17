package test;

import main.Canvas;
import main.Color;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Canvas")
public class TestCanvas {
    @Test
    @DisplayName("Creating a canvas")
    void createCanvas() {
        Canvas c = new Canvas(10, 20);
        assertEquals(20, c.getHeight());
        assertEquals(10, c.getWidth());
        Color black = new Color(0,0,0);
        for(int x = 0; x < 10; x++) {
            for(int y = 0; y < 20; y++) {
                assertEquals(black, c.getPixel(x, y));
            }
        }
    }

    @Test
    @DisplayName("Writing Pixels to a canvas")
    void writeToCanvas() {
        Canvas c = new Canvas(10, 20);
        Color red = new Color(1,0,0);
        c.writePixel(2,3,red);
        assertEquals(red, c.getPixel(2, 3));
    }
    private String toPpmString(Canvas c) {
        ByteArrayOutputStream ppmStream = new ByteArrayOutputStream();
        c.writePPMStream(ppmStream);
        return ppmStream.toString();
    }

    @Test
    @DisplayName("Constructing the PPM header")
    void constructPPMHeader() {
        Canvas c = new Canvas(5,3);
        String[] ppmLines = toPpmString(c).split("\n");
        assertEquals("P3", ppmLines[0]);
        assertEquals("5 3", ppmLines[1]);
        assertEquals("255", ppmLines[2]);
    }
    @Test
    @DisplayName("Constructing the PPM Pixel Data")
    void constructPPMData() {
        Canvas c = new Canvas(5,3);
        Color c1 = new Color(1.5,0,0);
        Color c2 = new Color(0,.5,0);
        Color c3 = new Color(-.5, 0, 1);
        c.writePixel(0,0,c1);
        c.writePixel(2,1,c2);
        c.writePixel(4,2, c3);
        String[] ppmLines = toPpmString(c).split("\n");
        assertEquals("255 0 0 0 0 0 0 0 0 0 0 0 0 0 0", ppmLines[3]);
        assertEquals("0 0 0 0 0 0 0 128 0 0 0 0 0 0 0", ppmLines[4]);
        assertEquals("0 0 0 0 0 0 0 0 0 0 0 0 0 0 255", ppmLines[5]);
    }

    @Test
    @DisplayName("Splitting long lines in PPM files")
    void splitLongLines() {
        Canvas c = new Canvas(10, 2);
        Color c1 = new Color(1, 0.8, 0.6);
        for (int x = 0; x < 10; x++) {
            for(int y = 0; y < 2; y++) {
                c.writePixel(x,y,c1);
            }
        }
        String[] ppmLines = toPpmString(c).split("\n");
        assertEquals("255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204", ppmLines[3]);
        assertEquals("153 255 204 153 255 204 153 255 204 153 255 204 153", ppmLines[4]);
        assertEquals("255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204", ppmLines[5]);
        assertEquals("153 255 204 153 255 204 153 255 204 153 255 204 153", ppmLines[6]);

    }
    @Test
    @DisplayName("PPM files are terminated by a newline character") //Given c ← canvas(5, 3) When ppm ← canvas_to_ppm(c) Then ppm ends with a newline character
    void endsInNewLine() {
        Canvas c = new Canvas(5,3);
        String ppmString = toPpmString(c);
        assertEquals(ppmString.substring(ppmString.length()-1), "\n");
    }
}
