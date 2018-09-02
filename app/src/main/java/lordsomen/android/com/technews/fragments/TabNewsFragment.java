package lordsomen.android.com.technews.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lordsomen.android.com.technews.R;
import lordsomen.android.com.technews.adapters.NewsArticleAdapter;
import lordsomen.android.com.technews.network.ApiClient;
import lordsomen.android.com.technews.network.ApiInterface;
import lordsomen.android.com.technews.pojos.ApiData;
import lordsomen.android.com.technews.pojos.NewsArticleData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class TabNewsFragment extends Fragment implements NewsArticleAdapter.NewsOnClickItemHandler{

    public static  final String QUERY = "query" ;
    public static final String SOURCE = "source";
    public static final String API_KEY = "b5d42bd3009c4988bc08b6d78854717f";

    private ApiInterface mApiInterface;
    private String mNewsQuery;
    private String mNewsSource;
    private List<NewsArticleData> mNewsArticleDataList;
    private NewsArticleAdapter mNewsArticleAdapter;

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

    public TabNewsFragment(){}

    public static TabNewsFragment init(String source,String query) {
        TabNewsFragment tabNewsFragment = new TabNewsFragment();

        Bundle args = new Bundle();

        args.putString(QUERY,query);
        args.putString(SOURCE,source);
        tabNewsFragment.setArguments(args);
        return tabNewsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null != getArguments()){
            mNewsSource = getArguments().getString(SOURCE);
            mNewsQuery = getArguments().getString(QUERY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_news,container,false);
        ButterKnife.bind(this,view);
        mApiInterface = ApiClient.getApiClientTopHeadlines().create(ApiInterface.class);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL,false);
        mNewsArticleAdapter = new NewsArticleAdapter(getActivity().getApplicationContext(),this);
        mRecyclerViewEverything.setLayoutManager(linearLayoutManager);
        mRecyclerViewEverything.setAdapter(mNewsArticleAdapter);
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
        final Call<ApiData> listCall = mApiInterface.getAllBakingData(mNewsSource,mNewsQuery,API_KEY);
        // now binding the data in the pojo class
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
//                    if (onSavedInstanceState != null) {
//                        mRecyclerView.getLayoutManager().onRestoreInstanceState(onSavedInstanceState);
//                    }
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
    public void onClickItem(NewsArticleData newsArticleData) {
        Toast.makeText(getActivity().getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
    }
}
