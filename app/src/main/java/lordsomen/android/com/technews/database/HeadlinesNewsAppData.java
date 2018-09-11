package lordsomen.android.com.technews.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "top_headlines_data")
public class HeadlinesNewsAppData {

    public static final String TABLE_NAME = "top_headlines_data";
    public static final String COLUMN_NEWS_ARTICLE_DATA = "newsArticleDataString";
    public static final String COLUMN_ID_ = "id";

    @PrimaryKey
    private int id;

    private String newsArticleDataString;


    public HeadlinesNewsAppData(int id, String newsArticleDataString) {

        this.id = id;
        this.newsArticleDataString = newsArticleDataString;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNewsArticleDataString() {
        return newsArticleDataString;
    }

    public void setNewsArticleDataString(String newsArticleDataString) {
        this.newsArticleDataString = newsArticleDataString;
    }
}
