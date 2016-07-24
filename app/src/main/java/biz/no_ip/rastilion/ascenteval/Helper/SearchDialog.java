package biz.no_ip.rastilion.ascenteval.Helper;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import biz.no_ip.rastilion.ascenteval.R;
import biz.no_ip.rastilion.ascenteval.SolarSysDb.Planet;
import biz.no_ip.rastilion.ascenteval.SolarSysDb.Sys;
import biz.no_ip.rastilion.ascenteval.SystemListFragment;

/**
 * Created by rastilion on 22.07.16.
 * Sets up searching of planets, returns their systems
 */
public class SearchDialog extends Dialog {

    public SearchDialog(Context ctx){
        super(ctx);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced_search);
        setTitle("Search for Planets");
        Button cancel = (Button) findViewById(R.id.cnclBtn);
        Button find = (Button) findViewById(R.id.findBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemListFragment.refreshList();
                dismiss();
            }
        });
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemListFragment.updateList(querySystems());
                dismiss();
            }
        });

    }

    private List<Sys> querySystems() {
        List<Planet> planets;
        List<Sys> systemList = new ArrayList<>();
        String query;
        query = "SELECT * FROM Planet WHERE "+
                "atmo>="+(((CheckBox)findViewById(R.id.hasAtmo)).isChecked()?1:0)+
                " AND gems>="+(((CheckBox)findViewById(R.id.hasGems)).isChecked()?1:0)+
                " AND grain >="+(((Spinner)findViewById(R.id.grainSelect)).getSelectedItemPosition())+
                " AND fruit >="+(((Spinner)findViewById(R.id.fruitSelect)).getSelectedItemPosition())+
                " AND veg >="+(((Spinner)findViewById(R.id.vegSelect)).getSelectedItemPosition())+
                " AND meat >="+(((Spinner)findViewById(R.id.meatSelect)).getSelectedItemPosition())+
                " AND (fruit >="+(((Spinner)findViewById(R.id.anySelect)).getSelectedItemPosition())+
                " OR grain >="+(((Spinner)findViewById(R.id.anySelect)).getSelectedItemPosition())+
                " OR veg >="+(((Spinner)findViewById(R.id.anySelect)).getSelectedItemPosition())+
                " OR meat >="+(((Spinner)findViewById(R.id.anySelect)).getSelectedItemPosition())+")"+
                " AND tob >="+(((Spinner)findViewById(R.id.tobSelect)).getSelectedItemPosition())+
                " AND al >="+((float)((Spinner)findViewById(R.id.alSelect)).getSelectedItemPosition()/10)+
                " AND carb >="+((float)((Spinner)findViewById(R.id.carbSelect)).getSelectedItemPosition()/10)+
                " AND fe >="+((float)((Spinner)findViewById(R.id.feSelect)).getSelectedItemPosition()/10)+
                " AND ti >="+((float)((Spinner)findViewById(R.id.tiSelect)).getSelectedItemPosition()/10)+
                " AND si <="+((10f-(float)(((Spinner)findViewById(R.id.siSelect)).getSelectedItemPosition()))/10);
        planets = Planet.findWithQuery(Planet.class,query);
        for (int i=0;i<planets.size();i++) {
            boolean found = false;
            Sys syst = planets.get(i).system;
            Long sysId = syst.getId();
            for (int j=0;j<systemList.size();j++){
                if (systemList.get(j).getId().equals(sysId)){
                    found=true;
                }
            }
            if (!found){
                systemList.add(syst);
            }
        }
        Collections.sort(systemList, new Comparator<Sys>() {
            @Override
            public int compare(Sys sys, Sys t1) {
                return compareNatural(sys.Name,t1.Name,true);
            }
        });
        return systemList;
    }

    private static int compareNatural(String s, String t, boolean caseSensitive) {
        int sIndex = 0;
        int tIndex = 0;

        int sLength = s.length();
        int tLength = t.length();

        while (true) {
            // both character indices are after a subword (or at zero)

            // Check if one string is at end
            if (sIndex == sLength && tIndex == tLength) {
                return 0;
            }
            if (sIndex == sLength) {
                return -1;
            }
            if (tIndex == tLength) {
                return 1;
            }

            // Compare sub word
            char sChar = s.charAt(sIndex);
            char tChar = t.charAt(tIndex);

            boolean sCharIsDigit = Character.isDigit(sChar);
            boolean tCharIsDigit = Character.isDigit(tChar);

            if (sCharIsDigit && tCharIsDigit) {
                // Compare numbers

                // skip leading 0s
                int sLeadingZeroCount = 0;
                while (sChar == '0') {
                    ++sLeadingZeroCount;
                    ++sIndex;
                    if (sIndex == sLength) {
                        break;
                    }
                    sChar = s.charAt(sIndex);
                }
                int tLeadingZeroCount = 0;
                while (tChar == '0') {
                    ++tLeadingZeroCount;
                    ++tIndex;
                    if (tIndex == tLength) {
                        break;
                    }
                    tChar = t.charAt(tIndex);
                }
                boolean sAllZero = sIndex == sLength || !Character.isDigit(sChar);
                boolean tAllZero = tIndex == tLength || !Character.isDigit(tChar);
                if (sAllZero && tAllZero) {
                    continue;
                }
                if (sAllZero && !tAllZero) {
                    return -1;
                }
                if (tAllZero) {
                    return 1;
                }

                int diff = 0;
                do {
                    if (diff == 0) {
                        diff = sChar - tChar;
                    }
                    ++sIndex;
                    ++tIndex;
                    if (sIndex == sLength && tIndex == tLength) {
                        return diff != 0 ? diff : sLeadingZeroCount - tLeadingZeroCount;
                    }
                    if (sIndex == sLength) {
                        if (diff == 0) {
                            return -1;
                        }
                        return Character.isDigit(t.charAt(tIndex)) ? -1 : diff;
                    }
                    if (tIndex == tLength) {
                        if (diff == 0) {
                            return 1;
                        }
                        return Character.isDigit(s.charAt(sIndex)) ? 1 : diff;
                    }
                    sChar = s.charAt(sIndex);
                    tChar = t.charAt(tIndex);
                    sCharIsDigit = Character.isDigit(sChar);
                    tCharIsDigit = Character.isDigit(tChar);
                    if (!sCharIsDigit && !tCharIsDigit) {
                        // both number sub words have the same length
                        if (diff != 0) {
                            return diff;
                        }
                        break;
                    }
                    if (!sCharIsDigit) {
                        return -1;
                    }
                    if (!tCharIsDigit) {
                        return 1;
                    }
                } while (true);
            } else {
                do {
                    if (sChar != tChar) {
                        if (caseSensitive) {
                            return sChar - tChar;
                        }
                        sChar = Character.toUpperCase(sChar);
                        tChar = Character.toUpperCase(tChar);
                        if (sChar != tChar) {
                            sChar = Character.toLowerCase(sChar);
                            tChar = Character.toLowerCase(tChar);
                            if (sChar != tChar) {
                                return sChar - tChar;
                            }
                        }
                    }
                    ++sIndex;
                    ++tIndex;
                    if (sIndex == sLength && tIndex == tLength) {
                        return 0;
                    }
                    if (sIndex == sLength) {
                        return -1;
                    }
                    if (tIndex == tLength) {
                        return 1;
                    }
                    sChar = s.charAt(sIndex);
                    tChar = t.charAt(tIndex);
                    sCharIsDigit = Character.isDigit(sChar);
                    tCharIsDigit = Character.isDigit(tChar);
                } while (!sCharIsDigit && !tCharIsDigit);
            }
        }
    }
}