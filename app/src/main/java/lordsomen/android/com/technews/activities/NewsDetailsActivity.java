package lordsomen.android.com.technews.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import lordsomen.android.com.technews.R;
import lordsomen.android.com.technews.fragments.TabNewsFragment;
import lordsomen.android.com.technews.pojos.NewsArticleData;
import lordsomen.android.com.technews.pojos.Source;
import lordsomen.android.com.technews.utils.GlideApp;

public class NewsDetailsActivity extends AppCompatActivity {

    private NewsArticleData mNewsArticleData;
//    @BindView(R.id.textView_title_details)
//    TextView mTitle;
    @BindView(R.id.imageView_details)
    ImageView mThumbnail;
    @BindView(R.id.layout_author_details)
    LinearLayout mAuthorLayout;
    @BindView(R.id.author_name_details)
    TextView mAuthor;
    @BindView(R.id.layout_publishedAt_details)
    LinearLayout mPublishedAtLayout;
    @BindView(R.id.publishedat_name_details)
    TextView mPublishedAt;
    @BindView(R.id.layout_source_details)
    LinearLayout mSourceLayout;
    @BindView(R.id.source_name_details)
    TextView mSource;
    @BindView(R.id.textView_description_details)
    TextView mDescription;
    @BindView(R.id.textView_description_label_details)
    TextView mDescriptionLabel;
    @BindView(R.id.toolbar_details)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        ButterKnife.bind(this);
        if (null != mToolbar) {
            setSupportActionBar(mToolbar);
            }
        Intent intent = getIntent();
        if(null != intent) {
            Bundle intentData = intent.getExtras();
            if(null != intentData) {
                mNewsArticleData = intentData.getParcelable(TabNewsFragment.NEWS_ARTICLE_DATA);
                if (mNewsArticleData != null) {

                    String imageUrl = mNewsArticleData.getUrlToImage();
                    if(null != imageUrl){
                        loadImage(imageUrl);
                    }
                    String title = mNewsArticleData.getTitle();
                    if(null != title){
                        mToolbar.setTitle(title);
                    }
                    Object author = mNewsArticleData.getAuthor();
                    if(null != author){
                        String authorS = String.valueOf(author);
                        mAuthor.setText(authorS);
                    }else {
                        mAuthorLayout.setVisibility(View.INVISIBLE);
                    }
                    String date = mNewsArticleData.getPublishedAt();
                    if(null != date){
                        mPublishedAt.setText(date);
                    }else {
                        mPublishedAt.setVisibility(View.INVISIBLE);
                    }
                    Source source = mNewsArticleData.getSource();
                    if(null != source) {
                        String name = source.getName();
                        if (null != name) {
                            mSource.setText(name);
                        } else {
                            mSourceLayout.setVisibility(View.INVISIBLE);
                        }
                    }else {
                        mSourceLayout.setVisibility(View.INVISIBLE);
                    }
                    String description = mNewsArticleData.getDescription();
                    if(null != description){
                        mDescription.setText(description);
                    }else {
                        mDescription.setVisibility(View.INVISIBLE);
                        mDescriptionLabel.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    }

    private void loadImage(String url) {
        GlideApp.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(mThumbnail);
    }
}
