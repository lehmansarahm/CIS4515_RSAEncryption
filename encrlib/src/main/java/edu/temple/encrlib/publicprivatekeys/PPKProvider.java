package edu.temple.encrlib.publicprivatekeys;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;

import edu.temple.encrlib.encryption.RsaEncryptor;
import edu.temple.encrlib.utils.Constants;

/**
 * Content provider to wrap application interactions with the public/private key database
 */
public class PPKProvider extends ContentProvider {

    private static HashMap<String, String> KEY_PROJ_MAP;
    private SQLiteDatabase db;

    // --------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------

    private static final int KEY_TABLE = 99999;
    private static final int KEY_LIST = 1;
    private static final int KEY_ID = 2;
    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(PPKContract.AUTHORITY, null, KEY_TABLE);
        URI_MATCHER.addURI(PPKContract.AUTHORITY, PPKContract.Keys.TABLE_NAME, KEY_LIST);
        URI_MATCHER.addURI(PPKContract.AUTHORITY, PPKContract.Keys.TABLE_NAME + "/#", KEY_ID);
    }

    // --------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------

    /**
     * Instantiate the provider data source from the associated DB helper
     * @return boolean flag indicating whether data source initialization was successful
     */
    @Override
    public boolean onCreate() {
        Context context = getContext();
        PPKDbHelper dbHelper = new PPKDbHelper(context);
        db = dbHelper.getWritableDatabase();
        return ((db == null) ? false : true);
    }

    /**
     * Insert a new record into the DB "key" table with the associated values
     * @param uri - URI of the DB into which to insert the new key pair
     * @param values - properties of the new key pair to insert
     * @return updated URI of the new key pair record
     * @throws SQLException if DB insertion operation fails
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // attempt to insert new record with new public / private key values
        long newKeyID;
        try {
            newKeyID = db.insert(PPKContract.Keys.TABLE_NAME, "", generateKeyPair(values));
        } catch (NoSuchAlgorithmException ex) {
            throw new SQLException("ALGORITHM ERROR.  Could not generate public private keys for URI: " + uri);
        } catch (NoSuchProviderException ex) {
            throw new SQLException("PROVIDER ERROR.  Could not generate public private keys for URI: " + uri);
        }

        // check to see if record insertion completed successfully
        if (newKeyID > 0) {
            Uri newUri = ContentUris.withAppendedId(PPKContract.Keys.CONTENT_URI, newKeyID);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }

        // if we get here, it means the DB insertion failed ... throw exception!
        else throw new SQLException("DB record insertion into URI: " + uri + " failed!");
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(PPKContract.Keys.TABLE_NAME);

        switch (URI_MATCHER.match(uri)) {
            case KEY_LIST:
                qb.setProjectionMap(KEY_PROJ_MAP);
                Log.d(Constants.LOG_TAG, "Getting PPK list for query URI: " + uri);
                break;
            case KEY_ID:
                String id = uri.getPathSegments().get(1);
                qb.appendWhere(String.format("%s=%s", PPKContract.Keys._ID, id));
                Log.d(Constants.LOG_TAG, "Getting single PPK with ID: " + id + ", for query URI: " + uri);
                break;
            default:
                Log.d(Constants.LOG_TAG, "Could not match URI: " + uri);
                return null;
        }

        if (sortOrder == null || sortOrder == "")
            sortOrder = PPKContract.Keys.SORT_ORDER_DEFAULT;

        Log.d(Constants.LOG_TAG, "Preparing to perform DB query with: "
            + "\n... projection: " + join(projection)
            + "\n... selection: " + selection
            + "\n... selection args: " + join(selectionArgs)
            + "\n... sort order: " + sortOrder);

        Cursor c = qb.query(db,	projection,	selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;
        switch (URI_MATCHER.match(uri)) {
            case KEY_LIST:
                count = db.delete(PPKContract.Keys.TABLE_NAME, selection, selectionArgs);
                break;
            case KEY_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(PPKContract.Keys.TABLE_NAME, String.format("%s=%s", PPKContract.Keys._ID, id)
                    + (!TextUtils.isEmpty(selection) ? String.format(" AND (%s)", selection) : ""), selectionArgs);
                break;
            case KEY_TABLE:
                PPKDbHelper.recreateKeyTable(db);
                count = 0;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;
        switch (URI_MATCHER.match(uri)) {
            case KEY_LIST:
                count = db.update(PPKContract.Keys.TABLE_NAME, values, selection, selectionArgs);
                break;
            case KEY_ID:
                count = db.update(PPKContract.Keys.TABLE_NAME, values,
                    String.format("%s=%s", PPKContract.Keys._ID, uri.getPathSegments().get(1))
                    + (!TextUtils.isEmpty(selection) ? String.format(" AND (%s)", selection) : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case KEY_LIST:
                return PPKContract.Keys.CONTENT_LIST_TYPE;
            case KEY_ID:
                return PPKContract.Keys.CONTENT_ITEM_TYPE;
            default:
                return null;
        }
    }

    private ContentValues generateKeyPair(ContentValues values) throws NoSuchAlgorithmException, NoSuchProviderException {
        String publicKey = "", privateKey = "";
        switch (PPKContract.DEFAULT_ENCRYPTION_SCHEME) {
            case RSA:
                KeyPair newKeyPair = RsaEncryptor.generateKeyPair();
                byte[] publicKeyBytes = Base64.encode(newKeyPair.getPublic().getEncoded(),0);
                byte[] privateKeyBytes = Base64.encode(newKeyPair.getPrivate().getEncoded(),0);
                publicKey = new String(publicKeyBytes);
                privateKey = new String(privateKeyBytes);
                break;
            case SHA256:
                // TODO - implement additional encryption methods
            default:
                break;
        }

        ContentValues newValues = new ContentValues();
        newValues.put(PPKContract.Keys.ALIAS, values.getAsString(PPKContract.Keys.ALIAS));
        newValues.put(PPKContract.Keys.PUBLIC, publicKey);
        newValues.put(PPKContract.Keys.PRIVATE, privateKey);
        return newValues;
    }

    private String join(String[] array) {
        if (array == null) return "null";
        String out = "";
        for (int i = 0; i < array.length; i++) {
            out += array[i];
            if (i < array.length - 1) out += ", ";
        }
        return out;
    }

}