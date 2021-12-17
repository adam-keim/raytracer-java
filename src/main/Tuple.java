package main;

public class Tuple {
    public double x, y, z, w;

    public Tuple(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }


    public Point toPoint() {
        return new Point(this.x, this.y, this.z, this.w);
    }

    public Vector toVector() {
        return new Vector(this.x, this.y, this.z, this.w);
    }

    public boolean isPoint() {
        return Util.EQUALS(w, 1.0);
    }

    public boolean isVector() {
        return Util.EQUALS(w, 0.0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tuple tuple)) return false;
        return Util.EQUALS(this.x, tuple.x) && Util.EQUALS(this.y, tuple.y) && Util.EQUALS(this.z, tuple.z) && Util.EQUALS(this.w, tuple.w);
    }

    public Tuple add(Tuple b) {
        return new Tuple(this.x + b.x, this.y + b.y, this.z + b.z, this.w + b.w);
    }

    public Tuple subtract(Tuple b) {
        return new Tuple(this.x - b.x, this.y - b.y, this.z - b.z, this.w - b.w);
    }

    public Tuple negate() {
        return new Tuple(-this.x, -this.y, -this.z, -this.w);
    }

    public Tuple multiply(double a) {
        return new Tuple(this.x * a, this.y * a, this.z * a, this.w * a);
    }

    public Tuple divide(double a) {
        return new Tuple(this.x / a, this.y / a, this.z / a, this.w / a);
    }


}
