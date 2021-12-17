package test;

import main.Color;
import main.Point;
import main.Tuple;
import main.Vector;
import main.Util;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Tuples")
public class TestTuples {
    void assertFuzzyEquals(double a, double b) {
        assertTrue(Util.EQUALS(a, b));
    }

    @Test
    @DisplayName("A tuple with w=1.0 is a point")
    void checkTuplePoint() {
        Tuple a = new Tuple(4.3, -4.2, 3.1, 1.0);
        assertFuzzyEquals(a.x, 4.3);
        assertFuzzyEquals(a.y, -4.2);
        assertFuzzyEquals(a.z, 3.1);
        assertFuzzyEquals(a.w, 1.0);
        assertTrue(a.isPoint());
        assertFalse(a.isVector());

    }

    @Test
    @DisplayName("A tuple with w=0.0 is a vector")
    void checkTupleVector() {
        Tuple a = new Tuple(4.3, -4.2, 3.1, 0.0);
        assertFuzzyEquals(a.x, 4.3);
        assertFuzzyEquals(a.y, -4.2);
        assertFuzzyEquals(a.z, 3.1);
        assertFuzzyEquals(a.w, 0.0);
        assertFalse(a.isPoint());
        assertTrue(a.isVector());

    }

    @Test
    @DisplayName("Point() creates tuples with w=1")
    void checkPoint() {
        Point p = new Point(4, -4, 3);
        assertEquals(p, new Tuple(4, -4, 3, 1));
    }

    @Test
    @DisplayName("Vector() creates tuples with w=1")
    void checkVector() {
        Vector p = new Vector(4, -4, 3);
        assertEquals(p, new Tuple(4, -4, 3, 0));
    }

    @Test
    @DisplayName("Adding two tuples")
    void addTuple() {
        Tuple a1 = new Tuple(3, -2, 5, 1);
        Tuple a2 = new Tuple(-2, 3, 1, 0);
        assertEquals(a1.add(a2), new Tuple(1, 1, 6, 1));
    }

    @Test
    @DisplayName("Subtracting two points")
    void subtractTwoPoints() {
        Point p1 = new Point(3, 2, 1);
        Point p2 = new Point(5, 6, 7);
        assertEquals(p1.subtract(p2), new Vector(-2, -4, -6));
    }

    @Test
    @DisplayName("Subtracting a vector from a point")
    void subtractVectorFromPoint() {
        Point p = new Point(3, 2, 1);
        Vector v = new Vector(5, 6, 7);
        assertEquals(p.subtract(v), new Point(-2, -4, -6));
    }

    @Test
    @DisplayName("Subtracting two vectors")
    void subtractTwoVectors() {
        Vector v1 = new Vector(3, 2, 1);
        Vector v2 = new Vector(5, 6, 7);
        assertEquals(v1.subtract(v2), new Vector(-2, -4, -6));
    }

    @Test
    @DisplayName("Subtracting a vector from the zero vector")
    void subtractFromZero() {
        Vector zero = new Vector(0, 0, 0);
        Vector v = new Vector(1, -2, 3);
        assertEquals(zero.subtract(v), new Vector(-1, 2, -3));
    }

    @Test
    @DisplayName("Negating a tuple")
    void negateTuple() {
        Tuple a = new Tuple(1, -2, 3, -4);
        assertEquals(a.negate(), new Tuple(-1, 2, -3, 4));
    }

    @Test
    @DisplayName("Multiplying a tuple by a scalar")
    void scalarMultipliation() {
        Tuple a = new Tuple(1, -2, 3, -4);
        assertEquals(a.multiply(3.5), new Tuple(3.5, -7, 10.5, -14));
    }

    @Test
    @DisplayName("Multiplying a tuple by a fraction")
    void scalarMultiplication() {
        Tuple a = new Tuple(1, -2, 3, -4);
        assertEquals(a.multiply(.5), new Tuple(.5, -1, 1.5, -2));
    }

    @Test
    @DisplayName("Dividing a tuple by a scalar")
    void scalarDivision() {
        Tuple a = new Tuple(1, -2, 3, -4);
        assertEquals(a.divide(2), new Tuple(0.5, -1, 1.5, -2));
    }

    @Test
    @DisplayName("Computing the magnitude of Vector(1, 0, 0)")
    void magnitude1() {
        Vector v = new Vector(1, 0, 0);
        assertFuzzyEquals(v.magnitude(), 1);
    }

