package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.List;

public class SimHandler {
    private final List<Wall> walls = new ArrayList<>();
    private final List<Particle> particles = new ArrayList<>();
    private final List<Food> food = new ArrayList<>();

    private double step, actualTime = 0, tf = 50;

    private double L = 50;

    // Particles characteristics
    private double rMin = 0.15, rMax = 0.32, vd = 4, ve = vd, tao = 0.5, beta = 1;

    private CIM cim;
    private boolean cimIsOn = false;
    private int count = 0;

    public SimHandler() {
        generateWalls();

        food.add(new Food(new Vector2(23, 23), 0.25));
        particles.add(new Particle(new Vector2(23, 25), new Vector2(-1,0), 1, rMin, rMin, rMax, tao, 4));
        particles.add(new Particle(new Vector2(22, 25), new Vector2(1,0), 1, rMin, rMin, rMax, tao, 4));
        cim = new CIM(particles, food, L, L);
        step = calculateStep(rMin, rMax, vd);
    }

    private double calculateStep(double rMin, double vd, double ve) {
        return rMin / (2 * Math.max(vd, ve));
    }

    private void generateWalls() {
        walls.add(new Wall(new Vector2(0, L), new Vector2(0, 0)));

        walls.add(new Wall(new Vector2(0, 0), new Vector2(L, 0)));

        walls.add(new Wall(new Vector2(L, 0), new Vector2(L, L)));

        walls.add(new Wall(new Vector2(L, L), new Vector2(0, L)));
    }

    public void iterate() {
        if (cimIsOn) {
            iterateCIMOn();
        } else {
            iterateCIMOf();
        }
    }

    public void iterateCIMOf() {
        if (count == 10) {
            System.out.println();
        }
        for (Particle p : particles) {
            // Find contacts with particles and calculate Ve
            p.calculateVe(particles, walls);
        }
        for (Particle p : particles) {
            // Adjust radius according to the rule
            p.applyRadiiRule(step);
        }
        for (Particle p : particles) {
            // Compute direction and sense of vd
            p.calculateVdDirection(food);
            // Compute magnitude of vd depending on the radius
            p.calculateVdMagnitude(beta);
        }
        for (Particle p : particles) {
            // Advance particles to the next position
            p.advanceParticlesPositions(step);
        }
        actualTime += step;
        count++;
    }



    public void iterateCIMOn() {
        for(Particle p : particles) {
            List<CIMParticle> neighbours = cim.calculateNeighbours(p);

            // Aca van las reglas de actualizacion
            cim.updateParticle(p);
        }
        actualTime += step;
    }


    public String printParticles() {
        StringBuilder sb = new StringBuilder();
        for(Particle p : particles) {
            sb.append(p.toXYZ());
        }
        return sb.toString();
    }

    public int wallsSize() {
        int accum = 0;
        for (Wall wall : walls) {
            accum += wall.wallXYZSize();
        }
        return accum;
    }

    public String printWalls() {
        StringBuilder sb = new StringBuilder();
        for(Wall w : walls) {
            sb.append(w.toXYZ());
        }
        return sb.toString();
    }

    public String printFood() {
        StringBuilder sb = new StringBuilder();
        for(Food f : food) {
            sb.append(f.toXYZ());
        }
        return sb.toString();
    }

    public String printSystem() {
        return String.format("%d\n\n%s%s%s", particles.size() + wallsSize() + food.size(), printParticles(), printWalls(), printFood());
    }

    public double getStep() {
        return step;
    }

    public double getActualTime() {
        return actualTime;
    }

    public double getTf() {
        return tf;
    }
}
