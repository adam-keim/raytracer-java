package main.shapes;

import main.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Cube extends Shape {
    @Override
    public ArrayList<Intersection> localIntersect(Ray r) {
        ArrayList<Intersection> xs = new ArrayList<>();
        double[] xts = checkAxis(r.origin.x, r.direction.x);
        double[] yts = checkAxis(r.origin.y, r.direction.y);
        double[] zts = checkAxis(r.origin.z, r.direction.z);
        double[] tmins = {xts[0], yts[0], zts[0]};
        double[] tmaxs = {xts[1], yts[1], zts[1]};
        double tmin = Arrays.stream(tmins).max().getAsDouble();
        double tmax = Arrays.stream(tmaxs).min().getAsDouble();
        if (tmin > tmax) {
            return xs;
        }
        xs.add(new Intersection(tmin, this));
        xs.add(new Intersection(tmax, this));
        return xs;
    }

    double[] checkAxis(double origin, double direction) {
        double tmin_numerator = (-1 - origin);
        double tmax_numerator = (1 - origin);
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

    @Override
    public Vector localNormalAt(Point point, Intersection hit) {
        double maxc = Arrays.stream(new double[]{Math.abs(point.x), Math.abs(point.y), Math.abs(point.z)}).max().getAsDouble();
        if (maxc == Math.abs(point.x)) {
            return new Vector(point.x, 0, 0);
        } else if (maxc == Math.abs(point.y)) {
            return new Vector(0, point.y, 0);
        }
        return new Vector(0, 0, point.z);
    }

    @Override
    public BoundingBox boundsOf() {
        return new BoundingBox(new Point(-1,-1,-1), new Point(1,1,1));
    }
}
