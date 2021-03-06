package biz.no_ip.rastilion.ascenteval.SolarSysDb;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by tgruetzmacher on 13.08.15.
 * Planetary system class
 */
public class Sys extends SugarRecord {
    public String Name;
    public long roidField = 0;

    public Sys() {
    }

    public Sys(String name) {
        this.Name = name;
    }

    public Sys(String name, int roid) {
        this.Name = name;
        this.roidField = roid;
    }
    public List<Planet> getPlanets(){
        return Planet.find(Planet.class, "system = ?", getId().toString());
    }
    public List<Giants> getGiants(){
        return Giants.find(Giants.class, "system = ?", getId().toString());
    }

    @Override
    public String toString(){
        return Name;
    }
}
