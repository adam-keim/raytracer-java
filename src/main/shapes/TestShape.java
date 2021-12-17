package main.shapes;

import main.*;

import java.util.ArrayList;

public class TestShape extends Shape {
    public Ray savedRay;

    @Override
    public ArrayList<Intersection> localIntersect(Ray r) {
        savedRay = r;
        return new ArrayList<Intersection>();
    }

    @Override
    public Vector localNormalAt(Point point, Intersection hit) {
        return new Vector(point.x, point.y, point.z);
    }

    @Override
    public BoundingBox boundsOf() {
        return new BoundingBox(new Point(-1,-1,-1), new Point(1,1,1));
    }
}
