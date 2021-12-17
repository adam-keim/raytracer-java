package test;

import main.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bounds")
public class TestBounds {
    @Test
    @DisplayName("Creating an empty bounding box")
    void emptyBox() {
        BoundingBox box = new BoundingBox();
        assertEquals(new Point(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), box.min);
        assertEquals(new Point(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY), box.max);
    }

    @Test
    @DisplayName("Creating a bounding box with volume")
    void volumeBox() {
        BoundingBox box = new BoundingBox(new Point(-1, -2, -3), new Point(3, 2, 1));
        assertEquals(new Point(-1, -2, -3), box.min);
        assertEquals(new Point(3, 2, 1), box.max);
    }

    @Test
    @DisplayName("Adding points to an empty bounding box")
    void addPoints() {
        BoundingBox box = new BoundingBox();
        Point p1 = new Point(-5, 2, 0);
        Point p2 = new Point(7, 0, -3);
        box.addPoint(p1);
        box.addPoint(p2);
        assertEquals(new Point(-5, 0, -3), box.min);
        assertEquals(new Point(7, 2, 0), box.max);
    }

    @Test
    @DisplayName("Adding one bounding box to another")
    void addBox() {
        BoundingBox box1 = new BoundingBox(new Point(-5, 2, 0), new Point(7, 4, 4));
        BoundingBox box2 = new BoundingBox(new Point(8, -7, -2), new Point(14, 2, 8));
        box1.addBox(box2);
        assertEquals(new Point(-5, -7, -2), box1.min);
        assertEquals(new Point(14, 4, 8), box1.max);
    }

    @Test
    @DisplayName("Checking to see if a box contains a given point")
    void boxContains() {
        Point[] points = {
                new Point(5, -2, 0),
                new Point(11, 4, 7),
                new Point(8, 1, 3),
                new Point(3, 0, 3),
                new Point(8, -4, 3),
                new Point(8, 1, -1),
                new Point(13, 1, 3),
                new Point(8, 5, 3),
                new Point(8, 1, 8)
        };
        boolean[] results = {true, true, true, false, false, false, false, false, false};
        BoundingBox box = new BoundingBox(new Point(5, -2, 0), new Point(11, 4, 7));
        for (int i = 0; i < points.length; i++) {
            assertEquals(results[i], box.containsPoint(points[i]));
        }
    }

    @Test
    @DisplayName("Checking to see if a box contains a given box")
    void boxContainsBox() {
        Point[] mins = {
                new Point(5, -2, 0),
                new Point(6, -1, 1),
                new Point(4, -3, -1),
                new Point(6, -1, 1)
        };
        Point[] maxs = {
                new Point(11, 4, 7),
                new Point(10, 3, 6),
                new Point(10, 3, 6),
                new Point(12, 5, 8)
        };
        boolean[] results = {true, true, false, false};
        BoundingBox box1 = new BoundingBox(new Point(5, -2, 0), new Point(11, 4, 7));
        for (int i = 0; i < mins.length; i++) {
            BoundingBox box2 = new BoundingBox(mins[i], maxs[i]);
            assertEquals(results[i], box1.containsBox(box2));
        }

    }

    @Test
    @DisplayName("Transforming a bounding box")
    void transformBox() {
        BoundingBox box = new BoundingBox(new Point(-1, -1, -1), new Point(1, 1, 1));
        Matrix t = Matrix.rotationX(Math.PI / 4).multiply(Matrix.rotationY(Math.PI / 4));
        BoundingBox box2 = box.transform(t);
        assertEquals(new Point(-1.41421, -1.7071, -1.7071), box2.min);
        assertEquals(new Point(1.41421, 1.7071, 1.7071), box2.max);
    }

    @Test
    @DisplayName("Intersecting a ray with a bounding box at the origin")
    void rayIntersect() {
        Point[] origins = {
                new Point(5, 0.5, 0),
                new Point(-5, 0.5, 0),
                new Point(0.5, 5, 0),
                new Point(0.5, -5, 0),
                new Point(0.5, 0, 5),
                new Point(0.5, 0, -5),
                new Point(0, 0.5, 0),
                new Point(-2, 0, 0),
                new Point(0, -2, 0),
                new Point(0, 0, -2),
                new Point(2, 0, 2),
                new Point(0, 2, 2),
                new Point(2, 2, 0)
        };
        Vector[] directions = {
                new Vector(-1, 0, 0),
                new Vector(1, 0, 0),
                new Vector(0, -1, 0),
                new Vector(0, 1, 0),
                new Vector(0, 0, -1),
                new Vector(0, 0, 1),
                new Vector(0, 0, 1),
                new Vector(2, 4, 6),
                new Vector(6, 2, 4),
                new Vector(4, 6, 2),
                new Vector(0, 0, -1),
                new Vector(0, -1, 0),
                new Vector(-1, 0, 0)
        };
        boolean[] results = {true, true, true, true, true, true, true, false, false, false, false, false, false};
        BoundingBox box = new BoundingBox(new Point(-1, -1, -1), new Point(1, 1, 1));
        for (int i = 0; i < origins.length; i++) {
            Vector direction = directions[i].normalize();
            Ray r = new Ray(origins[i], direction);
            assertEquals(results[i], box.intersects(r));
        }
    }

