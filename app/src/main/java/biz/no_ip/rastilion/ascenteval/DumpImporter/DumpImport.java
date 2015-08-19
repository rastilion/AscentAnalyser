package biz.no_ip.rastilion.ascenteval.DumpImporter;

import android.app.Application;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import biz.no_ip.rastilion.ascenteval.FileManipulator;
import biz.no_ip.rastilion.ascenteval.SolarSys.Composition;
import biz.no_ip.rastilion.ascenteval.SolarSys.Planet;
import biz.no_ip.rastilion.ascenteval.SolarSys.Sys;
import biz.no_ip.rastilion.ascenteval.StaticContext;

/**
 * Created by tgruetzmacher on 03.08.15.
 * Import class for soilsampledump-<id>.txt files
 */
public class DumpImport extends Application {
    static Context ctx = StaticContext.getCustomAppContext();

    public static List<Sys> parseFile(File inFile) {
        ObjectOutputStream oos = FileManipulator.getWriteStream(ctx.getApplicationContext());
        List<String> result = new ArrayList<String>();
        List<String> planets = new ArrayList<String>();
        List<Sys> returnValue;
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
        for (int i = 0; i < result.size(); i++) {
            if (i % 2 == 0) {
                system.add(new ArrayList<String>(Arrays.asList(result.get(i).substring(0, result.get(i).indexOf(" ")), result.get(i).substring(result.get(i).indexOf(" ") + 1))));
            }
            else {
                system.get(system.size() - 1).addAll(Arrays.asList(result.get(i).split(" |\\|")));
                system.get(system.size() - 1).remove(2);
            }
        }
        returnValue = BuildSys(system);
        try {
            oos.writeObject(returnValue);
            System.out.println("Written");
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileManipulator.close();

        return returnValue;
    }

    private static List<Sys> BuildSys(ArrayList<ArrayList<String>> sys) {
        boolean found = false;
        ObjectInputStream ois = FileManipulator.getReadStream(ctx.getApplicationContext());
        String oldSysName = "";
        List<Sys> parsed = null;

        try {
            parsed = (ArrayList<Sys>) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (parsed == null){
            parsed = new ArrayList<Sys>();
        }
        Sys s = null;
        Planet p;
        for (ArrayList<String> l : sys) {
            String sysName = l.get(0);
            if (!oldSysName.equals(sysName)) {
                oldSysName = sysName;
                if (s != null) {
                    for (int i =0; i< parsed.size()-1;i++) {
                        if (parsed.get(i).equals(s)) {
                            found = true;
                        }
                    }
                }
                if(!found && s!=null){
                    System.out.println("New System: " + s.getName());
                    parsed.add(s);
                }

                s = new Sys(sysName);
                for (int i=0;i<parsed.size()-1;i++){
                    if (parsed.get(i).getName().equals(sysName)){
                        s = parsed.get(i);
                    }
                }
            }
            if (s != null) {
                if (s.getPlanetCount()>0){
                    for (int i=0;i<s.getPlanetCount()-1;i++){
                        if (s.getPlanet(i).getName().equals(l.get(i))){
                            System.out.println("Planet vorhanden: " + s.getPlanet(i).getName());
                            break;
                        }
                    }
                }
                else {
                    p = new Planet(l.get(1));
                    if (l.size() == 21) {
                        Composition cmp = new Composition();
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
                    } else {
                        Composition cmp = new Composition();
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
                        cmp.setTob(0);
                        cmp.setGems(0);
                        cmp.setAtmo(Integer.parseInt(l.get(18)));
                        p.setComposition(cmp);
                        s.addPlanet(p);
                    }
                }
            }
        }

        return parsed;
    }
}

