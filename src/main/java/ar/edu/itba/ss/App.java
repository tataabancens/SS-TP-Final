package ar.edu.itba.ss;

import java.io.PrintWriter;

import static ar.edu.itba.ss.Utils.openFile;
import static ar.edu.itba.ss.Utils.writeToFile;

public class App {
    public static void main( String[] args ) {
        generateCreaturesAliveGraphsVsDays();
        generateCreaturesAlivePromAndError();
    }

    public static void generateCreaturesAlivePromAndError() {
        double[] vdMaxes = { 11, 12, 13, 14, 15};
        JsonPrinter jp = new JsonPrinter();
        DataAccumulator da = new DataAccumulator();

        for (double vdMax : vdMaxes) {
            int runsPerVdMax = 3;
            for (int i = 0; i < runsPerVdMax; i++) {
                SimHandler sh = new SimHandler(20, 50, 10, vdMax, 7);
                while(!sh.generalEndCondition()) {
                    jp.addParticleAlive(sh.getCreaturesAmount(), sh.getDaysElapsed());
                    sh.initDay();

                    while (!sh.dayFinished()) {
                        sh.iterate();
                    }
                    sh.concludeDay();
                }
                da.addEndPAToMap(vdMax, sh.getCreaturesAmount());
                System.out.println(sh.getCreaturesAmount());
            }
        }
        jp.savePromAndError(vdMaxes, da.getPromList(), da.getErrorList());

        PrintWriter pw = openFile("output/creaturesAlivePromError.json");
        writeToFile(pw, jp.getEndPAPromAndError().toJSONString());
    }


    public static void generateCreaturesAliveGraphsVsDays() {
        double[] vdMaxes = { 11, 12, 13, 14, 15};
        JsonPrinter jp = new JsonPrinter();

        for (double vdMax : vdMaxes) {
            SimHandler sh = new SimHandler(20, 50, 10, vdMax, 7);
            DataAccumulator da = new DataAccumulator();

            while(!sh.generalEndCondition()) {
                jp.addParticleAlive(sh.getCreaturesAmount(), sh.getDaysElapsed());
                sh.initDay();

                while (!sh.dayFinished()) {
                    sh.iterate();
                }
                da.addParticlesAlive(sh.getCreaturesAmount());
                sh.concludeDay();
            }

            PrintWriter pw = openFile("output/creaturesAlive" + (int) vdMax + ".json");
            writeToFile(pw, jp.getParticlesAliveArray().toJSONString());
        }
    }

    public static void generateOvito() {
        PrintWriter pw = openFile("output/system.xyz");
        SimHandler sh = new SimHandler(15, 50, 10, 11, 7);

        writeToFile(pw, sh.printSystem());
        double outerStep = 0.2, lastTime = sh.getActualTime();

        while(!sh.generalEndCondition()) {
            System.out.println(sh.getCreaturesAmount());
            sh.initDay();

            while (!sh.dayFinished()) {
                sh.iterate();

                if (sh.getActualTime() - lastTime > outerStep ) {
                    lastTime = sh.getActualTime();
                    writeToFile(pw, sh.printSystem());
                }
            }
            double waitToNextDay = 5, time = 0;
            while (time < waitToNextDay) {
                sh.iterate();
                time += 1;
                writeToFile(pw, sh.printSystem());
            }
            sh.concludeDay();
        }
    }
}
