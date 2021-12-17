package main;

import java.lang.Math;

public class Util {
    public static final double EPSILON = .00001;

    public static boolean EQUALS(double a, double b) {
        return Math.abs(a - b) < EPSILON || a == b;
    }
}
