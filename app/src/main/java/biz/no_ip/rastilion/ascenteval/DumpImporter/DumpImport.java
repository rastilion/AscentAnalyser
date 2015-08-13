package biz.no_ip.rastilion.ascenteval.DumpImporter;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import biz.no_ip.rastilion.ascenteval.SolarSys.Planet;
import biz.no_ip.rastilion.ascenteval.SolarSys.Sys;
import biz.no_ip.rastilion.ascenteval.helper.SQLHelper;

/**
 * Created by tgruetzmacher on 03.08.15.
 * Import class for soilsampledump-<id>.txt files
 */
public class DumpImport {
    public static void parseFile(File inFile){
        List<String> result = new ArrayList<String>();
        List<String> planets = new ArrayList<String>();
        ArrayList<ArrayList<String>> system = new ArrayList<ArrayList<String>>();
            try {
                BufferedReader br = new BufferedReader(new FileReader(inFile));
                String line;
                while ((line = br.readLine()) != null) {
                    planets = Arrays.asList(line.split(";"));
                }
                br.close();
                planets.set(0, planets.get(0).substring(3));
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        for (String line : planets) {
            result.addAll(Arrays.asList(line.split("\\-")));
        }
        for (int i = 0; i<result.size()-1;i++) {
            if (i%2==0){
                system.add(new ArrayList<String>(Arrays.asList(result.get(i).substring(0, result.get(i).indexOf(" ")).trim(), result.get(i).substring(result.get(i).indexOf(" ") + 1).trim())));
            }
            else {
                system.get(system.size()-1).addAll(Arrays.asList(result.get(i).split(" |\\|")));
                system.get(system.size()-1).remove(2);
            }
        }
        for (ArrayList<String> l : system){
            for (String s : l){
                System.out.println(s);
            }
        }
        BuildSys(system);
    }

    private static void BuildSys(ArrayList<ArrayList<String>> sys){
        String oldName = "";
        Sys s;
        Planet p;
        for (ArrayList<String> l : sys){
            String name = l.get(0);
            if (name!=oldName){
                oldName=name;
                s = new Sys(name);
            }
            s.addPlanet(l.get(1), SQLHelper.getPlanetId(s.getName()));
        }
    }
}

