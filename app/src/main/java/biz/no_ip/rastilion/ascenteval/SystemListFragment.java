package biz.no_ip.rastilion.ascenteval;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import biz.no_ip.rastilion.ascenteval.Helper.Constants;
import biz.no_ip.rastilion.ascenteval.SolarSysDb.Giants;
import biz.no_ip.rastilion.ascenteval.SolarSysDb.Sys;

/**
 * A list fragment representing a list of Systems. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link SystemDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class SystemListFragment extends ListFragment {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;
    private static ArrayAdapter adapt;
    public static List<Sys> systems = null;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SystemListFragment() {
    }

    public static void searchList(String s) {
        try {
            systems = Sys.findWithQuery(Sys.class, "SELECT * FROM Sys WHERE Name LIKE ? ORDER BY LOWER(substr(Name,1,3)) ASC,LENGTH(Name),Name ASC", "%"+s+"%");
        }
        catch (Exception e){
            systems = Sys.findWithQuery(Sys.class, "SELECT * FROM Sys ORDER BY LOWER(substr(Name,1,3)),LENGTH(Name),Name ASC");
        }
        adapt.clear();
        adapt.addAll(systems);
        adapt.notifyDataSetChanged();
    }

    public static void refreshList() {
        adapt.clear();
        Collections.sort(systems,new Comparator<Sys>() {
            @Override
            public int compare(Sys lhs, Sys rhs) {
                return lhs.Name.compareToIgnoreCase(rhs.Name);
            }
        });
        adapt.addAll(systems);
        adapt.notifyDataSetChanged();
    }

    public static void updateList() {
        adapt.clear();
        systems = Sys.findWithQuery(Sys.class, "SELECT * FROM Sys ORDER BY LOWER(substr(Name,1,3)),LENGTH(Name),Name ASC");
        adapt.addAll(systems);
        adapt.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        systems = Sys.findWithQuery(Sys.class, "SELECT * FROM Sys ORDER BY LOWER(substr(Name,1,3)),LENGTH(Name),Name ASC");

        adapt = new ArrayAdapter<>(
                getActivity(),
                R.layout.centerlist,
                android.R.id.text1,
                systems);
        adapt.setNotifyOnChange(true);

        // TODO: replace with a real list adapter.
        setListAdapter(adapt);
        if (!systems.isEmpty()) refreshList();
        else updateList();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView lv = getListView();

        if (systems==null) updateList();
        else refreshList();


        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
        lv.setDivider(new ColorDrawable(Color.BLACK));
        lv.setDividerHeight(2);
        lv.setOnItemLongClickListener(
            new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                    final Dialog astroSelect = new Dialog(view.getContext());
                    LinearLayout mlv = new LinearLayout(view.getContext());
                    LinearLayout hl = new LinearLayout(view.getContext());
                    hl.setOrientation(LinearLayout.HORIZONTAL);
                    mlv.setOrientation(LinearLayout.VERTICAL);
                    final List<CheckBox> cbl = new ArrayList<>();
                    for (int i = 0; i < Constants.roidTypes.values().length; i++) {
                        CheckBox cb = new CheckBox(view.getContext());
                        cb.setText(Constants.roidTypes.values()[i].name());
                        cbl.add(cb);
                        mlv.addView(cb);
                    }
                    Button btn1 = new Button(view.getContext());
                    btn1.setTextSize(14);
                    btn1.setGravity(Gravity.CENTER);
                    btn1.setText(R.string.add);
                    btn1.setBackgroundColor(Color.DKGRAY);
                    btn1.setTextColor(Color.LTGRAY);
                    btn1.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                long astroMask = 0;
                                for (int c = 0; c < Constants.roidTypes.values().length; c++) {
                                    if (cbl.get(c).isChecked())
                                        astroMask += (int) Math.pow(2, c);
                                }
                                Sys s = Sys.find(Sys.class, "name = ?",parent.getItemAtPosition(position).toString()).get(0);
                                s.roidField = astroMask;
                                s.save();
                                SystemListFragment.refreshList();
                                astroSelect.dismiss();
                            }
                        }
                    );
                    Button btn2 = new Button(view.getContext());
                    btn2.setTextSize(14);
                    btn2.setGravity(Gravity.CENTER);
                    btn2.setText(R.string.cancel);
                    btn2.setBackgroundColor(Color.DKGRAY);
                    btn2.setTextColor(Color.LTGRAY);
                    btn2.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    astroSelect.cancel();
                                }
                            }
                    );
                    hl.addView(btn1);
                    hl.addView(btn2);
                    mlv.addView(hl);
                    astroSelect.setContentView(mlv);
                    astroSelect.setTitle("Asteroids: ");
                    astroSelect.setCancelable(true);

                    final Dialog ggSelect = new Dialog(view.getContext());
                    LinearLayout gglv = new LinearLayout(view.getContext());
                    LinearLayout gghl = new LinearLayout(view.getContext());
                    hl.setOrientation(LinearLayout.HORIZONTAL);
                    gglv.setOrientation(LinearLayout.VERTICAL);
                    final List<CheckBox> ggcbl = new ArrayList<>();
                    for (int i = 0; i < Constants.gas.values().length; i++) {
                        CheckBox cb = new CheckBox(view.getContext());
                        cb.setText(Constants.gas.values()[i].name());
                        ggcbl.add(cb);
                        gglv.addView(cb);
                    }
                    Button ggbtn1 = new Button(view.getContext());
                    ggbtn1.setTextSize(14);
                    ggbtn1.setGravity(Gravity.CENTER);
                    ggbtn1.setText(R.string.add);
                    ggbtn1.setBackgroundColor(Color.DKGRAY);
                    ggbtn1.setTextColor(Color.LTGRAY);
                    ggbtn1.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int ggMask = 0;
                                for (int c = 0; c < Constants.gas.values().length; c++) {
                                    if (ggcbl.get(c).isChecked())
                                        ggMask += (int) Math.pow(2, c);
                                }
                                Giants gg = new Giants(ggMask);
                                gg.system = Sys.find(Sys.class, "name=?",parent.getItemAtPosition(position).toString()).get(0);
                                gg.save();
                                SystemListFragment.refreshList();
                                ggSelect.dismiss();
                            }
                        }
                    );
                    Button ggbtn2 = new Button(view.getContext());
                    ggbtn2.setTextSize(14);
                    ggbtn2.setGravity(Gravity.CENTER);
                    ggbtn2.setText(R.string.cancel);
                    ggbtn2.setBackgroundColor(Color.DKGRAY);
                    ggbtn2.setTextColor(Color.LTGRAY);
                    ggbtn2.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ggSelect.cancel();
                                }
                            }
                    );
                    gghl.addView(ggbtn1);
                    gghl.addView(ggbtn2);
                    gglv.addView(gghl);
                    ggSelect.setContentView(gglv);
                    ggSelect.setTitle("Gasses: ");
                    ggSelect.setCancelable(true);

                    final Dialog menu = new Dialog(view.getContext());
                    menu.setTitle("Add what?");
                    Button af = new Button(view.getContext());
                    af.setTextSize(14);
                    af.setGravity(Gravity.CENTER);
                    af.setText(R.string.roids);
                    af.setBackgroundColor(Color.DKGRAY);
                    af.setTextColor(Color.LTGRAY);
                    af.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                menu.dismiss();
                                astroSelect.show();
                            }
                        }
                    );
                    Button gg = new Button(view.getContext());
                    gg.setTextSize(14);
                    gg.setGravity(Gravity.CENTER);
                    gg.setText(R.string.ggs);
                    gg.setBackgroundColor(Color.DKGRAY);
                    gg.setTextColor(Color.LTGRAY);
                    gg.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                menu.dismiss();
                                ggSelect.show();
                            }
                        }
                    );
                    Button cncl = new Button(view.getContext());
                    cncl.setTextSize(14);
                    cncl.setGravity(Gravity.CENTER);
                    cncl.setText(R.string.cancel);
                    cncl.setBackgroundColor(Color.DKGRAY);
                    cncl.setTextColor(Color.LTGRAY);
                    cncl.setOnClickListener(
                        new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    menu.dismiss();
                                }
                            }
                    );
                    LinearLayout ml = new LinearLayout(view.getContext());
                    ml.setOrientation(LinearLayout.VERTICAL);
                    ml.addView(af);
                    ml.addView(gg);
                    ml.addView(cncl);
                    menu.setContentView(ml);
                    menu.show();
                    return true;
                }
            }
        );
        refreshList();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        String name = listView.getItemAtPosition(position).toString();

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(Sys.find(Sys.class, "Name = ?", name).get(0).Name);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(
            activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE
        );
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
}
