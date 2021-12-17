package main.scenes;

import main.*;
import main.shapes.Sphere;

import java.util.ArrayList;

public class UnshadedSphere {

    public static void main(String[] args) {
        Point ray_origin = new Point(0, 0, -5);
        double wall_z = 10;
        double wall_size = 7.0;
        int canvas_pixels = 1000;
        double pixel_size = wall_size / canvas_pixels;
        double half = wall_size / 2;

        Canvas canvas = new Canvas(canvas_pixels, canvas_pixels);
        Color color = new Color(1,0,0);
        Sphere shape = new Sphere();

        for (int y = 0; y < canvas_pixels; y++) {
            System.out.println(y);
            double world_y = half - (pixel_size * y);
            for(int x = 0; x < canvas_pixels;x++) {
                double world_x = -half + (pixel_size * x);
                Point pos = new Point(world_x, world_y, wall_z);
                Ray r = new Ray(ray_origin, pos.subtract(ray_origin).toVector().normalize());
                ArrayList<Intersection> xs = shape.intersect(r);
                if(Intersection.hit(xs) != null) {
                    canvas.writePixel(x, y, color);
                }

            }
        }
        canvas.writeFile("ch5.ppm");
    }

}
