package main.scenes;


import main.*;
import main.lights.PointLight;
import main.shapes.Sphere;

import java.util.ArrayList;

public class ShadedSphere {

    public static void main(String[] args) {
        Point ray_origin = new Point(0, 0, -5);
        double wall_z = 10;
        double wall_size = 7.0;
        int canvas_pixels = 1000;
        double pixel_size = wall_size / canvas_pixels;
        double half = wall_size / 2;

        Canvas canvas = new Canvas(canvas_pixels, canvas_pixels);
        Sphere sphere = new Sphere();

        sphere.material.color = new Color(1, 0.2, 1);

        Point light_position = new Point(-10,10,-10);
        Color light_color = new Color(1,1,1);
        PointLight light = new PointLight(light_position, light_color);



        for (int y = 0; y < canvas_pixels; y++) {
            System.out.println(y);
            double world_y = half - (pixel_size * y);
            for(int x = 0; x < canvas_pixels;x++) {
                double world_x = -half + (pixel_size * x);
                Point pos = new Point(world_x, world_y, wall_z);
                Ray r = new Ray(ray_origin, pos.subtract(ray_origin).toVector().normalize());
                ArrayList<Intersection> xs = sphere.intersect(r);
                Intersection hit = Intersection.hit(xs);
                if(hit != null) {
                    Point point = r.position(hit.t);
                    Vector normal = hit.object.normalAt(point, hit);
                    Vector eye = r.direction.negate().toVector();
                    Color c = hit.object.material.lighting(hit.object, light, point, eye, normal, false);
                    canvas.writePixel(x,y,c);
                }

            }
        }
        canvas.writeFile("ch6.ppm");
    }

}