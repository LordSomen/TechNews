package lordsomen.android.com.technews.fragments;

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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lordsomen.android.com.technews.R;
import lordsomen.android.com.technews.activities.NewsDetailsActivity;
import lordsomen.android.com.technews.adapters.NewsArticleAdapter;
import lordsomen.android.com.technews.network.ApiClient;
import lordsomen.android.com.technews.network.ApiInterface;
import lordsomen.android.com.technews.pojos.ApiData;
import lordsomen.android.com.technews.pojos.NewsArticleData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class TabNewsFragment extends Fragment implements NewsArticleAdapter.NewsOnClickItemHandler {

    public static final String QUERY = "query";
    public static final String SOURCE = "source";
    public static final String NEWS_ARTICLE_DATA = "newsArticleIntentData";
    private static final String SAVED_LAYOUT_MANAGER = "layout-manager-state";
    @BindView(R.id.tabNews_framelayout_everything)
    FrameLayout mFrameLayoutEverything;
    @BindView(R.id.tab_news_rv_everything)
    RecyclerView mRecyclerViewEverything;
    @BindView(R.id.tab_news_action_error_everything)
    LinearLayout mErrorLayoutEverything;
    @BindView(R.id.tab_news_progress_bar_everything)
    ProgressBar mProgressBarEverything;
    @BindView(R.id.tab_news_reload_button_everything)
    Button mRelaodEverything;
    private ApiInterface mApiInterface;
    private String mNewsQuery;
    private String mNewsSource;
    private List<NewsArticleData> mNewsArticleDataList;
    private NewsArticleAdapter mNewsArticleAdapter;
    private Parcelable onSavedInstanceState;

    public TabNewsFragment() {
    }

    public static TabNewsFragment init(String source, String query) {
        TabNewsFragment tabNewsFragment = new TabNewsFragment();

        Bundle args = new Bundle();

        args.putString(QUERY, query);
        args.putString(SOURCE, source);
        tabNewsFragment.setArguments(args);
        return tabNewsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mNewsSource = getArguments().getString(SOURCE);
            mNewsQuery = getArguments().getString(QUERY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_news, container, false);
        ButterKnife.bind(this, view);
        mApiInterface = ApiClient.getApiClientTopHeadlines().create(ApiInterface.class);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL, false);
        mNewsArticleAdapter = new NewsArticleAdapter(getActivity().getApplicationContext(), this);
        mRecyclerViewEverything.setLayoutManager(linearLayoutManager);
        mRecyclerViewEverything.setAdapter(mNewsArticleAdapter);
        if (savedInstanceState != null) {
            onSavedInstanceState = savedInstanceState.getParcelable(SAVED_LAYOUT_MANAGER);
        }
        loadData();
        mRelaodEverything.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        return view;
    }

    public void loadData() {
        final Call<ApiData> listCall = mApiInterface.getAllChannelData(mNewsSource, mNewsQuery,
                getResources().getString(R.string.API_KEY));
        // now binding the data in the pojo class
        mProgressBarEverything.setVisibility(View.VISIBLE);

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
                        showNewsEverythingList();
                        mNewsArticleAdapter.setNewsArticleData(mNewsArticleDataList);
                        if (onSavedInstanceState != null) {
                            mRecyclerViewEverything.getLayoutManager().onRestoreInstanceState(onSavedInstanceState);
                        }
                    } else {
                        showErrorEverything();
                    }
                }
            }

            //if data binding is not successful onFailed called
            @Override
            public void onFailure(Call<ApiData> call, Throwable t) {
                //cancelling the GET data request
                listCall.cancel();
                showErrorEverything();
            }
        });
    }

    private void showErrorEverything() {
        mRecyclerViewEverything.setVisibility(View.GONE);
        mProgressBarEverything.setVisibility(View.GONE);
        mErrorLayoutEverything.setVisibility(View.VISIBLE);
    }

    private void showNewsEverythingList() {
        mRecyclerViewEverything.setVisibility(View.VISIBLE);
        mProgressBarEverything.setVisibility(View.GONE);
        mErrorLayoutEverything.setVisibility(View.GONE);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_LAYOUT_MANAGER, mRecyclerViewEverything.getLayoutManager()
                .onSaveInstanceState());
    }

}
