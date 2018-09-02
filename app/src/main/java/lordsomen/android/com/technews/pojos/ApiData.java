package lordsomen.android.com.technews.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiData implements Parcelable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("totalResults")
    @Expose
    private Long totalResults;
    @SerializedName("articles")
    @Expose
    private List<NewsArticleData> articles = null;
    public final static Parcelable.Creator<ApiData> CREATOR = new Creator<ApiData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ApiData createFromParcel(Parcel in) {
            return new ApiData(in);
        }

        public ApiData[] newArray(int size) {
            return (new ApiData[size]);
        }

    }
            ;

    protected ApiData(Parcel in) {
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.totalResults = ((Long) in.readValue((Long.class.getClassLoader())));
        in.readList(this.articles, (lordsomen.android.com.technews.pojos.NewsArticleData.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public ApiData() {
    }

    /**
     *
     * @param articles
     * @param totalResults
     * @param status
     */
    public ApiData(String status, Long totalResults, List<NewsArticleData> articles) {
        super();
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Long totalResults) {
        this.totalResults = totalResults;
    }

    public List<NewsArticleData> getArticles() {
        return articles;
    }

    public void setArticles(List<NewsArticleData> articles) {
        this.articles = articles;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(totalResults);
        dest.writeList(articles);
    }

    public int describeContents() {
        return 0;
    }

}