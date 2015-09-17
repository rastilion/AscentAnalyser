package biz.no_ip.rastilion.ascenteval.dummy;

import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import biz.no_ip.rastilion.ascenteval.SolarSys.Planet;
import biz.no_ip.rastilion.ascenteval.SolarSys.Sys;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    public static ListAdapter adapt;

    static {
        // a sample item.
        Sys dummy = new Sys("Apollo");
        dummy.addPlanet(new Planet("Dummy Planet"));
        addItem(new DummyItem(dummy.getName(), dummy));
    }

    // add an item to the list
    public static void addItem(DummyItem item) {
        if(!ITEM_MAP.containsKey(item.id)) {
            ITEMS.add(item);
            ITEM_MAP.put(item.id, item);
        }
    }

    // reset list
    public static void resetMap(){
        ITEMS.clear();
        ITEM_MAP.clear();
    }

    // return all items in the list as new list
    public static List<Sys> getAllSystems(){
        List<Sys> systemList = new ArrayList<>();
        for (int i=0; i < ITEMS.size()-1;i++){
            systemList.add(ITEMS.get(i).content);
        }
        return systemList;
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public String id;
        public Sys content;

        public DummyItem(String id, Sys content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content.getName();
        }
    }
}
