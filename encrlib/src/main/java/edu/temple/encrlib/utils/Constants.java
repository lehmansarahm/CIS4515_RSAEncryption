package edu.temple.encrlib.utils;

import android.util.Base64;

public class Constants {

    public static final String LOG_TAG = "CIS4515-RSAEncryption";
    public static final String PACKAGE_NAME = "edu.temple.encrlib";

    public static final String CONTENT_PREFIX = "content://";

    public static final String DB_NAME = "CIS4515_DB";
    public static final int DB_VERSION = 1;

    public static final int DEFAULT_ENCODING = Base64.DEFAULT;
    public static final ENCRYPTION_SCHEME DEFAULT_ENCRYPTION_SCHEME = ENCRYPTION_SCHEME.RSA;
    public enum ENCRYPTION_SCHEME { RSA, SHA256 }

}