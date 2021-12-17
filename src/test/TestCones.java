package test;

import main.*;
import main.shapes.Cone;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cones")
public class TestCones {
    @Test
    @DisplayName("Intersecting a cone with a ray")
    void rayHit() {
        Point[] origins = {
                new Point(0, 0, -5),
                new Point(0, 0, -5),
                new Point(1, 1, -5)
        };
        Vector[] directions = {
                new Vector(0, 0, 1),
                new Vector(1, 1, 1),
                new Vector(-.5, -1, 1)
        };
        double[] t0s = {5, 8.66025, 4.55006};
        double[] t1s = {5, 8.66025, 49.44994};


        Cone cone = new Cone();

        for (int i = 0; i < origins.length; i++) {
            Vector direction = directions[i].normalize();
            Ray r = new Ray(origins[i], direction);
            ArrayList<Intersection> xs = cone.localIntersect(r);
            assertEquals(2, xs.size());
            assertTrue(Util.EQUALS(t0s[i], xs.get(0).t));
            assertTrue(Util.EQUALS(t1s[i], xs.get(1).t));
        }
    }

    @Test
    @DisplayName("Intersecting a cone with a ray parallel to one of its halves")
    void parallelRay() {
        Cone s = new Cone();
        Vector direction = new Vector(0,1,1).normalize();
        Ray r = new Ray(new Point(0,0,-1), direction);
        ArrayList<Intersection> xs = s.localIntersect(r);
        assertEquals(1, xs.size());
        assertTrue(Util.EQUALS(.35355, xs.get(0).t));
    }

    @Test
    @DisplayName("Intersecting a cone's end caps")
    void endCaps() {
        Point[] origins = new Point[] {
                new Point(0,0,-5),
                new Point(0,0,-.25),
                new Point(0,0,-.25)
        };
        Vector[] directions = new Vector[] {
                new Vector(0,1,0),
                new Vector(0,1,1),
                new Vector(0,1,0)
        };
        int[] counts = {0,2,4};

        Cone s = new Cone();
        s.minimum = -.5;
        s.maximum = 0.5;
        s.closed = true;

        for (int i = 0; i < origins.length; i++) {
            Vector direction = directions[i].normalize();
            Ray r = new Ray(origins[i], direction);
            ArrayList<Intersection> xs= s.localIntersect(r);
            assertEquals(counts[i], xs.size());
        }

    }

    @Test
    @DisplayName("Computing the normal vector on a cone")
    void coneNormal() {
        Point[] points = new Point[] {
                new Point(0,0,0),
                new Point(1,1,1),
                new Point(-1,-1,0)
        };
        Vector[] normals = new Vector[] {
                new Vector(0,0,0),
                new Vector(1,-Math.sqrt(2),1),
                new Vector(-1,1,0)
        };
        Cone c = new Cone();
        for(int i = 0; i < points.length; i++) {
            Vector n = c.localNormalAt(points[i], null);
            assertEquals(normals[i], n);
        }
    }

    @Test
    @DisplayName("An unbounded cone has a bounding box")
    void boundConeUnbounded() {
        Cone s = new Cone();
        BoundingBox box = s.boundsOf();
        assertEquals(new Point(Double.NEGATIVE_INFINITY,Double.NEGATIVE_INFINITY,Double.NEGATIVE_INFINITY), box.min);
        assertEquals(new Point(Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY), box.max);
    }

    @Test
    @DisplayName("A bounded cone has a bounding box")
    void boundConeBounded() {
        Cone s = new Cone();
        s.minimum = -5;
        s.maximum =3;
        BoundingBox box = s.boundsOf();
        assertEquals(new Point(-5,-5,-5), box.min);
        assertEquals(new Point(5,3,5), box.max);
    }

}
