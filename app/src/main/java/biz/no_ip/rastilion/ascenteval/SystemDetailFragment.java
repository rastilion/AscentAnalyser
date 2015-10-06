package biz.no_ip.rastilion.ascenteval;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import biz.no_ip.rastilion.ascenteval.SolarSys.Planet;
import biz.no_ip.rastilion.ascenteval.dummy.DummyContent;

/**
 * A fragment representing a single System detail screen.
 * This fragment is either contained in a {@link SystemListActivity}
 * in two-pane mode (on tablets) or a {@link SystemDetailActivity}
 * on handsets.
 */
public class SystemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SystemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_system_detail, container, false);
        TextView tv = (TextView) rootView.findViewById(R.id.system_detail);
        tv.setBackgroundColor(Color.DKGRAY);
        tv.setTextColor(Color.WHITE);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            String text = mItem.content.getName()+"<br /><br />Planets:<br />";
            for (Planet p : mItem.content.getPlanets()){
                text+=p.getName()+"<br /><br />";
                text+="\tStatistics:<br />";
                text+="\tGeo: "+(p.getComposition().getGeo()>3?"<font color='#00d600'>"+p.getComposition().getGeo()+"</font>":"<font color='#EE0000'>"+p.getComposition().getGeo()+"</font>")+"<br />";
                text+="\tHas Atmo "+(p.getComposition().getAtmo()==1?"<font color='#00d600'>Yes</font>":(p.getComposition().getAtmo()==0?"<font color='#EE0000'>No</font>":"<font color='#ffcc00'>Unknown</font>"))+"<br />";
                text+="\tAlu: "+p.getComposition().getAl()* 100+"%<br />";
                text+="\tCarb: "+p.getComposition().getCarb() *100+"%<br />";
                text+="\tIron: "+p.getComposition().getFe()*100+"%<br />";
                text+="\tSil: "+((p.getComposition().getSi() *100>10)?"<font color='#EE0000'>"+p.getComposition().getSi() *100+"</font>":((p.getComposition().getSi() *100>5)?"<font color='#ffcc00'>"+p.getComposition().getSi() *100+"</font>":"<font color='#00d600'>"+p.getComposition().getSi() *100+"</font>"))+"%<br />";
                text+="\tTita: "+p.getComposition().getTi()*100+"%<br />";
                text+="\tHas Gems "+(p.getComposition().getGems()==1?"<font color='#00d600'>Yes</font>":(p.getComposition().getGems()==0?"<font color='#EE0000'>No</font>":"<font color='#ffcc00'>Unknown</font>"))+"<br />";
                text+="\tFertilities<br />";
                text+="\tGrain: "+(p.getComposition().getGrain()==0?"<font color='#EE0000'>None</font>":(p.getComposition().getGrain()==1?"<font color='#ffcc00'>Poor</font>":"<font color='#00d600'>Fertile</font>"))+"<br />";
                text+="\tFruit: "+(p.getComposition().getFruit()==0?"<font color='#EE0000'>None</font>":(p.getComposition().getFruit()==1?"<font color='#ffcc00'>Poor</font>":"<font color='#00d600'>Fertile</font>"))+"<br />";
                text+="\tVegetables: "+(p.getComposition().getVeg()==0?"<font color='#EE0000'>None</font>":(p.getComposition().getVeg()==1?"<font color='#ffcc00'>Poor</font>":"<font color='#00d600'>Fertile</font>"))+"<br />";
                text+="\tMeat: "+(p.getComposition().getMeat()==0?"<font color='#EE0000'>None</font>":(p.getComposition().getMeat()==1?"<font color='#ffcc00'>Poor</font>":"<font color='#00d600'>Fertile</font>"))+"<br />";
                text+="\tTobacco: "+(p.getComposition().getTob()==0?"<font color='#EE0000'>None</font>":(p.getComposition().getTob()==1?"<font color='#ffcc00'>Poor</font>":(p.getComposition().getTob()==2?"<font color='#00d600'>Fertile</font>":"<font color='#ffcc00'>Unknown</font>")))+"<br /><br /><br />";
            }
            tv.setText(Html.fromHtml(text));
        }

        return rootView;
    }
}
