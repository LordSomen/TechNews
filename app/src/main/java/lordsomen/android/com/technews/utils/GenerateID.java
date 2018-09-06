package lordsomen.android.com.technews.utils;

import lordsomen.android.com.technews.pojos.NewsArticleData;
import lordsomen.android.com.technews.pojos.Source;

public class GenerateID {

    public static int generateId(NewsArticleData newsArticleData){
        int ID = 0;
        int NullValue = 1997;

        String title = newsArticleData.getTitle();
        if(null != title){
            ID += title.length();
        }else {
            ID += NullValue;
        }
        if(null != newsArticleData.getAuthor()) {
            String author = String.valueOf(newsArticleData.getAuthor());
            ID += author.length();
        }else {
            ID += NullValue;
        }
        String description = newsArticleData.getDescription();
        if(null != description){
            ID += description.length();
        }else {
            ID += NullValue;
        }
        String url = newsArticleData.getUrl();
        if(null != url){
            ID += url.length();
        }else {
            ID += NullValue;
        }
        String imgUrl = newsArticleData.getUrlToImage();
        if(null != imgUrl){
            ID += imgUrl.length();
        }else {
            ID += NullValue;
        }
        String date = newsArticleData.getPublishedAt();
        if(null != date){
            ID += date.length();
        }else {
            ID += NullValue;
        }
        Source source = newsArticleData.getSource();
        if(null != source){
            String name = source.getName();
            if(null != name){
                ID += name.length();
            }else {
                ID += NullValue;
            }
            String id = source.getId();
            if(null != id){
                ID += id.length();
            }else {
                ID += NullValue;
            }
        }
        return ID;
    }

}
