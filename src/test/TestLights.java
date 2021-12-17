package test;

import main.*;
import main.lights.AreaLight;
import main.lights.Light;
import main.lights.PointLight;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Lights")
public class TestLights {
    @Test
    @DisplayName("A point light has Position and Intensity")
    void pointLightBasic() {
        Color intensity = new Color(1,1,1);
        Point position = new Point(0,0,0);
        PointLight light = new PointLight(position, intensity);
        assertEquals(light.position, position);
        assertEquals(light.intensity, intensity);
    }

    @Test
    @DisplayName("Point lights evaluate the light intensity at a given point")
    void pointLightIntensity() {
        Point[] points = {
                new Point(0, 1.0001, 0),
                new Point(-1.0001, 0, 0),
                new Point(0, 0, -1.0001),
                new Point(0, 0, 1.0001),
                new Point(1.0001, 0, 0),
                new Point(0, -1.0001, 0),
                new Point(0, 0, 0)
        };
        double[] intensities = {1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0};

        World w = World.defaultWorld();
        Light light = w.getLight();
        for(int i = 0; i < points.length; i++) {
            double intensity = light.intensityAt(points[i], w);
            assertEquals(intensities[i], intensity);
        }
    }
    
    @Test
    @DisplayName("Creating an area light")
    void areaLight() {
        Point corner = new Point(0,0,0);
        Vector v1 = new Vector(2,0,0);
        Vector v2 = new Vector(0,0,1);
        
        AreaLight areaLight = new AreaLight(corner, v1, 4, v2, 2, new Color(1,1,1));
        assertEquals(areaLight.corner, corner);
        assertEquals(new Vector(0.5, 0, 0), areaLight.uvec);
        assertEquals(4, areaLight.usteps);
        assertEquals(new Vector(0, 0, 0.5), areaLight.vvec);
        assertEquals(2, areaLight.vsteps);
        assertEquals(8, areaLight.samples);
        assertEquals(new Point(1, 0, 0.5), areaLight.position);
    }

    @Test
    @DisplayName("Finding a single point on an area light")
    void areaLightPoint() {
        int[] u_values = {0,1,0,2,3};
        int[] v_values = {0,0,1,0,1};
        Point[] results = {
                new Point(0.25, 0, 0.25),
                new Point(0.75, 0, 0.25),
                new Point(0.25, 0, 0.75),
                new Point(1.25, 0, 0.25),
                new Point(1.75, 0, 0.75),
        };
        Point corner = new Point(0,0,0);
        Vector v1 = new Vector(2,0,0);
        Vector v2 = new Vector(0,0,1);

        AreaLight areaLight = new AreaLight(corner, v1, 4, v2, 2, new Color(1,1,1));
        for(int i = 0; i < u_values.length; i++) {
            Point pt = areaLight.pointOnLight(u_values[i], v_values[i]);
            assertEquals(results[i], pt);
        }
    }

    @Test
    @DisplayName("The area light intensity function")
    void areaLightIntensity() {
        double[] intensities = {.0, .25, .5, .75, 1.0};
        Point[] points = {
                new Point(0,0,2),
                new Point(1, -1, 2),
                new Point(1.5, 0, 2),
                new Point(1.25, 1.25, 3),
                new Point(0,0,-2)
        };
        World w = World.defaultWorld();
        Point corner = new Point(-.5, -.5, -5);
        Vector v1 = new Vector(1,0,0);
        Vector v2 = new Vector(0,1,0);
        AreaLight light = new AreaLight(corner, v1, 2, v2, 2, new Color(1,1,1));
        for(int i = 0; i < intensities.length; i++) {
            double intensity = light.intensityAt(points[i], w);
            assertEquals(intensities[i], intensity);
        }

    }

    @Test
    @DisplayName("Finding a single point on a jittered area light")
    void jitteredPoint() {
        int[] u_values = {0,1,0,2,3};
        int[] v_values = {0,0,1,0,1};
        Point[] results = {
                new Point(0.15, 0, 0.35),
                new Point(0.65, 0, 0.35),
                new Point(0.15, 0, 0.85),
                new Point(1.15, 0, 0.35),
                new Point(1.65, 0, 0.85),
        };
        Point corner = new Point(0,0,0);
        Vector v1 = new Vector(2,0,0);
        Vector v2 = new Vector(0,0,1);

        AreaLight areaLight = new AreaLight(corner, v1, 4, v2, 2, new Color(1,1,1));
        areaLight.jitterBy = new Sequence(new double[]{.3, .7});
        for(int i = 0; i < u_values.length; i++) {
            Point pt = areaLight.pointOnLight(u_values[i], v_values[i]);
            assertEquals(results[i], pt);
        }
    }

    @Test
    @DisplayName("The area light with jittered samples")
    void areaLightJitter() {
        double[] intensities = {.0, .25, .75, .75, 1.0};

        //double[] intensities = {0, .5, .75, .75, 1.0};
        Point[] points = {
                new Point(0,0,2),
                new Point(1, -1, 2),
                new Point(1.5, 0, 2),
                new Point(1.25, 1.25, 3),
                new Point(0,0,-2)
        };
        World w = World.defaultWorld();
        Point corner = new Point(-.5, -.5, -5);
        Vector v1 = new Vector(1,0,0);
        Vector v2 = new Vector(0,1,0);
        AreaLight light = new AreaLight(corner, v1, 2, v2, 2, new Color(1,1,1));
        light.jitterBy = new Sequence(new double[]{.7, .3, .9, .1, .5});
        for(int i = 0; i < intensities.length; i++) {
            double intensity = light.intensityAt(points[i], w);
            assertEquals(intensities[i], intensity);
        }

    }

}
