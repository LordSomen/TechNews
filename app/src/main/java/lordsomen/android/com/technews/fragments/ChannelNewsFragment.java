package lordsomen.android.com.technews.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import lordsomen.android.com.technews.R;
import lordsomen.android.com.technews.adapters.ViewPagerAdapter;

/**
 * this fragment class helps to load the channel fragment from the nav drawer
 * to the acitivity . On this fragment all the data loaded fragments will be
 * loaded.
 */

public class ChannelNewsFragment extends Fragment {


    private Context mContext;
    @BindView(R.id.channel_frag_tabs)
    TabLayout mTabLayout;
    @BindView(R.id.channel_frag_viewpager)
    ViewPager mViewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_news,container,false);
        ButterKnife.bind(this,view);
        setupViewPager();
        mTabLayout.setupWithViewPager(mViewPager);
        return view;
    }

    /**
     * this method is for setting up the view pager !!
     */
    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
    }

}
