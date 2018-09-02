package lordsomen.android.com.technews.network;

import lordsomen.android.com.technews.pojos.ApiData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by soumyajit on 1/9/18.
 */

public interface ApiInterface {



    @GET("everything")
    Call<ApiData> getAllBakingData(@Query("sources") String source , @Query("q") String query
            ,@Query("apiKey") String apiKey);

}
