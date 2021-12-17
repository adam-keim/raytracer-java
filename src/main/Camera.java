package main;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.IntStream;

public class Camera {

    private Matrix transform;
    public final int hsize;
    public final int vsize;
    public final double fov;
    public final double pixelSize;
    public final double halfWidth;
    public final double halfHeight;


    public Camera(int hsize, int vsize, double fov) {
        this.hsize = hsize;
        this.vsize = vsize;
        this.fov = fov;
        this.transform = Matrix.identity();
        double halfView = Math.tan(fov / 2);
        double aspect = (double) hsize / vsize;
        if (aspect >= 1) {
            this.halfWidth = halfView;
            this.halfHeight = halfView / aspect;
        } else {
            this.halfWidth = halfView * aspect;
            this.halfHeight = halfView;
        }

        this.pixelSize = (this.halfWidth * 2) / hsize;

    }

    public Matrix getTransform() {
        return transform;
    }

    public void setTransform(Matrix m) {
        this.transform = m;
    }

    public Ray ray(int x, int y) {
        double xOffset = (x + 0.5) * this.pixelSize;
        double yOffset = (y + 0.5) * this.pixelSize;

        double worldX = this.halfWidth - xOffset;
        double worldY = this.halfHeight - yOffset;

        Tuple pixel = this.transform.inverse().multiply(new Point(worldX, worldY, -1));
        Point origin = this.transform.inverse().multiply(new Point(0, 0, 0)).toPoint();
        Vector direction = pixel.subtract(origin).toVector().normalize();

        return new Ray(origin, direction);
    }

    public Canvas render(World w) {
        Canvas image = new Canvas(this.hsize, this.vsize);
        for (int y = 0; y < this.vsize; y++) {
            for (int x = 0; x < this.hsize; x++) {
                Ray ray = this.ray(x, y);
                Color color = w.colorAt(ray, 5);
                image.writePixel(x, y, color);
            }
        }
        return image;
    }

    public Canvas renderPrint(World w) {
        Instant starts = Instant.now();

        Canvas image = new Canvas(this.hsize, this.vsize);
        for (int y = 0; y < this.vsize; y++) {
            System.out.println(y);
            for (int x = 0; x < this.hsize; x++) {
                Ray ray = this.ray(x, y);
                Color color = w.colorAt(ray, 5);
                image.writePixel(x, y, color);
            }
        }
        Instant ends = Instant.now();
        System.out.println(Duration.between(starts, ends));

        return image;


    }

    public Canvas renderParallel(World w) {
        Instant starts = Instant.now();

        Canvas image = new Canvas(this.hsize, this.vsize);
        IntStream.range(0, this.vsize).parallel().forEach(y -> {
            // System.out.println(y);
            for (int x = 0; x < this.hsize; x++) {
                Ray ray = this.ray(x, y);
                Color color = w.colorAt(ray, 5);
                image.writePixel(x, y, color);
            }
        });
        Instant ends = Instant.now();
        System.out.println(Duration.between(starts, ends));

        return image;
    }

}
