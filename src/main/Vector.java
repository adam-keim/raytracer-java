package main;

public class Vector extends Tuple {
    public Vector(double x, double y, double z) {
        super(x, y, z, 0.0);
    }

    public Vector(double x, double y, double z, double w) {
        super(x, y, z, w);
    }

    public double magnitude() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2) + Math.pow(this.w, 2));
    }

    public Vector normalize() {
        return new Vector(this.x / this.magnitude(), this.y / this.magnitude(), this.z / this.magnitude(), this.w / this.magnitude());
    }

    public double dot(Vector b) {
        return this.x * b.x + this.y * b.y + this.z * b.z + this.w * b.w;
    }

    @Override
    public Vector multiply(double a) {
        return new Vector(this.x * a, this.y * a, this.z * a, this.w * a);
    }

    public Vector cross(Vector b) {
        return new Vector(this.y * b.z - this.z * b.y, this.z * b.x - this.x * b.z, this.x * b.y - this.y * b.x);
    }

    public Vector reflect(Vector n) {
        return this.subtract(n.multiply(2).multiply(this.dot(n))).toVector();
    }

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
