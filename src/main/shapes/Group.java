package main.shapes;


import main.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Group extends Shape {

    ArrayList<Shape> children;
    BoundingBox bounds;

    public Group() {
        this.children = new ArrayList<>();
        this.bounds = this.boundsOf();
    }

    public ArrayList<Shape> getChildren() {
        return children;
    }

    public void addChild(Shape child) {
        child.parent = this;
        this.children.add(child);
        this.update();
    }

    public Shape getChild(int index) {
        return this.children.get(index);
    }

    @Override
    public ArrayList<Intersection> localIntersect(Ray r) {
        ArrayList<Intersection> xs = new ArrayList<>();
        if(this.bounds.intersects(r)) {
            for (Shape shape : this.children) {
                xs.addAll(shape.intersect(r));
            }
            Collections.sort(xs);
        }
        return xs;
    }

    @Override
    public Vector localNormalAt(Point point, Intersection hit) {
        System.out.println("Please don't check the local normal of a group!");
        return null;
    }

    @Override
    public BoundingBox boundsOf() {
        BoundingBox box = new BoundingBox();
        if(this.children != null) {
            for (Shape shape : this.children) {
                box.addBox(shape.parentSpaceBoundsOf());
            }
        }
        return box;
    }

    @Override
    protected void update() {
        this.bounds = this.boundsOf();
        super.update();
    }

    public Group[] partitionChildren() {
        BoundingBox[] boxes = this.bounds.splitBounds();
        Group left = new Group();
        Group right = new Group();
        ArrayList<Shape> shapes = new ArrayList<>(this.children);
        for (Shape shape : shapes) {
            if(boxes[0].containsBox(shape.parentSpaceBoundsOf())) {
                left.addChild(shape);
                this.children.remove(shape);
            } else if(boxes[1].containsBox(shape.parentSpaceBoundsOf())) {
                right.addChild(shape);
                this.children.remove(shape);
            }
        }
        return new Group[] {left, right};
    }

    public void makeSubgroup(Shape[] shapes) {
        Group subgroup = new Group();
        for (Shape shape : shapes) {
            subgroup.addChild(shape);
        }
        this.addChild(subgroup);
    }

    @Override
    public void divide(double threshold) {
        if(threshold <= this.children.size()) {
            Group[] groups = this.partitionChildren();
            if(groups[0].getChildren().size() > 0) {
                this.makeSubgroup(new Shape[]{groups[0]});
            }
            if(groups[1].getChildren().size() > 0) {
                this.makeSubgroup(new Shape[]{groups[1]});
            }
        }
        for(Shape shape : this.children) {
            shape.divide(threshold);
        }
    }
}
