package lordsomen.android.com.technews.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import lordsomen.android.com.technews.fragments.TabNewsFragment;
import lordsomen.android.com.technews.utils.DataSource;

/**
 * this adapter is for the scrollable list of tab views !!
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final List<String> mSourceList = DataSource.NEWS_CHANNEL_LIST;
    private final List<String> mKeywordQueryList = DataSource.NEWS_QUERY_LIST;

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return TabNewsFragment.init("techcrunch",mKeywordQueryList.get(position));
    }

    @Override
    public int getCount() {
        return mKeywordQueryList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mKeywordQueryList.get(position);
    }
}
