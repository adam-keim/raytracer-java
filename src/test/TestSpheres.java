package test;


import main.*;
import main.shapes.Sphere;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Spheres")
public class TestSpheres {
    @Test
    @DisplayName("A ray intersects a sphere at two points")
    void raySphere2() {
        Ray r = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        Sphere s = new Sphere();
        ArrayList<Intersection> xs = s.intersect(r);
        assertEquals(2, xs.size());
        assertEquals(4.0, xs.get(0).t);
        assertEquals(6.0, xs.get(1).t);
    }

    @Test
    @DisplayName("A ray intersects a sphere at a tangent")
    void raySphereTan() {
        Ray r = new Ray(new Point(0, 1, -5), new Vector(0, 0, 1));
        Sphere s = new Sphere();
        ArrayList<Intersection> xs = s.intersect(r);
        assertEquals(2, xs.size());
        assertEquals(5.0, xs.get(0).t);
        assertEquals(5.0, xs.get(1).t);
    }

    @Test
    @DisplayName("A ray misses a sphere")
    void raySphereMiss() {
        Ray r = new Ray(new Point(0, 2, -5), new Vector(0, 0, 1));
        Sphere s = new Sphere();
        ArrayList<Intersection> xs = s.localIntersect(r);
        assertEquals(0, xs.size());
    }

    @Test
    @DisplayName("A ray originates inside a sphere")
    void rayInSphere() {
        Ray r = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Sphere s = new Sphere();
        ArrayList<Intersection> xs = s.localIntersect(r);
        assertEquals(2, xs.size());
        assertEquals(-1.0, xs.get(0).t);
        assertEquals(1.0, xs.get(1).t);
    }

    @Test
    @DisplayName("A sphere is behind a ray")
    void sphereBehindRay() {
        Ray r = new Ray(new Point(0, 0, 5), new Vector(0, 0, 1));
        Sphere s = new Sphere();
        ArrayList<Intersection> xs = s.localIntersect(r);
        assertEquals(2, xs.size());
        assertEquals(-6.0, xs.get(0).t);
        assertEquals(-4.0, xs.get(1).t);
    }

    @Test
    @DisplayName("Intersect sets the object on the intersection")
    void setObject() {
        Ray r = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        Sphere s = new Sphere();
        ArrayList<Intersection> xs = s.localIntersect(r);
        assertEquals(2, xs.size());
        assertEquals(s, xs.get(0).object);
        assertEquals(s, xs.get(1).object);

    }



    @Test
    @DisplayName("The normal on a sphere at a point on the x axis")
    void xNormal() {
        Sphere s = new Sphere();
        Vector n = s.localNormalAt(new Point(1, 0, 0), null);
        assertEquals(n, new Vector(1, 0, 0));
    }

    @Test
    @DisplayName("The normal on a sphere at a point on the y axis")
    void yNormal() {
        Sphere s = new Sphere();
        Vector n = s.localNormalAt(new Point(0, 1, 0), null);
        assertEquals(n, new Vector(0, 1, 0));
    }

    @Test
    @DisplayName("The normal on a sphere at a point on the z axis")
    void zNormal() {
        Sphere s = new Sphere();
        Vector n = s.localNormalAt(new Point(0, 0, 1), null);
        assertEquals(n, new Vector(0, 0, 1));
    }

    @Test
    @DisplayName("The normal on a sphere at a nonaxial point")
    void nonAxialNormal() {
        Sphere s = new Sphere();
        Vector n = s.localNormalAt(new Point(Math.sqrt(3) / 3, Math.sqrt(3) / 3, Math.sqrt(3) / 3), null);
        assertEquals(n, new Vector(Math.sqrt(3) / 3, Math.sqrt(3) / 3, Math.sqrt(3) / 3));
    }

    @Test
    @DisplayName("The normal is a normalized vector")
    void normalizedNormal() {
        Sphere s = new Sphere();
        Vector n = s.localNormalAt(new Point(Math.sqrt(3) / 3, Math.sqrt(3) / 3, Math.sqrt(3) / 3), null);
        assertEquals(n, n.normalize());

    }

    @Test
    @DisplayName("A helper for producing a sphere with a glassy material")
    void glassySphere() {
        Sphere s = Sphere.glassSphere();
        assertEquals(Matrix.identity(), s.getTransform());
        assertEquals(1.0, s.material.transparency);
        assertEquals(1.5, s.material.refractiveIndex);
    }

    @Test
    @DisplayName("A sphere has a bounding box")
    void boundSphere() {
        Sphere s = new Sphere();
        BoundingBox box = s.boundsOf();
        assertEquals(new Point(-1,-1,-1), box.min);
        assertEquals(new Point(1,1,1), box.max);
    }

}
