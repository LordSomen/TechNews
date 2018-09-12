package lordsomen.android.com.technews.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * this classs contains all kind of data sources , keywords , names etc for the API call
 */
public class DataSource {

    /**
     * this list have all kind of query or tab names
     */
    public static List<String> NEWS_QUERY_LIST = new ArrayList<>();
    /**
     * this list contains all kind of channel names , which's news will be visible to the app
     */
    public static List<String> NEWS_CHANNEL_LIST = new ArrayList<>();

    /**
     * this constructor initializes both of the lists
     */
    public DataSource() {
        NEWS_QUERY_LIST.addAll(Arrays.asList("artificial-intelligence", "startup",
                "blockchain", "research", "augmented-reality", "virtual-reality", "technology",
                "programming", "google", "apple", "amazon", "facebook"));

        NEWS_CHANNEL_LIST.addAll(Arrays.asList("ars-technica", "bbc-news",
                "wired", "cnn", "engadget", "techcrunch", "mashable", "the-next-web", "the-verge",
                "bloomberg", "hacker-news", "reddit-r-all", "crypto-coin-news", "recode", "techrader",
                "new-scientist", "vice-news"));

    }

}
