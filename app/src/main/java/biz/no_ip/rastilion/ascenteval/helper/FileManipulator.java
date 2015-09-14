package biz.no_ip.rastilion.ascenteval.helper;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by tgruetzmacher on 19.08.15.
 * Filewriter for internal use
 */
public class FileManipulator extends Application{
    private static String FILENAME="SysStore";
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;

    public static ObjectOutputStream getWriteStream(Context ctx){
        try {
            FileOutputStream fos = ctx.openFileOutput(FILENAME, MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
        }
        catch(Exception e){
            Log.i("FNF","File not found for writing");
        }
        return oos;
    }
    public static ObjectInputStream getReadStream(Context ctx){
        try {
            FileInputStream fis = ctx.openFileInput(FILENAME);
            ois = new ObjectInputStream(fis);
        }
        catch(Exception e){
            Log.i("FNF", "File not found for reading");
        }
        return ois;
    }
}