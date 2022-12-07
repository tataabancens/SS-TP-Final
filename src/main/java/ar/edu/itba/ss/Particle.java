package ar.edu.itba.ss;

import java.util.Locale;

public class Particle extends CIMParticle {
    private Vector2 lastR = new Vector2(0,0), actualV;
    private final double mass;
    private final int color = 0;

    private int cellX, cellY, cellIndex;

    public Particle(Vector2 actualR, Vector2 actualV, double mass, double radius) {
        super(actualR, radius);
        this.actualV = actualV;
        this.mass = mass;
        this.radius = radius;
    }

    public String toXYZ() {
        return String.format(Locale.US,"%f %f %f %d\n", getActualR().getX(), getActualR().getY(), radius, color);
    }

//    public void setCellCoords(int Mx, int My, double Lx, double Ly, double xOffset, double yOffset) {
//        cellX = (int)Math.floor(((getActualR().getX() - xOffset) * Mx) / (Lx));
//        cellY = (int)Math.floor(((getActualR().getY() - yOffset) * My) / (Ly));
//        cellIndex = cellX + cellY * Mx;
//    }
//
//    public int getCellX() {
//        return cellX;
//    }
//
//    public int getCellY() {
//        return cellY;
//    }

    public int getCellIndex() {
        return cellIndex;
    }
}
