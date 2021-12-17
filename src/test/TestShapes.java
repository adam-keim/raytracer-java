package test;

import main.*;
import main.shapes.Shape;
import main.shapes.Sphere;
import main.shapes.TestShape;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Shapes")
public class TestShapes {
    @Test
    @DisplayName("The default tranformation")
    void defaultTransformation() {
        Shape s = new TestShape();
        assertEquals(s.getTransform(), Matrix.identity());

    }

    @Test
    @DisplayName("Assigning a transformation")
    void changeTransform() {
        Shape s = new TestShape();
        Matrix t = Matrix.translation(2, 3, 4);
        s.setTransform(t);
        assertEquals(s.getTransform(), t);
    }

    @Test
    @DisplayName("The default materiial")
    void defaultMaterial() {
        Shape s = new TestShape();
        Material m = s.material;
        assertEquals(m, new Material());
    }

    @Test
    @DisplayName("Assigning a material")
    void assignMaterial() {
        Shape s = new TestShape();
        Material m = new Material();
        m.ambient = 1;
        s.material = m;
        assertEquals(s.material, m);
    }

    @Test
    @DisplayName("Intersecting a scaled shape with a ray")
    void intersectScaled() {
        Ray r = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        TestShape s = new TestShape();
        s.setTransform(Matrix.scaling(2, 2, 2));
        ArrayList<Intersection> xs = s.intersect(r);
        assertEquals(s.savedRay.origin, new Point(0, 0, -2.5));
        assertEquals(s.savedRay.direction, new Vector(0, 0, 0.5));

    }

    @Test
    @DisplayName("Intersecting a translated shape with a ray")
    void intersectTrans() {
        Ray r = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        TestShape s = new TestShape();
        s.setTransform(Matrix.translation(5, 0, 0));
        ArrayList<Intersection> xs = s.intersect(r);
        assertEquals(s.savedRay.origin, new Point(-5, 0, -5));
        assertEquals(s.savedRay.direction, new Vector(0, 0, 1));
    }

    @Test
    @DisplayName("Computing the normal on a translated shape")
    void translatedNormal() {
        TestShape s = new TestShape();
        s.setTransform(Matrix.translation(0, 1, 0));

        Vector n = s.normalAt(new Point(0, 1.70711, -0.70711), null);
        assertEquals(n, new Vector(0, 0.70711, -0.70711));


    }

    @Test
    @DisplayName("Computing the normal on a transformed shape")
    void transformedNormal() {
        TestShape s = new TestShape();
        s.setTransform(Matrix.scaling(1, .5, 1).multiply(Matrix.rotationZ(Math.PI / 5)));


        Vector n = s.normalAt(new Point(0, Math.sqrt(2) / 2, -Math.sqrt(2) / 2), null);
        assertEquals(n, new Vector(0, 0.97014, -0.24254));


    }

    @Test
    @DisplayName("A shape has a parent attribute")
    void shapeParent() {
        Shape s = new TestShape();
        assertNull(s.parent);
    }

    @Test
    @DisplayName("Test Shape has arbitrary bounds")
    void boundShape() {
        TestShape s = new TestShape();
        BoundingBox box = s.boundsOf();
        assertEquals(new Point(-1, -1, -1), box.min);
        assertEquals(new Point(1, 1, 1), box.max);

    }

    @Test
    @DisplayName("Querying a shape's bounding box in its parent's space")
    void parentSpaceBox() {
        Shape s = new Sphere();
        s.setTransform(Matrix.translation(1,-3,5).multiply(Matrix.scaling(0.5,2,4)));
        BoundingBox box = s.parentSpaceBoundsOf();
        assertEquals(new Point(0.5,-5,1), box.min);
        assertEquals(new Point(1.5,-1,9), box.max);
    }

    @Test
    @DisplayName("Subdividing a primitive does nothing")
    void subdividePrimitive() {
        Shape s = new Sphere();
        s.divide(1);
        assertEquals(s, new Sphere()); // Our sphere hasn't been changed
    }


}
