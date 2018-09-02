package lordsomen.android.com.technews.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lordsomen.android.com.technews.R;
import lordsomen.android.com.technews.pojos.NewsArticleData;
import lordsomen.android.com.technews.utils.GlideApp;

public class NewsArticleAdapter extends RecyclerView.Adapter<NewsArticleAdapter.
        NewsArticleAdapterViewHolder> {

    private List<NewsArticleData> mNewsArticleDataList;
    private Context mContext;
    private NewsOnClickItemHandler mNewsClickHandler;

    public NewsArticleAdapter(Context context, NewsOnClickItemHandler click) {

        mContext = context;
        mNewsClickHandler = click;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public NewsArticleAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        return new NewsArticleAdapterViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_tab_news_frag_everything, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(NewsArticleAdapterViewHolder holder, int position) {
        if (mNewsArticleDataList != null) {
           NewsArticleData articleData = mNewsArticleDataList.get(position);
           if(null != articleData){
               GlideApp.with(mContext)
                       .load(articleData.getUrlToImage())
                       .centerCrop()
                       .placeholder(R.drawable.placeholder)
                       .into(holder.mImageView);
               holder.mNewsTitle.setText(articleData.getTitle());
//               holder.mNewsDesc.setText(articleData.getDescription());
               if(null != articleData.getAuthor()) {
                   String author = String.valueOf(articleData.getAuthor());
                   holder.mNewsAuthor.setText(author);
               }
               holder.mNewsDate.setText(articleData.getPublishedAt());
               holder.mNewsPublisher.setText(articleData.getSource().getName());
           }
        }
    }

    @Override
    public int getItemCount() {
        if (mNewsArticleDataList == null) {
            return 0;
        } else {
            return mNewsArticleDataList.size();
        }
    }

    public void setNewsArticleData(List<NewsArticleData> newsArticleData) {
        if (newsArticleData != null)
            mNewsArticleDataList = newsArticleData;
        notifyDataSetChanged();
    }

    public interface NewsOnClickItemHandler {
        void onClickItem(NewsArticleData newsArticleData);
    }

    public class NewsArticleAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageView_everything_item)
        public ImageView mImageView;
        @BindView(R.id.textView_title_everything_item)
        public TextView mNewsTitle;
        @BindView(R.id.textView_description_everything_item)
        public TextView mNewsDesc;
        @BindView(R.id.textView_author_everything_item)
        public TextView mNewsAuthor;
        @BindView(R.id.textView_date_everything_item)
        public TextView mNewsDate;
        @BindView(R.id.textView_publisher_everything_item)
        public TextView mNewsPublisher;

        public NewsArticleAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            NewsArticleData articleData = mNewsArticleDataList.get(getAdapterPosition());
            mNewsClickHandler.onClickItem(articleData);

        }
    }
}

