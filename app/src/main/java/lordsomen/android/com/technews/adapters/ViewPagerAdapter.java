package lordsomen.android.com.technews.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import lordsomen.android.com.technews.fragments.TabNewsFragment;
import lordsomen.android.com.technews.utils.DataSource;

/**
 * this adapter is for the scrollable list of tab views !!
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<String> mSourceList = DataSource.NEWS_CHANNEL_LIST;
    private final List<String> mKeywordQueryList = DataSource.NEWS_QUERY_LIST;

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return TabNewsFragment.init("techcrunch",mKeywordQueryList.get(position));
    }

    @Override
    public int getCount() {
        return mKeywordQueryList.size();
    }


//    public void addFrag(Fragment fragment, String title) {
//        mFragmentList.add(fragment);
//        mFragmentTitleList.add(title);
//    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mKeywordQueryList.get(position);
    }
}
