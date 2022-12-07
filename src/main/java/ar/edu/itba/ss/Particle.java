package ar.edu.itba.ss;

import java.util.Locale;

public class Particle {
    private Vector2 actualR, lastR = new Vector2(0,0), actualV;
    private final double mass, radius;
    private final int color = 0;

    public Particle(Vector2 actualR, Vector2 actualV, double mass, double radius) {
        this.actualR = actualR;
        this.actualV = actualV;
        this.mass = mass;
        this.radius = radius;
    }

    public String toXYZ() {
        return String.format(Locale.US,"%f %f %f %d\n", actualR.getX(), actualR.getY(), radius, color);
    }

    public Vector2 getActualR() {
        return actualR;
    }

    public double getRadius() {
        return radius;
    }
}
