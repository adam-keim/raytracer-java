package main.lights;

import main.*;

public class AreaLight extends Light {
    public final Point corner;
    public final Vector uvec;
    public final Vector vvec;
    public Sequence jitterBy;

    public AreaLight(Point corner, Vector full_uvec, int usteps, Vector full_vvec, int vsteps, Color intensity) {
        this.intensity = intensity;
        this.corner = corner;
        this.vvec = full_vvec.divide(vsteps).toVector();
        this.uvec = full_uvec.divide(usteps).toVector();
        this.usteps = usteps;
        this.vsteps = vsteps;
        this.samples = usteps * vsteps;
        this.position = corner.add(this.vvec.multiply((double) vsteps / 2))
                .add(this.uvec.multiply((double) usteps / 2)).toPoint();
    }

    @Override
    public double intensityAt(Point point, World w) {
        double total = 0.0;
        for (int v = 0; v < this.vsteps; v++) {
            for (int u = 0; u < this.usteps; u++) {
                Point position = this.pointOnLight(u, v);
                if (!w.isShadowed(position, point)) {
                    total += 1.0;
                }
            }
        }
        return total / this.samples;
    }

    public Point pointOnLight(int u, int v) {
        if (this.jitterBy == null) {
            return this.corner.add(this.uvec.multiply(u + .5))
                    .add(this.vvec.multiply(v + .5)).toPoint();
        } else {
            return this.corner.add(this.uvec.multiply(u + this.jitterBy.next()))
                    .add(this.vvec.multiply(v + this.jitterBy.next())).toPoint();

        }
    }
}
