package main;

import java.util.ArrayList;
import java.util.Arrays;

public class BoundingBox {
    public Point min;
    public Point max;

    public BoundingBox() {
        this.min = new Point(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        this.max = new Point(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }

    public BoundingBox(Point min, Point max) {
        this.min = min;
        this.max = max;
    }

    public void addPoint(Point p) {
        this.min.x = Math.min(p.x, this.min.x);
        this.min.y = Math.min(p.y, this.min.y);
        this.min.z = Math.min(p.z, this.min.z);

        this.max.x = Math.max(p.x, this.max.x);
        this.max.y = Math.max(p.y, this.max.y);
        this.max.z = Math.max(p.z, this.max.z);
    }

    public void addBox(BoundingBox box) {
        this.addPoint(box.min);
        this.addPoint(box.max);
    }

    public boolean containsPoint(Point p) {
        return (p.x <= this.max.x && p.x >= this.min.x) &&
                (p.y <= this.max.y && p.y >= this.min.y) &&
                (p.z <= this.max.z && p.z >= this.min.z);
    }

    public boolean containsBox(BoundingBox box) {
        return this.containsPoint(box.min) && this.containsPoint(box.max);
    }

    public final BoundingBox transform(Matrix t) {
        Point p1 = this.min;
        Point p2 = new Point(this.min.x, this.min.y, this.max.z);
        Point p3 = new Point(this.min.x, this.max.y, this.min.z);
        Point p4 = new Point(this.min.x, this.max.y, this.max.z);
        Point p5 = new Point(this.max.x, this.min.y, this.min.z);
        Point p6 = new Point(this.max.x, this.min.y, this.max.z);
        Point p7 = new Point(this.max.x, this.max.y, this.min.z);
        Point p8 = this.max;
        Point[] points = {p1,p2,p3,p4,p5,p6,p7,p8};
        BoundingBox newBox = new BoundingBox();

        for(Point p:points) {
            newBox.addPoint(t.multiply(p).toPoint());
        }
        return newBox;
    }

    public boolean intersects(Ray r) {
        double[] xts = checkAxis(r.origin.x, r.direction.x, this.min.x, this.max.x);
        double[] yts = checkAxis(r.origin.y, r.direction.y, this.min.y, this.max.y);
        double[] zts = checkAxis(r.origin.z, r.direction.z, this.min.z, this.max.z);
        double[] tmins = {xts[0], yts[0], zts[0]};
        double[] tmaxs = {xts[1], yts[1], zts[1]};
        double tmin = Arrays.stream(tmins).max().getAsDouble();
        double tmax = Arrays.stream(tmaxs).min().getAsDouble();
        return !(tmin > tmax);
    }

    double[] checkAxis(double origin, double direction, double min, double max) {
        double tmin_numerator = (min - origin);
        double tmax_numerator = (max - origin);
        double tmin, tmax;
        if (Math.abs(direction) >= Util.EPSILON) {
            tmin = tmin_numerator / direction;
            tmax = tmax_numerator / direction;
        } else {
            tmin = tmin_numerator * Double.POSITIVE_INFINITY;
            tmax = tmax_numerator * Double.POSITIVE_INFINITY;
        }
        if (tmin > tmax) {
            double swap = tmin;
            tmin = tmax;
            tmax = swap;
        }
        return new double[]{tmin, tmax};
    }

    public BoundingBox[] splitBounds() {
        double dx = (this.max.x - this.min.x);
        double dy = (this.max.y - this.min.y);
        double dz = (this.max.z - this.min.z);
        Point midMin;
        Point midMax;
        double greatest = Math.max(Math.max(dx, dy), dz);
        if (greatest == dx) {
            double newX = this.min.x + (dx / 2);
            midMin = new Point(newX, this.min.y, this.min.z);
            midMax = new Point(newX, this.max.y, this.max.z);
        } else if (greatest == dy) {
            double newY = this.min.y + (dy / 2);
            midMin = new Point(this.min.x, newY, this.min.z);
            midMax = new Point(this.max.x, newY, this.max.z);
        } else {
            double newZ = this.min.z + (dz / 2);
            midMin = new Point(this.min.x, this.min.y, newZ);
            midMax = new Point(this.max.x, this.max.y, newZ);
        }
        BoundingBox left = new BoundingBox(this.min, midMax);
        BoundingBox right = new BoundingBox(midMin, this.max);
        return new BoundingBox[]{left, right};
    }
}
