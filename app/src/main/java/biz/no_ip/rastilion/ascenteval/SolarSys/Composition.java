package biz.no_ip.rastilion.ascenteval.SolarSys;

/**
 * Created by tgruetzmacher on 13.08.15.
 * Planetary composition class
 */
public class Composition {

    private int planetId;
    private float al;
    private float carb;
    private float fe;
    private float si;
    private float ti;
    private int geo=0;
    private int grain=0;
    private int fruit=0;
    private int veg=0;
    private int meat=0;
    private int tob=0;
    private int gems=0;
    private int atmo=0;

    public Composition(int planetId, float al, float carb, float fe, float si, float ti, int geo, int grain, int fruit, int veg, int meat, int tob, int gems, int atmo) {
        this.planetId = planetId;
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

    public Composition(){};

    public int getPlanetId() {
        return planetId;
    }

    public void setPlanetId(int planetId) {
        this.planetId = planetId;
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

    public int getVeg() {
        return veg;
    }

    public void setVeg(int veg) {
        this.veg = veg;
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
