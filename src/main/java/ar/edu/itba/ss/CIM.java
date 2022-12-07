package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.List;

public class CIM {
    private double Lx, Ly, xOffset = -1, yOffset = -1;
    private int Mx = 1, My = 1;
    private float rc = 0;

    private final List<List<CIMParticle>> cells;

    public CIM(List<Particle> particles, List<Food> food, double Lx, double Ly) {
        this.Lx = Lx + Math.abs(xOffset) * 2;
        this.Ly = Ly + Math.abs(yOffset) * 2;
        cells = new ArrayList<>(Mx * My);

        List<CIMParticle> cimParticles = new ArrayList<CIMParticle>(particles);
        cimParticles.addAll(food);

        calculateM(cimParticles);
        cellIndexMethodSetup(cimParticles);
    }

    public void calculateM(List<CIMParticle> particles) {
        double maxRadius = 0.0f;
        for(CIMParticle p : particles) {
            if (p.getRadius() > maxRadius) {
                maxRadius = p.getRadius();
            }
        }
        Mx = (int) Math.floor(Lx / (rc + 2 * maxRadius));
        My = (int) Math.floor(Ly / (rc + 2 * maxRadius));
    }

    public void cellIndexMethodSetup(List<CIMParticle> particles) {
        for (int i = 0; i < Mx * My; i++) {
            cells.add(new ArrayList<CIMParticle>());
        }
        storeInCells(particles);
    }

    public void storeInCells(List<CIMParticle> particles) {
        for (CIMParticle particle: particles) {
            // Calculates cell coordinates and stores them in particle
            particle.setCellCoords(Mx, My, Lx, Ly, xOffset, yOffset);
            int cellIndex = particle.getCellX() + particle.getCellY() * Mx;

            if (!isInBounds(cellIndex)) {
                System.out.println("Out in setup");
                continue;
            }
            // Adds the particle to de corresponding cell
            cells.get(cellIndex).add(particle);
        }
    }

    public void cellIndexUpdate(List<CIMParticle> particles) {
        for (int i = 0; i < Mx * My; i++) {
            cells.get(i).clear();
        }
        storeInCells(particles);
    }

    public void updateParticle(CIMParticle p) {
        int previousIndex = p.getCellX() + p.getCellY() * Mx;
        cells.get(previousIndex).remove(p);

        p.setCellCoords(Mx, My, Lx, Ly, xOffset, yOffset);
        int cellIndex = p.getCellX() + p.getCellY() * Mx;

        if (!isInBounds(cellIndex)) {
            System.out.println("out in update");
            return;
        }
        // Adds the particle to de corresponding cell
        cells.get(cellIndex).add(p);
    }

    public List<CIMParticle> calculateNeighbours(Particle p) {
        // Sets an object with the corresponding xy indexes
        NeighbourCells nc = new NeighbourCells(p, Mx, My);
        List<CIMParticle> neighbours = new ArrayList<>();
        for (int i = nc.xStart; i < nc.xEnd; i++) {
            for (int j = nc.yStart; j < nc.yEnd; j++) {
                int index = i + j * Mx;
                neighbours.addAll(cells.get(index));
            }
        }
        neighbours.remove(p);
        return neighbours;
    }

    private boolean isInBounds(int cellIndex) {
        return cellIndex >= 0 && cellIndex <= Mx + My * Mx;
    }

    private static class Cell {
        List<Particle> particles = new ArrayList<>();
        List<Food> food = new ArrayList<>();
    }

    private static class NeighbourCells {
        int xStart, xEnd, yStart, yEnd;

        public NeighbourCells(Particle p, int Mx, int My) {
            // set xStart
            if (p.getCellX() == 0) {
                xStart = 0;
            } else {
                xStart = p.getCellX() - 1;
            }
            // Set xEnd
            if (p.getCellX() == Mx - 1) {
                xEnd = Mx;
            } else {
                xEnd = p.getCellX() + 2;
            }
            // Set yStart
            if (p.getCellY() == 0) {
                yStart = 0;
            } else {
                yStart = p.getCellY() - 1;
            }
            // Set yEnd
            if (p.getCellY() == My - 1) {
                yEnd = My;
            } else {
                yEnd = p.getCellY() + 2;
            }
        }
    }
}
