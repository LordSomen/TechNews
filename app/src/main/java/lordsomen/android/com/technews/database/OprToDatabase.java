package lordsomen.android.com.technews.database;

import android.content.Context;

import com.google.gson.Gson;

import lordsomen.android.com.technews.pojos.NewsArticleData;
import lordsomen.android.com.technews.utils.AppExecutors;
import lordsomen.android.com.technews.utils.GenerateID;

public class OprToDatabase {

    private NewsAppDatabase newsAppDatabase;
    private NewsArticleData newsArticleData;


    public void add(NewsArticleData newsArticleData, final Context context) {
        newsAppDatabase = NewsAppDatabase.getDataInstance(context);
        final int id = GenerateID.generateId(newsArticleData);

        Gson gson = new Gson();

        String news = gson.toJson(newsArticleData);
        final NewsAppData newsAppData = new NewsAppData(id, news);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                newsAppDatabase.NewsAppDao().insert(newsAppData);
            }
        });

    }

    public void remove(final int id, Context context) {
        newsAppDatabase = NewsAppDatabase.getDataInstance(context);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                newsAppDatabase.NewsAppDao().deleteById(id);
            }
        });
    }

//    public boolean isBookmarked(final int id, Context context) throws InterruptedException {
//        newsAppDatabase = NewsAppDatabase.getDataInstance(context);
//        final boolean[] check = new boolean[1];
//        check[0] = false;
//        AppExecutors.getInstance().diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                check[0] = newsAppDatabase.NewsAppDao().isInTheTable(id);
//            }
//        });
//        wait(1000);
//        return check[0];
//    }

}

