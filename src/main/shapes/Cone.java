package main.shapes;

import main.*;

import java.util.ArrayList;

public class Cone extends Shape {

    public double minimum;
    public double maximum;
    public boolean closed;

    public Cone() {
        this.minimum = Double.NEGATIVE_INFINITY;
        this.maximum = Double.POSITIVE_INFINITY;
        this.closed = false;
    }

    @Override
    public ArrayList<Intersection> localIntersect(Ray r) {
        ArrayList<Intersection> xs = new ArrayList<>();

        double a = Math.pow(r.direction.x, 2) - Math.pow(r.direction.y, 2) + Math.pow(r.direction.z, 2);
        double b = (2 * r.origin.x * r.direction.x) - (2 * r.origin.y * r.direction.y) + (2 * r.origin.z * r.direction.z);
        double c = Math.pow(r.origin.x, 2) - Math.pow(r.origin.y, 2) + Math.pow(r.origin.z, 2);

        if (Util.EQUALS(a, 0)) {
            if (Util.EQUALS(b, 0)) {
                return intersectCaps(r);
            } else {
                double t = -c/(2*b);
                xs.add(new Intersection(t, this));
                xs.addAll(intersectCaps(r));
                return xs;
            }
        }

        double disc = Math.pow(b, 2) - 4 * a * c;

        if (disc < 0) {
            return xs;
        }

        double t0 = (-b - Math.sqrt(disc)) / (2 * a);
        double t1 = (-b + Math.sqrt(disc)) / (2 * a);

        double y0 = r.origin.y + t0 * r.direction.y;
        if (this.minimum < y0 && this.maximum > y0) {
            xs.add(new Intersection(t0, this));
        }
        double y1 = r.origin.y + t1 * r.direction.y;
        if (this.minimum < y1 && this.maximum > y1) {
            xs.add(new Intersection(t1, this));
        }
        xs.addAll(intersectCaps(r));
        return xs;

    }

    boolean checkCap(Ray r, double t, double y_level) {
        double x = r.origin.x + t * r.direction.x;
        double z = r.origin.z + t * r.direction.z;

        return ((Math.pow(x, 2) + Math.pow(z, 2)) <= y_level);
    }

    ArrayList<Intersection> intersectCaps(Ray r) {
        ArrayList<Intersection> xs = new ArrayList<>();
        if (Util.EQUALS(r.direction.y, 0) || !closed) {
            return xs;
        }
        double t = (minimum - r.origin.y) / r.direction.y;
        if (checkCap(r, t, Math.abs(minimum))) {
            xs.add(new Intersection(t, this));
        }
        t = (maximum - r.origin.y) / r.direction.y;
        if (checkCap(r, t, Math.abs(maximum))) {
            xs.add(new Intersection(t, this));
        }
        return xs;
    }

    @Override
    public Vector localNormalAt(Point point, Intersection hit) {
        double dist = Math.pow(point.x, 2) + Math.pow(point.z, 2);
        if (dist < 1 && point.y >= (maximum - Util.EPSILON)) {
            return new Vector(0, 1, 0);
        } else if (dist < 1 && point.y <= (minimum + Util.EPSILON)) {
            return new Vector(0, -1, 0);
        } else {
            double y = Math.sqrt(Math.pow(point.x, 2) + Math.pow(point.z, 2));
            y = (point.y > 0) ? -y:y;
            return new Vector(point.x, y, point.z);
        }
    }
    @Override
    public BoundingBox boundsOf() {
        double a = Math.abs(this.minimum);
        double b = Math.abs(this.maximum);
        double limit = Math.max(a,b);
        return new BoundingBox(new Point(-limit, this.minimum, -limit), new Point(limit, this.maximum, limit));
    }
}
