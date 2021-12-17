package main.lights;

import main.Color;
import main.Point;
import main.World;

public class PointLight extends Light {

    public PointLight(Point position, Color intensity) {
        this.position = position;
        this.intensity = intensity;
        this.samples = 1;
        this.usteps = 1;
        this.vsteps = 1;
    }

    @Override
    public double intensityAt(Point point, World w) {
        if(w.isShadowed(this.position, point)) {
            return 0.0;
        } else {
            return 1.0;
        }
    }

    public Point pointOnLight(int u, int v) {
        return this.position;
    }
}
