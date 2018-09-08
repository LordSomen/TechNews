package lordsomen.android.com.technews.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
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
import lordsomen.android.com.technews.pojos.ApiData;
import lordsomen.android.com.technews.pojos.NewsArticleData;
import lordsomen.android.com.technews.utils.GlideApp;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class NewsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    public static final String API_KEY = "b5d42bd3009c4988bc08b6d78854717f";

    private ApiInterface mApiInterface;
    private String mNewsQuery;
    private String mNewsSource = "techcrunch";
    private List<NewsArticleData> mNewsArticleDataList;
    private Cursor mCursor;
    private Context mContext;

    public  NewsRemoteViewsFactory(){}

    public NewsRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

//        try {
//            mContext.wait(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onDataSetChanged() {
//        mApiInterface = ApiClient.getApiClientTopHeadlines().create(ApiInterface.class);
//        loadData();
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
//        AppWidgetTarget appWidgetTarget;
        NewsArticleData newsArticleData = convertToArticle(mCursor.getString(mCursor
                .getColumnIndex(NewsAppData.COLUMN_NEWS_ARTICLE_DATA)));
//        NewsArticleData newsArticleData = mNewsArticleDataList.get(position);
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setTextViewText(R.id.textView_headline_widget_item,newsArticleData.getTitle());
//        appWidgetTarget = new AppWidgetTarget(mContext, R.id.imageView_widget_item, rv) {
//            @Override
//            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                super.onResourceReady(resource, transition);
//            }
//        };
//
//        GlideApp.with(mContext.getApplicationContext())
//                .asBitmap()
//                .load(newsArticleData.getUrlToImage())
//                .centerCrop()
//                .placeholder(R.drawable.placeholder)
//                .into(appWidgetTarget);
        try {
            Bitmap bitmap =  GlideApp.with(mContext.getApplicationContext())
                    .asBitmap()
                    .load(newsArticleData.getUrlToImage())
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .submit(100, 90)
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

    public void loadData() {

        final long identityToken = Binder.clearCallingIdentity();

        final Call<ApiData> listCall = mApiInterface.getAllTopHeadlinesDataWidget(mNewsSource,API_KEY);
        // now binding the data in the pojo class
//        mProgressBarEverything.setVisibility(View.VISIBLE);

        listCall.enqueue(new Callback<ApiData>() {
            //if data is successfully binded from json to the pojo class onResponse is called
            @Override
            public void onResponse(Call<ApiData> call,
                                   Response<ApiData> response) {
                Log.d(TAG, "Response : " + response.code());
                ApiData apiData = response.body();
                if (null != apiData) {
                    mNewsArticleDataList = apiData.getArticles();
                    if (null != mNewsArticleDataList) {
                        Binder.restoreCallingIdentity(identityToken);
//                        showNewsEverythingList();
//                    if (onSavedInstanceState != null) {
//                        mRecyclerView.getLayoutManager().onRestoreInstanceState(onSavedInstanceState);
//                    }
                    }
                }
            }
            //if data binding is not successful onFailed called
            @Override
            public void onFailure(Call<ApiData> call, Throwable t) {
                //cancelling the GET data request
                listCall.cancel();
//                showErrorEverything();
            }
        });
    }

}