    @Test
    @DisplayName("Intersecting a ray with a non-cubic bounding box")
    void nonCubic() {
        Point[] origins = {
                new Point(15, 1, 2),
                new Point(-5, -1, 4),
                new Point(7, 6, 5),
                new Point(9, -5, 6),
                new Point(8, 2, 12),
                new Point(6, 0, -5),
                new Point(8, 1, 3.5),
                new Point(9, -1, -8),
                new Point(8, 3, -4),
                new Point(9, -1, -2),
                new Point(4, 0, 9),
                new Point(8, 6, -1),
                new Point(12, 5, 4)
        };
        Vector[] directions = {
                new Vector(-1, 0, 0),
                new Vector(1, 0, 0),
                new Vector(0, -1, 0),
                new Vector(0, 1, 0),
                new Vector(0, 0, -1),
                new Vector(0, 0, 1),
                new Vector(0, 0, 1),
                new Vector(2, 4, 6),
                new Vector(6, 2, 4),
                new Vector(4, 6, 2),
                new Vector(0, 0, -1),
                new Vector(0, -1, 0),
                new Vector(-1, 0, 0)
        };
        boolean[] results = {true, true, true, true, true, true, true, false, false, false, false, false, false};
        BoundingBox box = new BoundingBox(new Point(5, -2, 0), new Point(11, 4, 7));
        for (int i = 0; i < origins.length; i++) {
            Vector direction = directions[i].normalize();
            Ray r = new Ray(origins[i], direction);
            assertEquals(results[i], box.intersects(r));
        }
    }

    @Test
    @DisplayName("Splitting a perfect cube")
    void splitCube() {
        BoundingBox box = new BoundingBox(new Point(-1, -4, -5), new Point(9, 6, 5));
        BoundingBox[] boxes = box.splitBounds();
        assertEquals(boxes[0].min, new Point(-1, -4, -5));
        assertEquals(boxes[0].max, new Point(4, 6, 5));
        assertEquals(boxes[1].min, new Point(4, -4, -5));
        assertEquals(boxes[1].max, new Point(9, 6, 5));
    }

    @Test
    @DisplayName("Splitting an x-wide box")
    void splitBoxX() {
        BoundingBox box = new BoundingBox(new Point(-1, -2, -3), new Point(9, 5.5, 3));
        BoundingBox[] boxes = box.splitBounds();
        assertEquals(boxes[0].min, new Point(-1, -2, -3));
        assertEquals(boxes[0].max, new Point(4, 5.5, 3));
        assertEquals(boxes[1].min, new Point(4, -2, -3));
        assertEquals(boxes[1].max, new Point(9, 5.5, 3));
    }

    @Test
    @DisplayName("Splitting an y-wide box")
    void splitBoxY() {
        BoundingBox box = new BoundingBox(new Point(-1, -2, -3), new Point(5, 8, 3));
        BoundingBox[] boxes = box.splitBounds();
        assertEquals(boxes[0].min, new Point(-1, -2, -3));
        assertEquals(boxes[0].max, new Point(5, 3, 3));
        assertEquals(boxes[1].min, new Point(-1, 3, -3));
        assertEquals(boxes[1].max, new Point(5, 8, 3));
    }

    @Test
    @DisplayName("Splitting an z-wide box")
    void splitBoxZ() {
        BoundingBox box = new BoundingBox(new Point(-1, -2, -3), new Point(5, 3, 7));
        BoundingBox[] boxes = box.splitBounds();
        assertEquals(boxes[0].min, new Point(-1, -2, -3));
        assertEquals(boxes[0].max, new Point(5, 3, 2));
        assertEquals(boxes[1].min, new Point(-1, -2, 2));
        assertEquals(boxes[1].max, new Point(5, 3, 7));
    }

}
