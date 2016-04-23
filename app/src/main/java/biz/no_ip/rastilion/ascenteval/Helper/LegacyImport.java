package biz.no_ip.rastilion.ascenteval.Helper;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;


/**
 * Created by rastilion on 23.04.16.
 * Filereader for legacy save import
 */
public class LegacyImport {

    private ObjectInputStream ois = null;
    List<Syst> sysImport;
    enum roidTypes {Angrite,Autunite,Chrondrite,Colombite,Kamacite,Neurocrystallite,Siderolite,Ureilite}
    enum gas {Hydrogen,Nitrogen,Oxygen,Tritium}

    public LegacyImport(Context ctx){
        getReadStream(ctx);
        if (ois != null){
            try{
                sysImport =(ArrayList) ois.readObject();
                Log.e("Import",sysImport.toString());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private String FILENAME="SysStore";
    // get a reading stream from the data directory
    public ObjectInputStream getReadStream(Context ctx){
        try {
            FileInputStream fis = ctx.openFileInput(FILENAME);
            ois = new ObjectInputStream(fis);
        }
        catch(Exception e){
            Log.i("FNF", "File not found for reading");
        }
        return ois;
    }

    private class Syst implements Serializable {
        private String Name;
        private List<Planets> planets= new ArrayList<>();
        private BitSet roidField = new BitSet(roidTypes.values().length);
        private List<BitSet> ggs = new ArrayList<>();

        public Syst(String name) {
            Name = name;
        }

        public Syst(String name, List<Planets> planets) {
            Name = name;
            this.planets = planets;
        }

        public List<Planets> getPlanets() {
            return planets;
        }

        public void setPlanets(List<Planets> planets) {
            this.planets = planets;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getName() {
            return Name;
        }

        public Planets getPlanet(int id){
            return planets.get(id);
        }

        public void addPlanet(Planets p){
            planets.add(p);
        }
        public int getPlanetCount(){
            return planets.size();
        }

        public BitSet getRoidField() {
            return roidField;
        }

        public void setRoidField(BitSet roidField) {
            this.roidField = roidField;
        }

        public List<BitSet> getGgs() {
            return ggs;
        }

        public void setGgs(List<BitSet> ggs) {
            this.ggs = ggs;
        }

        public void addGgs(int gg){
            ggs.add(BitSet.valueOf(new long[]{gg}));
        }

        public void delGgs(int pos){
            ggs.remove(pos);
        }
    }

    private class Planets implements Serializable {
        private String name;
        private Composition composition;

        public Planets(String name, Composition composition) {
            this.name = name;
            this.composition = composition;
        }

        public Composition getComposition() {
            return composition;
        }

        public void setComposition(Composition composition) {
            this.composition = composition;
        }

        public Planets(String name) {
            this.name = name;
        }


        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

    private class Composition implements Serializable {

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

        public Composition(){}

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

}
