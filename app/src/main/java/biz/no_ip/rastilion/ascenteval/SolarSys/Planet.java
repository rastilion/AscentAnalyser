package biz.no_ip.rastilion.ascenteval.SolarSys;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tgruetzmacher on 13.08.15.
 * Planets template
 */
public class Planet {
    private String name;
    private int system;
    private Composition composition;

    public Planet(String name, int system, Composition composition) {
        this.name = name;
        this.system = system;
        this.composition = composition;
    }

    public Composition getComposition() {
        return composition;
    }

    public void setComposition(Composition composition) {
        this.composition = composition;
    }

    public Planet(String name, int system) {
        this.name = name;
        this.system = system;
    }

    public void setSystem(int system) {
        this.system = system;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getSystem() {
        return system;
    }
}
