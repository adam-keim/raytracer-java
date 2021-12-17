package test;

import main.*;
import main.shapes.Plane;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Planes")
public class TestPlanes {
    @Test
    @DisplayName("The normal of a plane is constant everywhere")
    void constantNormal() {
        Plane p = new Plane();
        Vector n1 = p.localNormalAt(new Point(0, 0, 0), null);
        Vector n2 = p.localNormalAt(new Point(10, 0, -10), null);
        Vector n3 = p.localNormalAt(new Point(-5, 0, 150), null);

        assertEquals(n1, new Vector(0, 1, 0));
        assertEquals(n2, new Vector(0, 1, 0));
        assertEquals(n3, new Vector(0, 1, 0));
    }

    @Test
    @DisplayName("Intersect with a ray parallel to the plane")
    void intersectParallel() {
        Plane p = new Plane();
        Ray r = new Ray(new Point(0, 10, 0), new Vector(0, 0, 1));
        ArrayList<Intersection> xs = p.localIntersect(r);
        assertEquals(0, xs.size());
    }

    @Test
    @DisplayName("Intersect with a coplanar ray")
    void intersectCoplanar() {
        Plane p = new Plane();
        Ray r = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        ArrayList<Intersection> xs = p.localIntersect(r);
        assertEquals(0, xs.size());
    }

    @Test
    @DisplayName("A ray intersecting a plane from above")
    void intersectFromAbove() {
        Plane p = new Plane();
        Ray r = new Ray(new Point(0,1,0), new Vector(0,-1,0));
        ArrayList<Intersection> xs = p.localIntersect(r);
        assertEquals(1, xs.size());
        assertEquals(1, xs.get(0).t);
        assertEquals(xs.get(0).object, p);
    }

    @Test
    @DisplayName("A ray intersecting a plane from below")
    void intersectFromBelow() {
        Plane p = new Plane();
        Ray r = new Ray(new Point(0,-1,0), new Vector(0,1,0));
        ArrayList<Intersection> xs = p.localIntersect(r);
        assertEquals(1, xs.size());
        assertEquals(1, xs.get(0).t);
        assertEquals(xs.get(0).object, p);
    }

    @Test
    @DisplayName("A plane has a bounding box")
    void boundPlane() {
        Plane s = new Plane();
        BoundingBox box = s.boundsOf();
        assertEquals(new Point(Double.NEGATIVE_INFINITY,0,Double.NEGATIVE_INFINITY), box.min);
        assertEquals(new Point(Double.POSITIVE_INFINITY,0,Double.POSITIVE_INFINITY), box.max);
    }
}
