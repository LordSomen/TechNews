package lordsomen.android.com.technews.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {NewsAppData.class, HeadlinesNewsAppData.class}, version = 1, exportSchema = false)
public abstract class NewsAppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "News";
    private static final Object LOCK = new Object();
    private static final String LOG_TAG = NewsAppDatabase.class.getSimpleName();
    private static NewsAppDatabase dataInstance;

    public static NewsAppDatabase getDataInstance(Context context) {
        if (null == dataInstance) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                dataInstance = Room.databaseBuilder(context.getApplicationContext(),
                        NewsAppDatabase.class, NewsAppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");

        return dataInstance;
    }

    public abstract NewsAppDao NewsAppDao();

    public abstract HeadlinesNewsAppDao HeadlinesNewsAppDao();


}
