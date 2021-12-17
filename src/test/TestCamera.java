package test;

import main.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Camera")
public class TestCamera {

    @Test
    @DisplayName("Constructing a camera")
    void cameraBasic() {
        int hsize = 160;
        int vsize = 120;
        double fov = Math.PI / 2;
        Camera c = new Camera(hsize, vsize, fov);
        assertEquals(160, c.hsize);
        assertEquals(120, c.vsize);
        assertEquals(Math.PI / 2, c.fov);
        assertEquals(c.getTransform(), Matrix.identity());
    }

    private void assertFuzzyEquals(double b) {
        assertTrue(Util.EQUALS(0.01, b));
    }

    @Test
    @DisplayName("The pixel size for a hosizontal canvas")
    void hcanvasPixelSize() {
        Camera c = new Camera(200, 125, Math.PI / 2);
        assertFuzzyEquals(c.pixelSize);
    }


    @Test
    @DisplayName("The pixel size for a vertical canvas")
    void vcanvasPixelSize() {
        Camera c = new Camera(125, 200, Math.PI / 2);
        assertFuzzyEquals(c.pixelSize);
    }

    @Test
    @DisplayName("Constructing a ray through the center of the canvas")
    void centerCanvasRay() {
        Camera c = new Camera(201, 101, Math.PI / 2);
        Ray r = c.ray(100, 50);
        assertEquals(r.origin, new Point(0, 0, 0));
        assertEquals(r.direction, new Vector(0, 0, -1));
    }

    @Test
    @DisplayName("Constructing a ray through a corner of the canvas")
    void cornerCanvasRay() {
        Camera c = new Camera(201, 101, Math.PI / 2);
        Ray r = c.ray(0, 0);
        assertEquals(r.origin, new Point(0, 0, 0));
        assertEquals(r.direction, new Vector(0.66519, 0.33259, -0.66851));
    }

    @Test
    @DisplayName("Constructing a ray when the camera is transformed")
    void transformCanvasRay() {
        Camera c = new Camera(201, 101, Math.PI / 2);
        c.setTransform(Matrix.rotationY(Math.PI / 4).multiply(Matrix.translation(0, -2, 5)));
        Ray r = c.ray(100, 50);
        assertEquals(r.origin, new Point(0, 2, -5));
        assertEquals(r.direction, new Vector(Math.sqrt(2) / 2, 0, -Math.sqrt(2) / 2));
    }

    @Test
    @DisplayName("Rendering a world with a camera")
    void renderWorld() {
        World w = World.defaultWorld();
        Camera c = new Camera(11,11,Math.PI/2);

        Point from = new Point(0,0,-5);
        Point to = new Point(0,0,0);
        Vector up = new Vector(0,1,0);
        c.setTransform(Matrix.viewTransform(from, to, up));
        Canvas image = c.render(w);
        assertEquals(image.getPixel(5, 5), new Color(.38066, .47583, .2855));


    }

}
