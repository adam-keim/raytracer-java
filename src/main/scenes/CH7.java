package main.scenes;

import main.*;
import main.lights.PointLight;
import main.shapes.Sphere;

public class CH7 {
    public static void main(String[] args) {
        int width = 5000;

        Sphere floor = new Sphere();
        floor.setTransform(Matrix.scaling(10,.01,10));
        floor.material.color = new Color(1,0.9,0.9);
        floor.material.specular = 0;

        Sphere leftWall = new Sphere();
        leftWall.setTransform(Matrix.translation(0,0,5).multiply(
                Matrix.rotationY(-Math.PI/4)).multiply(
                        Matrix.rotationX(Math.PI/2)).multiply(Matrix.scaling(10, .01, 10)));
        leftWall.material = floor.material;


        Sphere rightWall = new Sphere();
        rightWall.setTransform(Matrix.translation(0,0,5).multiply(
                Matrix.rotationY(Math.PI/4)).multiply(
                        Matrix.rotationX(Math.PI/2)).multiply(Matrix.scaling(10, .01, 10)));
        rightWall.material = floor.material;

        Sphere middle = new Sphere();
        middle.setTransform(Matrix.translation(-.5, 1, .5));
        middle.material.color = new Color(0.1, 1, 0.5);
        middle.material.diffuse = 0.7;
        middle.material.specular = 0.3;

        Sphere right = new Sphere();
        right.setTransform(Matrix.translation(1.5, 0.5, -.5).multiply(Matrix.scaling(0.5,0.5,0.5)));
        right.material.color = new Color(0.5, 1, 0.1);
        right.material.diffuse = 0.7;
        right.material.specular = 0.3;

        Sphere left = new Sphere();
        left.setTransform(Matrix.translation(-1.5, 0.33, -.75).multiply(Matrix.scaling(0.33,0.33,0.33)));
        left.material.color = new Color(1, .1, 0.1);
        right.material.diffuse = 0.7;
        right.material.specular = 0.3;

        World w = new World();
        w.addShape(floor);
        w.addShape(leftWall);
        w.addShape(rightWall);
        w.addShape(middle);
        w.addShape(left);
        w.addShape(right);

        w.setLight(new PointLight(new Point(-10,10,-10), new Color(1,1,1)));

        Camera camera = new Camera(width, width/2, Math.PI / 3);
        camera.setTransform(Matrix.viewTransform(new Point(0,1.5,-5), new Point(0,1,0), new Vector(0,1,0)));

        Canvas canvas = camera.renderParallel(w);
        canvas.writeFile("Ch7.ppm");






    }
}
