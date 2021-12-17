package test;

import main.*;
import main.shapes.Plane;
import main.shapes.Shape;
import main.shapes.Sphere;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Intersections")
public class TestIntersections {

    @Test
    @DisplayName("An intersection encapsulates t and object")
    void testIntersection() {
        Sphere s = new Sphere();
        Intersection i = new Intersection(3.5, s);
        assertEquals(3.5, i.t);
        assertEquals(s, i.object);

    }

    @Test
    @DisplayName("Aggregating intersections")
    void aggregateIntersection() {
        Sphere s = new Sphere();
        Intersection i1 = new Intersection(1, s);
        Intersection i2 = new Intersection(2, s);
        ArrayList<Intersection> xs = new ArrayList<>();
        xs.add(i1);
        xs.add(i2);
        assertEquals(2, xs.size());
        assertEquals(1, xs.get(0).t);
        assertEquals(2, xs.get(1).t);
    }

    @Test
    @DisplayName("The hit, when all intersections have positive t")
    void hitAllPos() {
        Sphere s = new Sphere();
        Intersection i1 = new Intersection(1, s);
        Intersection i2 = new Intersection(2, s);
        ArrayList<Intersection> xs = new ArrayList<>();
        xs.add(i2);
        xs.add(i1);
        Intersection i = Intersection.hit(xs);
        assertEquals(i, i1);
    }

    @Test
    @DisplayName("The hit, when some intersections have negative t")
    void hitSomeNeg() {
        Sphere s = new Sphere();
        Intersection i1 = new Intersection(-1, s);
        Intersection i2 = new Intersection(1, s);
        ArrayList<Intersection> xs = new ArrayList<>();
        xs.add(i2);
        xs.add(i1);
        Intersection i = Intersection.hit(xs);
        assertEquals(i, i2);
    }

    @Test
    @DisplayName("The hit, when all intersections have negative t")
    void hitAllNeg() {
        Sphere s = new Sphere();
        Intersection i1 = new Intersection(-2, s);
        Intersection i2 = new Intersection(-1, s);
        ArrayList<Intersection> xs = new ArrayList<>();
        xs.add(i2);
        xs.add(i1);
        Intersection i = Intersection.hit(xs);
        assertNull(i);
    }

    @Test
    @DisplayName("The hit is always the lowest non-negative intersection")
    void hitFirst() {
        Sphere s = new Sphere();
        Intersection i1 = new Intersection(5, s);
        Intersection i2 = new Intersection(7, s);
        Intersection i3 = new Intersection(-3, s);
        Intersection i4 = new Intersection(2, s);
        ArrayList<Intersection> xs = new ArrayList<>();
        xs.add(i1);
        xs.add(i2);
        xs.add(i3);
        xs.add(i4);
        Intersection i = Intersection.hit(xs);
        assertEquals(i, i4);
    }

    @Test
    @DisplayName("Precomputing the state of an intersection")
    void preCompute() {
        Ray r = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        Sphere s = new Sphere();
        Intersection i = new Intersection(4, s);
        Comps comps = i.prepareComputations(r);
        assertEquals(i.t, comps.t);
        assertEquals(i.object, comps.object);
        assertEquals(comps.point, new Point(0, 0, -1));
        assertEquals(comps.eyev, new Vector(0, 0, -1));
        assertEquals(comps.normalv, new Vector(0, 0, -1));


    }

    @Test
    @DisplayName("The hit, when an intersection occurs on the outside")
    void hitOutside() {
        Ray r = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        Sphere s = new Sphere();
        Intersection i = new Intersection(4, s);
        Comps comps = i.prepareComputations(r);
        assertFalse(comps.inside);
    }


    @Test
    @DisplayName("The hit, when an intersection occurs on the inside")
    void hitInside() {
        Ray r = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Sphere s = new Sphere();
        Intersection i = new Intersection(1, s);
        Comps comps = i.prepareComputations(r);
        assertEquals(comps.point, new Point(0, 0, 1));
        assertEquals(comps.eyev, new Vector(0, 0, -1));
        assertTrue(comps.inside);
        assertEquals(comps.normalv, new Vector(0, 0, -1));
    }

    @Test
    @DisplayName("The hit should offset the poiint")
    void hitOffset() {
        Ray r = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        Sphere shape = new Sphere();
        shape.setTransform(Matrix.translation(0, 0, 1));
        Intersection i = new Intersection(5, shape);
        Comps comps = i.prepareComputations(r);
        assertTrue(comps.overPoint.z < -Util.EPSILON / 2);
        assertTrue(comps.point.z > comps.overPoint.z);

    }

