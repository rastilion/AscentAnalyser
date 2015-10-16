package biz.no_ip.rastilion.ascenteval.Helper;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by tgruetzmacher on 19.08.15.
 * Filewriter for internal use
 */
public class FileManipulator{
    private static String FILENAME="SysStore";
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;

    // get a writing stream to the data directory
    public static ObjectOutputStream getWriteStream(Context ctx){
        try {
            FileOutputStream fos = ctx.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
        }
        catch(Exception e){
            Log.i("FNF","File not found for writing");
        }
        return oos;
    }
    // get a reading stream from the data directory
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
