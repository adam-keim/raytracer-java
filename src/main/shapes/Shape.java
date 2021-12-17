package main.shapes;

import main.*;

import java.util.ArrayList;
import java.util.Objects;

public abstract class Shape {
    public Shape parent = null;
    boolean shadow = true;
    private Matrix transform;
    public Material material;

    public Shape() {
        this.setTransform(Matrix.identity());
        this.material = new Material();
    }

    public boolean isShadow() {
        return shadow;
    }

    public void setShadow(boolean shadow) {
        this.shadow = shadow;
    }

    public ArrayList<Intersection> intersect(Ray r) {
        Ray local_ray = r.transform(this.getTransform().inverse());
        return localIntersect(local_ray);
    }

    public abstract ArrayList<Intersection> localIntersect(Ray r);

    public Vector normalAt(Point worldPoint, Intersection hit) {
        Point localPoint = worldToObject(worldPoint);
        Vector localNormal = localNormalAt(localPoint, hit);
        return normalToWorld(localNormal);
    }

    public abstract Vector localNormalAt(Point point, Intersection hit);


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shape shape = (Shape) o;
        return Objects.equals(getTransform(), shape.getTransform()) && Objects.equals(material, shape.material) && (this.parent == shape.parent);
    }

    public Point worldToObject(Point point) {

        if(this.parent != null) {
            point = this.parent.worldToObject(point);
        }
        return this.getTransform().inverse().multiply(point).toPoint();
    }

    public Vector normalToWorld(Vector localNormal) {
        Vector normal = this.getTransform().inverse().transpose().multiply(localNormal).toVector();
        normal.w = 0;
        normal = normal.normalize();
        if(this.parent != null) {
            normal = this.parent.normalToWorld(normal);
        }
        return normal;
    }

    public abstract BoundingBox boundsOf();

    public BoundingBox parentSpaceBoundsOf() {
        return this.boundsOf().transform(this.getTransform());
    }

    public Matrix getTransform() {
        return transform;
    }

    public void setTransform(Matrix transform) {
        this.transform = transform;
        update();
    }

    /**
     * Propagate changes from the child components to their parents.
     * For example, if we update the transform of a shape AFTER adding it to the group, then the group should update its bounding box.
     * Each shape can override the update function, but should call super.update()
     */
    protected void update() {
        if(this.parent != null) {
            this.parent.update();
        }
    }

    public void divide(double threshold) {
        // Do nothing if the shape is a primitive
    }
}
