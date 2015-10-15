package biz.no_ip.rastilion.ascenteval;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import biz.no_ip.rastilion.ascenteval.SolarSys.Planet;
import biz.no_ip.rastilion.ascenteval.SolarSys.Sys;
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
                BitSet roids = mItem.content.getRoidField();
                if (!roids.isEmpty()){
                    for (int i =0; i<roids.length();i++){
                        if (roids.get(i) & 1 == 1){

                        }
                    }
                }
                for (int i=0; i < mItem.content.getPlanets().size();i++) {
                    Planet p = mItem.content.getPlanet(i);
                    List<String> data = new ArrayList<>();
                    groups.add(p.getName());
                    data.add("Statistics: ");
                    data.add("Geologic rating: "+(p.getComposition().getGeo()>3?"<font color='#00d600'>"+p.getComposition().getGeo()+"</font>":"<font color='#EE0000'>"+p.getComposition().getGeo()+"</font>"));
                    data.add("Atmosphere: "+(p.getComposition().getAtmo()==1?getString(R.string.present):(p.getComposition().getAtmo()==0?getString(R.string.none):getString(R.string.unknown))));
                    data.add("Gems: "+(p.getComposition().getGems()==1?getString(R.string.present):(p.getComposition().getGems()==0?getString(R.string.none):getString(R.string.unknown))));
                    data.add("");
                    data.add("Composition: ");
                    data.add("Al: "+p.getComposition().getAl()* 100 + "%");
                    data.add("C : "+p.getComposition().getCarb() *100 + "%");
                    data.add("Fe: "+p.getComposition().getFe()*100 + "%");
                    data.add("Si: "+((p.getComposition().getSi() *100>10)?"<font color='#EE0000'>"+p.getComposition().getSi() *100+"%</font>":((p.getComposition().getSi() *100>5)?"<font color='#ffcc00'>"+p.getComposition().getSi() *100+"%</font>":"<font color='#00d600'>"+p.getComposition().getSi() *100+"%</font>")));
                    data.add("Ti: "+p.getComposition().getTi()*100 + "%");
                    data.add("");
                    data.add("Fertilities:");
                    data.add("Grain: "+(p.getComposition().getGrain()==0?getString(R.string.none):(p.getComposition().getGrain()==1?getString(R.string.poor):getString(R.string.fertile))));
                    data.add("Fruit: "+(p.getComposition().getFruit()==0?getString(R.string.none):(p.getComposition().getFruit()==1?getString(R.string.poor):getString(R.string.fertile))));
                    data.add("Vegetables: "+(p.getComposition().getVeg()==0?getString(R.string.none):(p.getComposition().getVeg()==1?getString(R.string.poor):getString(R.string.fertile))));
                    data.add("Meat: "+(p.getComposition().getMeat()==0?getString(R.string.none):(p.getComposition().getMeat()==1?getString(R.string.poor):getString(R.string.fertile))));
                    data.add("Tobacco: "+(p.getComposition().getTob()==0?getString(R.string.none):(p.getComposition().getTob()==1?getString(R.string.poor):(p.getComposition().getTob()==2?getString(R.string.fertile):getString(R.string.unknown)))));
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
