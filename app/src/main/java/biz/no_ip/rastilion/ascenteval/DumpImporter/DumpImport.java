package biz.no_ip.rastilion.ascenteval.DumpImporter;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tgruetzmacher on 03.08.15.
 */
public class DumpImport {
    public static List<String> parseFile(File inFile){
        List<String> result = new ArrayList<String>();
        List<String> planets = new ArrayList<String>();
            try {
                BufferedReader br = new BufferedReader(new FileReader(inFile));
                String line;
                while ((line = br.readLine()) != null) {
                    planets = Arrays.asList(line.split(";"));
                }
                br.close();
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        System.out.println(planets.toString());
        return result;
    }
}

