package ar.edu.itba.ss;

import java.util.Locale;

public class Particle {
    private Vector2 actualR, lastR = new Vector2(0,0), actualV;
    private final double mass, radius;
    private final int color = 0;

    private int cellX, cellY, cellIndex;

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

    public void setCellCoords(int Mx, int My, double Lx, double Ly, double xOffset, double yOffset) {
        cellX = (int)Math.floor(((actualR.getX() - xOffset) * Mx) / (Lx));
        cellY = (int)Math.floor(((actualR.getY() - yOffset) * My) / (Ly));
        cellIndex = cellX + cellY * Mx;
    }

    public int getCellX() {
        return cellX;
    }

    public int getCellY() {
        return cellY;
    }

    public int getCellIndex() {
        return cellIndex;
    }
}
