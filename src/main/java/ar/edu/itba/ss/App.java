package ar.edu.itba.ss;

import java.io.PrintWriter;

import static ar.edu.itba.ss.Utils.openFile;
import static ar.edu.itba.ss.Utils.writeToFile;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        PrintWriter pw = openFile("output/system.xyz");
        SimHandler sh = new SimHandler(5);

        writeToFile(pw, sh.printSystem());
        double outerStep = 0.2, lastTime = sh.getActualTime();
        while(!sh.generalEndCondition()) {
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
