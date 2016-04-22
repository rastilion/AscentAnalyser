package biz.no_ip.rastilion.ascenteval;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import biz.no_ip.rastilion.ascenteval.Helper.Constants;
import biz.no_ip.rastilion.ascenteval.SolarSys.Giants;
import biz.no_ip.rastilion.ascenteval.SolarSys.Planet;
import biz.no_ip.rastilion.ascenteval.SolarSys.Sys;

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
    public static String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Sys mItem;


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
            List<Sys> items = Sys.find(Sys.class,"name = ?",getArguments().getString(ARG_ITEM_ID));
            mItem = items.get(0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_system_detail_expandable, null);
        ExpandableListView tv = (ExpandableListView) rootView.findViewById(R.id.list);
        sysInfoAdapter adapt = new sysInfoAdapter();
        adapt.initAdapter();
        tv.setAdapter(adapt);

        return rootView;
    }

        public class sysInfoAdapter extends BaseExpandableListAdapter {
            private List<String> groups = new ArrayList<>();

            private List<List<String>> children = new ArrayList<>();

            public void initAdapter() {
                BitSet roids = BitSet.valueOf(new long[]{mItem.roidField});
                List<Giants> gg = mItem.getGiants();

                if (!roids.isEmpty()){
                    List<String> data = new ArrayList<>();
                    groups.add("Asteroid field");
                    data.add("Asteroids present:");
                    for (int i =0; i<roids.length();i++){
                        if (roids.get(i)){
                            data.add(Constants.roidTypes.values()[i].name());
                        }
                    }
                    children.add(data);
                }
                if (!gg.isEmpty()){
                    List<String> data;
                    for (int j=0;j<gg.size();j++) {
                        data = new ArrayList<>();
                        groups.add("Gas Giant "+j);
                        BitSet comp = BitSet.valueOf(new long[]{gg.get(j).gasses});
                        for (int i = 0; i < comp.size(); i++) {
                            if (comp.get(i)) {
                                data.add(Constants.gas.values()[i].name());
                            }
                        }
                        children.add(data);
                    }
                }
                for (int i=0; i < mItem.getPlanets().size();i++) {
                    Planet p = mItem.getPlanets().get(i);
                    List<String> data = new ArrayList<>();
                    groups.add(p.name);
                    data.add("Statistics: ");
                    data.add("Geologic rating: "+(p.geo>3?"<font color='#00d600'>"+p.geo+"</font>":"<font color='#EE0000'>"+p.geo+"</font>"));
                    data.add("Atmosphere: "+(p.atmo==1?getString(R.string.present):(p.atmo==0?getString(R.string.none):getString(R.string.unknown))));
                    data.add("Gems: "+(p.gems==1?getString(R.string.present):(p.gems==0?getString(R.string.none):getString(R.string.unknown))));
                    data.add("");
                    data.add("Composition: ");
                    data.add("Al: "+p.al* 100 + "%");
                    data.add("C : "+p.carb *100 + "%");
                    data.add("Fe: "+p.fe*100 + "%");
                    data.add("Si: "+((p.si *100>10)?"<font color='#EE0000'>"+p.si *100+"%</font>":((p.si *100>5)?"<font color='#ffcc00'>"+p.si *100+"%</font>":"<font color='#00d600'>"+p.si *100+"%</font>")));
                    data.add("Ti: "+p.ti*100 + "%");
                    data.add("");
                    data.add("Fertilities:");
                    data.add("Grain: "+(p.grain==0?getString(R.string.none):(p.grain==1?getString(R.string.poor):getString(R.string.fertile))));
                    data.add("Fruit: "+(p.fruit==0?getString(R.string.none):(p.fruit==1?getString(R.string.poor):getString(R.string.fertile))));
                    data.add("Vegetables: "+(p.veg==0?getString(R.string.none):(p.veg==1?getString(R.string.poor):getString(R.string.fertile))));
                    data.add("Meat: "+(p.meat==0?getString(R.string.none):(p.meat==1?getString(R.string.poor):getString(R.string.fertile))));
                    data.add("Tobacco: "+(p.tob==0?getString(R.string.none):(p.tob==1?getString(R.string.poor):(p.tob==2?getString(R.string.fertile):getString(R.string.unknown)))));
                    //data.add(text);
                    children.add(data);
                }
            }

            @Override
            public int getGroupCount() {
                return groups.size();
            }

            @Override
            public int getChildrenCount(int i) {
                return children.get(i).size();
            }

            @Override
            public Object getGroup(int i) {
                return groups.get(i);
            }

            @Override
            public Object getChild(int i, int i1) {
                return children.get(i).get(i1);
            }

            @Override
            public long getGroupId(int i) {
                return i;
            }

            @Override
            public long getChildId(int i, int i1) {
                return i1;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
                TextView textView = new TextView(SystemDetailFragment.this.getActivity());
                textView.setPadding(0, 10, 0, 10);
                textView.setTextSize(24);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setTextColor(Color.parseColor("#CCCCCC"));
                textView.setText(getGroup(i).toString());
                return textView;
            }

            @Override
            public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
                TextView textView = new TextView(SystemDetailFragment.this.getActivity());
                textView.setTextColor(Color.parseColor("#CCCCCC"));
                textView.setTextSize(14);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setText(Html.fromHtml((String)getChild(i, i1)));
                return textView;
            }

            @Override
            public boolean isChildSelectable(int i, int i1) {
                return true;
            }

        }
}
