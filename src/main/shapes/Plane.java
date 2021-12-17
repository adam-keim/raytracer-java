package main.shapes;

import main.*;

import java.util.ArrayList;

public class Plane extends Shape {


    @Override
    public ArrayList<Intersection> localIntersect(Ray r) {
        ArrayList<Intersection> xs = new ArrayList<>();
        if(Math.abs(r.direction.y) < Util.EPSILON) {
            return xs;
        }
        double t = -r.origin.y / r.direction.y;
        xs.add(new Intersection(t, this));
        return xs;
    }

    @Override
    public Vector localNormalAt(Point poin, Intersection hti) {
        return new Vector(0,1,0);
    }

    @Override
    public BoundingBox boundsOf() {
        return new BoundingBox(new Point(Double.NEGATIVE_INFINITY,0,Double.NEGATIVE_INFINITY),
                new Point(Double.POSITIVE_INFINITY,0,Double.POSITIVE_INFINITY));
    }
}
