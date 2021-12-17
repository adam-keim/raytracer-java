package test;

import main.*;
import main.lights.PointLight;
import main.patterns.TestPattern;
import main.shapes.Plane;
import main.shapes.Shape;
import main.shapes.Sphere;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("World")
public class TestWorld {
    @Test
    @DisplayName("Creating a world")
    void worldCreation() {
        World w = new World();
        assertEquals(0, w.getShapes().size());
        assertNull(w.getLight());
    }

    @Test
    @DisplayName("The default world")
    void defaultWorld() {
        World w = World.defaultWorld();
        PointLight light = new PointLight(new Point(-10, 10, -10), new Color(1, 1, 1));

        Sphere s1 = new Sphere();
        s1.material.color = new Color(0.8, 1.0, 0.6);
        s1.material.diffuse = 0.7;
        s1.material.specular = 0.2;

        Sphere s2 = new Sphere();
        s2.setTransform(Matrix.scaling(0.5, 0.5, 0.5));

        assertEquals(w.getLight(), light);
        assertTrue(w.getShapes().contains(s1));
        assertTrue(w.getShapes().contains(s2));

    }

    @Test
    @DisplayName("Intersect a world with a ray")
    void intersectWorld() {
        World w = World.defaultWorld();
        Ray r = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        ArrayList<Intersection> xs = w.intersect(r);
        assertEquals(4, xs.size());
        assertEquals(4, xs.get(0).t);
        assertEquals(4.5, xs.get(1).t);
        assertEquals(5.5, xs.get(2).t);
        assertEquals(6, xs.get(3).t);
    }

    @Test
    @DisplayName("Shading an intersection")
    void shadeIntersection() {
        World w = World.defaultWorld();
        Ray r = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        Sphere s = (Sphere) w.getShapes().get(0);
        Intersection i = new Intersection(4, s);
        Comps c = i.prepareComputations(r);
        Color color = w.shadeHit(c);
        assertEquals(color, new Color(0.38066, 0.47583, 0.2855));
    }

    @Test
    @DisplayName("Shading an intersection from the inside")
    void shadeIntersectionInside() {
        World w = World.defaultWorld();
        w.setLight(new PointLight(new Point(0, 0.25, 0), new Color(1, 1, 1)));

        Ray r = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Sphere s = (Sphere) w.getShapes().get(1);
        Intersection i = new Intersection(.5, s);
        Comps c = i.prepareComputations(r);
        Color color = w.shadeHit(c);
        assertEquals(color, new Color(0.90498, 0.90498, 0.90498));
    }

    @Test
    @DisplayName("The color when a ray misses")
    void rayMiss() {
        World w = World.defaultWorld();
        Ray r = new Ray(new Point(0, 0, -5), new Vector(0, 1, 0));
        Color c = w.colorAt(r);
        assertEquals(c, new Color(0, 0, 0));
    }

    @Test
    @DisplayName("The color when a ray hits")
    void rayHit() {
        World w = World.defaultWorld();
        Ray r = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        Color c = w.colorAt(r);
        assertEquals(c, new Color(0.38066, 0.47583, 0.2855));
    }

    @Test
    @DisplayName("The color with an intersection behind the ray")
    void intersectionBehind() {
        World w = World.defaultWorld();
        w.getShapes().get(0).material.ambient = 1;
        w.getShapes().get(1).material.ambient = 1;

        Ray r = new Ray(new Point(0, 0, 0.75), new Vector(0, 0, -1));
        Color c = w.colorAt(r);
        assertEquals(c, w.getShapes().get(1).material.color);
    }

    @Test
    @DisplayName("ShadeHit is given an intersection in shadow")
    void shadeHitShadow() {
        World w = new World();
        w.setLight(new PointLight(new Point(0, 0, -10), new Color(1, 1, 1)));
        Sphere s1 = new Sphere();
        w.addShape(s1);
        Sphere s2 = new Sphere();
        s2.setTransform(Matrix.translation(0, 0, 10));
        w.addShape(s2);
        Ray r = new Ray(new Point(0, 0, 5), new Vector(0, 0, 1));
        Intersection i = new Intersection(4, s2);
        Comps comps = i.prepareComputations(r);
        Color c = w.shadeHit(comps);
        assertEquals(c, new Color(0.1, 0.1, 0.1));
    }

