package lordsomen.android.com.technews.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;



public class NewsContentProvider extends ContentProvider {


    private static final String AUTHORITY = "lordsomen.android.com.technews.database";

    public static final Uri newsUri = Uri.parse("content://" + AUTHORITY + "/" + 
            NewsAppData.TABLE_NAME);

    private static final int NEWS_DIR_CODE = 100;

    private static final int NEWS_ITEM_CODE = 101;


    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, NewsAppData.TABLE_NAME, NEWS_DIR_CODE);
        MATCHER.addURI(AUTHORITY, NewsAppData.TABLE_NAME + "/*", NEWS_ITEM_CODE);
    }


    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s,
                        @Nullable String[] strings1, @Nullable String s1) {

        final int code = MATCHER.match(uri);
        if (code == NEWS_DIR_CODE || code == NEWS_ITEM_CODE) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }

            NewsAppDao newsAppDao = NewsAppDatabase.getDataInstance(context).NewsAppDao();
            final Cursor cursor;

            cursor = newsAppDao.selectAllCursor();
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
