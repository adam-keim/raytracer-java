package test;

import main.*;
import main.lights.AreaLight;
import main.lights.PointLight;
import main.patterns.StripePattern;
import main.shapes.Shape;
import main.shapes.Sphere;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Materials")
public class TestMaterials {
    @Test
    @DisplayName("The default material")
    void defaultMaterial() {
        Material m = new Material();
        assertEquals(m.color, new Color(1, 1, 1));
        assertEquals(.1, m.ambient);
        assertEquals(.9, m.diffuse);
        assertEquals(.9, m.specular);
        assertEquals(200, m.shininess);

    }

    @Test
    @DisplayName("Lighting with the eye between the light and the surface")
    void lighting1() {
        Material m = new Material();
        Point position = new Point(0, 0, 0);
        Sphere s = new Sphere();

        Vector eyev = new Vector(0, 0, -1);
        Vector normalv = new Vector(0, 0, -1);
        PointLight light = new PointLight(new Point(0, 0, -10), new Color(1, 1, 1));
        Color result = m.lighting(s, light, position, eyev, normalv, false);
        assertEquals(result, new Color(1.9, 1.9, 1.9));

    }

    @Test
    @DisplayName("Lighting with the eye between light and surface, eye offset 45 deg")
    void lighting2() {
        Material m = new Material();
        Point position = new Point(0, 0, 0);
        Sphere s = new Sphere();

        Vector eyev = new Vector(0, Math.sqrt(2) / 2, -Math.sqrt(2) / 2);
        Vector normalv = new Vector(0, 0, -1);
        PointLight light = new PointLight(new Point(0, 0, -10), new Color(1, 1, 1));
        Color result = m.lighting(s, light, position, eyev, normalv, false);
        assertEquals(result, new Color(1.0, 1.0, 1.0));

    }

    @Test
    @DisplayName("Lighting with the eye opposite, light offset 45 deg")
    void lighting3() {
        Material m = new Material();
        Point position = new Point(0, 0, 0);
        Sphere s = new Sphere();

        Vector eyev = new Vector(0, 0, -1);
        Vector normalv = new Vector(0, 0, -1);
        PointLight light = new PointLight(new Point(0, 10, -10), new Color(1, 1, 1));
        Color result = m.lighting(s, light, position, eyev, normalv, false);
        assertEquals(result, new Color(.7364, .7364, .7364));

    }

    @Test
    @DisplayName("Lighting with eye in the path of the reflection vector")
    void lighting4() {
        Material m = new Material();
        Point position = new Point(0, 0, 0);
        Sphere s = new Sphere();

        Vector eyev = new Vector(0, -Math.sqrt(2) / 2, -Math.sqrt(2) / 2);
        Vector normalv = new Vector(0, 0, -1);
        PointLight light = new PointLight(new Point(0, 10, -10), new Color(1, 1, 1));
        Color result = m.lighting(s, light, position, eyev, normalv, false);
        assertEquals(result, new Color(1.6364, 1.6364, 1.6364));
    }

    @Test
    @DisplayName("Lighting with eye in the path of the reflection vector")
    void lighting5() {
        Material m = new Material();
        Point position = new Point(0, 0, 0);
        Sphere s = new Sphere();

        Vector eyev = new Vector(0, 0, -1);
        Vector normalv = new Vector(0, 0, -1);
        PointLight light = new PointLight(new Point(0, 0, 10), new Color(1, 1, 1));
        Color result = m.lighting(s, light, position, eyev, normalv, false);
        assertEquals(result, new Color(.1, .1, .1));
    }

    @Test
    @DisplayName("Lighting with the surface in shadow")
    void lightingShadow() {
        Material m = new Material();
        Point position = new Point(0, 0, 0);
        Sphere s = new Sphere();

        Vector eyev = new Vector(0, 0, -1);
        Vector normalv = new Vector(0, 0, -1);
        PointLight light = new PointLight(new Point(0, 0, -10), new Color(1, 1, 1));
        boolean inShadow = true;
        Color result = m.lighting(s, light, position, eyev, normalv, true);
        assertEquals(result, new Color(0.1, 0.1, 0.1));
    }

    @Test
    @DisplayName("Lighting with a pattern applied")
    void lightingPattern() {
        Material m = new Material();
        Point position = new Point(0, 0, 0);
        Sphere s = new Sphere();

        m.pattern = new StripePattern(new Color(1, 1, 1), new Color(0, 0, 0));
        m.ambient = 1;
        m.diffuse = 0;
        m.specular = 0;
        Vector eyev = new Vector(0, 0, -1);
        Vector normalv = new Vector(0, 0, -1);
        PointLight light = new PointLight(new Point(0, 0, -10), new Color(1, 1, 1));

        Color c1 = m.lighting(s, light, new Point(0.9, 0, 0), eyev, normalv, false);
        Color c2 = m.lighting(s, light, new Point(1.1, 0, 0), eyev, normalv, false);

        assertEquals(c1, new Color(1, 1, 1));
        assertEquals(c2, new Color(0, 0, 0));

    }

    @Test
    @DisplayName("Reflectivity for the default material")
    void defaultReflectivity() {
        Material m = new Material();
        assertEquals(0, m.reflective);
    }

    @Test
    @DisplayName("Transparency and refractive index for the default material")
    void transparencyRefractive() {
        Material m = new Material();
        assertEquals(0, m.transparency);
        assertEquals(1, m.refractiveIndex);
    }

    @Test
    @DisplayName("lighting() uses light intensity to attenuate color")
    void lightingIntensity() {
        double[] intensities = {1.0, 0.5, 0.0};
        Color[] colors = {new Color(1, 1, 1), new Color(0.55, 0.55, 0.55), new Color(0.1, 0.1, 0.1)};
        World w = World.defaultWorld();
        w.setLight(new PointLight(new Point(0, 0, -10), new Color(1, 1, 1)));
        Shape s = w.getShapes().get(0);
        s.material.ambient = .1;
        s.material.diffuse = .9;
        s.material.specular = 0;
        s.material.color = new Color(1, 1, 1);
        Point pt = new Point(0, 0, -1);
        Vector eyev = new Vector(0, 0, -1);
        Vector normalv = new Vector(0, 0, -1);
        for (int i = 0; i < intensities.length; i++) {
            Color result = s.material.lighting(s, w.getLight(), pt, eyev, normalv, intensities[i]);
            assertEquals(colors[i], result);
        }

    }

    @Test
    @DisplayName("lighting() samples the area light")
    void sampleAreaLight() {
        Point[] points = new Point[]{
                new Point(0,0,-1),
                new Point(0, 0.7071, -0.7071)
        };
        Color[] colors = new Color[] {
                new Color(0.9965, 0.9965, 0.9965),
                new Color(0.62318, 0.62318, 0.62318)
        };
        Point corner = new Point(-.5, -.5, -5);
        Vector v1 = new Vector(1,0,0);
        Vector v2 = new Vector(0,1,0);
        AreaLight light = new AreaLight(corner, v1, 2, v2, 2, new Color(1,1,1));
        Shape s = new Sphere();
        s.material.ambient = .1;
        s.material.diffuse = .9;
        s.material.specular = 0;
        s.material.color = new Color(1,1,1);
        Point eye = new Point(0,0,-5);
        for(int i = 0; i < points.length; i++) {
            Vector eyev = eye.subtract(points[i]).toVector().normalize();
            Vector normalv = new Vector(points[i].x, points[i].y, points[i].z);
            Color result = s.material.lighting(s, light, points[i], eyev, normalv, 1.0);
            assertEquals(colors[i], result);
        }

    }

}
