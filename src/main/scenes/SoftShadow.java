package main.scenes;

import main.*;
import main.lights.AreaLight;
import main.shapes.Cube;
import main.shapes.Group;
import main.shapes.Plane;
import main.shapes.Sphere;

public class SoftShadow {
    public static void main(String[] args) {
        int width = 3072;
        int height = 1920;

        Camera camera = new Camera(width, height, Math.PI / 4);
        camera.setTransform(Matrix.viewTransform(new Point(-3, 1, 2.5), new Point(0, .5, 0), new Vector(0, 1, 0)));
        World w = new World();

        int steps = 10;
        AreaLight light = new AreaLight(new Point(-1, 2, 4), new Vector(2, 0, 0), steps,
                new Vector(0, 2, 0), steps,
                new Color(1.5, 1.5, 1.5));
        light.jitterBy = Sequence.pseudo();
        w.setLight(light);

        Cube cube = new Cube();
        cube.material.color = new Color(1.5, 1.5, 1.5);
        cube.material.ambient = 1;
        cube.material.diffuse = 0;
        cube.material.specular = 0;
        cube.setTransform(Matrix.translation(0,3,4)
                .multiply(Matrix.scaling(1,1,.01)));
        cube.setShadow(false);
        w.addShape(cube);

        Plane plane = new Plane();
        plane.material.color = new Color(1,1,1);
        plane.material.ambient = 0.025;
        plane.material.diffuse = 0.67;
        plane.material.specular = 0;
        w.addShape(plane);

        Group shapes = new Group();

        Cube s1 = new Cube();


        Sphere s2 = new Sphere();
        shapes.addChild(s2);


        shapes.addChild(s1);
        s1.setTransform(Matrix.translation(1, .5, 0)
                .multiply(Matrix.scaling(.5,.5,.5)).multiply(Matrix.rotationY(1)));
        s1.material.color = new Color(1,0,0);
        s1.material.ambient = .1;
        s1.material.specular = 0;
        s1.material.diffuse =.6;
        s1.material.reflective = .3;
//        s1.material.refractiveIndex = 1.5;
//        s1.material.transparency = .6;

        s2.setTransform(Matrix.translation(-.25, .33, 0)
                .multiply(Matrix.scaling(.33,.33,.33)));
        s2.material.color = new Color(.5,.5,1);
        s2.material.ambient = .1;
        s2.material.specular = 0;
        s2.material.diffuse =.6;
        s2.material.reflective = .3;

        // shapes.divide(2); This slows down the render
        w.addShape(shapes);
//        w.setLight(new PointLight(new Point(-10, 10, -10), new Color(1, 1, 1)));

        Canvas canvas = camera.renderParallel(w);
        canvas.writeFile("softShadow.ppm");

    }

}
