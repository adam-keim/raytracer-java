package main;

public class Color {
    public final double red;
    public final double green;
    public final double blue;

    public Color(double red, double green, double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color = (Color) o;
        return Util.EQUALS(color.red, red) && Util.EQUALS(color.green, green) && Util.EQUALS(color.blue, blue);
    }


    public Color add(Color b) {
        return new Color(this.red + b.red, this.green + b.green, this.blue + b.blue);
    }

    public Color subtract(Color b) {
        return new Color(this.red - b.red, this.green - b.green, this.blue - b.blue);
    }

    public Color negate() {
        return new Color(-this.red, -this.green, -this.blue);
    }

    public Color multiply(double a) {
        return new Color(this.red * a, this.green * a, this.blue * a);
    }

    public Color multiply(Color c) {
        return new Color(this.red * c.red, this.green*c.green, this.blue*c.blue);
    }
    public Color divide(double a) {
        return new Color(this.red / a, this.green / a, this.blue / a);
    }

    @Override
    public String toString() {
        return "Color{" +
                "r=" + red +
                ", g=" + green +
                ", b=" + blue +
                '}';
    }
}
