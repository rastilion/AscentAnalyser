package biz.no_ip.rastilion.ascenteval.SolarSys;

import com.orm.SugarRecord;

/**
 * Created by tgruetzmacher on 13.08.15.
 * Planetary system class
 */
public class Giants extends SugarRecord {
    public int gasses=0;

    public Sys system;

    public Giants() {
    }

    public Giants( int c) {
        this.gasses = c;
    }
}
