package ar.edu.itba.ss;

import java.util.Locale;

public class Food extends CIMParticle{
    private double radius;
    private int color = 25;

    public Food(Vector2 R, double radius) {
        super(R, radius);
    }

    public double getRadius() {
        return radius;
    }

    public String toXYZ() {
        return String.format(Locale.US,"%f %f %f %d\n", getActualR().getX(), getActualR().getY(), radius, color);
    }
}
