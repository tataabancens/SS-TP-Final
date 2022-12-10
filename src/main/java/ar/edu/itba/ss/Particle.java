package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Particle extends CIMParticle {
    private Vector2 lastR = new Vector2(0,0), actualV, ve, vd;
    private final double mass, rMin, rMax, tao, vdMax, sense = 6, maxEnergy = 700;
    private final int color = 0;

    private boolean contact = false, resetedDirection = true;
    private int foodCount = 0;

    private double timeSinceChangedDirection = 0, intervalBetweenChangeOfDirection = 1, currentEnergy = 0;


    private int cellX, cellY, cellIndex;

    public Particle(Vector2 actualR, Vector2 actualV, double mass, double radius, double rMin, double rMax, double tao, double vdMax) {
        super(actualR, radius);
        this.actualV = actualV;
        this.mass = mass;
        this.rMin = rMin;
        this.rMax = rMax;
        this.tao = tao;
        this.vdMax = vdMax;
        currentEnergy = maxEnergy;
    }

    public void calculateVe(List<Particle> particles, List<Wall> walls) {
        ve = new Vector2(0,0);
        // Check overlap with walls, if overlapped calculate force
        for (Wall w : walls) {
            double overlap = calculateOverlap(w);
            if (overlap > 0) {
                contact = true;
                if (w.isColliding(this)) {
                    Vector2 normalVersor = w.getNormalVersor(this).scalarProduct(-1);
                    ve = ve.sum(normalVersor);
                }
            }
        }

        // Check overlap with particles, if overlapped calculate force
        for (Particle p : particles) {
            if (p != this) {
                double overlap = calculateOverlap(p);
                if (overlap > 0) {
                    contact = true;
                    Vector2 normalVersor = getActualR().substract(p.getActualR()).normalize();
                    ve = ve.sum(normalVersor);
                }
            }
        }
        ve = ve.normalize().scalarProduct(vdMax);
    }

    private double calculateOverlap(Particle p) {
        return getRadius() + p.getRadius() - getActualR().distanceTo(p.getActualR());
    }

    private double calculateOverlap(Wall w) {
        double distanceToWall = w.distanceToPoint(this);
        return getRadius() - distanceToWall;
    }

    public String toXYZ() {
        return String.format(Locale.US,"%f %f %f %d\n", getActualR().getX(), getActualR().getY(), getRadius(), color);
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public boolean isContact() {
        return contact;
    }

    public void applyRadiiRule(double step) {
        if (contact) {
            setRadius(rMin);
            resetedDirection = false;
        } else if (getRadius() < rMax) {
            double increment = rMax / (tao / step);
            double newRadii = getRadius() + increment;
            setRadius(newRadii);
        }
    }
    public List<Food> lookForCloseFood(List<Food> food, double sense) {
        List<Food> closeFood = new ArrayList<>();
        for (Food f : food) {
            if (f.getActualR().distSquared(getActualR()) < sense * sense) {
                closeFood.add(f);
            }
        }
        return closeFood;
    }

    public void calculateVdDirection(List<Food> food) {
        List<Food> closeFood = lookForCloseFood(food, sense);
        Food closerFood = null;
        double distanceToCloserFoodSquared = Double.POSITIVE_INFINITY;

        for (Food f : closeFood) {
            double distanceToFoodSquared = f.getActualR().distSquared(getActualR());

            if (distanceToFoodSquared < distanceToCloserFoodSquared) {
                distanceToCloserFoodSquared = distanceToFoodSquared;
                closerFood = f;
            }
        }
        if (closerFood != null) {
            vd = closerFood.getActualR().substract(getActualR()).normalize();
            timeSinceChangedDirection = 0;
        } else if (!resetedDirection) {
            // Aca redirigir para otro lado la criatura
            Random random = new Random();
            vd = new Vector2(-0.5 + random.nextDouble(), -0.5 + random.nextDouble()).normalize();
            if (!contact) {
                resetedDirection = true;
            }
        } else {
            // Aca mantener la direccion de la criatura o cambiarla despues de cierta cantidad de tiempo
            if (timeSinceChangedDirection > intervalBetweenChangeOfDirection) {
                Random random = new Random();
                if (random.nextInt(2) % 2 == 0) {
                    vd = actualV.rotate(Math.PI/6);
                } else {
                    vd = actualV.rotate(-Math.PI/6);
                }

                timeSinceChangedDirection = 0;
            } else {
                vd = actualV.normalize();
            }
        }
    }

    public void calculateVdMagnitude(double beta) {
        double term = (radius - rMin) / (rMax - rMin);
        double vdModule = vdMax * Math.pow(term, beta);
        vd = vd.scalarProduct(vdModule);
    }

    public void advanceParticlesPositions(double step) {
        actualV = getVelocity();
        setActualR(getActualR().sum(actualV.scalarProduct(step)));
        contact = false;
        timeSinceChangedDirection += step;

        // Consume energy
        double consume = (sense + 0.5 * (vdMax * vdMax)) * step;
        currentEnergy -= consume;
    }

    private Vector2 getVelocity() {
        if (radius == rMin) {
            return ve;
        } else {
            return vd;
        }
    }

    public void addFoodCount() {
        foodCount++;
    }

    public boolean hasEnergy() {
        return currentEnergy > 0;
    }
}
