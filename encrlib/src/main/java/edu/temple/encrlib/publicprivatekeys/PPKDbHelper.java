package edu.temple.encrlib.publicprivatekeys;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import edu.temple.encrlib.utils.Constants;

/**
 * Database helper class to manage a set of public and private encryption keys
 */
public class PPKDbHelper extends SQLiteOpenHelper {

    private static final String DB_CREATE_KEY_TABLE =
            "CREATE TABLE " + PPKContract.Keys.TABLE_NAME +
                    "(" + PPKContract.Keys.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PPKContract.Keys.ALIAS + " TEXT NOT NULL, " +
                    PPKContract.Keys.PUBLIC + " TEXT NOT NULL, " +
                    PPKContract.Keys.PRIVATE + " TEXT NOT NULL);";

    private static final String DB_DELETE_KEY_TABLE =
            "DROP TABLE IF EXISTS " + PPKContract.Keys.TABLE_NAME;

    private static final String DB_CHECK_KEY_TABLE =
            "SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '" + PPKContract.Keys.TABLE_NAME + "'";

    /**
     * Default constructor for DB helper
     * @param context - the context with which to associate the DB helper
     */
    public PPKDbHelper(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    /**
     * Creates a new "key" table in the provided DB instance
     * @param database - the DB for which to create a "key" table
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        createKeyTable(database);
    }

    /**
     * Updates the "key" table in the associated DB instance
     * @param database - the DB for which to update the "key" table
     * @param oldVersion - the old version of the provided DB
     * @param newVersion - the new version of the provided DB
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        recreateKeyTable(database);
    }

    private static void createKeyTable(SQLiteDatabase database) {
        database.execSQL(DB_CREATE_KEY_TABLE);
        Log.d(Constants.LOG_TAG, "Created key table with query: \n" + DB_CREATE_KEY_TABLE);
    }

    private static void deleteKeyTable(SQLiteDatabase database) {
        database.execSQL(DB_DELETE_KEY_TABLE);
        Log.d(Constants.LOG_TAG, "Deleted key table with query: \n" + DB_DELETE_KEY_TABLE);
    }

    public static void recreateKeyTable(SQLiteDatabase database) {
        deleteKeyTable(database);
        createKeyTable(database);
    }

}