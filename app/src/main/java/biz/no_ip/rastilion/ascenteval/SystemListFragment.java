package biz.no_ip.rastilion.ascenteval;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Layout;
import android.text.TextPaint;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import biz.no_ip.rastilion.ascenteval.SolarSys.Sys;
import biz.no_ip.rastilion.ascenteval.dummy.DummyContent;

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

    public static void refreshList(){
        adapt.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapt = new ArrayAdapter<>(
                getActivity(),
                R.layout.centerlist,
                android.R.id.text1,
                DummyContent.ITEMS);
        adapt.setNotifyOnChange(true);

        DummyContent.adapt=adapt;
        // TODO: replace with a real list adapter.
        setListAdapter(adapt);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView lv =getListView();

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
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    final Dialog astroSelect = new Dialog(view.getContext());
                    LinearLayout mlv = new LinearLayout(view.getContext());
                    mlv.setOrientation(LinearLayout.VERTICAL);
                    final List<CheckBox> cbl = new ArrayList<CheckBox>();
                    for (int i = 0; i < Sys.roidTypes.values().length; i++) {
                        CheckBox cb = new CheckBox(view.getContext());
                        cb.setText(Sys.roidTypes.values()[i].name());
                        cbl.add(cb);
                        mlv.addView(cb);
                    }
                    Button btn = new Button(view.getContext());
                    btn.setTextSize(14);
                    btn.setGravity(Gravity.CENTER);
                    btn.setText("Add");
                    btn.setBackgroundColor(Color.DKGRAY);
                    btn.setTextColor(Color.LTGRAY);
                    btn.setTextAppearance(view.getContext(),R.style.BlackFont);
                    btn.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    long astroMask = 0;
                                    for (int c = 0; c < Sys.roidTypes.values().length; c++) {
                                        if (cbl.get(c).isChecked())
                                            astroMask += (int) Math.pow(2, c);
                                    }
                                    DummyContent.ITEMS.get(position).content.setRoidField(BitSet.valueOf(new long[]{astroMask}));
                                    DummyContent.saveItems();
                                    SystemListFragment.refreshList();
                                    astroSelect.dismiss();
                                }
                            }
                    );
                    mlv.addView(btn);
                    astroSelect.setContentView(mlv);
                    astroSelect.setCancelable(true);
                    astroSelect.create();
                    astroSelect.show();
                    return true;
                }
            }
        );
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

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
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

    public void updateList(){
        adapt.notifyDataSetChanged();
    }
}
