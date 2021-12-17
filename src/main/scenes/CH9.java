package main.scenes;

import main.*;
import main.lights.AreaLight;
import main.patterns.CheckersPattern;
import main.patterns.GradientPattern;
import main.patterns.PerlinPattern;
import main.patterns.StripePattern;
import main.shapes.Cylinder;
import main.shapes.Plane;
import main.shapes.Sphere;

public class CH9 {
    public static void main(String[] args) {
        int width = 2500;

        Plane floor = new Plane();
        floor.material.pattern = new CheckersPattern(new Color(0, 1, 0), new Color(1, 1, 1));

        Sphere middle = new Sphere();
        middle.setTransform(Matrix.translation(-.5, 1, .5));
        middle.material.pattern = new PerlinPattern(1000, new StripePattern(new Color(0, 1, 0), new Color(0, .5, 0)));
        middle.material.pattern.setTransform(Matrix.rotationY(-.5).multiply(
                Matrix.scaling(.25, .25, .25)));
        middle.material.diffuse = .7;
        middle.material.specular = .3;

        Cylinder right = new Cylinder();
        right.maximum = 2.5;
        right.setTransform(Matrix.translation(1.5, 0.5, -.5).multiply(Matrix.scaling(0.5, 0.5, 0.5)));
        right.material.pattern = new GradientPattern(new Color(1, 0, 0), new Color(0, 1, 0));
        right.material.pattern.setTransform(Matrix.translation(1, 0, 0).multiply(Matrix.scaling(2.1, 2.1, 2.1)));
        right.material.diffuse = 0.7;
        right.material.specular = 0.3;


        Sphere left = new Sphere();
        left.setTransform(Matrix.translation(-1.5, 0.33, -.75).multiply(Matrix.scaling(0.33, 0.33, 0.33)));
        left.material.color = new Color(.1, .1, 0.1);
        left.material.diffuse = 0.1;
        left.material.ambient = .1;
        left.material.specular = 1;
        left.material.reflective = .9;
        left.material.refractiveIndex = 1.5;
        left.material.transparency = 0.9;
        left.material.shininess = 300;
        left.setShadow(false);


        World w = new World();
        w.addShape(floor);
        w.addShape(middle);
        w.addShape(left);
        w.addShape(right);
        int steps = 8;
        AreaLight light = new AreaLight(new Point(-10, 10, -10), new Vector(5, 0, 0), steps, new Vector(0,5,0), steps, new Color(1,1,1));
        light.jitterBy = new Sequence(new double[]{.7, .3, .9, .1, .5});

//        w.setLight(new PointLight(new Point(-10, 10, -10), new Color(1, 1, 1)));
        w.setLight(light);
        Camera camera = new Camera(width, width / 2, Math.PI / 3);
        camera.setTransform(Matrix.viewTransform(new Point(0, 1.5, -5), new Point(0, 1, 0), new Vector(0, 1, 0)));

        Canvas canvas = camera.renderParallel(w);
        canvas.writeFile("Ch9.ppm");


    }
}
