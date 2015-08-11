package biz.no_ip.rastilion.ascenteval;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


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
    public final int REQUEST_SAVE=1, REQUEST_LOAD=0;
    public File toImport=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_list);

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

        // TODO: If exposing deep links into your app, handle intents here.
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

        List<String> planets = new ArrayList<String>();
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_SAVE) {
                System.out.println("Saving...");
            } else if (requestCode == REQUEST_LOAD) {
                System.out.println("Loading...");
                String filePath = data.getStringExtra(FileDialog.RESULT_PATH);
                toImport = new File(filePath);
            }

        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getBaseContext(),"No file loaded",Toast.LENGTH_LONG);
        }
        if (toImport != null) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(toImport));
                String line;
                while ((line = br.readLine()) != null) {
                    planets = Arrays.asList(line.split(";"));
                }
                br.close();
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
            Toast.makeText(getBaseContext(), planets.toString(), Toast.LENGTH_LONG);
        }
    }

    public void selectSystem(View view){
        System.out.println("Click");
        Intent intent = new Intent(getBaseContext(), FileDialog.class);
        intent.putExtra(FileDialog.START_PATH, "/sdcard1");

        //can user select directories or not
        intent.putExtra(FileDialog.CAN_SELECT_DIR, false);

        //alternatively you can set file filter
        intent.putExtra(FileDialog.FORMAT_FILTER, new String[]{"txt"});

        //deactivates the "new file" button
        intent.putExtra(FileDialog.SELECTION_MODE, SelectionMode.MODE_OPEN);

        startActivityForResult(intent, REQUEST_LOAD);

    }
}
