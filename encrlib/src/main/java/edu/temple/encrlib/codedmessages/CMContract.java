package edu.temple.encrlib.codedmessages;

import android.net.Uri;
import android.provider.BaseColumns;

import edu.temple.encrlib.utils.Constants;

public class CMContract {

    public static final String AUTHORITY = (Constants.PACKAGE_NAME + ".codedmessages.CMProvider");
    public static final Uri CONTENT_URI = Uri.parse(Constants.CONTENT_PREFIX + AUTHORITY);

    public static final int DEFAULT_ENCODING = Constants.DEFAULT_ENCODING;
    public static final Constants.ENCRYPTION_SCHEME DEFAULT_ENCRYPTION_SCHEME = Constants.DEFAULT_ENCRYPTION_SCHEME;

    public static final class Messages implements BaseColumns {

        public static final String DATASET_NAME = "messages";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(CMContract.CONTENT_URI, DATASET_NAME);

        public static final String INPUT_MESSAGE = "input_message";
        public static final String OUTPUT_MESSAGE = "output_message";

    }

}