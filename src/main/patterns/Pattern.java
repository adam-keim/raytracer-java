package main.patterns;

import main.Color;
import main.Matrix;
import main.Point;
import main.shapes.Shape;

public abstract class Pattern {
    public final Color a;
    public final Color b;
    private Matrix transform;

    public Pattern(Color a, Color b) {
        this.a = a;
        this.b = b;
        this.transform = Matrix.identity();
    }

    public abstract Color patternAt(Point p);

    public Color patternAtShape(Shape object, Point point) {
        Point objectPoint = object.getTransform().inverse().multiply(point).toPoint();
        Point patternPoint = getTransform().inverse().multiply(objectPoint).toPoint();

        return patternAt(patternPoint);
    }

    public Matrix getTransform() {
        return transform;
    }

    public void setTransform(Matrix transform) {
        this.transform = transform;
    }
}
