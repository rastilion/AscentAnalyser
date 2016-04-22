package biz.no_ip.rastilion.ascenteval.SolarSys;


import com.orm.SugarRecord;

/**
 * Created by tgruetzmacher on 13.08.15.
 * Planets template
 */
public class Planet extends SugarRecord {
    public String name;
    public float al;
    public float carb;
    public float fe;
    public float si;
    public float ti;
    public int geo=0;
    public int grain=0;
    public int fruit=0;
    public int veg=0;
    public int meat=0;
    public int tob=0;
    public int gems=0;
    public int atmo=0;

    public Sys system;

    public Planet(){
    }

    public Planet(String name) {
        this.name = name;
    }

    public void setComposition(float al, float carb, float fe, float si, float ti, int geo, int grain, int fruit, int veg, int meat, int tob, int gems, int atmo) {
        this.al = al;
        this.carb = carb;
        this.fe = fe;
        this.si = si;
        this.ti = ti;
        this.geo = geo;
        this.grain = grain;
        this.fruit = fruit;
        this.veg = veg;
        this.meat = meat;
        this.tob = tob;
        this.gems = gems;
        this.atmo = atmo;
    }

}
