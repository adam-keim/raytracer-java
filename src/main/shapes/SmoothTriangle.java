package main.shapes;

import main.*;

import java.util.ArrayList;

public class SmoothTriangle extends Shape {
    public Point p1;
    public Point p2;
    public Point p3;
    public Vector n1;
    public Vector n2;
    public Vector n3;
    public Vector e1;
    public Vector e2;
    public Vector normal;

    public SmoothTriangle(Point p1, Point p2, Point p3, Vector n1, Vector n2, Vector n3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.n1 = n1;
        this.n2 = n2;
        this.n3 = n3;
        this.e1 = p2.subtract(p1).toVector();
        this.e2 = p3.subtract(p1).toVector();

    }

    @Override
    public ArrayList<Intersection> localIntersect(Ray r) {
        Vector dirCrossE2 = r.direction.cross(this.e2);
        double det = this.e1.dot(dirCrossE2);
        ArrayList<Intersection> xs = new ArrayList<>();
        if (Math.abs(det) < Util.EPSILON) {
            return xs;
        }

        double f = 1.0 / det;
        Vector p1ToOrigin = r.origin.subtract(this.p1).toVector();
        double u = f * p1ToOrigin.dot(dirCrossE2);
        if(u < 0 || u > 1) {
            return xs;
        }
        Vector originCrossE1 = p1ToOrigin.cross(this.e1);
        double v = f * r.direction.dot(originCrossE1);
        if(v < 0 || (u+v) > 1) {
            return xs;
        }

        double t = f * this.e2.dot(originCrossE1);
        xs.add(new Intersection(t, this, u, v));
        return xs;
    }

    @Override
    public Vector localNormalAt(Point point, Intersection hit) {
        return this.n2.multiply(hit.u).add(this.n3.multiply(hit.v)).add(this.n1.multiply(1-hit.u-hit.v)).toVector();

    }
    @Override
    public BoundingBox boundsOf() {
        BoundingBox box = new BoundingBox();
        box.addPoint(this.p1);
        box.addPoint(this.p2);
        box.addPoint(this.p3);
        return box;
    }
}
