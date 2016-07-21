package biz.no_ip.rastilion.ascenteval.SolarSysDb;


import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

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
        this.setName(name);
    }

    public void setComposition(float al, float carb, float fe, float si, float ti, int geo, int grain, int fruit, int veg, int meat, int tob, int gems, int atmo) {
        this.setAl(al);
        this.setCarb(carb);
        this.setFe(fe);
        this.setSi(si);
        this.setTi(ti);
        this.setGeo(geo);
        this.setGrain(grain);
        this.setFruit(fruit);
        this.setVeg(veg);
        this.setMeat(meat);
        this.setTob(tob);
        this.setGems(gems);
        this.setAtmo(atmo);
    }

    public List<String> getComposition(){

        List<String> composition = new ArrayList<>();

        composition.add(String.valueOf(getAl()));
        composition.add(String.valueOf(getCarb()));
        composition.add(String.valueOf(getFe()));
        composition.add(String.valueOf(getSi()));
        composition.add(String.valueOf(getTi()));
        composition.add(String.valueOf(getGeo()));
        composition.add(String.valueOf(getGrain()));
        composition.add(String.valueOf(getFruit()));
        composition.add(String.valueOf(getVeg()));
        composition.add(String.valueOf(getMeat()));
        composition.add(String.valueOf(getTob()));
        composition.add(String.valueOf(getGems()));
        composition.add(String.valueOf(getAtmo()));

        return composition;
    }

    public int getVeg() {
        return veg;
    }

    public void setVeg(int veg) {
        this.veg = veg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAl() {
        return al;
    }

    public void setAl(float al) {
        this.al = al;
    }

    public float getCarb() {
        return carb;
    }

    public void setCarb(float carb) {
        this.carb = carb;
    }

    public float getFe() {
        return fe;
    }

    public void setFe(float fe) {
        this.fe = fe;
    }

    public float getSi() {
        return si;
    }

    public void setSi(float si) {
        this.si = si;
    }

    public float getTi() {
        return ti;
    }

    public void setTi(float ti) {
        this.ti = ti;
    }

    public int getGeo() {
        return geo;
    }

    public void setGeo(int geo) {
        this.geo = geo;
    }

    public int getGrain() {
        return grain;
    }

    public void setGrain(int grain) {
        this.grain = grain;
    }

    public int getFruit() {
        return fruit;
    }

    public void setFruit(int fruit) {
        this.fruit = fruit;
    }

    public int getMeat() {
        return meat;
    }

    public void setMeat(int meat) {
        this.meat = meat;
    }

    public int getTob() {
        return tob;
    }

    public void setTob(int tob) {
        this.tob = tob;
    }

    public int getGems() {
        return gems;
    }

    public void setGems(int gems) {
        this.gems = gems;
    }

    public int getAtmo() {
        return atmo;
    }

    public void setAtmo(int atmo) {
        this.atmo = atmo;
    }
}
