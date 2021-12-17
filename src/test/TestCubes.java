package test;


import main.*;
import main.shapes.Cube;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cubes")
public class TestCubes {
    @Test
    @DisplayName("A ray intersects a cube")
    void rayCube() {
        Point[] origins = {
                new Point(5, 0.5, 0),
                new Point(-5, 0.5, 0),
                new Point(.5, 5, 0),
                new Point(0.5, -5, 0),
                new Point(0.5, 0, 5),
                new Point(0.5, 0, -5),
                new Point(0, 0.5, 0)
        };
        Vector[] directions = {
                new Vector(-1, 0, 0),
                new Vector(1, 0, 0),
                new Vector(0, -1, 0),
                new Vector(0, 1, 0),
                new Vector(0, 0, -1),
                new Vector(0, 0, 1),
                new Vector(0, 0, 1)
        };
        double[] t1s = {4, 4, 4, 4, 4, 4, -1};
        double[] t2s = {6, 6, 6, 6, 6, 6, 1};
        Cube c = new Cube();
        for (int i = 0; i < origins.length; i++) {
            Ray r = new Ray(origins[i], directions[i]);
            ArrayList<Intersection> xs = c.localIntersect(r);
            assertEquals(2, xs.size());
            assertEquals(xs.get(0).t, t1s[i]);
            assertEquals(xs.get(1).t, t2s[i]);
        }

    }

    @Test
    @DisplayName("A ray misses a cube")
    void rayMiss() {
        Cube c = new Cube();
        Point[] points = {
                new Point(-2, 0, 0),
                new Point(0, -2, 0),
                new Point(0, 0, -2),
                new Point(2, 0, 2),
                new Point(0, 2, 2),
                new Point(2, 2, 0)
        };
        Vector[] vectors = {
                new Vector(0.2673, 0.5345, 0.8018),
                new Vector(0.8018, 0.2673, 0.5345),
                new Vector(0.5345, 0.8018, 0.2673),
                new Vector(0,0,-1),
                new Vector(0,-1,0),
                new Vector(-1,0,0)
        };

        for(int i = 0; i < points.length; i++) {
            Ray r = new Ray(points[i], vectors[i]);
            ArrayList<Intersection> xs = c.localIntersect(r);
            assertEquals(0, xs.size());
        }
    }

    @Test
    @DisplayName("The normal on the surface of a cube")
    void normalSurface() {
        Point[] points = {
                new Point(1, 0.5, -0.8),
                new Point(-1,-0.2, 0.9),
                new Point(-0.4, 1, -0.1),
                new Point(.3, -1, -.7),
                new Point(-.6, .3, 1),
                new Point(.4, .4, -1),
                new Point(1,1,1),
                new Point(-1,-1,-1)
        };
        Vector[] normals = {
                new Vector(1,0,0),
                new Vector(-1,0,0),
                new Vector(0,1,0),
                new Vector(0,-1,0),
                new Vector(0,0,1),
                new Vector(0,0,-1),
                new Vector(1,0,0),
                new Vector(-1,0,0),
        };
        Cube c = new Cube();
        for(int i = 0; i < points.length; i++) {
            Vector normal = c.localNormalAt(points[i], null);
            assertEquals(normals[i], normal);
        }
    }

    @Test
    @DisplayName("A cube has a bounding box")
    void boundCube() {
        Cube s = new Cube();
        BoundingBox box = s.boundsOf();
        assertEquals(new Point(-1,-1,-1), box.min);
        assertEquals(new Point(1,1,1), box.max);
    }
}
