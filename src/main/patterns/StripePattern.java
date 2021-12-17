package main.patterns;

import main.Color;
import main.Point;

public class StripePattern extends Pattern {

    public StripePattern(Color a, Color b) {
        super(a, b);
    }

    @Override
    public Color patternAt(Point p) {
        if (Math.floor(p.x) % 2 == 0) {
            return a;
        }
        return b;
    }
}