    @Test
    @DisplayName("The reflected color for a nonreflective material")
    void reflectedNonreflective() {
        World w = World.defaultWorld();
        Ray r = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        Shape s = w.getShapes().get(1);
        s.material.ambient = 1;
        Intersection i = new Intersection(1, s);
        Comps comps = i.prepareComputations(r);
        Color c = w.reflectedColor(comps);
        assertEquals(new Color(0, 0, 0), c);
    }

    @Test
    @DisplayName("The reflected color for a reflecive material")
    void reflectedReflective() {
        World w = World.defaultWorld();
        Shape s = new Plane();
        s.material.reflective = .5;
        s.setTransform(Matrix.translation(0, -1, 0));
        w.addShape(s);
        Ray r = new Ray(new Point(0, 0, -3), new Vector(0, -Math.sqrt(2) / 2, Math.sqrt(2) / 2));
        Intersection i = new Intersection(Math.sqrt(2), s);
        Comps comps = i.prepareComputations(r);
        Color c = w.reflectedColor(comps);
        assertEquals(new Color(0.19033, 0.23791, 0.14274), c);
    }

    @Test
    @DisplayName("shadeHit with a reflective amterial")
    void reflectedHit() {
        World w = World.defaultWorld();
        Shape s = new Plane();
        s.material.reflective = .5;
        s.setTransform(Matrix.translation(0, -1, 0));
        w.addShape(s);
        Ray r = new Ray(new Point(0, 0, -3), new Vector(0, -Math.sqrt(2) / 2, Math.sqrt(2) / 2));
        Intersection i = new Intersection(Math.sqrt(2), s);
        Comps comps = i.prepareComputations(r);
        Color c = w.shadeHit(comps);
        assertEquals(new Color(0.876757, 0.92434, 0.82917), c);
    }

    @Test
    @DisplayName("colorAt with mutually reflective surfaces")
    void recursiveReflection() {
        World w = new World();
        w.setLight(new PointLight(new Point(0, 0, 0), new Color(1, 1, 1)));

        Shape lower = new Plane();
        lower.material.reflective = 1;
        lower.setTransform(Matrix.translation(0, -1, 0));
        w.addShape(lower);

        Shape upper = new Plane();
        upper.material.reflective = 1;
        upper.setTransform(Matrix.translation(0, 1, 0));
        w.addShape(upper);

        Ray r = new Ray(new Point(0, 0, 0), new Vector(0, 1, 0));
        assertNotNull(w.colorAt(r));
    }

    @Test
    @DisplayName("The reflected color at the maximum recursive depth")
    void maximumDepth() {
        World w = World.defaultWorld();
        Shape shape = new Plane();
        shape.material.reflective = 0.5;
        shape.setTransform(Matrix.translation(0, -1, 0));

        Ray r = new Ray(new Point(0, 0, -3), new Vector(0, -Math.sqrt(2) / 2, Math.sqrt(2) / 2));
        Intersection i = new Intersection(Math.sqrt(2), shape);
        Comps comps = i.prepareComputations(r);
        Color c = w.reflectedColor(comps, 0);
        assertEquals(new Color(0, 0, 0), c);

    }

    @Test
    @DisplayName("The refracted color with an opaque surface")
    void refractedOpaque() {
        World w = World.defaultWorld();
        Shape shape = w.getShapes().get(0);
        Ray r = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        ArrayList<Intersection> xs = new ArrayList<>();
        xs.add(new Intersection(4, shape));
        xs.add(new Intersection(6, shape));
        Comps comps = xs.get(0).prepareComputations(r, xs);
        Color c = w.refractedColor(comps, 5);
        assertEquals(new Color(0, 0, 0), c);
    }

    @Test
    @DisplayName("The refracted color at the maximum recursive depth")
    void refractedMax() {
        World w = World.defaultWorld();
        Shape shape = w.getShapes().get(0);
        shape.material.transparency = 1.0;
        shape.material.refractiveIndex = 1.5;
        Ray r = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        ArrayList<Intersection> xs = new ArrayList<>();
        xs.add(new Intersection(4, shape));
        xs.add(new Intersection(6, shape));
        Comps comps = xs.get(0).prepareComputations(r, xs);
        Color c = w.refractedColor(comps, 0);
        assertEquals(new Color(0, 0, 0), c);
    }

