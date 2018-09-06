package lordsomen.android.com.technews.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.ArraySet;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lordsomen.android.com.technews.R;
import lordsomen.android.com.technews.adapters.NewsArticleAdapter;
import lordsomen.android.com.technews.database.NewsAppData;
import lordsomen.android.com.technews.database.NewsAppDatabase;
import lordsomen.android.com.technews.pojos.NewsArticleData;

public class FavNewsFragment extends Fragment implements NewsArticleAdapter.NewsOnClickItemHandler,
        LoaderManager.LoaderCallbacks<List<NewsAppData>> {

    private static final int NEWS_DATA_LOADER = 2400;

    private NewsArticleAdapter mNewsArticleAdapter;
    private List<NewsArticleData> mNewsArticleDataList;
    private Context mContext;
    @BindView(R.id.fav_news_framelayout)
    FrameLayout mFrameLayoutFav;
    @BindView(R.id.fav_news_rv)
    RecyclerView mRecyclerViewFav;
    @BindView(R.id.fav_news_action_error)
    LinearLayout mErrorLayoutFav;
    @BindView(R.id.fav_news_progress_bar)
    ProgressBar mProgressBarFav;
    @BindView(R.id.fav_news_reload_button)
    Button mRelaodEverything;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav_news, container, false);
        ButterKnife.bind(this, view);
        mContext = view.getContext();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL, false);
        mNewsArticleAdapter = new NewsArticleAdapter(getActivity().getApplicationContext(), this);
        mRecyclerViewFav.setLayoutManager(linearLayoutManager);
        mRecyclerViewFav.setAdapter(mNewsArticleAdapter);
        loadData();
        mRelaodEverything.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        return view;
    }

    private void loadData() {
        mProgressBarFav.setVisibility(View.VISIBLE);
        mNewsArticleDataList = new ArrayList<>();

        Bundle bundle = new Bundle();
        LoaderManager loaderManager = getLoaderManager();
        Loader<ArraySet<NewsAppData>> loader = loaderManager.getLoader(NEWS_DATA_LOADER);
        if (loader == null) {
            loaderManager.initLoader(NEWS_DATA_LOADER, bundle, this);
        } else {
            loaderManager.restartLoader(NEWS_DATA_LOADER, bundle, this);
        }

    }



    @Override
    public void onClickItem(NewsArticleData newsArticleData) {

    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<List<NewsAppData>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<NewsAppData>>(getActivity().getApplicationContext()) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                mProgressBarFav.setVisibility(View.VISIBLE);
                if (args == null) {
                    return;
                } else {
                    forceLoad();
                }
            }

            @Override
            public List<NewsAppData> loadInBackground() {
                final NewsAppDatabase newsAppDatabase = NewsAppDatabase.getDataInstance(getActivity()
                        .getApplicationContext());
                return newsAppDatabase.NewsAppDao().selectAllList();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<NewsAppData>> loader, List<NewsAppData> data) {
        mProgressBarFav.setVisibility(View.GONE);
        if(null != data && data.size() != 0){
            mNewsArticleDataList = new ArrayList<>();
            for(NewsAppData newsAppData : data){
                    String news = newsAppData.getNewsArticleDataString();
                    mNewsArticleDataList.add(convertToArticle(news));
                }
                if(mNewsArticleDataList != null){
                    showNewsEverythingList();
                    mNewsArticleAdapter.setNewsArticleData(mNewsArticleDataList);
                }
        }else {
            showErrorEverything();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsAppData>> loader) {

    }

    private void showErrorEverything() {
        mRecyclerViewFav.setVisibility(View.GONE);
        mProgressBarFav.setVisibility(View.GONE);
        mErrorLayoutFav.setVisibility(View.VISIBLE);
    }


    private void showNewsEverythingList() {
        mRecyclerViewFav.setVisibility(View.VISIBLE);
        mProgressBarFav.setVisibility(View.GONE);
        mErrorLayoutFav.setVisibility(View.GONE);
    }

    public NewsArticleData convertToArticle(String news) {
        Type type = new TypeToken<NewsArticleData>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(news, type);
    }

}
