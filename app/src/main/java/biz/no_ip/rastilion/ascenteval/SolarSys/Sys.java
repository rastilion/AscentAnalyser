package biz.no_ip.rastilion.ascenteval.SolarSys;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Created by tgruetzmacher on 13.08.15.
 * Planetary system class
 */
public class Sys implements Serializable{
    private String Name;
    private List<Planet> planets= new ArrayList<>();
    public enum roidTypes {Angrite,Autunite,Chrondrite,Colombite,Kamacite,Neurocrystallite,Siderolite,Ureilite};
    private BitSet roidField = new BitSet(roidTypes.values().length);

    public Sys(String name) {
        Name = name;
    }

    public Sys(String name, List<Planet> planets) {
        Name = name;
        this.planets = planets;
    }

    public List<Planet> getPlanets() {
        return planets;
    }

    public void setPlanets(List<Planet> planets) {
        this.planets = planets;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public Planet getPlanet(int id){
        return planets.get(id);
    }

    public void addPlanet(Planet p){
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
}
