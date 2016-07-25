package biz.no_ip.rastilion.ascenteval.DumpImporter;

import android.app.Application;
import android.os.AsyncTask;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import biz.no_ip.rastilion.ascenteval.SolarSysDb.Planet;
import biz.no_ip.rastilion.ascenteval.SolarSysDb.Sys;
import biz.no_ip.rastilion.ascenteval.SystemListActivity;
import biz.no_ip.rastilion.ascenteval.SystemListFragment;

/**
 * Created by tgruetzmacher on 03.08.15.
 * Import class for soilsampledump-<id>.txt files
 */
public class DumpImport extends Application {

    /**
     *
     * @param  inFile The file to parse
     */
    public static void parseFile(File inFile) {
        List<String> result = new ArrayList<>();
        List<String> planets = new ArrayList<>();
        ArrayList<ArrayList<String>> system = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(inFile));
            String line;
            while ((line = br.readLine()) != null) {
                // split dump into separate entries for each planet
                planets = Arrays.asList(line.split(";"));
            }
            br.close();
            // remove non-letters and non-numbers
            planets.set(0,planets.get(0).replaceAll("[^ -~]", ""));
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        for (String line : planets) {
            // split to system/planet and stats
            result.addAll(Arrays.asList(line.split("\\-")));
        }
        for (int i = 0; i < result.size(); i++) {
            if (i % 2 == 0) {
                // name = last index of system name
                // sysinfo = first index of planet and stats
                int name = 0, sysinfo = 0;
                String sysName;
                String sysComp;
                // Try to find the system name by planet
                if (result.get(i).contains("Rocky Planet")){
                    name = result.get(i).indexOf(" Rocky Planet");
                    sysinfo = result.get(i).indexOf(" Rocky Planet")+1;
                }
                else if (result.get(i).contains("Moon")){
                    name = result.get(i).indexOf(" Moon");
                    sysinfo = result.get(i).indexOf(" Moon")+1;
                }
                else if (result.get(i).split(" ").length>2){
                    // Try to find the system name by name comparison
                    if (result.size()>=3){
                        boolean found=false;
                        for (int j = 0; j < result.get(i).length(); j++){
                            if ((i-2)>=0 && !found && result.get(i).charAt(0)==result.get(i-2).charAt(0)){
                                if ((result.get(i).charAt(j) != result.get(i-2).charAt(j))) {
                                    name = j-1;
                                    sysinfo = j;
                                    found = true;
                                }
                            }
                            else if ((i+2)<result.size() && !found && result.get(i).charAt(0)==result.get(i+2).charAt(0)){
                                if ((result.get(i).charAt(j) != result.get(i+2).charAt(j))) {
                                    name = j-1;
                                    sysinfo = j;
                                    found = true;
                                }
                            }
                            else {
                                String systemName = result.get(i);
                                ArrayList<Integer> positions = new ArrayList<>();
                                for (int c=0; c < systemName.length(); c++){
                                    if (systemName.charAt(c) == ' '){
                                        positions.add(c);
                                    }
                                }
                                name = positions.get(positions.size()/2);
                                sysinfo = name + 1;
                            }
                        }
                    }
                }
                else {
                    name = result.get(i).indexOf(" ");
                    sysinfo = name+1;
                }
                sysName =result.get(i).substring(0, name);
                sysComp = result.get(i).substring(sysinfo);
                system.add(new ArrayList<>(Arrays.asList(sysName,sysComp)));
            }
            else {
                system.get(system.size() - 1).addAll(Arrays.asList(result.get(i).split(" |\\|")));
                system.get(system.size() - 1).remove(2);
            }
        }
        //Try loading system info if named planets distort
        //name parsing
        for (int i = 0; i < system.size(); i++){
            if (system.get(i).get(0).equals("")){
                try {
                    if (result.get(i).equals(system.get(i + 1).get(0))) {
                        system.get(i).set(0, result.get(i));
                    } else if (result.get(i).equals(system.get(i - 1).get(0))) {
                        system.get(i).set(0, result.get(i));
                    }
                }
                catch (Exception e){
                    system.get(i).set(0,system.get(i).get(1).substring(0,system.get(i).get(1).indexOf(" ",system.get(i).get(1).indexOf(" ")+1)));
                    system.get(i).set(1,system.get(i).get(1).substring(system.get(i).get(1).indexOf(" ",system.get(i).get(1).indexOf(" ")+1)+1));
                }
            }
        }
        BuildSys(system);
    }

    private static void BuildSys(ArrayList<ArrayList<String>> sys) {
        Sys s;
        Planet p;
        //Look for system in existing data
        for (ArrayList<String> l : sys) {
            String sysName = l.get(0);
            String pName = l.get(1);
            boolean pfound = false;
            List<Sys> systems = Sys.find(Sys.class,"name=?",sysName);
            s = new Sys(sysName);
            List<Planet> found = new ArrayList<>();
            if (systems.size()>0) {
                s = systems.get(0);
                found = Planet.find(Planet.class,"name = ? AND system = ?", pName,systems.get(0).getId().toString());
                if (found.size()>0) {
                    pfound = true;
                }
            }
            //Build new planet if system or planet not found
            if(!pfound) {
                p = new Planet(pName);
                // Preset the later added stats to "Unknown"
                p.setTob(3);
                p.setGems(2);
                p.setAtmo(2);
                switch (l.size()){
                    case 21:
                        //Set mats for new dumps
                        p.setGems(Integer.parseInt(l.get(19)));
                        p.setAtmo(Integer.parseInt(l.get(20)));
                    case 19:
                        // Set mats for pre-gem dumps
                        p.setTob(Integer.parseInt(l.get(18)));
                    case 18:
                        // Set default mats
                        p.setAl(Float.parseFloat(l.get(3)));
                        p.setSi(Float.parseFloat(l.get(5)));
                        p.setGeo(Integer.parseInt(l.get(7)));
                        p.setCarb(Float.parseFloat(l.get(9)));
                        p.setFe(Float.parseFloat(l.get(11)));
                        p.setTi(Float.parseFloat(l.get(13)));
                        p.setGrain(Integer.parseInt(l.get(14)));
                        p.setFruit(Integer.parseInt(l.get(15)));
                        p.setVeg(Integer.parseInt(l.get(16)));
                        p.setMeat(Integer.parseInt(l.get(17)));
                        break;
                }
                //Set System's planets
                p.system=s;
                s.save();
                p.save();
            }
            else {
                Planet pl = found.get(0);
                if(pl.getAtmo() == 2){
                    if (l.size() == 21){
                        pl.setGems(Integer.parseInt(l.get(19)));
                        pl.setAtmo(Integer.parseInt(l.get(20)));
                        if (pl.getTob() == 3){
                            pl.setTob(Integer.parseInt(l.get(18)));
                        }
                        pl.save();
                    }
                }
            }
        }
    }


    public static class ImportFilesTask extends AsyncTask<File, String, String> {
        protected String name;
        protected String doInBackground(File... inFile) {
            String retVal;
            try {
                name=inFile[0].getName();
                parseFile(inFile[0]);
                retVal = "OK";
            }
            catch (Exception e){

                retVal = e.getMessage();
            }
            return retVal;
        }

        @Override
        protected void onPreExecute() {
            SystemListActivity.pBtn.setMode(ActionProcessButton.Mode.ENDLESS);
            SystemListActivity.pBtn.setProgress(1);
            SystemListActivity.pBtn.setEnabled(false);
        }

        protected void onPostExecute(String result) {
            SystemListActivity.pBtn.setMode(ActionProcessButton.Mode.PROGRESS);
            SystemListActivity.pBtn.setProgress(100);
            SystemListActivity.pBtn.setEnabled(true);
            Toast.makeText(SystemListActivity.ctx,"Parsed file: "+name, Toast.LENGTH_SHORT).show();
            SystemListFragment.refreshList();
        }
    }
}

