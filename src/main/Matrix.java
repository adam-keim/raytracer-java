package main;

import main.Util;
import org.ejml.simple.SimpleMatrix;

public class Matrix {
    private final int numRows;
    private final int numCols;
    private final double[] values;

    private Matrix(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.values = new double[numRows * numCols];
    }

    public static Matrix create(int numRows, int numCols, double[] initValues) {
        Matrix m = new Matrix(numRows, numCols);
        m.init(initValues);
        return m;

    }

    public static Matrix translation(double x, double y, double z) {
        Matrix m = Matrix.identity();
        m.set(0, 3, x);
        m.set(1, 3, y);
        m.set(2, 3, z);
        return m;
    }

    public static Matrix scaling(double x, double y, double z) {
        Matrix m = new Matrix(4, 4);
        m.set(0, 0, x);
        m.set(1, 1, y);
        m.set(2, 2, z);
        m.set(3, 3, 1);
        return m;
    }

    public static Matrix rotationX(double r) {
        Matrix m = new Matrix(4, 4);
        m.set(0, 0, 1);
        m.set(1, 1, Math.cos(r));
        m.set(1, 2, -Math.sin(r));
        m.set(2, 1, Math.sin(r));
        m.set(2, 2, Math.cos(r));
        m.set(3, 3, 1);
        return m;
    }

    public static Matrix rotationY(double r) {
        Matrix m = new Matrix(4, 4);
        m.set(0, 0, Math.cos(r));
        m.set(0, 2, Math.sin(r));

        m.set(1, 1, 1);

        m.set(2, 0, -Math.sin(r));
        m.set(2, 2, Math.cos(r));

        m.set(3, 3, 1);

        return m;
    }

    public static Matrix rotationZ(double r) {
        Matrix m = new Matrix(4, 4);
        m.set(0, 0, Math.cos(r));
        m.set(0, 1, -Math.sin(r));


        m.set(1, 0, Math.sin(r));
        m.set(1, 1, Math.cos(r));

        m.set(2, 2, 1);
        m.set(3, 3, 1);

        return m;
    }

    public static Matrix shearing(double x_y, double x_z, double y_x, double y_z, double z_x, double z_y) {
        Matrix m = Matrix.identity();
        m.set(0, 1, x_y);
        m.set(0, 2, x_z);
        m.set(1, 0, y_x);
        m.set(1, 2, y_z);
        m.set(2, 0, z_x);
        m.set(2, 1, z_y);
        return m;
    }

    public static Matrix viewTransform(Point from, Point to, Vector up) {
        Vector forward = to.subtract(from).toVector().normalize();
        Vector upn = up.normalize();
        Vector left = forward.cross(upn);
        Vector true_up = left.cross(forward);
        Matrix m = new Matrix(4, 4);

        m.set(0, 0, left.x);
        m.set(0, 1, left.y);
        m.set(0, 2, left.z);

        m.set(1, 0, true_up.x);
        m.set(1, 1, true_up.y);
        m.set(1, 2, true_up.z);

        m.set(2, 0, -forward.x);
        m.set(2, 1, -forward.y);
        m.set(2, 2, -forward.z);

        m.set(3, 3, 1);

        return m.multiply(Matrix.translation(-from.x, -from.y, -from.z));
    }

    private void init(double[] initValues) {
        System.arraycopy(initValues, 0, this.values, 0, initValues.length);
    }

    public double get(int row, int col) {
        return this.values[row * this.numCols + col];
    }

    public void set(int row, int col, double value) {
        this.values[row * this.numCols + col] = value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix matrix = (Matrix) o;
        boolean equals = true;
        for (int index = 0; index < this.numRows * this.numCols; index++) {
            if (!Util.EQUALS(this.values[index], matrix.values[index])) {
                equals = false;
                break;
            }
        }
        return numRows == matrix.numRows && numCols == matrix.numCols && equals;
    }

    public Matrix multiply(Matrix b) {
        Matrix m = new Matrix(4, 4);
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                double value = this.get(row, 0) * b.get(0, col) +
                        this.get(row, 1) * b.get(1, col) +
                        this.get(row, 2) * b.get(2, col) +
                        this.get(row, 3) * b.get(3, col);
                m.set(row, col, value);
            }
        }
        return m;
    }

    public Tuple multiply(Tuple b) {
        double[] values = new double[4];
        for (int row = 0; row < 4; row++) {
            values[row] = this.get(row, 0) * b.x +
                    this.get(row, 1) * b.y +
                    this.get(row, 2) * b.z +
                    this.get(row, 3) * b.w;
        }
        return new Tuple(values[0], values[1], values[2], values[3]);
    }

    public static Matrix identity() {
        Matrix m = new Matrix(4, 4);
        for (int i = 0; i < 4; i++) {
            m.set(i, i, 1);
        }
        return m;
    }

    public Matrix transpose() {
        Matrix m = new Matrix(this.numCols, this.numRows);
        for (int row = 0; row < this.numRows; row++) {
            for (int col = 0; col < this.numCols; col++) {
                m.set(col, row, this.get(row, col));
            }
        }
        return m;
    }

    public double determinant() {
        if (numRows == 2) {
            return get(0, 0) * get(1, 1) - get(0, 1) * get(1, 0);
        }
        double det = 0;
        for (int col = 0; col < numCols; col++) {
            det += this.get(0, col) * this.cofactor(0, col);
        }
        return det;
    }

    public Matrix submatrix(int row, int col) {
        Matrix m = new Matrix(numRows - 1, numCols - 1);
        for (int row_i = 0; row_i < numRows - 1; row_i++) {
            for (int col_i = 0; col_i < numCols - 1; col_i++) {
                int rowOffset = (row_i >= row) ? 1 : 0;
                int colOffset = (col_i >= col) ? 1 : 0;
                m.set(row_i, col_i, this.get(row_i + rowOffset, col_i + colOffset));
            }
        }
        return m;
    }

    public double minor(int row, int col) {
        Matrix m = this.submatrix(row, col);
        return m.determinant();
    }

    public double cofactor(int row, int col) {
        double minor = this.minor(row, col);
        return ((row + col) % 2 > 0) ? -minor : minor;
    }

    public boolean invertible() {
        return (this.determinant() != 0);
    }

    public Matrix inverse() {

//Use EJML for massive speedup.
        SimpleMatrix m = new SimpleMatrix(4, 4, true, this.values);


        return Matrix.create(4, 4, m.invert().getMatrix().data);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < this.numRows; row++) {
            for (int col = 0; col < this.numCols; col++) {
                sb.append(this.get(row, col));
                sb.append("\t");
            }
            sb.append('\n');

        }
        return sb.toString();
    }
}
