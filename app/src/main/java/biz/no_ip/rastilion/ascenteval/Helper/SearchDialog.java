package biz.no_ip.rastilion.ascenteval.Helper;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;
import biz.no_ip.rastilion.ascenteval.R;
import biz.no_ip.rastilion.ascenteval.SolarSysDb.Planet;
import biz.no_ip.rastilion.ascenteval.SolarSysDb.Sys;
import biz.no_ip.rastilion.ascenteval.SystemListFragment;

/**
 * Created by rastilion on 22.07.16.
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
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemListFragment.updateList();
                dismiss();
            }
        });
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemListFragment.systems.clear();
                querySystems();
                SystemListFragment.refreshList();
                dismiss();
            }
        });

    }

    private void querySystems() {
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
                " AND si <="+((float)(((Spinner)findViewById(R.id.siSelect)).getSelectedItemPosition())/10);
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
        SystemListFragment.systems = systemList;
    }
}
