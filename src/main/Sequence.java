package main;

public class Sequence {
    final double[] values;
    int current = 0;

    public Sequence(double[] values) {
        this.values = values;
    }

    public static Sequence pseudo() {
        return new Sequence(new double[]{0.36, 0.04, 0.36, 0.22, 0.50, 0.01, 0.97, 0.76, 0.74, 0.31, 0.31, 0.54, 0.13, 0.31, 0.08, 0.62, 0.63, 0.27, 0.83, 0.93});
    }

    public double next() {
        return values[current++ % values.length];
    }


}
