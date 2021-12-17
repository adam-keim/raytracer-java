package test;

import main.*;
import main.shapes.SmoothTriangle;
import main.shapes.Triangle;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Smooth Triangles")
public class TestSmoothTriangles {
    Point p1 = new Point(0,1,0);
    Point p2 = new Point(-1,0,0);
    Point p3 = new Point(1,0,0);
    Vector n1 = new Vector(0,1,0);
    Vector n2 = new Vector(-1, 0, 0);
    Vector n3 = new Vector(1,0,0);
    SmoothTriangle tri = new SmoothTriangle(p1,p2,p3,n1,n2,n3);

    @Test
    @DisplayName("Constructing a smooth triangle")
    void constructTri() {
        assertEquals(p1, tri.p1);
        assertEquals(p2, tri.p2);
        assertEquals(p3, tri.p3);
        assertEquals(n1, tri.n1);
        assertEquals(n2, tri.n2);
        assertEquals(n3, tri.n3);
    }

    @Test
    @DisplayName("An intersection can encapsulate 'u' and 'v'")
    void encapsulateUV() {
        Triangle s = new Triangle(new Point(0,1,0), new Point(-1,0,0), new Point(1,0,0));
        Intersection i = new Intersection(3.5, s, .2, .4);
        assertEquals(.2, i.u);
        assertEquals(.4, i.v);
    }

    @Test
    @DisplayName("An intersection with a smooth tringle stores u/v")
    void storeUV() {
        Ray r = new Ray(new Point(-0.2, 0.3, -2), new Vector(0,0,1));
        ArrayList<Intersection> xs = tri.localIntersect(r);
        assertTrue(Util.EQUALS(.45, xs.get(0).u));
        assertTrue(Util.EQUALS(.25, xs.get(0).v));
    }

    @Test
    @DisplayName("A smooth triangle uses uv to interpolate the normal")
    void interpolateNormal() {
        Intersection i = new Intersection(1, tri, .45, .25);
        Vector n = tri.normalAt(new Point(0,0,0), i);
        assertEquals(new Vector(-0.5547, 0.83205, 0), n);
    }

    // TODO: Finish smooth triangle implementation
}
