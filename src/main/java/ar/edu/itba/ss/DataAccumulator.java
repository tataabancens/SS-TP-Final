package ar.edu.itba.ss;

import java.util.*;

public class DataAccumulator {
    List<Integer> particlesAlive = new ArrayList<>();

    Map<Double, List<Integer>> vdMaxVsParticlesAliveEnd = new HashMap<>();

    public void addEndPAToMap(double vdMax, Integer endPA) {
        vdMaxVsParticlesAliveEnd.putIfAbsent(vdMax, new ArrayList<>());

        List<Integer> list = vdMaxVsParticlesAliveEnd.get(vdMax);
        list.add(endPA);
    }

    public List<Double> getPromList() {
        List<Double> toRet = new ArrayList<>();
        for (List<Integer> list : vdMaxVsParticlesAliveEnd.values()) {
            double toAdd = 0;
            for (Integer i: list) {
                toAdd += i;
            }
            toRet.add(toAdd / list.size());
        }
        return toRet;
    }

    public List<Integer> getErrorList() {
        List<Integer> toRet = new ArrayList<>();
        for (List<Integer> list : vdMaxVsParticlesAliveEnd.values()) {
            toRet.add(list.stream().max(Comparator.naturalOrder()).get() - list.stream().min(Comparator.naturalOrder()).get());
        }
        return toRet;
    }

    public void addParticlesAlive(Integer amount) {
        particlesAlive.add(amount);
    }

    public List<Integer> getParticlesAlive() {
        return particlesAlive;
    }

}
