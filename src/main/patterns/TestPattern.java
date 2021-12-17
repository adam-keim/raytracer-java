package main.patterns;

import main.Color;
import main.Point;

public class TestPattern extends Pattern {
    public TestPattern() {
        super(null, null);
    }

    @Override
    public Color patternAt(Point p) {
        return new Color(p.x, p.y, p.z);
    }
}
