package ar.edu.itba.ss;

import java.util.*;

public class DataAccumulator {
    List<Integer> particlesAlive = new ArrayList<>();

    Map<Double, List<Double>> vdMaxVsParticlesAliveEnd = new HashMap<>();
    Map<Integer, List<Double>> promMap = new HashMap<>();

    Map<Double, List<Double>> qPromMap = new HashMap<>();

    public void addEndPAToMap(double vdMax, Double endPA) {
        vdMaxVsParticlesAliveEnd.putIfAbsent(vdMax, new ArrayList<>());

        List<Double> list = vdMaxVsParticlesAliveEnd.get(vdMax);
        list.add(endPA);
    }

    public List<Double> getPromList() {
        List<Double> toRet = new ArrayList<>();
        for (List<Double> list : vdMaxVsParticlesAliveEnd.values()) {
            double toAdd = 0;
            for (Double i: list) {
                toAdd += i;
            }
            toRet.add(toAdd / list.size());
        }
        return toRet;
    }

    public List<Double> getErrorList() {
        List<Double> toRet = new ArrayList<>();
        for (List<Double> list : vdMaxVsParticlesAliveEnd.values()) {
            toRet.add(getStandardDeviation(list));
//            toRet.add(list.stream().max(Comparator.naturalOrder()).get() - list.stream().min(Comparator.naturalOrder()).get());
        }
        return toRet;
    }

    public void addParticlesAlive(Integer amount) {
        particlesAlive.add(amount);
    }

    public List<Integer> getParticlesAlive() {
        return particlesAlive;
    }

    public void addParticleVdMaxDay(int daysElapsed, double creaturesAmount) {
        promMap.putIfAbsent(daysElapsed, new ArrayList<>());
        promMap.get(daysElapsed).add(creaturesAmount);
    }

    public List<Double> getPromsParticlesAliveVsTime() {
        List<Double> toRet = new ArrayList<>();

        for (List<Double> list : promMap.values()) {
            double prom = 0;
            for (Double d : list) {
                prom += d;
            }
            toRet.add(prom / list.size());
        }
        return toRet;
    }

    public List<Double> getQ(int upperLimit, List<Double> aliveParticles) {
        int topIndex = Math.min(upperLimit, aliveParticles.size());

        List<Double> toRet = new ArrayList<>();

        for (int i = 0; i < topIndex - 1; i++) {
            double particleCount = aliveParticles.get(i);
            double particleCountPlus = aliveParticles.get(i + 1);

            toRet.add((particleCountPlus - particleCount) / (i + 1 - i));
        }
        return toRet;
    }
    public void addQtoQPromMap(double vdMax, List<Double> qOverTime) {
        qPromMap.putIfAbsent(vdMax, new ArrayList<>());

        double prom = 0;
        for (Double d : qOverTime) {
            prom += d;
        }
        qPromMap.get(vdMax).add(prom / qOverTime.size());
    }

    public List<Double> getQPromList() {
        List<Double> toRet = new ArrayList<>();
        for (List<Double> list : qPromMap.values()) {
            toRet.add(getProm(list));
        }
        return toRet;
    }

    public double getProm(List<Double> list) {
        double prom = 0;
        for (Double i: list) {
            prom += i;
        }
        return prom / list.size();
    }

    public List<Double> getQPromError() {
        List<Double> toRet = new ArrayList<>();

        for (List<Double> list : qPromMap.values()) {
//            toRet.add(list.stream().max(Comparator.naturalOrder()).get() - list.stream().min(Comparator.naturalOrder()).get());
            toRet.add(getStandardDeviation(list));
        }
        return toRet;
    }

    public double getStandardDeviation(List<Double> list) {
        double prom = getProm(list);
        double toRet = 0;

        for (Double d: list) {
            toRet += Math.pow((d - prom), 2);
        }
        return Math.sqrt(toRet / list.size());
    }
}
