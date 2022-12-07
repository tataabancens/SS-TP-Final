package ar.edu.itba.ss;

import java.util.Locale;

public class Wall {
    private Vector2 startPoint, endPoint;
    private Vector2 startPointRef, endPointRef;
    private double xyzRadius = 0.25;
    private int color = 100;

    public Wall(Vector2 startPoint, Vector2 endPoint) {
        this.startPointRef = startPoint.clone();
        this.endPointRef = endPoint.clone();
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public String toXYZ() {
        StringBuilder sb = new StringBuilder();
        Vector2 lineVersor = endPoint.substract(startPoint).normalize();
        Vector2 current = startPoint.sum(lineVersor.scalarProduct(xyzRadius));
        for (int i = 0; i < wallXYZSize(); i++) {
            sb.append( String.format(Locale.US,"%f %f %f %d\n", current.getX(), current.getY(), xyzRadius, color));
            current = current.sum(lineVersor.scalarProduct(xyzRadius * 2));
        }
        return sb.toString();
    }

    public int wallXYZSize() {
        double distance = startPoint.distanceTo(endPoint) - xyzRadius * 2;
        return (int)(distance / (xyzRadius * 2)) + 1;
    }

    public double distanceToPoint(Particle particle) {
        double term1 = (endPoint.getX() - startPoint.getX()) * (startPoint.getY() - particle.getActualR().getY());
        double term2 = (startPoint.getX() - particle.getActualR().getX()) * (endPoint.getY() - startPoint.getY());
        double numerator = Math.abs(term1 - term2);

        double denominator = endPoint.distanceTo(startPoint);
        return numerator / denominator;
    }

    public Vector2 getNormalVersor(Particle p) {
        double segmentDist = startPoint.distanceTo(endPoint);
        if (startPoint.distanceTo(p.getActualR()) < segmentDist && endPoint.distanceTo(p.getActualR()) < segmentDist) {
            // Funcionamiento normal de la pared
            return startPoint.substract(endPoint).normalize().getOrthogonal();
        } else if (startPoint.distanceTo(p.getActualR()) < endPoint.distanceTo(p.getActualR())) {
            // En esta caso impacta contra el startPoint
            return startPoint.substract(p.getActualR()).normalize();
        } else {
            // En este caso impacta contra el endpoint
            return endPoint.substract(p.getActualR()).normalize();
        }
    }

    public boolean isColliding(Particle p) {
        double segmentDist = startPoint.distanceTo(endPoint) + p.getRadius(); // Could be buggy
        return startPoint.distanceTo(p.getActualR()) < segmentDist && endPoint.distanceTo(p.getActualR()) < segmentDist;
    }
}
