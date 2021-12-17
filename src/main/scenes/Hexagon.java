package main.scenes;

import main.*;
import main.lights.PointLight;
import main.shapes.Cylinder;
import main.shapes.Group;
import main.shapes.Sphere;

public class Hexagon {
    public static void main(String[] args) {
        int width = 2000;
        World w = new World();
        w.addShape(hexagon());
        w.setLight(new PointLight(new Point(-10,10,-10), new Color(1,.25,1)));

        Camera camera = new Camera(width, width/2, Math.PI / 3);
        camera.setTransform(Matrix.viewTransform(new Point(3,2.5,-5), new Point(0,0,0), new Vector(0,1,0)));

        Canvas canvas = camera.renderParallel(w);
        canvas.writeFile("hexagon.ppm");


    }

    static Sphere hexagonCorner() {
        Sphere corner = new Sphere();
        corner.setTransform(Matrix.translation(0,0,-1).multiply(Matrix.scaling(.25, .25, .25)));
        return corner;

    }

    static Cylinder hexagonEdge() {
        Cylinder edge = new Cylinder();
        edge.minimum = 0;
        edge.maximum = 1;
        edge.setTransform(Matrix.translation(0,0,-1).multiply(Matrix.rotationY(-Math.PI / 6)).multiply(Matrix.rotationZ(-Math.PI /2 )).multiply(Matrix.scaling(.25, 1, .25)));
        return edge;
    }

    static Group hexagonSide() {
        Group side = new Group();
        side.addChild(hexagonCorner());
        side.addChild(hexagonEdge());
        return side;
    }

    static Group hexagon() {
        Group hex = new Group();
        for(int i = 0; i < 6; i++) {
            Group side = hexagonSide();
            side.setTransform(Matrix.rotationY(i*Math.PI/3));
            hex.addChild(side);
        }
        return hex;
    }
}