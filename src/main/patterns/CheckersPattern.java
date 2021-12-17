package main.patterns;

import main.Color;
import main.Point;

public class CheckersPattern extends Pattern {
    public CheckersPattern(Color a, Color b) {
        super(a, b);
    }

    @Override
    public Color patternAt(Point p) {
        if ((Math.floor(p.x) + Math.floor(p.y) + Math.floor(p.z)) % 2 == 0) {
            return this.a;
        }
        return this.b;
    }
}
