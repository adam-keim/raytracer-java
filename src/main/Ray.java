package main;

import java.util.Objects;

public class Ray {
    public final Vector direction;
    public final Point origin;

    public Ray(Point origin, Vector direction) {
        this.direction = direction;
        this.origin = origin;
    }

    public Point position(double t) {
        return this.origin.add(this.direction.multiply(t)).toPoint();
    }

    public Ray transform(Matrix t) {
        return new Ray(t.multiply(origin).toPoint(), t.multiply(direction).toVector());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ray ray = (Ray) o;
        return Objects.equals(direction, ray.direction) && Objects.equals(origin, ray.origin);
    }

}
