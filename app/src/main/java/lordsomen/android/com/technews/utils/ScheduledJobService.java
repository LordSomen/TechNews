package lordsomen.android.com.technews.utils;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.List;

import lordsomen.android.com.technews.database.OprToDatabase;
import lordsomen.android.com.technews.network.ApiClient;
import lordsomen.android.com.technews.network.ApiInterface;
import lordsomen.android.com.technews.pojos.ApiData;
import lordsomen.android.com.technews.pojos.NewsArticleData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class ScheduledJobService extends JobService {

    private ApiInterface mApiInterface;
    private static final String API_KEY = "b5d42bd3009c4988bc08b6d78854717f";
    private OprToDatabase oprToDatabase;
    private List<NewsArticleData> mNewsArticleDataList;
    List<String> sources ;
    @Override
    public boolean onStartJob(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                oprToDatabase = new OprToDatabase();
//                oprToDatabase.removeAllHeadlinesData(getApplicationContext());
                saveIntoDataBase(params);
            }
        }).start();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }


    public void saveIntoDataBase(JobParameters parameters){
        try {
            sources = DataSource.NEWS_CHANNEL_LIST;
            mApiInterface = ApiClient.getApiClientTopHeadlines().create(ApiInterface.class);
            loadData(sources.get(0));
        } finally {
            jobFinished(parameters, true);
        }
    }


    public void loadData(final String source) {
        if(sources.size() <= 0){
            return;
        }
        final Call<ApiData> listCall = mApiInterface.getAllTopHeadlinesData(source,API_KEY);
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
                        for(NewsArticleData newsArticleData : mNewsArticleDataList){
                            oprToDatabase.addToHeadlinesData(newsArticleData,getApplicationContext());
                        }
                        sources.remove(0);
                        loadData(sources.get(0));
                    }
                }
            }
            //if data binding is not successful onFailed called
            @Override
            public void onFailure(Call<ApiData> call, Throwable t) {
                //cancelling the GET data request
                listCall.cancel();
            }
        });
    }

}