    @Test
    @DisplayName("Computing the magnitude of Vector(0, 1, 0)")
    void magnitude2() {
        Vector v = new Vector(0, 1, 0);
        assertFuzzyEquals(v.magnitude(), 1);
    }

    @Test
    @DisplayName("Computing the magnitude of Vector(0, 0, 1)")
    void magnitude3() {
        Vector v = new Vector(0, 0, 1);
        assertFuzzyEquals(v.magnitude(), 1);
    }

    @Test
    @DisplayName("Computing the magnitude of Vector(1, 2, 3)")
    void magnitude4() {
        Vector v = new Vector(1, 2, 3);
        assertFuzzyEquals(v.magnitude(), Math.sqrt(14));
    }

    @Test
    @DisplayName("Computing the magnitude of Vector(-1, -2, -3)")
    void magnitude5() {
        Vector v = new Vector(-1, -2, -3);
        assertFuzzyEquals(v.magnitude(), Math.sqrt(14));
    }

    @Test
    @DisplayName("Normalizing Vector(4, 0, 0) gives (1, 0, 0)")
    void normalize1() {
        Vector v = new Vector(4, 0, 0);
        assertEquals(v.normalize(), new Vector(1, 0, 0));
    }

    @Test
    @DisplayName("Normalizing vector(1, 2, 3)")
    void normalize2() {
        Vector v = new Vector(1, 2, 3);
        assertEquals(v.normalize(), new Vector(0.26726, 0.53452, 0.80178));
    }

    @Test
    @DisplayName("The magnitude of a normalized vector")
    void normalizedMagnitude() {
        Vector v = new Vector(1, 2, 3);
        Vector norm = v.normalize();
        assertFuzzyEquals(norm.magnitude(), 1);
    }

    @Test
    @DisplayName("The dot product of two tuples")
    void dotProduct() {
        Vector a = new Vector(1, 2, 3);
        Vector b = new Vector(2, 3, 4);
        assertFuzzyEquals(a.dot(b), 20);
    }

    @Test
    @DisplayName("The cross product of two vectors")
    void crossProduct() {
        Vector a = new Vector(1, 2, 3);
        Vector b = new Vector(2, 3, 4);
        assertEquals(a.cross(b), new Vector(-1, 2, -1));
        assertEquals(b.cross(a), new Vector(1, -2, 1));
    }

    @Test
    @DisplayName("Colors are (red, green, blue) tuples")
    void basicColor() {
        Color c = new Color(-.5, .4, 1.7);
        assertFuzzyEquals(c.red, -.5);
        assertFuzzyEquals(c.green, .4);
        assertFuzzyEquals(c.blue, 1.7);
    }

    @Test
    @DisplayName("Adding colors")
    void addingColors() {
        Color c1 = new Color(.9, .6, .75);
        Color c2 = new Color(.7, .1, .25);
        assertEquals(c1.add(c2), new Color(1.6, .7, 1.0));
    }

    @Test
    @DisplayName("Subtracting colors")
    void subtractingColors() {
        Color c1 = new Color(.9, .6, .75);
        Color c2 = new Color(.7, .1, .25);
        assertEquals(c1.subtract(c2), new Color(0.2, .5, .5));
    }

    @Test
    @DisplayName("Multiplying a color by a scalar")
    void multiplyingColor() {
        Color c = new Color(.2, .3, .4);
        assertEquals(c.multiply(2), new Color(0.4, .6, .8));
    }

    @Test
    @DisplayName("Multiplying colors")
    void multiplyingColors() {
        Color c1 = new Color(1, .2, .4);
        Color c2 = new Color(.9, 1, .1);

        assertEquals(c1.multiply(c2), new Color(0.9, .2, .04));
    }

    @Test
    @DisplayName("Reflecting a vector approaching at 45 deg")
    void reflect45() {
        Vector v = new Vector(1,-1,0);
        Vector n = new Vector(0,1,0);
        Vector r = v.reflect(n);
        assertEquals(r, new Vector(1, 1, 0));
    }
    @Test
    @DisplayName("Reflecting a vector off a slanted surface")
    void reflectSlanted() {
        Vector v = new Vector(0,-1,0);
        Vector n = new Vector(Math.sqrt(2)/2,Math.sqrt(2)/2,0);
        Vector r = v.reflect(n);
        assertEquals(r, new Vector(1, 0, 0));
    }



}
