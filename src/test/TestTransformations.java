package test;

import main.Matrix;
import main.Point;
import main.Vector;
import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Transformations")
public class TestTransformations {
    @Test
    @DisplayName("Multiplying by a translation matrix")
    void translation() {
        Matrix t = Matrix.translation(5, -3, 2);
        Point p = new Point(-3, 4, 5);
        assertEquals(t.multiply(p), new Point(2, 1, 7));
    }

    @Test
    @DisplayName("Multiplying by the inverse of a translation matrix")
    void invTranslation() {
        Matrix t = Matrix.translation(5, -3, 2);
        Matrix inv = t.inverse();
        Point p = new Point(-3, 4, 5);
        assertEquals(inv.multiply(p), new Point(-8, 7, 3));
    }

    @Test
    @DisplayName("Translation does not affect vectors")
    void vectorTranslation() {
        Matrix t = Matrix.translation(5, -3, 2);
        Vector v = new Vector(-3, 4, 5);
        assertEquals(t.multiply(v), v);
    }

    @Test
    @DisplayName("A scaling matrix applied to a point")
    void pointScaling() {
        Matrix t = Matrix.scaling(2, 3, 4);
        Point p = new Point(-4, 6, 8);
        assertEquals(t.multiply(p), new Point(-8, 18, 32));
    }

    @Test
    @DisplayName("A scaling matrix applied to a vector")
    void vectorScaling() {
        Matrix t = Matrix.scaling(2, 3, 4);
        Vector v = new Vector(-4, 6, 8);
        assertEquals(t.multiply(v), new Vector(-8, 18, 32));
    }

    @Test
    @DisplayName("Multiplying by the inverse of a translation matrix")
    void invScaling() {
        Matrix t = Matrix.scaling(2, 3, 4);
        Matrix inv = t.inverse();
        Vector v = new Vector(-4, 6, 8);
        assertEquals(inv.multiply(v), new Vector(-2, 2, 2));
    }

    @Test
    @DisplayName("Reflection is scaling by a negative value")
    void reflection() {
        Matrix t = Matrix.scaling(-1, 1, 1);
        Point p = new Point(2, 3, 4);
        assertEquals(t.multiply(p), new Point(-2, 3, 4));
    }

    @Test
    @DisplayName("Rotating a point around the x axis")
    void xRotation() {
        Point p = new Point(0, 1, 0);
        Matrix half_quarter = Matrix.rotationX(Math.PI / 4);
        Matrix full_quarter = Matrix.rotationX(Math.PI / 2);
        assertEquals(half_quarter.multiply(p), new Point(0, Math.sqrt(2) / 2, Math.sqrt(2) / 2));
        assertEquals(full_quarter.multiply(p), new Point(0, 0, 1));


    }

    @Test
    @DisplayName("The inverse of an x-rotation rotates in the opposite direction")
    void invXRotation() {
        Point p = new Point(0, 1, 0);
        Matrix half_quarter = Matrix.rotationX(Math.PI / 4);
        Matrix inv = half_quarter.inverse();
        assertEquals(inv.multiply(p), new Point(0, Math.sqrt(2) / 2, -Math.sqrt(2) / 2));
    }

    @Test
    @DisplayName("Rotating a point around the y axis")
    void yRotation() {
        Point p = new Point(0, 0, 1);
        Matrix half_quarter = Matrix.rotationY(Math.PI / 4);
        Matrix full_quarter = Matrix.rotationY(Math.PI / 2);
        assertEquals(half_quarter.multiply(p), new Point(Math.sqrt(2) / 2, 0, Math.sqrt(2) / 2));
        assertEquals(full_quarter.multiply(p), new Point(1, 0, 0));
    }

    @Test
    @DisplayName("Rotating a point around the z axis")
    void zRotation() {
        Point p = new Point(0, 1, 0);
        Matrix half_quarter = Matrix.rotationZ(Math.PI / 4);
        Matrix full_quarter = Matrix.rotationZ(Math.PI / 2);
        assertEquals(half_quarter.multiply(p), new Point(-Math.sqrt(2) / 2, Math.sqrt(2) / 2, 0));
        assertEquals(full_quarter.multiply(p), new Point(-1, 0, 0));
    }

    @Test
    @DisplayName("A shearing transformation moves x in proportion to y")
    void xyShear() {
        Matrix t = Matrix.shearing(1, 0, 0, 0, 0, 0);
        Point p = new Point(2, 3, 4);
        assertEquals(t.multiply(p), new Point(5, 3, 4));
    }

