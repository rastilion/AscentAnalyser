package biz.no_ip.rastilion.ascenteval.Helper;

import java.util.ArrayList;
import java.util.List;

import biz.no_ip.rastilion.ascenteval.SolarSysDb.Planet;
import biz.no_ip.rastilion.ascenteval.SolarSysDb.Sys;

/**
 * Created by rastilion on 19.07.16.
 */
public class SystemsHelper {

    static List<Planet> db;

    public static List searchPlanets(String[][] term){
        db = new Sys().getPlanets();
        List<Planet> hits = new ArrayList<>();
        for (Planet p : db){
            List comp = p.getComposition();
            for (int i = 0; i<term.length;i++){
                switch (term[i][0]){
                    case "lt":
                        if (Float.parseFloat(String.valueOf(comp.get(i)))<Float.parseFloat(term[i][1])){
                            hits.add(p);
                        }
                        break;
                    case "gt":
                        if (Float.parseFloat(String.valueOf(comp.get(i)))>Float.parseFloat(term[i][1])){
                            hits.add(p);
                        }
                        break;
                    case "eq":
                        if (Float.parseFloat(String.valueOf(comp.get(i)))==Float.parseFloat(term[i][1])){
                            hits.add(p);
                        }
                        break;

                }
            }
        }
        return hits;
    }
}
