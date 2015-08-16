package biz.no_ip.rastilion.ascenteval.DumpImporter;

import android.app.Activity;
import android.app.Application;
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

import biz.no_ip.rastilion.ascenteval.SolarSys.Composition;
import biz.no_ip.rastilion.ascenteval.SolarSys.Planet;
import biz.no_ip.rastilion.ascenteval.SolarSys.Sys;
import biz.no_ip.rastilion.ascenteval.helper.SQLHelper;

/**
 * Created by tgruetzmacher on 03.08.15.
 * Import class for soilsampledump-<id>.txt files
 */
public class DumpImport extends Application {
//    private Context ctx = this.getApplicationContext();
//    SQLHelper hlp = new SQLHelper(ctx);

    public static List<Sys> parseFile(File inFile) {
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
        for (int i = 0; i < result.size() - 1; i++) {
            if (i % 2 == 0) {
                system.add(new ArrayList<String>(Arrays.asList(result.get(i).substring(0, result.get(i).indexOf(" ")).trim(), result.get(i).substring(result.get(i).indexOf(" ") + 1).trim())));
            }
            else {
                system.get(system.size() - 1).addAll(Arrays.asList(result.get(i).split(" |\\|")));
                system.get(system.size() - 1).remove(2);
            }
        }
        return BuildSys(system);
    }

    private static List<Sys> BuildSys(ArrayList<ArrayList<String>> sys) {
        String oldName = "";
        List<Sys> parsed = new ArrayList<>();
        Sys s = null;
        Planet p;
        for (ArrayList<String> l : sys) {
            String name = l.get(0);
            if (!name.equalsIgnoreCase(oldName)) {
                oldName = name;
                if (s!=null){
                    parsed.add(s);
                }
                s = new Sys(name);
            }
            if (s != null) {
                int pid = s.getPlanets().size();
                p = new Planet(l.get(1), pid);
                Composition cmp =new Composition();
                cmp.setAl(Float.parseFloat(l.get(3)));
                cmp.setSi(Float.parseFloat(l.get(5)));
                cmp.setGeo(Integer.parseInt(l.get(7)));
                cmp.setCarb(Float.parseFloat(l.get(9)));
                cmp.setFe(Float.parseFloat(l.get(11)));
                cmp.setTi(Float.parseFloat(l.get(13)));
                cmp.setGrain(Integer.parseInt(l.get(14)));
                cmp.setFruit(Integer.parseInt(l.get(15)));
                cmp.setVeg(Integer.parseInt(l.get(16)));
                cmp.setMeat(Integer.parseInt(l.get(17)));
                cmp.setTob(Integer.parseInt(l.get(18)));
                cmp.setGems(Integer.parseInt(l.get(19)));
                cmp.setAtmo(Integer.parseInt(l.get(20)));
                p.setComposition(cmp);
                s.addPlanet(p);
            }
        }

        return parsed;
    }
}

