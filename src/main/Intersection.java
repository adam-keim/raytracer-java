package main;

import main.shapes.Shape;

import java.util.ArrayList;
import java.util.Objects;

public class Intersection implements Comparable<Intersection> {
    public final double t;
    public final Shape object;
    public double u, v;

    public Intersection(double t, Shape object) {
        this.t = t;
        this.object = object;

    }

    public Intersection(double t, Shape object, double u, double v) {
        this.t = t;
        this.object = object;
        this.u = u;
        this.v = v;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Intersection that = (Intersection) o;
        return Double.compare(that.t, t) == 0 && Objects.equals(object, that.object);
    }

    public static Intersection hit(ArrayList<Intersection> xs) {
        Intersection i = null;
        double t = Double.POSITIVE_INFINITY;
        for (Intersection x : xs) {
            if (x.t < t && x.t > 0) {
                i = x;
                t = i.t;
            }
        }

        return i;
    }

    @Override
    public int compareTo(Intersection o) {
        return Double.compare(this.t, o.t);
    }

    public Comps prepareComputations(Ray r) {
        ArrayList<Intersection> xs = new ArrayList<>();
        xs.add(this);
        return prepareComputations(r, xs);
    }

    public Comps prepareComputations(Ray r, ArrayList<Intersection> xs) {
        Comps c = new Comps();
        c.t = this.t;
        c.object = this.object;

        c.point = r.position(this.t);
        c.eyev = r.direction.negate().toVector();
        c.normalv = this.object.normalAt(c.point, this);

        if (c.normalv.dot(c.eyev) < 0) {
            c.inside = true;
            c.normalv = c.normalv.negate().toVector();
        } else {
            c.inside = false;
        }

        c.overPoint = c.point.add(c.normalv.multiply(Util.EPSILON)).toPoint();
        c.underPoint = c.point.subtract(c.normalv.multiply(Util.EPSILON)).toPoint();

        c.reflectv = r.direction.reflect(c.normalv);

        ArrayList<Shape> containers = new ArrayList<>();
        for (Intersection x : xs) {
            if (this.equals(x)) {
                if (containers.size() == 0) {
                    c.n1 = 1.0;
                } else {
                    c.n1 = containers.get(containers.size() - 1).material.refractiveIndex;
                }
            }
            if (containers.contains(x.object)) {
                containers.remove(x.object);
            } else {
                containers.add(x.object);
            }

            if (this.equals(x)) {
                if (containers.size() == 0) {
                    c.n2 = 1.0;
                } else {
                    c.n2 = containers.get(containers.size() - 1).material.refractiveIndex;
                }
                break;
            }
        }
        return c;
    }
}
