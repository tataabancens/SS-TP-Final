package ar.edu.itba.ss;

public class CIMParticle {
    private Vector2 actualR;
    private int cellX, cellY, cellIndex;

    double radius;

    public CIMParticle(Vector2 actualR, double radius) {
        this.actualR = actualR;
        this.radius = radius;
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

    public Vector2 getActualR() {
        return actualR;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setActualR(Vector2 actualR) {
        this.actualR = actualR;
    }
}
