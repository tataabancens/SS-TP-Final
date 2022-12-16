package ar.edu.itba.ss;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

public class JsonPrinter {
    private JSONArray particlesAliveArray;
    private JSONArray endPAPromAndError;

    public JsonPrinter() {
        particlesAliveArray = new JSONArray();
        endPAPromAndError = new JSONArray();
    }

    public void addParticleAlive(int amount, int day) {
        JSONObject particle = new JSONObject();
        particle.put("amount", amount);
        particle.put("day", day);
        particlesAliveArray.add(particle);
    }

    public JSONArray getParticlesAliveArray() {
        return particlesAliveArray;
    }

    public JSONArray printPromAndError(double[] vdMaxes, List<Double> promList, List<Double> errorList) {
        JSONArray arr = new JSONArray();
        for (int i = 0; i < vdMaxes.length; i++) {
            JSONObject obj = new JSONObject();
            obj.put("vdMax", vdMaxes[i]);
            obj.put("prom", promList.get(i));
            obj.put("error", errorList.get(i));
            arr.add(obj);
        }
        return arr;
    }

    public JSONArray getEndPAPromAndError() {
        return endPAPromAndError;
    }

    public JSONArray printParticlesAlive(double vdMax, List<Double> promsParticlesAliveVsTime) {
        JSONArray arr = new JSONArray();
        for (int i = 0; i < promsParticlesAliveVsTime.size(); i++) {
            JSONObject obj = new JSONObject();
            obj.put("day", i);
            obj.put("amount", promsParticlesAliveVsTime.get(i));
            arr.add(obj);
        }
        return arr;
    }

    public JSONArray printQPromAndError(double[] vdMaxes, List<Double> promList, List<Integer> errorList) {
        JSONArray arr = new JSONArray();
        return null;
    }
}
