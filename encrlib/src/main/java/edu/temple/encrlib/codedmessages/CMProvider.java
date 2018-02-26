package edu.temple.encrlib.codedmessages;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

public class CMProvider extends ContentProvider {

    private static final String PREF_FILE_NAME = "CIS4515_Encryption_CMProvider_Prefs";
    private static final String ENCODED_MESSAGE = "encoded_message";

    @Override
    public boolean onCreate() { return false; }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SharedPreferences sharedPrefs = getContext().getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(ENCODED_MESSAGE, values.getAsString(CMContract.Messages.INPUT_MESSAGE));
        editor.commit();
        return uri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SharedPreferences sharedPrefs = getContext().getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        MatrixCursor cursor = new MatrixCursor(new String[] { CMContract.Messages.OUTPUT_MESSAGE });
        cursor.addRow(new String[] { sharedPrefs.getString(ENCODED_MESSAGE, "") });
        getContext().getContentResolver().notifyChange(CMContract.Messages.CONTENT_URI, null);
        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return "";
    }

}