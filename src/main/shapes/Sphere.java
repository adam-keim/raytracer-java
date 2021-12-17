package main.shapes;

import main.*;

import java.util.ArrayList;

public class Sphere extends Shape {


    public static Sphere glassSphere() {
        Sphere s = new Sphere();
        s.material.transparency = 1.0;
        s.material.refractiveIndex = 1.5;
        return s;
    }

    @Override
    public ArrayList<Intersection> localIntersect(Ray r) {
        ArrayList<Intersection> xs = new ArrayList<>();
        Vector sphere_to_ray = r.origin.subtract(new Point(0, 0, 0)).toVector();

        double a = r.direction.dot(r.direction);
        double b = 2 * r.direction.dot(sphere_to_ray);
        double c = sphere_to_ray.dot(sphere_to_ray) - 1;
        double disc = b * b - 4 * a * c;
        if (disc < 0) {
            return xs;
        }
        double t1 = (-b - Math.sqrt(disc)) / (2 * a);
        double t2 = (-b + Math.sqrt(disc)) / (2 * a);

        xs.add(new Intersection(t1, this));
        xs.add(new Intersection(t2, this));
        return xs;

    }

    @Override
    public Vector localNormalAt(Point point, Intersection hit) {
        return point.subtract(new Point(0, 0, 0)).toVector();
    }

    @Override
    public BoundingBox boundsOf() {
        return new BoundingBox(new Point(-1,-1,-1), new Point(1,1,1));
    }
}