    @Test
    @DisplayName("A shearing transformation moves x in proportion to z")
    void xzShear() {
        Matrix t = Matrix.shearing(0, 1, 0, 0, 0, 0);
        Point p = new Point(2, 3, 4);
        assertEquals(t.multiply(p), new Point(6, 3, 4));
    }

    @Test
    @DisplayName("A shearing transformation moves y in proportion to x")
    void yxShear() {
        Matrix t = Matrix.shearing(0, 0, 1, 0, 0, 0);
        Point p = new Point(2, 3, 4);
        assertEquals(t.multiply(p), new Point(2, 5, 4));
    }

    @Test
    @DisplayName("A shearing transformation moves y in proportion to z")
    void yzShear() {
        Matrix t = Matrix.shearing(0, 0, 0, 1, 0, 0);
        Point p = new Point(2, 3, 4);
        assertEquals(t.multiply(p), new Point(2, 7, 4));
    }

    @Test
    @DisplayName("A shearing transformation moves z in proportion to x")
    void zxShear() {
        Matrix t = Matrix.shearing(0, 0, 0, 0, 1, 0);
        Point p = new Point(2, 3, 4);
        assertEquals(t.multiply(p), new Point(2, 3, 6));
    }

    @Test
    @DisplayName("A shearing transformation moves z in proportion to y")
    void zyShear() {
        Matrix t = Matrix.shearing(0, 0, 0, 0, 0, 1);
        Point p = new Point(2, 3, 4);
        assertEquals(t.multiply(p), new Point(2, 3, 7));
    }

    @Test
    @DisplayName("Individual transformations are applied in sequence")
    void transSequence() {
        Point p = new Point(1, 0, 1);
        Matrix a = Matrix.rotationX(Math.PI / 2);
        Matrix b = Matrix.scaling(5, 5, 5);
        Matrix c = Matrix.translation(10, 5, 7);

        Point p2 = a.multiply(p).toPoint();
        assertEquals(p2, new Point(1, -1, 0));

        Point p3 = b.multiply(p2).toPoint();
        assertEquals(p3, new Point(5, -5, 0));

        Point p4 = c.multiply(p3).toPoint();
        assertEquals(p4, new Point(15, 0, 7));
    }

    @Test
    @DisplayName("Chained transformations must be applied in reverse order")
    void transChain() {
        Point p = new Point(1, 0, 1);
        Matrix a = Matrix.rotationX(Math.PI / 2);
        Matrix b = Matrix.scaling(5, 5, 5);
        Matrix c = Matrix.translation(10, 5, 7);
        Matrix t = c.multiply(b).multiply(a);
        assertEquals(t.multiply(p), new Point(15, 0, 7));
    }

    @Test
    @DisplayName("The transformation matrix for the default orientation")
    void defaultView() {
        Point from = new Point(0, 0, 0);
        Point to = new Point(0, 0, -1);
        Vector up = new Vector(0, 1, 0);

        Matrix t = Matrix.viewTransform(from, to, up);
        assertEquals(t, Matrix.identity());
    }

    @Test
    @DisplayName("A view transformation matrix looking in positive z direction")
    void posZView() {
        Point from = new Point(0, 0, 0);
        Point to = new Point(0, 0, 1);
        Vector up = new Vector(0, 1, 0);

        Matrix t = Matrix.viewTransform(from, to, up);
        assertEquals(t, Matrix.scaling(-1, 1, -1));
    }

    @Test
    @DisplayName("The view transformation moves the world")
    void moveWorld() {
        Point from = new Point(0, 0, 8);
        Point to = new Point(0, 0, 0);
        Vector up = new Vector(0, 1, 0);

        Matrix t = Matrix.viewTransform(from, to, up);
        assertEquals(t, Matrix.translation(0, 0, -8));
    }

    @Test
    @DisplayName("An arbitrary view transformation")
    void arbitraryView() {
        Point from = new Point(1,3,2);
        Point to = new Point(4,-2,8);
        Vector up = new Vector(1,1,0);

        Matrix t = Matrix.viewTransform(from, to, up);
        Matrix m = Matrix.create(4,4, new double[] {-0.50709, 0.50709, 0.67612, -2.36643, 0.76772, 0.60609, 0.12122, -2.82843, -0.35857, 0.59761, -0.71714, 0.00000, 0.00000, 0.00000, 0.00000, 1.00000});

        assertEquals(t, m);

    }
}
