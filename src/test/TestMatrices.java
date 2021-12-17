package test;

import main.Matrix;
import main.Tuple;
import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Matrices")
public class TestMatrices {
    @Test
    @DisplayName("Constructing and inspecting a 4x4 matrix")
    void Matrix44Basic() {
        Matrix m = Matrix.create(4, 4, new double[]{1, 2, 3, 4, 5.5, 6.5, 7.5, 8.5, 9, 10, 11, 12, 13.5, 14.5, 15.5, 16.5});
        assertEquals(m.get(0, 0), 1);
        assertEquals(m.get(0, 3), 4);
        assertEquals(m.get(1, 0), 5.5);
        assertEquals(m.get(1, 2), 7.5);
        assertEquals(m.get(2, 2), 11);
        assertEquals(m.get(3, 0), 13.5);
        assertEquals(m.get(3, 2), 15.5);

    }

    @Test
    @DisplayName("A 2x2 matrix ought to be representable")
    void Matrix22Basic() {
        Matrix m = Matrix.create(2, 2, new double[]{-3, 5, 1, -2});
        assertEquals(m.get(0, 0), -3);
        assertEquals(m.get(0, 1), 5);
        assertEquals(m.get(1, 0), 1);
        assertEquals(m.get(1, 1), -2);
    }

    @Test
    @DisplayName("A 3x3 matrix ought to be representable")
    void Matrix33Basic() {
        Matrix m = Matrix.create(3, 3, new double[]{-3, 5, 0, 1, -2, -7, 0, 1, 1});
        assertEquals(m.get(0, 0), -3);
        assertEquals(m.get(1, 1), -2);
        assertEquals(m.get(2, 2), 1);
    }

