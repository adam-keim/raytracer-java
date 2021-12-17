package main;

import main.lights.Light;
import main.lights.PointLight;
import main.shapes.Shape;
import main.shapes.Sphere;

import java.util.ArrayList;
import java.util.Collections;

public class World {
    Light light = null;
    ArrayList<Shape> shapes = new ArrayList<>();

    public World() {
    }

    public static World defaultWorld() {
        World w = new World();
        w.setLight(new PointLight(new Point(-10, 10, -10), new Color(1, 1, 1)));

        Sphere s1 = new Sphere();
        s1.material.color = new Color(0.8, 1.0, 0.6);
        s1.material.diffuse = 0.7;
        s1.material.specular = 0.2;

        Sphere s2 = new Sphere();
        s2.setTransform(Matrix.scaling(0.5, 0.5, 0.5));

        w.addShape(s1);
        w.addShape(s2);

        return w;
    }

    public void setShapes(ArrayList<Shape> shapes) {
        this.shapes = shapes;
    }

    public void addShape(Shape shape) {
        this.shapes.add(shape);
    }

    public ArrayList<Shape> getShapes() {
        return this.shapes;
    }

    public Light getLight() {
        return light;
    }

    public void setLight(Light light) {
        this.light = light;
    }

    public ArrayList<Intersection> intersect(Ray r) {
        ArrayList<Intersection> xs = new ArrayList<>();
        for (Shape shape : this.shapes) {
            ArrayList<Intersection> new_xs = shape.intersect(r);
            xs.addAll(new_xs);
        }
        Collections.sort(xs);
        return xs;
    }

    public Color shadeHit(Comps c) {
        return shadeHit(c, 5);
    }

    public Color shadeHit(Comps c, int remaining) {
        double intensity = this.getLight().intensityAt(c.overPoint, this);
        Color surface = c.object.material.lighting(c.object, this.getLight(), c.overPoint, c.eyev, c.normalv, intensity);
        Color reflected = reflectedColor(c, remaining);
        Color refracted = refractedColor(c, remaining);

        Material m = c.object.material;
        if (m.reflective > 0 && m.transparency > 0) {
            double reflectance = c.schlick();
            return surface.add(reflected.multiply(reflectance)).add(refracted.multiply(1-reflectance));
        }
        return surface.add(reflected).add(refracted);
    }

    public Color colorAt(Ray r) {
        return colorAt(r, 5);
    }

    public Color colorAt(Ray r, int remaining) {
        ArrayList<Intersection> xs = this.intersect(r);
        Intersection hit = Intersection.hit(xs);
        if (hit == null) {
            return new Color(0, 0, 0);
        } else {
            Comps c = hit.prepareComputations(r, xs);
            return this.shadeHit(c, remaining);
        }
    }

    public boolean isShadowed(Point light, Point p) {
        Vector v = light.subtract(p).toVector();
        double distance = v.magnitude();
        Vector direction = v.normalize();

        Ray r = new Ray(p, direction);
        ArrayList<Intersection> xs = this.intersect(r);
        Intersection hit = Intersection.hit(xs);
        return hit != null && hit.t < distance && hit.object.isShadow();

    }

    public Color reflectedColor(Comps comps) {
        return reflectedColor(comps, 5);
    }

    public Color reflectedColor(Comps comps, int remaining) {
        if (comps.object.material.reflective == 0 || remaining <= 0) {
            return new Color(0, 0, 0);
        }
        Ray reflect_ray = new Ray(comps.overPoint, comps.reflectv);
        Color color = this.colorAt(reflect_ray, remaining - 1);
        return color.multiply(comps.object.material.reflective);
    }

    public Color refractedColor(Comps comps, int remaining) {
        if (comps.object.material.transparency == 0 || remaining <= 0) {
            return new Color(0,0,0);
        }

        double n_ratio = comps.n1 / comps.n2;
        double cos_i = comps.eyev.dot(comps.normalv);
        double sin2_t = Math.pow(n_ratio, 2) * (1-Math.pow(cos_i, 2));
        if(sin2_t > 1) {
            return new Color(0,0,0);
        }

        double cos_t = Math.sqrt(1.0 - sin2_t);
        Vector direction = comps.normalv.multiply(n_ratio * cos_i - cos_t).subtract(comps.eyev.multiply(n_ratio)).toVector();
        Ray refract_ray = new Ray(comps.underPoint, direction);


        return this.colorAt(refract_ray, remaining-1).multiply(comps.object.material.transparency);

    }
}
