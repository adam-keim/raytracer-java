package test;

import main.*;
import main.shapes.Triangle;
import org.junit.jupiter.api.*;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Triangles")
public class TestTriangles {
    @Test
    @DisplayName("Constructing a Triangle")
    void newTriangle() {
        Point p1 = new Point(0, 1, 0);
        Point p2 = new Point(-1, 0, 0);
        Point p3 = new Point(1, 0, 0);
        Triangle t = new Triangle(p1, p2, p3);
        assertEquals(p1, t.p1);
        assertEquals(p2, t.p2);
        assertEquals(p3, t.p3);
        assertEquals(new Vector(-1, -1, 0), t.e1);
        assertEquals(new Vector(1, -1, 0), t.e2);
        assertEquals(new Vector(0, 0, -1), t.normal);
    }

    @Test
    @DisplayName("Finding the normal on a triangle")
    void triangleNormal() {
        Triangle t = new Triangle(new Point(0, 1, 0), new Point(-1, 0, 0), new Point(1, 0, 0));
        Vector n1 = t.localNormalAt(new Point(0, 0.5, 0), null);
        Vector n2 = t.localNormalAt(new Point(-0.5, 0.75, 0), null);
        Vector n3 = t.localNormalAt(new Point(.5, 0.25, 0), null);
        assertEquals(t.normal, n1);
        assertEquals(t.normal, n2);
        assertEquals(t.normal, n3);
    }

    @Test
    @DisplayName("Intersecting a ray parallel to the triangle")
    void parallelIntersect() {
        Triangle t = new Triangle(new Point(0, 1, 0), new Point(-1, 0, 0), new Point(1, 0, 0));
        Ray r = new Ray(new Point(0, -1, -2), new Vector(0, 1, 0));
        ArrayList<Intersection> xs = t.localIntersect(r);
        assertEquals(0, xs.size());
    }

    @Test
    @DisplayName("A ray misses the p1-p3 edge")
    void rayMissP1P3() {
        Triangle t = new Triangle(new Point(0, 1, 0), new Point(-1, 0, 0), new Point(1, 0, 0));
        Ray r = new Ray(new Point(1, -1, -2), new Vector(0, 0, 1));
        ArrayList<Intersection> xs = t.localIntersect(r);
        assertEquals(0, xs.size());
    }

    @Test
    @DisplayName("A ray misses the p1-p2 edge")
    void rayMissP1P2() {
        Triangle t = new Triangle(new Point(0, 1, 0), new Point(-1, 0, 0), new Point(1, 0, 0));
        Ray r = new Ray(new Point(-1, -1, -2), new Vector(0, 0, 1));
        ArrayList<Intersection> xs = t.localIntersect(r);
        assertEquals(0, xs.size());
    }

    @Test
    @DisplayName("A ray misses the p2-p3 edge")
    void rayMissP2P3() {
        Triangle t = new Triangle(new Point(0, 1, 0), new Point(-1, 0, 0), new Point(1, 0, 0));
        Ray r = new Ray(new Point(0, -1, -2), new Vector(0, 0, 1));
        ArrayList<Intersection> xs = t.localIntersect(r);
        assertEquals(0, xs.size());
    }

    @Test
    @DisplayName("A ray strikes a triangle")
    void rayHit() {
        Triangle t = new Triangle(new Point(0, 1, 0), new Point(-1, 0, 0), new Point(1, 0, 0));
        Ray r = new Ray(new Point(0, 0.5, -2), new Vector(0, 0, 1));
        ArrayList<Intersection> xs = t.localIntersect(r);
        assertEquals(1, xs.size());
        assertEquals(2, xs.get(0).t);
    }

    @Test
    @DisplayName("A triangle has a bounding box")
    void boundTriangle() {
        Point p1 = new Point(-3, 7, 2);
        Point p2 = new Point(6,2,-4);
        Point p3 = new Point(2,-1,-1);
        Triangle s = new Triangle(p1,p2,p3);
        BoundingBox box = s.boundsOf();
        assertEquals(new Point(-3,-1,-4), box.min);
        assertEquals(new Point(6,7,2), box.max);
    }

}