    @Test
    @DisplayName("Precomputing the reflection vector")
    void preComputeReflection() {
        Plane s = new Plane();
        Ray r = new Ray(new Point(0, 1, -1), new Vector(0, -Math.sqrt(2) / 2, Math.sqrt(2) / 2));
        Intersection i = new Intersection(Math.sqrt(2), s);
        Comps comps = i.prepareComputations(r);
        assertEquals(new Vector(0, Math.sqrt(2) / 2, Math.sqrt(2) / 2), comps.reflectv);
    }

    @Test
    @DisplayName("Finding n1 and n2 at various intersections")
    void findN1N2() {
        double[] nValues = new double[]{
                1.0, 1.5,
                1.5, 2.0,
                2.0, 2.5,
                2.5, 2.5,
                2.5, 1.5,
                1.5, 1.0};


        Sphere a = Sphere.glassSphere();
        a.setTransform(Matrix.scaling(2, 2, 2));
        a.material.refractiveIndex = 1.5;

        Sphere b = Sphere.glassSphere();
        b.setTransform(Matrix.translation(0, 0, -.25));
        b.material.refractiveIndex = 2.0;

        Sphere c = Sphere.glassSphere();
        c.setTransform(Matrix.translation(0, 0, .25));
        c.material.refractiveIndex = 2.5;

        Ray r = new Ray(new Point(0, 0, -4), new Vector(0, 0, 1));
        ArrayList<Intersection> xs = new ArrayList<>();
        xs.add(new Intersection(2, a));
        xs.add(new Intersection(2.75, b));
        xs.add(new Intersection(3.25, c));
        xs.add(new Intersection(4.75, b));
        xs.add(new Intersection(5.25, c));
        xs.add(new Intersection(6, a));
        for (int i = 0; i < xs.size(); i++) {
            Comps comps = xs.get(i).prepareComputations(r, xs);
            assertEquals(nValues[i * 2], comps.n1);
            assertEquals(nValues[i * 2 + 1], comps.n2);

        }
    }


    @Test
    @DisplayName("The under point is offset below the surface")
    void underPoint() {
        Ray r = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        Shape s = Sphere.glassSphere();
        s.setTransform(Matrix.translation(0, 0, 1));
        Intersection i = new Intersection(5, s);
        Comps comps = i.prepareComputations(r);
        assertTrue(comps.underPoint.z > Util.EPSILON / 2);
        assertTrue(comps.point.z < comps.underPoint.z);
    }

    @Test
    @DisplayName("The Schlick approximation under total internal reflection")
    void schlickTIR() {
        Shape s = Sphere.glassSphere();
        Ray r = new Ray(new Point(0, 0, Math.sqrt(2) / 2), new Vector(0, 1, 0));
        ArrayList<Intersection> xs = new ArrayList<>();
        xs.add(new Intersection(-Math.sqrt(2) / 2, s));
        xs.add(new Intersection(Math.sqrt(2) / 2, s));
        Comps comps = xs.get(1).prepareComputations(r, xs);
        double reflectance = comps.schlick();
        assertEquals(1.0, reflectance);
    }


    @Test
    @DisplayName("The Schlick approximation with a perpendicular viewing angle")
    void schlickPerpendicular() {
        Shape s = Sphere.glassSphere();
        Ray r = new Ray(new Point(0, 0, 0), new Vector(0, 1, 0));
        ArrayList<Intersection> xs = new ArrayList<>();
        xs.add(new Intersection(-1, s));
        xs.add(new Intersection(1, s));
        Comps comps = xs.get(1).prepareComputations(r, xs);
        double reflectance = comps.schlick();
        assertTrue(Util.EQUALS(0.04, reflectance));
    }

    @Test
    @DisplayName("The Schlick approximation with a perpendicular viewing angle")
    void schlickSmallAngleLargerN2() {
        Shape s = Sphere.glassSphere();
        Ray r = new Ray(new Point(0, 0.99, -2), new Vector(0, 0, 1));
        Intersection i = new Intersection(1.8589, s);
        Comps comps = i.prepareComputations(r);
        double reflectance = comps.schlick();
        assertTrue(Util.EQUALS(.48873, reflectance));
    }


}
