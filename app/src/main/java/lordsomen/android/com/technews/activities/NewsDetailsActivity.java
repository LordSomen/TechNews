package lordsomen.android.com.technews.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";
    public static final String VIEW_NAME_HEADER_TITLE = "detail:header:title";
    private final static String ARTICLE_SCROLL_POSITION = "article_scroll_position";
    @BindView(R.id.textView_title_details)
    TextView mTitle;
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
    @BindView(R.id.button_read_article)
    Button mReadArticle;
    @BindView(R.id.news_details_scrolllview)
    NestedScrollView scrollView;
    private int[] scrollPosition = null;
    private NewsArticleData mNewsArticleData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (null != intent) {
            Bundle intentData = intent.getExtras();
            if (null != intentData) {
                mNewsArticleData = intentData.getParcelable(TabNewsFragment.NEWS_ARTICLE_DATA);

                if (mNewsArticleData != null) {

                    String imageUrl = mNewsArticleData.getUrlToImage();
                    if (null != imageUrl) {
                        loadImage(imageUrl);
                    }
                    String title = mNewsArticleData.getTitle();
                    if (null != title) {
                        mTitle.setText(title);
                    }
                    Object author = mNewsArticleData.getAuthor();
                    if (null != author) {
                        String authorS = String.valueOf(author);
                        mAuthor.setText(authorS);
                    } else {
                        mAuthorLayout.setVisibility(View.INVISIBLE);
                    }
                    String date = mNewsArticleData.getPublishedAt();
                    if (null != date) {
                        mPublishedAt.setText(date);
                    } else {
                        mPublishedAt.setVisibility(View.INVISIBLE);
                    }
                    Source source = mNewsArticleData.getSource();
                    if (null != source) {
                        String name = source.getName();
                        if (null != name) {
                            mSource.setText(name);
                        } else {
                            mSourceLayout.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        mSourceLayout.setVisibility(View.INVISIBLE);
                    }
                    String description = mNewsArticleData.getDescription();
                    if (null != description) {
                        mDescription.setText(description);
                    } else {
                        mDescription.setVisibility(View.INVISIBLE);
                        mDescriptionLabel.setVisibility(View.INVISIBLE);
                    }
                    final String url = mNewsArticleData.getUrl();
                    if (url != null) {
                        mReadArticle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                if (webIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(webIntent);
                                }
                            }
                        });
                    } else {
                        mReadArticle.setVisibility(View.INVISIBLE);
                    }
                }

                ViewCompat.setTransitionName(mThumbnail, VIEW_NAME_HEADER_IMAGE);
                ViewCompat.setTransitionName(mTitle, VIEW_NAME_HEADER_TITLE);
            }
        }

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);

        }
    }

    private void loadImage(String url) {
        GlideApp.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(mThumbnail);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(ARTICLE_SCROLL_POSITION,
                new int[]{scrollView.getScrollX(), scrollView.getScrollY()});
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        scrollPosition = savedInstanceState.getIntArray(ARTICLE_SCROLL_POSITION);
        if (scrollPosition != null) {
            scrollView.postDelayed(new Runnable() {
                public void run() {
                    scrollView.scrollTo(scrollPosition[0], scrollPosition[1]);
                }
            }, 100);
        }
    }
}
