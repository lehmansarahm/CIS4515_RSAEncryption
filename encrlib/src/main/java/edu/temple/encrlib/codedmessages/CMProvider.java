package edu.temple.encrlib.codedmessages;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;

import edu.temple.encrlib.utils.Constants;

public class CMProvider extends FileProvider {

    private static final String CONTENT_URL = Constants.PACKAGE_NAME + ".CMProvider";
    private static final String ENCRYPTED_FILE_NAME = "encrypted.txt";
    private static final String MESSAGES_DIR = "msgs";
    public static Uri CONTENT_URI;

    @Override
    public boolean onCreate() {
        File msgPath = new File(Environment.getExternalStorageDirectory(), MESSAGES_DIR);
        File encrFile = new File(msgPath, ENCRYPTED_FILE_NAME);
        CONTENT_URI = getUriForFile(getContext(),CONTENT_URL, encrFile);
        return false;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
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