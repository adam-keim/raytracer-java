package test;

import main.Matrix;
import main.Point;
import main.Ray;
import main.Vector;
import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Rays")
public class TestRays {
    @Test
    @DisplayName("Creating and querying a ray")
    void basicRay() {
        Point origin = new Point(1, 2, 3);
        Vector direction = new Vector(4, 5, 6);
        Ray r = new Ray(origin, direction);
        assertEquals(r.origin, origin);
        assertEquals(r.direction, direction);
    }

    @Test
    @DisplayName("Computing a point from a distance")
    void distanceRay() {
        Ray r = new Ray(new Point(2,3,4), new Vector(1,0,0));
        assertEquals(r.position(0), new Point(2, 3, 4));
        assertEquals(r.position(1), new Point(3, 3, 4));
        assertEquals(r.position(-1), new Point(1, 3, 4));
        assertEquals(r.position(2.5), new Point(4.5, 3, 4));
    }

    @Test
    @DisplayName("Translating a ray")
    void rayTranslate() {
        Ray r = new Ray(new Point(1,2,3), new Vector(0,1,0));
        Matrix m = Matrix.translation(3,4,5);
        Ray r2 = r.transform(m);

        assertEquals(r2.origin, new Point(4, 6, 8));
        assertEquals(r2.direction, new Vector(0, 1, 0));

    }
    @Test
    @DisplayName("Scaling a ray")
    void rayScale() {
        Ray r = new Ray(new Point(1,2,3), new Vector(0,1,0));
        Matrix m = Matrix.scaling(2,3,4);
        Ray r2 = r.transform(m);

        assertEquals(r2.origin, new Point(2, 6, 12));
        assertEquals(r2.direction, new Vector(0, 3, 0));

    }

}
