package lordsomen.android.com.technews.utils;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import lordsomen.android.com.technews.database.NewsAppData;
import lordsomen.android.com.technews.database.NewsAppDatabase;

public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<NewsAppData>> mNewsList;

    public MainViewModel(Application application) {
        super(application);
        NewsAppDatabase newsAppDatabase = NewsAppDatabase.getDataInstance(this.getApplication());
        mNewsList = newsAppDatabase.NewsAppDao().selectAllList();
    }

    public LiveData<List<NewsAppData>> getmNewsList() {
        return mNewsList;
    }
}