    @Test
    @DisplayName("The refracted color under total internal reflection")
    void TIR() {
        World w = World.defaultWorld();
        Shape shape = w.getShapes().get(0);
        shape.material.transparency = 1.0;
        shape.material.refractiveIndex = 1.5;
        Ray r = new Ray(new Point(0, 0, Math.sqrt(2) / 2), new Vector(0, 1, 0));
        ArrayList<Intersection> xs = new ArrayList<>();
        xs.add(new Intersection(-Math.sqrt(2) / 2, shape));
        xs.add(new Intersection(Math.sqrt(2) / 2, shape));
        Comps comps = xs.get(1).prepareComputations(r, xs);
        Color c = w.refractedColor(comps, 5);
        assertEquals(new Color(0, 0, 0), c);
    }

    @Test
    @DisplayName("The refracted color with a refracted ray")
    void refracted() {
        World w = World.defaultWorld();

        Shape a = w.getShapes().get(0);
        a.material.ambient = 1.0;
        a.material.pattern = new TestPattern();

        Shape b = w.getShapes().get(1);
        b.material.transparency = 1.0;
        b.material.refractiveIndex = 1.5;

        Ray r = new Ray(new Point(0, 0, 0.1), new Vector(0, 1, 0));
        ArrayList<Intersection> xs = new ArrayList<>();
        xs.add(new Intersection(-.9899, a));
        xs.add(new Intersection(-.4899, b));
        xs.add(new Intersection(.4899, b));
        xs.add(new Intersection(.9899, a));
        Comps comps = xs.get(2).prepareComputations(r, xs);
        Color c = w.refractedColor(comps, 5);
        assertEquals(new Color(0, .99888, .04721), c);
    }

    @Test
    @DisplayName("shadeHit with a transparent material")
    void shadeHitTransparent() {
        World w = World.defaultWorld();

        Plane floor = new Plane();
        floor.setTransform(Matrix.translation(0, -1, 0));
        floor.material.transparency = 0.5;
        floor.material.refractiveIndex = 1.5;
        w.addShape(floor);

        Sphere ball = new Sphere();
        ball.material.color = new Color(1, 0, 0);
        ball.material.ambient = .5;
        ball.setTransform(Matrix.translation(0, -3.5, -0.5));
        w.addShape(ball);

        Ray r = new Ray(new Point(0, 0, -3), new Vector(0, -Math.sqrt(2) / 2, Math.sqrt(2) / 2));
        Intersection x = new Intersection(Math.sqrt(2), floor);
        Comps comps = x.prepareComputations(r);
        Color c = w.shadeHit(comps, 5);
        assertEquals(new Color(0.93642, 0.68642, 0.68642), c);


    }

    @Test
    @DisplayName("shadeHit with a reflective, transparent material")
    void shadeHitReflectiveTransparent() {
        World w = World.defaultWorld();

        Ray r = new Ray(new Point(0, 0, -3), new Vector(0, -Math.sqrt(2) / 2, Math.sqrt(2) / 2));
        Shape floor = new Plane();
        floor.setTransform(Matrix.translation(0, -1, 0));
        floor.material.reflective = 0.5;
        floor.material.transparency = 0.5;
        floor.material.refractiveIndex = 1.5;
        w.addShape(floor);

        Shape ball = new Sphere();
        ball.setTransform(Matrix.translation(0, -3.5, -0.5));
        ball.material.color = new Color(1, 0, 0);
        ball.material.ambient = 0.5;
        w.addShape(ball);

        Intersection i = new Intersection(Math.sqrt(2), floor);
        Comps comps = i.prepareComputations(r);
        Color c = w.shadeHit(comps, 5);
        assertEquals(new Color(0.93391, 0.69643, 0.69243), c);
    }

    @Test
    @DisplayName("isShadow tests for occlusion between two points")
    void isShadowOcclusion() {
        Point[] points = {
                new Point(-10, -10, 10),
                new Point(10, 10, 10),
                new Point(-20, -20, -20),
                new Point(-5, -5, -5)
        };
        boolean[] isShadowed = {false, true, false, false};
        World w = World.defaultWorld();
        Point lightPosition = new Point(-10, -10, -10);
        for (int i = 0; i < points.length; i++) {
            assertEquals(isShadowed[i], w.isShadowed(lightPosition, points[i]));
        }
    }
}
