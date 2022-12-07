package ar.edu.itba.ss;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Utils {

    public static PrintWriter openFile(String filepath) {
        try {
            new File(filepath).delete();
            new File(filepath).createNewFile();
            FileWriter fw = new FileWriter(filepath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            return new PrintWriter(bw);
        } catch (Exception e) {
            System.out.println("Failed");
        }
        return null;
    }

    public static void writeToFile(PrintWriter pw, String toWrite) {
        pw.print(toWrite);
        pw.flush();
    }
}