    @Test
    @DisplayName("Matrix equality with identical matrices")
    void matrixEquality() {
        Matrix a = Matrix.create(4, 4, new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2});
        Matrix b = Matrix.create(4, 4, new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2});
        assertEquals(a, b);
    }

    @Test
    @DisplayName("Matrix equality with different matrices")
    void matrixInequality() {
        Matrix a = Matrix.create(4, 4, new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2});
        Matrix b = Matrix.create(4, 4, new double[]{2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1});
        assertNotEquals(a, b);
    }

    @Test
    @DisplayName("Multiplying two matrices")
    void matrixMult() {
        Matrix a = Matrix.create(4, 4, new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2});
        Matrix b = Matrix.create(4, 4, new double[]{-2, 1, 2, 3, 3, 2, 1, -1, 4, 3, 6, 5, 1, 2, 7, 8});
        Matrix c = Matrix.create(4, 4, new double[]{20, 22, 50, 48, 44, 54, 114, 108, 40, 58, 110, 102, 16, 26, 46, 42});
        assertEquals(a.multiply(b), c);
    }

    @Test
    @DisplayName("A matrix multiplied by a tuple")
    void matrixTupleMult() {
        Matrix a = Matrix.create(4, 4, new double[]{1, 2, 3, 4, 2, 4, 4, 2, 8, 6, 4, 1, 0, 0, 0, 1});
        Tuple b = new Tuple(1, 2, 3, 1);
        assertEquals(a.multiply(b), new Tuple(18, 24, 33, 1));
    }

    @Test
    @DisplayName("Multiplying a matrix by the identity matrix")
    void identityMultMatrix() {
        Matrix a = Matrix.create(4, 4, new double[]{0, 1, 2, 4, 1, 2, 4, 8, 2, 4, 8, 16, 4, 8, 16, 32});
        Matrix i = Matrix.identity();
        assertEquals(a.multiply(i), a);
    }

    @Test
    @DisplayName("Multiplying the identity matrix by a tuple")
    void identityMultTuple() {
        Tuple a = new Tuple(1, 2, 3, 4);
        Matrix i = Matrix.identity();
        assertEquals(i.multiply(a), a);
    }

    @Test
    @DisplayName("Transposing a matrix")
    void transpose() {
        Matrix a = Matrix.create(4, 4, new double[]{0, 9, 3, 0, 9, 8, 0, 8, 1, 8, 5, 3, 0, 0, 5, 8});
        Matrix a_t = Matrix.create(4, 4, new double[]{0, 9, 1, 0, 9, 8, 8, 0, 3, 0, 5, 5, 0, 8, 3, 8});
        assertEquals(a.transpose(), a_t);
    }

    @Test
    @DisplayName("Calculating the determinant of a 2x2 matrix")
    void determinant22() {
        Matrix a = Matrix.create(2, 2, new double[]{1, 5, -3, 2});
        assertEquals(17, a.determinant());
    }

    @Test
    @DisplayName("A submatrix of a 3x3 matrix is a 2x2 matrix")
    void submatrix33() {
        Matrix a = Matrix.create(3, 3, new double[]{1, 5, 0, -3, 2, 7, 0, 6, -3});
        Matrix b = Matrix.create(2, 2, new double[]{-3, 2, 0, 6});
        assertEquals(a.submatrix(0, 2), b);
    }

    @Test
    @DisplayName("A submatrix of a 4x4 matrix is a 3x3 matrix")
    void submatrix44() {
        Matrix a = Matrix.create(4, 4, new double[]{-6, 1, 1, 6, -8, 5, 8, 6, -1, 0, 8, 2, -7, 1, -1, 1});
        Matrix b = Matrix.create(3, 3, new double[]{-6, 1, 6, -8, 8, 6, -7, -1, 1});
        assertEquals(a.submatrix(2, 1), b);
    }

    @Test
    @DisplayName("Calculating a minor of a 3x3 matrix")
    void minor33() {
        Matrix a = Matrix.create(3, 3, new double[]{3, 5, 0, 2, -1, -7, 6, -1, 5});
        Matrix b = a.submatrix(1, 0);
        assertEquals(25, b.determinant());
        assertEquals(25, a.minor(1, 0));
    }

    @Test
    @DisplayName("Calculating a cofactor of a 3x3 matrix")
    void cofactor33() {
        Matrix a = Matrix.create(3, 3, new double[]{3, 5, 0, 2, -1, -7, 6, -1, 5});
        assertEquals(-12, a.minor(0, 0));
        assertEquals(-12, a.cofactor(0, 0));
        assertEquals(25, a.minor(1, 0));
        assertEquals(-25, a.cofactor(1, 0));
    }

    @Test
    @DisplayName("Calculating the determinant of a 3x3 matrix")
    void determinant33() {
        Matrix a = Matrix.create(3, 3, new double[]{1, 2, 6, -5, 8, -4, 2, 6, 4});
        assertEquals(56, a.cofactor(0, 0));
        assertEquals(12, a.cofactor(0, 1));
        assertEquals(-46, a.cofactor(0, 2));
        assertEquals(-196, a.determinant());
    }

    @Test
    @DisplayName("Calculating the determinant of a 4x4 matrix")
    void determinant44() {
        Matrix a = Matrix.create(4, 4, new double[]{-2, -8, 3, 5, -3, 1, 7, 3, 1, 2, -9, 6, -6, 7, 7, -9});
        assertEquals(690, a.cofactor(0, 0));
        assertEquals(447, a.cofactor(0, 1));
        assertEquals(210, a.cofactor(0, 2));
        assertEquals(51, a.cofactor(0, 3));
        assertEquals(-4071, a.determinant());
    }

    @Test
    @DisplayName("Testing an invertible matrix for invertibility")
    void invertibility() {
        Matrix a = Matrix.create(4, 4, new double[]{6, 4, 4, 4, 5, 5, 7, 6, 4, -9, 3, -7, 9, 1, 7, -6});
        assertEquals(-2120, a.determinant());
        assertTrue(a.invertible());
    }

    @Test
    @DisplayName("Testing a noninvertible matrix for invertibility")
    void noninvertibility() {
        Matrix a = Matrix.create(4, 4, new double[]{-4, 2, -2, -3, 9, 6, 2, 6, 0, -5, 1, -5, 0, 0, 0, 0});
        assertEquals(0, a.determinant());
        assertFalse(a.invertible());
    }

    @Test
    @DisplayName("Calculating the inverse of a matrix")
    void inverse() {
        Matrix a = Matrix.create(4, 4, new double[]{-5, 2, 6, -8, 1, -5, 1, 8, 7, 7, -6, -7, 1, -3, 7, 4});
        Matrix b = a.inverse();
        Matrix b_exp = Matrix.create(4, 4, new double[]{.21805, .45113, .24060, -.04511, -.80827, -1.45677, -.44361, .52068, -.07895, -.22368, -.05263, .19737, -.52256, -.81391, -.30075, .30639});
        assertEquals(532, a.determinant());
        assertEquals(-160, a.cofactor(2,3));
        assertEquals(-160.0/532, b.get(3,2));
        assertEquals(105, a.cofactor(3,2));
        assertEquals(105.0/532, b.get(2,3));
        assertEquals(b_exp, b);


    }
    @Test
    @DisplayName("Calculating the inverse of another matrix")
    void inverse2() {
        Matrix a = Matrix.create(4, 4, new double[]{8,-5,9,2,7,5,6,1,-6,0,9,6,-3,0,-9,-4});
        Matrix b = a.inverse();
        Matrix b_exp = Matrix.create(4, 4, new double[]{-0.15385, -0.15385, -0.28205, -0.53846, -0.07692, 0.12308, 0.02564, 0.03077,0.35897, 0.35897, 0.43590, 0.92308,-0.69231, -0.69231, -0.76923, -1.92308});
        assertEquals(b_exp, b);

    }
    @Test
    @DisplayName("Calculating the inverse of a third matrix")
    void inverse3() {
        Matrix a = Matrix.create(4, 4, new double[]{9,3,0,9,-5,-2,-6,-3,-4,9,6,4,-7,6,6,2});
        Matrix b = a.inverse();
        Matrix b_exp = Matrix.create(4, 4, new double[]{-0.04074, -0.07778, 0.14444, -0.22222, -0.07778, 0.03333, 0.36667, -0.33333, -0.02901, -0.14630, -0.10926, 0.12963, 0.17778, 0.06667, -0.26667, 0.33333});
        assertEquals(b_exp, b);
    }

    @Test
    @DisplayName("Multiplying a product by its inverse")
    void inverseMult() {
        Matrix a = Matrix.create(4,4,new double[] {3,-9,7,3,3,-8,2,-9,-4,4,4,1,-6,5,-1,1});
        Matrix b = Matrix.create(4,4,new double[] {8,2,2,2,3,-1,7,0,7,0,5,4,6,-2,0,5});
        Matrix c = a.multiply(b);
        assertEquals(c.multiply(b.inverse()), a);
    }
}
