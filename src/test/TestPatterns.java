package test;


import main.Color;
import main.Matrix;
import main.Point;
import main.patterns.*;
import main.shapes.Sphere;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Patterns")
public class TestPatterns {

    final Color black = new Color(0, 0, 0);
    final Color white = new Color(1, 1, 1);


    @Test
    @DisplayName("Creating a stripe Pattern")
    void stripePattern() {
        Pattern pattern = new StripePattern(white, black);
        assertEquals(pattern.a, white);
        assertEquals(pattern.b, black);
    }

    @Test
    @DisplayName("A stripe pattern is constant in y")
    void constantY() {
        Pattern pattern = new StripePattern(white, black);
        assertEquals(pattern.patternAt(new Point(0, 0, 0)), white);
        assertEquals(pattern.patternAt(new Point(0, 1, 0)), white);
        assertEquals(pattern.patternAt(new Point(0, 2, 0)), white);
    }

    @Test
    @DisplayName("A stripe pattern is constant in y")
    void constantZ() {
        Pattern pattern = new StripePattern(white, black);
        assertEquals(pattern.patternAt(new Point(0, 0, 0)), white);
        assertEquals(pattern.patternAt(new Point(0, 0, 1)), white);
        assertEquals(pattern.patternAt(new Point(0, 0, 2)), white);
    }

    @Test
    @DisplayName("A stripe pattern is constant in x")
    void constantX() {
        Pattern pattern = new StripePattern(white, black);
        assertEquals(pattern.patternAt(new Point(0, 0, 0)), white);
        assertEquals(pattern.patternAt(new Point(0.9, 0, 0)), white);
        assertEquals(pattern.patternAt(new Point(1, 0, 0)), black);
        assertEquals(pattern.patternAt(new Point(-.1, 0, 0)), black);
        assertEquals(pattern.patternAt(new Point(-1, 0, 0)), black);
        assertEquals(pattern.patternAt(new Point(-1.1, 0, 0)), white);
    }

    @Test
    @DisplayName("Stripes with an object transformation")
    void stripeObjectTransform() {
        Sphere object = new Sphere();
        object.setTransform(Matrix.scaling(2, 2, 2));
        Pattern pattern = new StripePattern(white, black);
        Color c = pattern.patternAtShape(object, new Point(1.5, 0, 0));
        assertEquals(c, white);
    }

    @Test
    @DisplayName("Stripes with a pattern transformation")
    void stripePatternTransform() {
        Sphere object = new Sphere();
        Pattern pattern = new StripePattern(white, black);
        pattern.setTransform(Matrix.scaling(2, 2, 2));
        Color c = pattern.patternAtShape(object, new Point(1.5, 0, 0));
        assertEquals(c, white);
    }

    @Test
    @DisplayName("Stripes with both an object and a pattern transformation")
    void stripeObjectPatternTransform() {
        Sphere object = new Sphere();
        object.setTransform(Matrix.scaling(2, 2, 2));
        Pattern pattern = new StripePattern(white, black);
        pattern.setTransform(Matrix.translation(.5, 0, 0));
        Color c = pattern.patternAtShape(object, new Point(2.5, 0, 0));
        assertEquals(c, white);
    }

    @Test
    @DisplayName("The default pattern transformation")
    void patternTransform() {
        Pattern pattern = new TestPattern();
        assertEquals(pattern.getTransform(), Matrix.identity());
    }


    @Test
    @DisplayName("Assigning a transformation")
    void patternTransformAssign() {
        Pattern pattern = new TestPattern();
        pattern.setTransform(Matrix.translation(1, 2, 3));

        assertEquals(pattern.getTransform(), Matrix.translation(1, 2, 3));
    }


    @Test
    @DisplayName("A pattern with an object transformation")
    void patternObjectTransform() {
        Sphere s = new Sphere();
        s.setTransform(Matrix.scaling(2, 2, 2));
        Pattern pattern = new TestPattern();
        Color c = pattern.patternAtShape(s, new Point(2, 3, 4));

        assertEquals(c, new Color(1, 1.5, 2));
    }

    @Test
    @DisplayName("A pattern with an object transformation")
    void patternPatternTransform() {
        Sphere s = new Sphere();
        Pattern pattern = new TestPattern();
        pattern.setTransform(Matrix.scaling(2, 2, 2));
        Color c = pattern.patternAtShape(s, new Point(2, 3, 4));

        assertEquals(c, new Color(1, 1.5, 2));

    }

    @Test
    @DisplayName("A pattern with both an object and a pattern transformation")
    void bothTransforms() {
        Sphere s = new Sphere();
        s.setTransform(Matrix.scaling(2, 2, 2));

        Pattern pattern = new TestPattern();
        pattern.setTransform(Matrix.translation(0.5, 1, 1.5));

        Color c = pattern.patternAtShape(s, new Point(2.5, 3, 3.5));
        assertEquals(c, new Color(0.75, 0.5, 0.25));
    }

    @Test
    @DisplayName("A gradient linearly interpolates between colors")
    void gradient() {
        Pattern pattern = new GradientPattern(white, black);
        assertEquals(white, pattern.patternAt(new Point(0, 0, 0)));
        assertEquals(new Color(0.75, 0.75, 0.75), pattern.patternAt(new Point(0.25, 0, 0)));
        assertEquals(new Color(0.5, 0.5, 0.5), pattern.patternAt(new Point(0.5, 0, 0)));
        assertEquals(new Color(0.25, 0.25, 0.25), pattern.patternAt(new Point(0.75, 0, 0)));
    }

    @Test
    @DisplayName("A ring should extend in both x and z")
    void ring() {
        Pattern pattern = new RingPattern(white, black);
        assertEquals(white, pattern.patternAt(new Point(0, 0, 0)));
        assertEquals(black, pattern.patternAt(new Point(1, 0, 0)));
        assertEquals(black, pattern.patternAt(new Point(0, 0, 1)));
        assertEquals(black, pattern.patternAt(new Point(.708, 0, .708)));
    }

    @Test
    @DisplayName("Checkers should repeat in x")
    void checkerRepeatX() {
        Pattern pattern = new CheckersPattern(white, black);
        assertEquals(white, pattern.patternAt(new Point(0, 0, 0)));
        assertEquals(white, pattern.patternAt(new Point(0.99, 0, 0)));
        assertEquals(black, pattern.patternAt(new Point(1.01, 0, 0)));
    }

    @Test
    @DisplayName("Checkers should repeat in y")
    void checkerRepeatY() {
        Pattern pattern = new CheckersPattern(white, black);
        assertEquals(white, pattern.patternAt(new Point(0, 0, 0)));
        assertEquals(white, pattern.patternAt(new Point(0, 0.99, 0)));
        assertEquals(black, pattern.patternAt(new Point(0, 1.01, 0)));
    }

    @Test
    @DisplayName("Checkers should repeat in z")
    void checkerRepeatZ() {
        Pattern pattern = new CheckersPattern(white, black);
        assertEquals(white, pattern.patternAt(new Point(0, 0, 0)));
        assertEquals(white, pattern.patternAt(new Point(0, 0, 0.99)));
        assertEquals(black, pattern.patternAt(new Point(0, 0, 1.01)));
    }
}

