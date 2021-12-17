package main;

import main.lights.Light;
import main.patterns.Pattern;
import main.shapes.Shape;

import java.util.Objects;

public class Material {
    public Color color;
    public double ambient;
    public double diffuse;
    public double specular;
    public double shininess;
    public Pattern pattern;
    public double reflective;
    public double transparency;
    public double refractiveIndex;

    public Material() {
        this.color = new Color(1, 1, 1);
        this.ambient = .1;
        this.diffuse = .9;
        this.specular = .9;
        this.shininess = 200;
        this.reflective = 0;
        this.transparency = 0;
        this.refractiveIndex = 1.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Material material = (Material) o;
        return Double.compare(material.ambient, ambient) == 0 &&
                Double.compare(material.diffuse, diffuse) == 0 &&
                Double.compare(material.specular, specular) == 0 &&
                Double.compare(material.shininess, shininess) == 0 &&
                Objects.equals(color, material.color);
    }


    public Color lighting(Shape object, Light light, Point point, Vector eyev, Vector normalv, boolean inShadow) {
        return lighting(object, light, point, eyev, normalv, inShadow ? 0 : 1);
    }

    public Color lighting(Shape object, Light light, Point point, Vector eyev, Vector normalv, double intensity) {
        Color effective_color;
        if (this.pattern != null) {
            effective_color = this.pattern.patternAtShape(object, point).multiply(light.intensity);
        } else {
            effective_color = this.color.multiply(light.intensity);
        }
        Color ambient = effective_color.multiply(this.ambient);
        Color sum = new Color(0,0,0);
        for(int u = 0; u < light.usteps; u++) {
            for(int v = 0; v < light.vsteps; v++) {
                Vector lightv = light.pointOnLight(u, v).subtract(point).toVector().normalize();
                double light_dot_normal = lightv.dot(normalv);
                Color diffuse;
                Color specular;
                if (light_dot_normal < 0) {
                    diffuse = new Color(0, 0, 0);
                    specular = new Color(0, 0, 0);
                } else {
                    diffuse = effective_color.multiply(this.diffuse).multiply(light_dot_normal);

                    Vector reflectv = lightv.negate().toVector().reflect(normalv);
                    double reflect_dot_eye = reflectv.dot(eyev);

                    if (reflect_dot_eye <= 0) {
                        specular = new Color(0, 0, 0);
                    } else {
                        double factor = Math.pow(reflect_dot_eye, this.shininess);
                        specular = light.intensity.multiply(this.specular).multiply(factor);
                    }
                }
                sum = sum.add(diffuse);
                sum = sum.add(specular);
            }
        }

        return ambient.add(sum.divide(light.samples).multiply(intensity));
    }
}
