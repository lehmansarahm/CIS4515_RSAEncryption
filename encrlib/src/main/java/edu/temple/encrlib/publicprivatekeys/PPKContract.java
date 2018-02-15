package edu.temple.encrlib.publicprivatekeys;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import edu.temple.encrlib.utils.Constants;

public final class PPKContract {

    public static final String AUTHORITY = (Constants.PACKAGE_NAME + ".publicprivatekeys.PPKProvider");
    public static final Uri CONTENT_URI = Uri.parse(Constants.CONTENT_PREFIX + AUTHORITY);

    public static final ENCRYPTION_SCHEME DEFAULT_ENCRYPTION_SCHEME = ENCRYPTION_SCHEME.RSA;
    public enum ENCRYPTION_SCHEME { RSA, SHA256 }

    public static final class Keys implements BaseColumns {

        public static final String TABLE_NAME = "keys";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(PPKContract.CONTENT_URI, TABLE_NAME);

        public static final String ID = "_id";
        public static final String ALIAS = "alias";
        public static final String PUBLIC = "public";
        public static final String PRIVATE = "private";

        public static final String SORT_ORDER_DEFAULT = (ALIAS + " ASC");

        private static final String CONTENT_GROUP_NAME = ("/" + Constants.PACKAGE_NAME + "_" + TABLE_NAME);
        public static final String CONTENT_LIST_TYPE = (ContentResolver.CURSOR_DIR_BASE_TYPE + CONTENT_GROUP_NAME);
        public static final String CONTENT_ITEM_TYPE = (ContentResolver.CURSOR_ITEM_BASE_TYPE + CONTENT_GROUP_NAME);

        public static final String[] PROJECTION_ALL = { BaseColumns._ID, ALIAS, PUBLIC, PRIVATE };

    }

}