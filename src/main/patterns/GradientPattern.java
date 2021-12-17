package main.patterns;

import main.Color;
import main.Point;

public class GradientPattern extends Pattern{
    public GradientPattern(Color a, Color b) {
        super(a, b);
    }

    @Override
    public Color patternAt(Point p) {
        Color distance = b.subtract(a);
        double fraction = p.x - Math.floor(p.x);
        return a.add(distance.multiply(fraction));
    }
}
