package biz.no_ip.rastilion.ascenteval.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import biz.no_ip.rastilion.ascenteval.SolarSys.Composition;
import biz.no_ip.rastilion.ascenteval.SolarSys.Planet;
import biz.no_ip.rastilion.ascenteval.SolarSys.Sys;

/**
 * Created by tgruetzmacher on 13.08.15.
 */
public class SQLHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Systems";

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_SYSTEM_TABLE = "CREATE TABLE systems ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name TEXT, ";

        // SQL statement to create book table
        String CREATE_PLANET_TABLE = "CREATE TABLE planets ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "System_id INTEGER, "+
                "Name TEXT, "+
                "Atmosphere TINYINT, "+
                "FOREIGN KEY FK_planets_System (System_id) REFERENCES systems (id) "+
                ")";

        String CREATE_MATERIAL_TABLE = "CREATE TABLE mats ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name TEXT, ";

        String CREATE_COMPOSITION_TABLE = "CREATE TABLE composition ( " +
                "planet_id INTEGER, " +
                "mat_id INTEGER, " +
                "amount NUMERIC(15,14), "+
                "FOREIGN KEY FK_planet (planet_id) REFERENCES planets (id) "+
                "FOREIGN KEY FK_composition (mat_id) REFERENCES mats (id) ";

        // create Systems info tables
        db.execSQL(CREATE_SYSTEM_TABLE);
        db.execSQL(CREATE_PLANET_TABLE);
        db.execSQL(CREATE_MATERIAL_TABLE);
        db.execSQL(CREATE_COMPOSITION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older systems tables if existed
        db.execSQL("DROP TABLE IF EXISTS systems");
        db.execSQL("DROP TABLE IF EXISTS planets");
        db.execSQL("DROP TABLE IF EXISTS mats");
        db.execSQL("DROP TABLE IF EXISTS composition");

        // create fresh systems tables
        this.onCreate(db);
    }/**
     * CRUD operations (create "add", read "get", update, delete) book + get all books + delete all books
     */

    // Books table name
    private static final String TABLE_SYSTEMS = "systems";
    private static final String TABLE_PLANETS = "planets";
    private static final String TABLE_MATERIALS = "mats";
    private static final String TABLE_COMPOSITION = "composition";

    // Books Table Columns names
    private static final String KEY_SYSTEM_ID = "id";
    private static final String KEY_SYSTEM_NAME = "Name";
    private static final String KEY_PLANET_ID = "id";
    private static final String KEY_PLANET_SYSTEM = "System_id";
    private static final String KEY_PLANET_NAME = "Name";
    private static final String KEY_PLANET_ATMOSPHERE = "Atmosphere";
    private static final String KEY_MATERIAL_NAME = "Name";
    private static final String KEY_MATERIAL_ID = "id";
    private static final String KEY_COMPOSITION_PLANET = "planet_id";
    private static final String KEY_COMPOSITION_MATERIAL = "mat_id";
    private static final String KEY_COMPOSITION_AMOUNT = "value";

    private static final String[] SYSTEM_COLUMNS = {KEY_SYSTEM_ID,KEY_SYSTEM_NAME};
    private static final String[] PLANET_COLUMNS = {KEY_PLANET_ID,KEY_PLANET_SYSTEM,KEY_PLANET_NAME,KEY_PLANET_ATMOSPHERE};
    private static final String[] MATERIAL_COLUMNS = {KEY_MATERIAL_ID,KEY_MATERIAL_NAME};
    private static final String[] COMPOSITION_COLUMNS = {KEY_COMPOSITION_PLANET,KEY_COMPOSITION_MATERIAL,KEY_COMPOSITION_AMOUNT};

    public void addSystem(Sys sys){
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_SYSTEM_NAME, sys.getName()); // get title

        // 3. insert
        db.insert(TABLE_SYSTEMS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
        for (Planet p : sys.getPlanets()){
            addPlanets(p);
        }
    }

    public void addPlanets(Planet planet){
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_PLANET_SYSTEM, planet.getSystem()); // get system id
        values.put(KEY_PLANET_NAME, planet.getName()); // get title

        // 3. insert
        db.insert(TABLE_PLANETS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
        addComposition(planet.getComposition());

    }

    public void addMaterials(){
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_MATERIAL_NAME, "Aluminium");
        values.put(KEY_MATERIAL_NAME, "Carbon");
        values.put(KEY_MATERIAL_NAME, "Iron");
        values.put(KEY_MATERIAL_NAME, "Silicone");
        values.put(KEY_MATERIAL_NAME, "Titanium");
        values.put(KEY_MATERIAL_NAME, "Grain");
        values.put(KEY_MATERIAL_NAME, "Fruit");
        values.put(KEY_MATERIAL_NAME, "Vegetables");
        values.put(KEY_MATERIAL_NAME, "Meat");
        values.put(KEY_MATERIAL_NAME, "Tobacco");
        values.put(KEY_MATERIAL_NAME, "Gems");

        // 3. insert
        db.insert(TABLE_MATERIALS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public void addComposition(int pid, Composition composition){
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_COMPOSITION_PLANET, pid); // get system id
        //values.put(KEY_PLANET_NAME, planet.getName()); // get title

        // 3. insert
        db.insert(TABLE_PLANETS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public int getPlanetId(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SYSTEMS, new String[]{ "System_id"}, "name = "+name, null, null, null, null);
        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();
        int systemId = Integer.parseInt(cursor.getString(0));
        return systemId;
    }
/*
    public Book getBook(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_BOOKS, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        Book book = new Book();
        book.setId(Integer.parseInt(cursor.getString(0)));
        book.setTitle(cursor.getString(1));
        book.setAuthor(cursor.getString(2));

        Log.d("getBook("+id+")", book.toString());

        // 5. return book
        return book;
    }

    // Get All Books
    public List<Book> getAllBooks() {
        List<Book> books = new LinkedList<Book>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_BOOKS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Book book = null;
        if (cursor.moveToFirst()) {
            do {
                book = new Book();
                book.setId(Integer.parseInt(cursor.getString(0)));
                book.setTitle(cursor.getString(1));
                book.setAuthor(cursor.getString(2));

                // Add book to books
                books.add(book);
            } while (cursor.moveToNext());
        }

        Log.d("getAllBooks()", books.toString());

        // return books
        return books;
    }

    // Updating single book
    public int updateBook(Book book) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("title", book.getTitle()); // get title
        values.put("author", book.getAuthor()); // get author

        // 3. updating row
        int i = db.update(TABLE_BOOKS, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(book.getId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    // Deleting single book
    public void deleteBook(Book book) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_BOOKS,
                KEY_ID + " = ?",
                new String[]{String.valueOf(book.getId())});

        // 3. close
        db.close();

        Log.d("deleteBook", book.toString());

    }*/
}

