package at.pria.osiris.osiris.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import at.pria.osiris.osiris.controllers.ControllerType;
import at.pria.osiris.osiris.view.elements.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to save a Profile Object in the SQL-lite database on the android phone.
 *
 * Created by helmuthbrunner on 10/02/15.
 */
public class DataBaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contactsManager";
    private static final String TABLE_PROFILES = "profiles";

    // Columns
    public static final String _ID = "_id"; // the _id variable must be named as _ID
    public static final String KEY_HOST = "host";
    public static final String KEY_PORT= "port";
    public static final String KEY_TYPE= "controllertype";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("Osiris", "In database Handler");
    }

    /**
     * Method to create the database tables
     *
     * Is only call if the database doesn't exist.
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String create_query=
                "CREATE TABLE " + TABLE_PROFILES + " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        KEY_HOST + " TEXT, " +
                        KEY_PORT + " INTEGER, " +
                        KEY_TYPE + " TEXT" +
                        ")"
                ;
        Log.d("Osiris", create_query);
        db.execSQL(create_query);

    }

    /**
     * Has no functionality //TODO implementate the functionality
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop all tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);

        // Create tables again
        onCreate(db);
    }

    /**
     * Method to add a new profile to database
     * @param profile the new profile
     */
    public void addProfile(Profile profile) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_HOST, profile.getHost());
        values.put(KEY_PORT, profile.getPort());
        values.put(KEY_TYPE, profile.getType().toString());

        db.insert(TABLE_PROFILES, null, values);
        db.close();
    }

    public Cursor fetchAllProfiles() {
        return this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_PROFILES, null);
    }

    /**
     * Method to get all Proile objects in the Database
     * @return the objects as a collection
     */
    public List<Profile> getAll() {

        List<Profile> profileList = new ArrayList<Profile>();

        String selectQuery = "SELECT * FROM " + TABLE_PROFILES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        ControllerType type= null;
        Profile profile;

        if (cursor.moveToFirst()) {
            do {

                if(cursor.getString(3).equals(ControllerType.Botball.toString())) {
                    type= ControllerType.Botball;
                }
                if(cursor.getString(3).equals(ControllerType.Hedgehog.toString())) {
                    type= ControllerType.Hedgehog;
                }

                profile= new Profile(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), type);

                profileList.add(profile);

            } while (cursor.moveToNext());
        }

        return profileList;
    }

    /**
     * Method to read from the database
     * @param id
     * @return the profile with this id
     */
    public Profile getProfile(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        ControllerType type= null;

        Cursor c= db.query(
                TABLE_PROFILES,
                new String[] {_ID, KEY_HOST, KEY_PORT, KEY_TYPE},
                _ID + "=?",
                new String[] { String.valueOf(id) },
                null, null, null, null );

        if (c != null)
            c.moveToFirst();

        if(c.getString(3).equals(ControllerType.Botball.toString())) {
            type= ControllerType.Botball;
        }
        if(c.getString(3).equals(ControllerType.Hedgehog.toString())) {
            type= ControllerType.Hedgehog;
        }

        Profile profile= new Profile(c.getInt(0), c.getString(1), c.getInt(2), type);

        c.close();
        db.close();

        return profile;
    }
}
