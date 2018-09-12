package lordsomen.android.com.technews.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import lordsomen.android.com.technews.R;
import lordsomen.android.com.technews.database.NewsAppData;
import lordsomen.android.com.technews.database.NewsContentProvider;
import lordsomen.android.com.technews.network.ApiInterface;
import lordsomen.android.com.technews.pojos.NewsArticleData;
import lordsomen.android.com.technews.utils.GlideApp;

public class NewsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {


    private ApiInterface mApiInterface;
    private String mNewsQuery;
    private String mNewsSource = "techcrunch";
    private List<NewsArticleData> mNewsArticleDataList;
    private Cursor mCursor;
    private Context mContext;

    public NewsRemoteViewsFactory() {
    }

    public NewsRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        final long identityToken = Binder.clearCallingIdentity();
        Uri uri = NewsContentProvider.newsUri;
        mCursor = mContext.getContentResolver().query(uri,
                null,
                null,
                null,
                null);
        Binder.restoreCallingIdentity(identityToken);

    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                mCursor == null || !mCursor.moveToPosition(position)) {
            return null;
        }
        NewsArticleData newsArticleData = convertToArticle(mCursor.getString(mCursor
                .getColumnIndex(NewsAppData.COLUMN_NEWS_ARTICLE_DATA)));
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setTextViewText(R.id.textView_headline_widget_item, newsArticleData.getTitle());

        try {
            Bitmap bitmap = GlideApp.with(mContext.getApplicationContext())
                    .asBitmap()
                    .load(newsArticleData.getUrlToImage())
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .submit(720, 720)
                    .get();

            rv.setImageViewBitmap(R.id.imageView_widget_item, bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public NewsArticleData convertToArticle(String news) {
        Type type = new TypeToken<NewsArticleData>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(news, type);
    }


}
