package edu.temple.encrlib.publicprivatekeys;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import edu.temple.encrlib.utils.Constants;

public final class PPKContract {

    public static final String AUTHORITY = (Constants.PACKAGE_NAME + ".publicprivatekeys.PPKProvider");
    public static final Uri CONTENT_URI = Uri.parse(Constants.CONTENT_PREFIX + AUTHORITY);

    public static final int DEFAULT_ENCODING = Constants.DEFAULT_ENCODING;
    public static final Constants.ENCRYPTION_SCHEME DEFAULT_ENCRYPTION_SCHEME = Constants.DEFAULT_ENCRYPTION_SCHEME;

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

    public static PublicPrivateKey getPPK(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(Keys.ID);
        int aliasIndex = cursor.getColumnIndex(Keys.ALIAS);
        int publicIndex = cursor.getColumnIndex(Keys.PUBLIC);
        int privateIndex = cursor.getColumnIndex(Keys.PRIVATE);

        PublicPrivateKey ppk = new PublicPrivateKey();
        ppk.setID(cursor.getInt(idIndex));
        ppk.setAlias(cursor.getString(aliasIndex));
        ppk.setPublicKey(cursor.getString(publicIndex));
        ppk.setPrivateKey(cursor.getString(privateIndex));
        return ppk;
    }

    public static class PublicPrivateKey {
        private int ID;
        private String alias;
        private String publicKey;
        private String privateKey;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }
    }

}