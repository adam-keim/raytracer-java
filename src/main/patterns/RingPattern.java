package main.patterns;

import main.Color;
import main.Point;

public class RingPattern extends Pattern {
    public RingPattern(Color a, Color b) {
        super(a, b);
    }

    @Override
    public Color patternAt(Point p) {
        if(Math.floor(Math.sqrt(Math.pow(p.x, 2) + Math.pow(p.z, 2))) % 2 == 0) {
            return this.a;
        }
        return this.b;
    }
}
