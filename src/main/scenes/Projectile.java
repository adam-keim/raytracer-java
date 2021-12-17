package main.scenes;

import main.Canvas;
import main.Color;
import main.Point;
import main.Vector;

public class Projectile {
    final Point position;
    final Vector velocity;

    public Projectile(Point position, Vector velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    public static void main(String[] args) {
        Point start = new Point(0,1,0);
        Vector velocity = new Vector(1,1.8,0).normalize().multiply(11.25);
        Projectile p = new Projectile(start, velocity);
        int width = 900;
        int height = 550;

        Vector gravity = new Vector(0,-.1,0);
        Vector wind = new Vector(-.01,0,0);
        Environment e = new Environment(gravity, wind);

        Canvas c = new Canvas(width, height);
        Color red = new Color(1,0,0);
        while(0 < p.position.y && p.position.y < height) {
            c.writePixel((int)p.position.x, (int)(height - p.position.y), red);
            p = tick(e, p);

        }
        c.writeFile("projectile.ppm");

    }

    public static Projectile tick(Environment env, Projectile proj) {
        Point position = proj.position.add(proj.velocity).toPoint();
        Vector velocity = proj.velocity.add(env.gravity).add(env.wind).toVector();
        return new Projectile(position, velocity);


    }
}

class Environment {
    public final Vector gravity;
    public final Vector wind;

    public Environment(Vector gravity, Vector wind) {
        this.gravity = gravity;
        this.wind = wind;
    }
}