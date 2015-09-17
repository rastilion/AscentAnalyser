package biz.no_ip.rastilion.ascenteval;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import biz.no_ip.rastilion.ascenteval.DumpImporter.DumpImport;
import biz.no_ip.rastilion.ascenteval.SolarSys.Sys;
import biz.no_ip.rastilion.ascenteval.dummy.DummyContent;
import biz.no_ip.rastilion.ascenteval.Helper.FileDialog;
import biz.no_ip.rastilion.ascenteval.Helper.FileManipulator;
import biz.no_ip.rastilion.ascenteval.Helper.SelectionMode;
import biz.no_ip.rastilion.ascenteval.Helper.StaticContext;


/**
 * An activity representing a list of Systems. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link SystemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link SystemListFragment} and the item details
 * (if present) is a {@link SystemDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link SystemListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class SystemListActivity extends FragmentActivity
        implements SystemListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    public final int REQUEST_LOAD=0;
    public File toImport=null;
    List<Sys> systems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_list);
        StaticContext.setContext(getApplicationContext());
        List<Sys> sysImport = new ArrayList<>();
        // load saved data at app start
        try{
            sysImport = (ArrayList) FileManipulator.getReadStream(getApplicationContext()).readObject();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if (sysImport.size()>0){
            DummyContent.resetMap();
            for (int i =0 ; i < sysImport.size();i++){
                DummyContent.addItem(new DummyContent.DummyItem(sysImport.get(i).getName(),sysImport.get(i)));
            }
        }
        Collections.sort(DummyContent.ITEMS, new Comparator<DummyContent.DummyItem>() {
            @Override
            public int compare(DummyContent.DummyItem lhs, DummyContent.DummyItem rhs) {
                return lhs.content.getName().compareToIgnoreCase(rhs.content.getName());
            }
        });
        if (findViewById(R.id.system_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((SystemListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.system_list))
                    .setActivateOnItemClick(true);
        }

    }

    /**
     * Callback method from {@link SystemListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(SystemDetailFragment.ARG_ITEM_ID, id);
            SystemDetailFragment fragment = new SystemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.system_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, SystemDetailActivity.class);
            detailIntent.putExtra(SystemDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    public synchronized void onActivityResult(final int requestCode,
                                               int resultCode, final Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_LOAD) {
                String filePath = data.getStringExtra(FileDialog.RESULT_PATH);
                toImport = new File(filePath);
                systems = DumpImport.parseFile(toImport);
                // Reset itemlist if dummyitem present
                if (DummyContent.ITEMS.get(0).content.getName().equalsIgnoreCase("Apollo")) DummyContent.resetMap();
                for (int i =0 ; i < systems.size();i++){
                    DummyContent.addItem(new DummyContent.DummyItem(systems.get(i).getName(),systems.get(i)));
                }
                Collections.sort(DummyContent.ITEMS, new Comparator<DummyContent.DummyItem>() {
                    @Override
                    public int compare(DummyContent.DummyItem lhs, DummyContent.DummyItem rhs) {
                        return lhs.content.getName().compareToIgnoreCase(rhs.content.getName());
                    }
                });
                SystemListFragment.refreshList();
            }

        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getBaseContext(), "No file loaded", Toast.LENGTH_LONG).show();
        }
    }

    public void selectSystem(View view){
        Intent intent = new Intent(getApplicationContext(), FileDialog.class);
        intent.putExtra(FileDialog.START_PATH, Environment.getExternalStorageDirectory().getPath());

        //can user select directories or not
        intent.putExtra(FileDialog.CAN_SELECT_DIR, false);

        //alternatively you can set file filter
        intent.putExtra(FileDialog.FORMAT_FILTER, new String[]{"txt"});

        //deactivates the "new file" button
        intent.putExtra(FileDialog.SELECTION_MODE, SelectionMode.MODE_OPEN);

        startActivityForResult(intent, REQUEST_LOAD);

    }
}
