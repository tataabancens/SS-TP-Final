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

    public void savePromAndError(double[] vdMaxes, List<Double> promList, List<Integer> errorList) {
        for (int i = 0; i < vdMaxes.length; i++) {
            JSONObject obj = new JSONObject();
            obj.put("vdMax", vdMaxes[i]);
            obj.put("prom", promList.get(i));
            obj.put("error", errorList.get(i));
            endPAPromAndError.add(obj);
        }
    }

    public JSONArray getEndPAPromAndError() {
        return endPAPromAndError;
    }
}
