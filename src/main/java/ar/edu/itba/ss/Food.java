package ar.edu.itba.ss;

import java.util.Locale;

public class Food {
    private Vector2 R;
    private double radius;
    private int color = 25;

    public Food(Vector2 R, double radius) {
        this.R = R;
        this.radius = radius;
    }

    public Vector2 getR() {
        return R;
    }

    public double getRadius() {
        return radius;
    }

    public String toXYZ() {
        return String.format(Locale.US,"%f %f %f %d\n", R.getX(), R.getY(), radius, color);
    }
}
