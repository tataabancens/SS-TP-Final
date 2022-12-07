package ar.edu.itba.ss;

import java.util.Locale;

public class Food extends CIMParticle{
    private int color = 25;

    public Food(Vector2 R, double radius) {
        super(R, radius);
    }

    public String toXYZ() {
        return String.format(Locale.US,"%f %f %f %d\n", getActualR().getX(), getActualR().getY(), getRadius(), color);
    }
}
