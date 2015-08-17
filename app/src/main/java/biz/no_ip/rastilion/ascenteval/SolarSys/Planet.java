package biz.no_ip.rastilion.ascenteval.SolarSys;


/**
 * Created by tgruetzmacher on 13.08.15.
 * Planets template
 */
public class Planet {
    private String name;
    private Composition composition;

    public Planet(String name, Composition composition) {
        this.name = name;
        this.composition = composition;
    }

    public Composition getComposition() {
        return composition;
    }

    public void setComposition(Composition composition) {
        this.composition = composition;
    }

    public Planet(String name) {
        this.name = name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
