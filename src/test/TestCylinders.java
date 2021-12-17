package test;


import main.*;
import main.shapes.Cylinder;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cylinders")
public class TestCylinders {
    @Test
    @DisplayName("A ray misses a cylinder")
    void rayMiss() {
        Point[] origins = {
                new Point(1, 0, 0),
                new Point(0, 0, 0),
                new Point(0, 0, -5)
        };
        Vector[] directions = {
                new Vector(0, 1, 0),
                new Vector(0, 1, 0),
                new Vector(1, 1, 1)
        };
        Cylinder cyl = new Cylinder();
        for (int i = 0; i < origins.length; i++) {
            Vector direction = directions[i].normalize();
            Ray r = new Ray(origins[i], direction);
            ArrayList<Intersection> xs = cyl.localIntersect(r);
            assertEquals(0, xs.size());
        }
    }

    @Test
    @DisplayName("A ray strikes a cylinder")
    void rayHit() {
        Point[] origins = {
                new Point(1, 0, -5),
                new Point(0, 0, -5),
                new Point(0.5, 0, -5)
        };
        Vector[] directions = {
                new Vector(0, 0, 1),
                new Vector(0, 0, 1),
                new Vector(.1, 1, 1)
        };
        double[] t0s = {5, 4, 6.80798};
        double[] t1s = {5, 6, 7.08872};
        Cylinder cyl = new Cylinder();

        for (int i = 0; i < origins.length; i++) {
            Vector direction = directions[i].normalize();
            Ray r = new Ray(origins[i], direction);
            ArrayList<Intersection> xs = cyl.localIntersect(r);
            assertEquals(2, xs.size());
            assertTrue(Util.EQUALS(t0s[i], xs.get(0).t));
            assertTrue(Util.EQUALS(t1s[i], xs.get(1).t));
        }
    }

    @Test
    @DisplayName("Normal vector on a Cylinder")
    void normalVector() {
        Point[] points = {
                new Point(1, 0, 0),
                new Point(0, 5, -1),
                new Point(0, -2, 1),
                new Point(-1, 1, 0)
        };

        Vector[] normals = {
                new Vector(1, 0, 0),
                new Vector(0, 0, -1),
                new Vector(0, 0, 1),
                new Vector(-1, 0, 0)
        };

        Cylinder cyl = new Cylinder();
        for (int i = 0; i < points.length; i++) {
            Vector n = cyl.localNormalAt(points[i], null);
            assertEquals(normals[i], n);
        }
    }

    @Test
    @DisplayName("The default minimum and maximum for a cylinder")
    void minMax() {
        Cylinder cyl = new Cylinder();
        assertEquals(Double.NEGATIVE_INFINITY, cyl.minimum);
        assertEquals(Double.POSITIVE_INFINITY, cyl.maximum);
    }

    @Test
    void constrainedCyl() {
        Point[] origins = {
                new Point(0, 1.5, 0),
                new Point(0, 3, -5),
                new Point(0, 0, -5),
                new Point(0, 2, -5),
                new Point(0, 1, -5),
                new Point(0, 1.5, -2)
        };

        Vector[] directions = {
                new Vector(0.1, 1, 0),
                new Vector(0, 0, 1),
                new Vector(0, 0, 1),
                new Vector(0, 0, 1),
                new Vector(0, 0, 1),
                new Vector(0, 0, 1),
        };

        double[] counts = {0,0,0,0,0,2};
        Cylinder cyl = new Cylinder();
        cyl.minimum = 1;
        cyl.maximum = 2;
        for(int i = 0; i < origins.length; i++) {
            Vector direction = directions[i].normalize();
            Ray r = new Ray(origins[i], direction);
            ArrayList<Intersection> xs = cyl.localIntersect(r);
            assertEquals(counts[i], xs.size());
        }
    }

    @Test
    @DisplayName("The default closed value for a cylinder")
    void defaultClosed() {
        Cylinder cyl = new Cylinder();
        assertFalse(cyl.closed);
    }

    @Test
    @DisplayName("Intersecting the caps of a closed cylinder")
    void closedIntersect() {
        Point[] origins = {
                new Point(0, 3, 0),
                new Point(0, 3, -2),
                new Point(0, 4, -2),
                new Point(0, 0, -2),
                new Point(0, 1, -2)
        };

        Vector[] directions = {
                new Vector(0, -1, 0),
                new Vector(0, -1, 2),
                new Vector(0, -1, 1),
                new Vector(0, 1, 2),
                new Vector(0, 1, 1),
        };

        double[] counts = {2,2,2,2,2};

        Cylinder cyl = new Cylinder();
        cyl.maximum = 2;
        cyl.minimum = 1;
        cyl.closed = true;
        for(int i = 0; i < origins.length; i++) {
            Vector direction = directions[i].normalize();
            Ray r = new Ray(origins[i], direction);
            ArrayList<Intersection> xs = cyl.localIntersect(r);
            assertEquals(counts[i], xs.size());
        }
    }

    @Test
    @DisplayName("The normal vector on a cylinder's end caps")
    void normalEnd() {
        Point[] points = {
                new Point(0, 1, 0),
                new Point(.5, 1, 0),
                new Point(0, 1, .5),
                new Point(0, 2, 0),
                new Point(.5, 2, 0),
                new Point(0, 2, .5)
        };

        Vector[] normals = {
                new Vector(0, -1, 0),
                new Vector(0, -1, 0),
                new Vector(0, -1, 0),
                new Vector(0, 1, 0),
                new Vector(0, 1, 0),
                new Vector(0, 1, 0)
        };

        Cylinder cyl = new Cylinder();
        cyl.maximum = 2;
        cyl.minimum = 1;
        cyl.closed = true;
        for(int i = 0; i < points.length; i++) {
           Vector n = cyl.localNormalAt(points[i], null);
           assertEquals(normals[i], n);
        }
    }

    @Test
    @DisplayName("An unbounded cylinder has a bounding box")
    void boundCylUnbounded() {
        Cylinder s = new Cylinder();
        BoundingBox box = s.boundsOf();
        assertEquals(new Point(-1,Double.NEGATIVE_INFINITY,-1), box.min);
        assertEquals(new Point(1,Double.POSITIVE_INFINITY,1), box.max);
    }

    @Test
    @DisplayName("A bounded cylinder has a bounding box")
    void boundCylBounded() {
        Cylinder s = new Cylinder();
        s.minimum = -5;
        s.maximum = 3;
        BoundingBox box = s.boundsOf();
        assertEquals(new Point(-1,-5,-1), box.min);
        assertEquals(new Point(1,3,1), box.max);
    }
}
