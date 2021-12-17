package test;

import main.*;
import main.shapes.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Groups")
public class TestGroups {
    @Test
    @DisplayName("Creating a new group")
    void newGroup() {
        Group g = new Group();
        assertEquals(Matrix.identity(), g.getTransform());
        assertEquals(0, g.getChildren().size());
    }


    @Test
    @DisplayName("Adding a child to a group")
    void addChild() {
        Group g = new Group();
        Shape s = new TestShape();
        g.addChild(s);
        assertTrue(g.getChildren().size() > 0);
        assertTrue(g.getChildren().contains(s));
        assertSame(s.parent, g);
    }

    @Test
    @DisplayName("Intersecting a ray with an empty group")
    void emptyRay() {
        Group g = new Group();
        Ray r = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1));
        ArrayList<Intersection> xs = g.localIntersect(r);
        assertEquals(0, xs.size());
    }

    @Test
    @DisplayName("Intersecting a ray with a nonempty group")
    void nonemptyRay() {
        Group g = new Group();
        Sphere s1 = new Sphere();
        Sphere s2 = new Sphere();
        s2.setTransform(Matrix.translation(0, 0, -3));
        Sphere s3 = new Sphere();
        s3.setTransform(Matrix.translation(5, 0, 0));
        g.addChild(s1);
        g.addChild(s2);
        g.addChild(s3);
        Ray r = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        ArrayList<Intersection> xs = g.localIntersect(r);
        assertEquals(4, xs.size());
        assertSame(xs.get(0).object, s2);
        assertSame(xs.get(1).object, s2);
        assertSame(xs.get(2).object, s1);
        assertSame(xs.get(3).object, s1);
    }


    @Test
    @DisplayName("Intersecting a transformed group")
    void transformedGroup() {
        Group g = new Group();
        g.setTransform(Matrix.scaling(2, 2, 2));
        Sphere s = new Sphere();
        s.setTransform(Matrix.translation(5, 0, 0));
        g.addChild(s);
        Ray r = new Ray(new Point(10, 0, -10), new Vector(0, 0, 1));
        ArrayList<Intersection> xs = g.intersect(r);
        assertEquals(2, xs.size());
    }

    @Test
    @DisplayName("Converting a point from world to object space")
    void convertPoint() {
        Group g1 = new Group();
        g1.setTransform(Matrix.rotationY(Math.PI / 2));
        Group g2 = new Group();
        g2.setTransform(Matrix.scaling(2, 2, 2));

        g1.addChild(g2);
        Sphere s = new Sphere();
        s.setTransform(Matrix.translation(5, 0, 0));
        g2.addChild(s);
        Point p = s.worldToObject(new Point(-2, 0, -10));
        assertEquals(new Point(0, 0, -1), p);
    }

    @Test
    @DisplayName("Converting a normal from object to world space")
    void convertNormal() {
        Group g1 = new Group();
        g1.setTransform(Matrix.rotationY(Math.PI / 2));
        Group g2 = new Group();
        g2.setTransform(Matrix.scaling(1, 2, 3));

        g1.addChild(g2);
        Sphere s = new Sphere();
        s.setTransform(Matrix.translation(5, 0, 0));
        g2.addChild(s);

        Vector n = s.normalToWorld(new Vector(Math.sqrt(3) / 3, Math.sqrt(3) / 3, Math.sqrt(3) / 3));
        assertEquals(new Vector(0.28571, 0.42857, -0.85714), n);
    }

    @Test
    @DisplayName("Finding the normal on a child object")
    void childNormal() {
        Group g1 = new Group();
        g1.setTransform(Matrix.rotationY(Math.PI / 2));
        Group g2 = new Group();
        g2.setTransform(Matrix.scaling(1, 2, 3));

        g1.addChild(g2);
        Sphere s = new Sphere();
        s.setTransform(Matrix.translation(5, 0, 0));
        g2.addChild(s);

        Vector n = s.normalAt(new Point(1.7321, 1.1547, -5.5774), null);
        assertEquals(new Vector(0.28570, 0.42854, -0.85716), n);

    }

    @Test
    @DisplayName("A group has a bounding box that contains its children")
    void groupBounds() {
        Sphere s = new Sphere();
        s.setTransform(Matrix.translation(2, 5, -3).multiply(Matrix.scaling(2, 2, 2)));
        Cylinder c = new Cylinder();
        c.minimum = -2;
        c.maximum = 2;
        c.setTransform(Matrix.translation(-4, -1, 4).multiply(Matrix.scaling(.5, 1, .5)));
        Group shape = new Group();
        shape.addChild(s);
        shape.addChild(c);
        BoundingBox box = shape.boundsOf();
        assertEquals(new Point(-4.5, -3, -5), box.min);
        assertEquals(new Point(4, 7, 4.5), box.max);
    }

    @Test
    @DisplayName("Intersecting ray and group doesn't main.test children if box is missed")
    void rayGroupMiss() {
        TestShape child = new TestShape();
        Group shape = new Group();
        shape.addChild(child);
        Ray r = new Ray(new Point(0, 0, -5), new Vector(0, 1, 0));
        shape.intersect(r);
        assertNull(child.savedRay);
    }

    @Test
    @DisplayName("Intersecting ray and group tests children if box is hit")
    void rayGroupHit() {
        TestShape child = new TestShape();
        Group shape = new Group();
        shape.addChild(child);
        Ray r = new Ray(new Point(0, 0, -5), new Vector(0, 0, 1));
        shape.intersect(r);
        assertNotNull(child.savedRay);
    }

    @Test
    @DisplayName("Partitioning a group's children")
    void partition() {
        Sphere s1 = new Sphere();
        s1.setTransform(Matrix.translation(-2, 0, 0));
        Sphere s2 = new Sphere();
        s2.setTransform(Matrix.translation(2, 0, 0));
        Sphere s3 = new Sphere();
        Group g = new Group();
        g.addChild(s1);
        g.addChild(s2);
        g.addChild(s3);

        Group[] groups = g.partitionChildren();
        assertEquals(1, g.getChildren().size());
        assertTrue(g.getChildren().contains(s3));
        assertTrue(groups[0].getChildren().contains(s1));

        assertTrue(groups[1].getChildren().contains(s2));
    }

    @Test
    @DisplayName("Creating a subgroup from a list of children")
    void subGroup() {
        Sphere s1 = new Sphere();
        Sphere s2 = new Sphere();
        Group g = new Group();
        g.makeSubgroup(new Shape[]{s1, s2});
        assertEquals(1, g.getChildren().size());
        assertTrue(g.getChildren().get(0) instanceof Group);
        assertTrue(((Group) g.getChildren().get(0)).getChildren().contains(s1));
        assertTrue(((Group) g.getChildren().get(0)).getChildren().contains(s2));

    }

    @Test
    @DisplayName("Subdividing a group partitions its children")
    void subdivideGroup() {
        Sphere s1 = new Sphere();
        s1.setTransform(Matrix.translation(-2, -2, 0));

        Sphere s2 = new Sphere();
        s2.setTransform(Matrix.translation(-2, 2, 0));

        Sphere s3 = new Sphere();
        s3.setTransform(Matrix.scaling(4, 4, 4));

        Group g = new Group();
        g.addChild(s1);
        g.addChild(s2);
        g.addChild(s3);
        g.divide(1);

        assertEquals(2, g.getChildren().size());
        assertTrue(g.getChildren().contains(s3));
        Shape sub = g.getChildren().get(1);
        assertTrue(sub instanceof Group);
        Group subgroup = (Group) ((Group) sub).getChild(0);

        assertEquals(2, subgroup.getChildren().size());
        assertTrue(subgroup.getChild(0) instanceof Group);
        assertTrue(((Group)((Group) subgroup.getChild(0)).getChild(0)).getChildren().contains(s1));
        assertTrue(subgroup.getChild(1) instanceof Group);
        assertTrue(((Group)((Group) subgroup.getChild(1)).getChild(0)).getChildren().contains(s2));

    }

    @Test
    @DisplayName("Subdividing a group with too few children")
    void subdivideToofew() {
        Sphere s1 = new Sphere();
        s1.setTransform(Matrix.translation(-2,0,0));

        Sphere s2 = new Sphere();
        s2.setTransform(Matrix.translation(2,1,0));


        Sphere s3 = new Sphere();
        s3.setTransform(Matrix.translation(2,-1,0));
        Group subgroup = new Group();
        subgroup.addChild(s1);
        subgroup.addChild(s2);
        subgroup.addChild(s3);

        Sphere s4 = new Sphere();
        Group g = new Group();
        g.addChild(s4);
        g.addChild(subgroup);

        g.divide(3);

        //TODO: Add more tests here.


    }
}
