package lordsomen.android.com.technews.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lordsomen.android.com.technews.R;
import lordsomen.android.com.technews.activities.NewsDetailsActivity;
import lordsomen.android.com.technews.adapters.NewsArticleAdapter;
import lordsomen.android.com.technews.database.NewsAppData;
import lordsomen.android.com.technews.pojos.NewsArticleData;
import lordsomen.android.com.technews.utils.MainViewModel;

import static android.support.constraint.Constraints.TAG;

public class FavNewsFragment extends Fragment implements NewsArticleAdapter.NewsOnClickItemHandler {

    public static final String NEWS_ARTICLE_DATA = "newsArticleIntentData";
    private static final String SAVED_LAYOUT_MANAGER = "layout-manager-state";
    @BindView(R.id.fav_news_rv)
    RecyclerView mRecyclerViewFav;
    @BindView(R.id.fav_news_action_error)
    LinearLayout mErrorLayoutFav;
    @BindView(R.id.fav_news_progress_bar)
    ProgressBar mProgressBarFav;
    @BindView(R.id.fav_news_reload_button)
    Button mReloadEverything;
    private NewsArticleAdapter mNewsArticleAdapter;
    private List<NewsArticleData> mNewsArticleDataList;
    private Context mContext;
    private Parcelable onSavedInstanceState;

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
        setupViewModel();
        if (savedInstanceState != null) {
            onSavedInstanceState = savedInstanceState.getParcelable(SAVED_LAYOUT_MANAGER);
        }
        mReloadEverything.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupViewModel();
            }
        });
        return view;
    }

    private void setupViewModel() {

        mProgressBarFav.setVisibility(View.VISIBLE);

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getmNewsList().observe(this, new Observer<List<NewsAppData>>() {
            @Override
            public void onChanged(@Nullable List<NewsAppData> newsAppData) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                if (null != newsAppData && newsAppData.size() != 0) {
                    mNewsArticleDataList = new ArrayList<>();
                    for (NewsAppData newsData : newsAppData) {
                        String news = newsData.getNewsArticleDataString();
                        mNewsArticleDataList.add(convertToArticle(news));
                    }
                    if (mNewsArticleDataList != null) {
                        showNewsEverythingList();
                        mNewsArticleAdapter.setNewsArticleData(mNewsArticleDataList);
                        if (onSavedInstanceState != null) {
                            mRecyclerViewFav.getLayoutManager().onRestoreInstanceState(onSavedInstanceState);
                        }
                    }

                } else {
                    showErrorEverything();
                }
            }
        });
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

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_LAYOUT_MANAGER, mRecyclerViewFav.getLayoutManager()
                .onSaveInstanceState());

    }

    @Override
    public void onClickItem(NewsArticleData newsArticleData, ImageView mImage, TextView mTitle) {
        Intent newsIntent = new Intent(getActivity().getApplicationContext(),
                NewsDetailsActivity.class);
        newsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(NEWS_ARTICLE_DATA, newsArticleData);
        newsIntent.putExtras(mBundle);
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(),
                new Pair<View, String>(mImage,
                        NewsDetailsActivity.VIEW_NAME_HEADER_IMAGE),
                new Pair<View, String>(mTitle,
                        NewsDetailsActivity.VIEW_NAME_HEADER_TITLE));

        ActivityCompat.startActivity(getActivity(), newsIntent, activityOptions.toBundle());
    }
}
