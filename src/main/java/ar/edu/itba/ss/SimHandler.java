package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimHandler {
    private final List<Wall> walls = new ArrayList<>();
    private final List<Particle> particles = new ArrayList<>();
    private final List<Particle> deadParticles = new ArrayList<>();
    private final List<Food> food = new ArrayList<>();

    private double step, actualTime = 0, tf = 55;

    private double L = 80;

    // Particles characteristics
    private double rMin = 0.15, rMax = 0.32, vd, ve, tao = 0.5, beta = 1, sense;

    private CIM cim;
    private boolean cimIsOn = false;
    private int count = 0;
    private int foodAmount = 10, initialCreaturesAmount = 30, daysElapsed = 0, endDay;
    private int bornCreaturesCount = 0, deadCreaturesCount = 0;
    private double foodRadius = 0.1;

    public SimHandler(int endDay, int foodAmount, int initialCreaturesAmount, double vdMax, double sense) {
        this.vd = vdMax;
        this.sense = sense;
        ve = vd;
        this.endDay = endDay;
        this.foodAmount = foodAmount;
        this.initialCreaturesAmount = initialCreaturesAmount;
        generateWalls();
        generateParticles(rMin, walls);
        cim = new CIM(particles, food, L, L);
        step = calculateStep(rMin, rMax, vd);
        System.out.println("Started");
    }

    private void generateDummyStuff() {
//        food.add(new Food(new Vector2(5, 10), 0.1));
//        food.add(new Food(new Vector2(10, 5), 0.1));
//        particles.add(new Particle(new Vector2(15, 15), new Vector2(-1,0), 1, rMin, rMin, rMax, tao, vd, sense));
        particles.add(new Particle(new Vector2(rMax, rMax), new Vector2(1,0), 1, rMin, rMin, rMax, tao, vd, sense));
    }

    private void generateParticles(double rMin, List<Wall> walls) {
        Random random = new Random();
        for (int i = 0; i < initialCreaturesAmount;) {
            int wallIndex = random.nextInt(walls.size());
            Wall wall = walls.get(wallIndex);

            Vector2 tangVectorToWall = wall.getTangVector();
            double wallLength = tangVectorToWall.module();
            Vector2 wallVersor = tangVectorToWall.normalize();

            double distanceFromStartPoint = rMin + random.nextDouble() * (wallLength - rMin * 2);
            Vector2 aux = wallVersor.scalarProduct(distanceFromStartPoint);
            Vector2 R = wall.getStartPoint().sum(aux);

            Vector2 normalVersor = wallVersor.getOrthogonal();
            Vector2 aux2 = normalVersor.scalarProduct(rMin * 4);
            R = R.sum(aux2);

            boolean ok = true;
            for (Food f : food) {
                if (R.distanceTo(f.getActualR()) < rMin + f.getRadius()) {
                    ok = false;
                    break;
                }
            }
            for (Particle p : particles) {
                if (R.distanceTo(p.getActualR()) < rMin + p.getRadius()) {
                    ok = false;
                    break;
                }
            }
            if (!ok) {
                continue;
            }
            particles.add(new Particle(R, normalVersor, 1, rMin, rMin, rMax, tao, vd, sense));
            i++;
        }
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

    public void generateFoodParticles(double radius, int daysElapsed) {
        Random r = new Random();
        for (int i = 0; i < foodAmount;) {
            Vector2 R = new Vector2(L/8 + r.nextDouble() * (7 * L/8 - L/8), L/8 + r.nextDouble() * (7 * L / 8 - L/8));
            boolean ok = true;
            for (Food p : food) {
                if (R.distanceTo(p.getActualR()) < radius + p.getRadius()) {
                    ok = false;
                    break;
                }
            }
            if (!ok) {
                continue;
            }
            food.add(new Food(R, radius));
            i++;
        }
    }

    public Vector2 generateNewLocation(double radius) {
        Random r = new Random(0);
        boolean ok = false;
        Vector2 R = null;
        while (!ok) {
            boolean overlapped = false;
            R = new Vector2(radius + r.nextDouble() * (L - radius * 2), radius + r.nextDouble() * (L - radius * 2));
            for (Particle p : particles) {
                if (R.distanceTo(p.getActualR()) < radius + p.getRadius()) {
                    overlapped = true;
                    break;
                }
            }
            for (Food p : food) {
                if (R.distanceTo(p.getActualR()) < radius + p.getRadius()) {
                    overlapped = true;
                    break;
                }
            }
            if (overlapped) {
                continue;
            }
            ok = true;
        }
        return R;
    }

    public void iterate() {
        if (cimIsOn) {
            iterateCIMOn();
        } else {
            iterateCIMOf();
        }
    }

    public void checkParticlesEnergy() {
        List<Particle> aux = new ArrayList<>(particles);
        for (Particle p : aux) {
            if (!p.hasEnergy()) {
                p.setColor(70);
                deadParticles.add(p);
                particles.remove(p);
            }
        }
    }

    public void iterateCIMOf() {
        // Checks if a particle energy got depleted and removes it from the list
        checkParticlesEnergy();
        updateDeadParticles();

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
            p.calculateVdDirection(food, walls);
            // Compute magnitude of vd depending on the radius
            p.calculateVdMagnitude(beta);
        }
        for (Particle p : particles) {
            // Advance particles to the next position
            p.advanceParticlesPositions(step);
        }
        List<Food> auxFood = new ArrayList<>(food);
        for (Food f : auxFood) {
            List<Particle> competitors = f.checkOverlaps(particles);
            if (competitors.size() != 0) {
                assignFoodToCompetitor(competitors);
//                f.setActualR(generateNewLocation(f.getRadius()));
                food.remove(f);
            }
        }
        actualTime += step;
        count++;
    }

    private void updateDeadParticles() {
        List<Particle> aux = new ArrayList<>(deadParticles);
        for(Particle p : aux) {
            p.addDeadTimer(step);
            if (p.getTimeSinceDeath() > 3) {
                deadParticles.remove(p);
            }
        }
    }

    private void assignFoodToCompetitor(List<Particle> competitors) {
            Random random = new Random();
            int competitorIndex = random.nextInt(competitors.size());
            competitors.get(competitorIndex).addFoodCount();
    }


    public void iterateCIMOn() {
        for(Particle p : particles) {
            List<CIMParticle> neighbours = cim.calculateNeighbours(p);

            // Aca van las reglas de actualizacion
            cim.updateParticle(p);
        }
        actualTime += step;
    }


    public String printParticles(List<Particle> particles) {
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
        return String.format("%d\n\n%s%s%s%s", particles.size() + wallsSize() + food.size() + deadParticles.size(),
                printParticles(particles), printWalls(), printFood(), printParticles(deadParticles));
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

    public boolean generalEndCondition() {
        return daysElapsed == endDay || getCreaturesAmount() == 0;
    }

    public boolean dayFinished() {
        return particlesGotHome() && deadParticles.size() == 0;
    }

    public boolean particlesGotHome() {
        for (Particle p : particles) {
            if (!p.isHome()) {
                return false;
            }
        }
        return true;
    }

    public void initDay() {

        for (Particle p : particles) {
            p.initDay();
        }
        generateFoodParticles(foodRadius, daysElapsed);
    }

    public void concludeDay() {
        daysElapsed++;

        List<Particle> aux = new ArrayList<>(particles);
        for (Particle p : aux) {
            if (p.gotTwoFood()) {
                Particle newP = p.reproduce(walls);
                particles.add(newP);
            }
        }
        food.clear();
    }

    public int getCreaturesAmount() {
        return particles.size();
    }

    public int getDaysElapsed() {
        return daysElapsed;
    }
}

