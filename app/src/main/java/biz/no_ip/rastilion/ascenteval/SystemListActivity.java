package biz.no_ip.rastilion.ascenteval;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import biz.no_ip.rastilion.ascenteval.DumpImporter.DumpImport;
import biz.no_ip.rastilion.ascenteval.SolarSysDb.Giants;
import biz.no_ip.rastilion.ascenteval.SolarSysDb.Planet;
import biz.no_ip.rastilion.ascenteval.SolarSysDb.Sys;
import biz.no_ip.rastilion.ascenteval.Helper.FileDialog;
import biz.no_ip.rastilion.ascenteval.Helper.SelectionMode;


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
    private static EditText sfield;
    public static Context ctx;
    public static ActionProcessButton pBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_list);
        ctx = getApplicationContext();
        pBtn = (ActionProcessButton)findViewById(R.id.addSys);
        List<Planet> sysImport = new ArrayList<>();
        // load saved data at app start
        try{
            sysImport = Planet.listAll(Planet.class);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Collections.sort(sysImport, new Comparator<Planet>() {
            @Override
            public int compare(Planet lhs, Planet rhs) {
                return lhs.name.compareToIgnoreCase(rhs.name);
            }
        });
        try {
            sfield = (EditText) findViewById(R.id.searchInput);
        }
        catch (Exception e){
            Log.e("Crap","Fuck this shit!");
        }

        sfield.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                SystemListFragment.searchList(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                          int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
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
            final SystemListFragment list = ((SystemListFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.system_list)
            );
            list.setActivateOnItemClick(true);
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
                new DumpImport.ImportFilesTask().execute(toImport);
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
    public void clear(View v){
        Sys.deleteAll(Sys.class);
        Planet.deleteAll(Planet.class);
        Giants.deleteAll(Giants.class);
        SystemListFragment.refreshList();
    }
}
