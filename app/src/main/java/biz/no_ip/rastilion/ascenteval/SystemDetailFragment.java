package biz.no_ip.rastilion.ascenteval;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            String text = mItem.content.getName()+"\n\nPlanets:\n";
            for (Planet p : mItem.content.getPlanets()){
                text+=p.getName()+"\n\n";
                text+="\tStatistics:\n";
                text+="\tGeo: "+p.getComposition().getGeo()+"\n";
                text+="\tHas Atmo "+(p.getComposition().getAtmo()==1?"Yes":"No")+"\n";
                text+="\tAlu: "+p.getComposition().getAl()* 100+"%\n";
                text+="\tCarb: "+p.getComposition().getCarb() *100+"%\n";
                text+="\tIron: "+p.getComposition().getFe()*100+"%\n";
                text+="\tSil: "+p.getComposition().getSi() *100+"%\n";
                text+="\tTita: "+p.getComposition().getTi()*100+"%\n";
                text+="\tHas Gems "+(p.getComposition().getGems()==1?"Yes":"No")+"\n";
                text+="\tFertilities\n";
                text+="\tGrain: "+(p.getComposition().getGrain()==0?"None":(p.getComposition().getGrain()==1?"Poor":"Fertile"))+"\n";
                text+="\tFruit: "+(p.getComposition().getFruit()==0?"None":(p.getComposition().getFruit()==1?"Poor":"Fertile"))+"\n";
                text+="\tVegetables: "+(p.getComposition().getVeg()==0?"None":(p.getComposition().getVeg()==1?"Poor":"Fertile"))+"\n";
                text+="\tMeat: "+(p.getComposition().getMeat()==0?"None":(p.getComposition().getMeat()==1?"Poor":"Fertile"))+"\n";
                text+="\tTobacco: "+(p.getComposition().getTob()==0?"None":(p.getComposition().getTob()==1?"Poor":"Fertile"))+"\n\n\n";
            }
            ((TextView) rootView.findViewById(R.id.system_detail)).setText(text);
        }

        return rootView;
    }
}
