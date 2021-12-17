package main.lights;

import main.Color;
import main.Point;
import main.World;

import java.util.Objects;

public abstract class Light {

    public Point position;
    public Color intensity;
    public int samples;
    public int usteps, vsteps;

    public abstract double intensityAt(Point point, World w);
    public abstract Point pointOnLight(int u, int v);


        @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Light light)) return false;
        return Objects.equals(position, light.position) && Objects.equals(intensity, light.intensity);
    }

}
