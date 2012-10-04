
package com.netomarin.tablemountain.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.netomarin.tablemountain.data.FeedDAO;
import com.netomarin.tablemountain.data.PostDAO;
import com.netomarin.tablemountain.data.RSSOpenHelper;

public class RSSProvider extends ContentProvider {

    public static final String AUTHORITY = "com.netomarin.tablemountain.provider.rss";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    private final static int FEED_PROVIDER_URI = 1;
    private final static int POST_PROVIDER_URI = 2;

    private final static UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, "feed/#", FEED_PROVIDER_URI);
        URI_MATCHER.addURI(AUTHORITY, "post/#", POST_PROVIDER_URI);
    }

    public static final class POST implements BaseColumns {
        public static final String _ID = "_ID";
        public static final String POST_ID = "POST_ID";
        public static final String FEED_ID = "FEED_ID";
        public static final String PUBLISHED = "PUBLISHED";
        public static final String UPDATED = "UPDATED";
        public static final String EDITED = "EDITED";
        public static final String CATEGORIES = "CATEGORIES";
        public static final String TITLE = "TITLE";
        public static final String CONTENT = "CONTENT";
        public static final String AUTHOR = "AUTHOR";
        public static final String CONTRIBUTORS = "CONTRIBUTORS";
    }

    public static final class FEED implements BaseColumns {
        public static final String _ID = "_ID";
        public static final String FEED_ID = "FEED_ID";
        public static final String UPDATED = "UPDATED";
        public static final String CATEGORIES = "CATEGORIES";
        public static final String TITLE = "TITLE";
        public static final String SUBTITLE = "SUBTITLE";
        public static final String ALT_LINK = "ALT_LINK";
        public static final String NEXT_LINK = "NEXT_LINK";
        public static final String AUTHOR = "AUTHOR";
        public static final String TOTAL_RESULTS = "TOTAL_RESULTS";
        public static final String START_INDEX = "START_INDEX";
        public static final String ITEMS_PER_PAGE = "ITEMS_PER_PAGE";
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        
        Uri insertedRow = null;
        
        switch (URI_MATCHER.match(uri)) {
            case FEED_PROVIDER_URI:
                insertedRow = insertFeed(values);
                break;
            case POST_PROVIDER_URI:
                insertedRow = insertPost(values);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri");
        }
        
        return insertedRow;
    }

    private Uri insertPost(ContentValues values) {
       if (!values.containsKey(POST.FEED_ID) ||
               !values.containsKey(POST.TITLE) ||
               !values.containsKey(POST.CONTENT)) {
           throw new IllegalArgumentException("Missing required field");
       }
       
       SQLiteDatabase db = new RSSOpenHelper(getContext()).getWritableDatabase();
       long newId = db.insert(PostDAO.POST_TABLE, null, values);
       db.close();
       
       return Uri.parse("content://" + AUTHORITY + "/post/"+newId);
    }

    private Uri insertFeed(ContentValues values) throws IllegalArgumentException {
        if (!values.containsKey(FEED.TITLE)) {
            throw new IllegalArgumentException("Missing required field: title");
        }
                
        SQLiteDatabase db = new RSSOpenHelper(getContext()).getWritableDatabase();
        long newId = db.insert(FeedDAO.FEED_TABLE, "", values);
        db.close();
        
        return Uri.parse("content://" + AUTHORITY + "/feed/"+newId);
    }

    @Override
    public boolean onCreate() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

}