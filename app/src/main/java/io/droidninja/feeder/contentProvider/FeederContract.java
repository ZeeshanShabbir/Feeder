package io.droidninja.feeder.contentProvider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Zeeshan on 2/12/17.
 */

public class FeederContract {

    //content authority
    public static final String CONTENT_AUTHORITY = "io.droidninja.feeder";
    //creating base uri
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_SOURCE = "source";
    public static final String PATH_ARTICLE = "article";

    public static final class SourceEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_SOURCE)
                .build();
        public static final String TABLE_NAME = "source";
        public static final String NAME = "name";
        public static final String IDENTIFIER = "identifier";
    }

    public static final class ArticleEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_ARTICLE)
                .build();
        public static final String TABLE_NAME = "article";
        public static final String AUTHOR = "author";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String URL = "url";
        public static final String URL_TO_IMAGE = "url_to_image";
        public static final String PUBLISH_AT = "published";
        public static final String SOURCE = "source";
    }
}
