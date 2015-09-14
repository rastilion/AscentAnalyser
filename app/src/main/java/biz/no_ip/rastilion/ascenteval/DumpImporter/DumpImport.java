package biz.no_ip.rastilion.ascenteval.DumpImporter;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import biz.no_ip.rastilion.ascenteval.FileManipulator;
import biz.no_ip.rastilion.ascenteval.SolarSys.Composition;
import biz.no_ip.rastilion.ascenteval.SolarSys.Planet;
import biz.no_ip.rastilion.ascenteval.SolarSys.Sys;
import biz.no_ip.rastilion.ascenteval.StaticContext;
import biz.no_ip.rastilion.ascenteval.dummy.DummyContent;

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
            planets.set(0,planets.get(0).replaceAll("[^ -~]", ""));
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        for (String line : planets) {
            result.addAll(Arrays.asList(line.split("\\-")));
        }
        for (int i = 0; i < result.size(); i++) {
            if (i % 2 == 0) {
                int name = 0, sysinfo = 0;
                String sysName;
                String sysComp;
                if (result.get(i).contains("Rocky Planet")){
                    name = result.get(i).indexOf(" Rocky Planet");
                    sysinfo = result.get(i).indexOf(" Rocky Planet")+1;
                }
                else if (result.get(i).contains("Moon")){
                    name = result.get(i).indexOf(" Moon");
                    sysinfo = result.get(i).indexOf(" Moon")+1;
                }
                else {
                    if (result.size()>=3){
                        boolean found=false;
                        for (int j = 0; j < result.get(i).length(); j++){
                            if ((i-2)>=0 && !found && result.get(i).charAt(0)==result.get(i-2).charAt(0)){
                                if ((result.get(i).charAt(j) != result.get(i-2).charAt(j))) {
                                    name = j-1;
                                    sysinfo = j;
                                    found = true;
                                }
                            }
                            else if ((i+2)<result.size() && !found && result.get(i).charAt(0)==result.get(i+2).charAt(0)){
                                if ((result.get(i).charAt(j) != result.get(i+2).charAt(j))) {
                                    name = j-1;
                                    sysinfo = j;
                                    found = true;
                                }
                            }
                        }
                    }
                }
                sysName =result.get(i).substring(0, name);
                sysComp = result.get(i).substring(sysinfo);
                system.add(new ArrayList<String>(Arrays.asList(sysName,sysComp)));
            }
            else {
                system.get(system.size() - 1).addAll(Arrays.asList(result.get(i).split(" |\\|")));
                system.get(system.size() - 1).remove(2);
            }
        }
        for (int i = 0; i < system.size(); i++){
            if (system.get(i).get(0).equals("")){
                try {
                    if (result.get(i).equals(system.get(i + 1).get(0))) {
                        system.get(i).set(0, result.get(i));
                    } else if (result.get(i).equals(system.get(i - 1).get(0))) {
                        system.get(i).set(0, result.get(i));
                    }
                }
                catch (Exception e){
                    system.get(i).set(0,system.get(i).get(1).substring(0,system.get(i).get(1).indexOf(" ",system.get(i).get(1).indexOf(" ")+1)));
                    system.get(i).set(1,system.get(i).get(1).substring(system.get(i).get(1).indexOf(" ",system.get(i).get(1).indexOf(" ")+1)+1));
                }
            }
        }
        returnValue = BuildSys(system);
        try {
            oos.writeObject(returnValue);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnValue;
    }

    private static List<Sys> BuildSys(ArrayList<ArrayList<String>> sys) {
        String oldSysName = "";
        List<Sys> parsed = new ArrayList<Sys>();
        Sys s = null;
        Planet p;

        try {
            parsed = DummyContent.getAllSystems();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (ArrayList<String> l : sys) {
            String sysName = l.get(0);

            s = new Sys(sysName);
            for (int i=0;i<parsed.size();i++){
                if (s.getName().equals(parsed.get(i).getName())){
                    s = parsed.get(i);
                    break;
                }
            }
            if (!oldSysName.equals(sysName)) {
                oldSysName = sysName;
            }
            if (s != null) {
                boolean pfound = false;
                if (s.getPlanetCount() > 0) {
                    for (int i = 0; i < s.getPlanetCount(); i++) {
                        if (l.get(1).equals(s.getPlanet(i).getName())) {
                            pfound=true;
                            break;
                        }
                    }
                }
                if(!pfound) {
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
                    parsed.add(s);
                }
            }
            Collections.sort(s.getPlanets(), new Comparator<Planet>() {
                @Override
                public int compare(Planet lhs, Planet rhs) {
                    if (0==extractInt(lhs.getName())){
                        return lhs.getName().compareToIgnoreCase(rhs.getName());
                    }
                    else {
                        return extractInt(lhs.getName()) - extractInt(rhs.getName());
                    }
                }

                int extractInt(String s) {
                    String num = s.replaceAll("\\D", "");
                    // return 0 if no digits found
                    return num.isEmpty() ? 0 : Integer.parseInt(num);
                }
            });
        }

        return parsed;
    }
}

