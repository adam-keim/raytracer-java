package main.scenes;

import main.*;
import main.lights.AreaLight;
import main.shapes.Shape;

public class Dragon {
    public static void main(String[] args) {
        int width = 2500;
        Parser p = Parser.parseFile("dragon.obj");
        System.out.println("Finished parsing");
        Camera camera = new Camera(width, width / 2, Math.PI / 4);
        camera.setTransform(Matrix.viewTransform(new Point(10, 10, 10), new Point(0, .5, 0), new Vector(0, 1, 0)));
        World w = new World();
        assert p != null;
        w.addShape(p.defaultGroup);
        p.defaultGroup.divide(100);
        System.out.println("Finished dividing");

        int steps = 4;
        AreaLight light = new AreaLight(new Point(1, 2, 4), new Vector(2, 0, 0), steps,
                new Vector(0, 2, 0), steps,
                new Color(1.5, 1.5, 1.5));
        light.jitterBy = Sequence.pseudo();
        w.setLight(light);


        Canvas canvas = camera.renderParallel(w);
        canvas.writeFile("dragon.ppm");
    }
}
