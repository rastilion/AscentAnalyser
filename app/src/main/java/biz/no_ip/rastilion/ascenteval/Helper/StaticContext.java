package biz.no_ip.rastilion.ascenteval.Helper;

import android.app.Application;
import android.content.Context;

/**
 * Created by tgruetzmacher on 19.08.15.
 */
public class StaticContext extends Application {
    private static Context ctx;

    public static Context getCustomAppContext(){
        return ctx;
    }

    public static void setContext(Context context){
        ctx = context;
    }
}
