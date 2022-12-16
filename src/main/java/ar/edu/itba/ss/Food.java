package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Food extends CIMParticle{
    private int color = 25;

    public Food(Vector2 R, double radius) {
        super(R, radius);
    }

    public String toXYZ() {
        return String.format(Locale.US,"%f %f %f %d\n", getActualR().getX(), getActualR().getY(), getRadius() * 3, color);
    }

    public List<Particle> checkOverlaps(List<Particle> particles) {
        List<Particle> competitors = new ArrayList<>();
        // Check overlap with particles, if overlapped calculate force
        for (Particle p : particles) {
            if (!p.gotTwoFood()) {
                double overlap = calculateOverlap(p);
                if (overlap > 0) {
                    competitors.add(p);
                }
            }
        }

        return competitors;
    }

    private double calculateOverlap(Particle p) {
        return getRadius() + p.getRadius() - getActualR().distanceTo(p.getActualR());
    }
}
