package lordsomen.android.com.technews.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import lordsomen.android.com.technews.pojos.NewsArticleData;
import lordsomen.android.com.technews.utils.AppExecutors;
import lordsomen.android.com.technews.utils.GenerateID;

public class OprToDatabase {

    private static final String POS = "pos";
    private static final String SHARED_PREF_BUTTON = "shared_pref";
    private NewsAppDatabase newsAppDatabase;
    private NewsArticleData newsArticleData;


    public void addToFavData(NewsArticleData newsArticleData, final Context context) {
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

    public void removeFromFavData(final int id, Context context) {
        newsAppDatabase = NewsAppDatabase.getDataInstance(context);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                newsAppDatabase.NewsAppDao().deleteById(id);
            }
        });
    }

    public void addToHeadlinesData(NewsArticleData newsArticleData, final Context context) {
        newsAppDatabase = NewsAppDatabase.getDataInstance(context);
        final int id = GenerateID.generateId(newsArticleData);
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_BUTTON
                , Context.MODE_PRIVATE);

        if (!sharedPreferences.contains(POS + id)){

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(POS + id, id);
            editor.apply();
            Gson gson = new Gson();

            String news = gson.toJson(newsArticleData);
            final HeadlinesNewsAppData newsAppData = new HeadlinesNewsAppData(id, news);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    newsAppDatabase.HeadlinesNewsAppDao().insert(newsAppData);
                }
            });
        }
    }

    public void removeAllHeadlinesData(Context context) {
        newsAppDatabase = NewsAppDatabase.getDataInstance(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_BUTTON
                , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                newsAppDatabase.HeadlinesNewsAppDao().deleteAll();
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

