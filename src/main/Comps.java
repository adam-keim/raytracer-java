package main;

import main.shapes.Shape;

public class Comps {
    public double t;
    public Shape object;
    public Point point;
    public Point overPoint;
    public Vector eyev;
    public Vector normalv;
    public boolean inside;
    public Vector reflectv;
    public double n1;
    public double n2;
    public Point underPoint;


    public Comps() {
    }

    public double schlick() {
        double cos = this.eyev.dot(this.normalv);

        if(this.n1 > this.n2) {
            double n = this.n1 / this.n2;
            double sin2_t = Math.pow(n, 2)*(1-Math.pow(cos, 2));
            if(sin2_t > 1.0) {
                return 1.0;
            }

            cos = Math.sqrt(1.0-sin2_t);
        }
        double r0 = Math.pow(((this.n1 - this.n2) / (this.n1 + this.n2)), 2);
        return r0 + (1-r0) * Math.pow(1-cos, 5);
    }
}
