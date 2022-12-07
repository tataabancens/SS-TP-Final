package ar.edu.itba.ss;

public class Vector2 {
    private double x,y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double innerProduct(Vector2 v) {
        return this.x * v.getX() + this.y * v.getY();
    }

    public double module() {
        return Math.sqrt(innerProduct(this));
    }

    public double distanceTo(Vector2 v) {
        double dx = x - v.getX(), dy = y - v.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double distSquared(Vector2 v) {
        double dx = x - v.getX(), dy = y - v.getY();
        return dx * dx + dy * dy;
    }

    public Vector2 getOrthogonal() {
        return new Vector2(-y, x);
    }

    public Vector2 scalarProduct(double k) {
        return new Vector2(x * k, y * k);
    }

    public Vector2 substract(Vector2 v) {
        return new Vector2(getX() - v.getX(), getY() - v.getY());
    }

    public Vector2 sum(Vector2 v) {
        return new Vector2(x + v.getX(), y + v.getY());
    }

    public Vector2 normalize() {
        double aux = Math.sqrt(innerProduct(this));
        return scalarProduct(1/aux);
    }

    public Vector2 clone() {
        return new Vector2(x, y);
    }

    public void set(Vector2 v) {
        x = v.getX();
        y = v.getY();
    }

    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Vector2 rotate(double departureAngle) {
        double angle = Math.atan2(y, x);
        return new Vector2(Math.cos(angle + departureAngle), Math.sin(angle + departureAngle)).scalarProduct(module());
    }
}
