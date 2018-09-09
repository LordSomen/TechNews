package lordsomen.android.com.technews.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lordsomen.android.com.technews.R;
import lordsomen.android.com.technews.fragments.ChannelNewsFragment;
import lordsomen.android.com.technews.fragments.FavNewsFragment;
import lordsomen.android.com.technews.utils.DataSource;
import lordsomen.android.com.technews.widget.NewsAppWidget;

public class MainNewsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int CHANNEL = View.generateViewId();

    private HashMap<String, Integer> mNewsChannelMap = new HashMap<>();
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_news);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        //Initializing DataSource class
        DataSource dataSource = new DataSource();
        //for creation of channels nav menu id map
        createMap();
        //creation of drawer layout

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

//        addMenuItemInNavMenuDrawer();
        //this lines set the initial nav fragment
        setFragment(R.id.nav_techcrunch);
        NewsAppWidget.sendRefreshBroadcast(getApplicationContext());
    }

    private void createMap() {
        for (String item : DataSource.NEWS_CHANNEL_LIST) {
            mNewsChannelMap.put(item, View.generateViewId());
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        setFragment(id);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setFragment(int fragmentId) {
        Fragment fragment = null;
        switch (fragmentId) {

            case R.id.nav_ars_technica:
                fragment = ChannelNewsFragment.init("ars_technica");
                break;
            case R.id.nav_bbc_news:
                fragment = ChannelNewsFragment.init("bbc_news");
                break;
            case R.id.nav_bloomberg:
                fragment = ChannelNewsFragment.init("bloomberg");
                break;
            case R.id.nav_cnet:
                fragment = ChannelNewsFragment.init("cnet");
                break;
            case R.id.nav_crypto_coin_news:
                fragment = ChannelNewsFragment.init("crypto_coin_news");
                break;
            case R.id.nav_engadget:
                fragment = ChannelNewsFragment.init("engadget");
                break;
            case R.id.nav_google_news:
                fragment = ChannelNewsFragment.init("google_news");
                break;
            case R.id.nav_hacker_news:
                fragment = ChannelNewsFragment.init("hacker_news");
                break;
            case R.id.nav_mashable:
                fragment = ChannelNewsFragment.init("mashable");
                break;
            case R.id.nav_new_scientist:
                fragment = ChannelNewsFragment.init("new_scientist");
                break;
            case R.id.nav_recode:
                fragment = ChannelNewsFragment.init("recode");
                break;
            case R.id.nav_reddit_r_all:
                fragment = ChannelNewsFragment.init("reddit_r_all");
                break;
            case R.id.nav_techrader:
                fragment = ChannelNewsFragment.init("techrader");
                break;
            case R.id.nav_techcrunch:
                fragment = ChannelNewsFragment.init("techcrunch");
                break;
            case R.id.nav_the_next_web:
                fragment = ChannelNewsFragment.init("the_next_web");
                break;
            case R.id.nav_the_verge:
                fragment = ChannelNewsFragment.init("the_verge");
                break;
            case R.id.nav_vice_news:
                fragment = ChannelNewsFragment.init("vice_news");
                break;
            case R.id.nav_wired:
                fragment = ChannelNewsFragment.init("wired");
                break;
            case R.id.nav_favourite:
                fragment = new FavNewsFragment();
                break;
        }
        constructFragment(fragment);
    }

    public void constructFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit();
    }

    /**
     * this method adds menu item in the nav drawer dynamically
     */
    private void addMenuItemInNavMenuDrawer() {

        Menu menu = mNavigationView.getMenu();
        Menu submenu = menu.addSubMenu(0, CHANNEL, 0, "Channels");

        List<String> newsChannels = DataSource.NEWS_CHANNEL_LIST;
        for (int i = 0; i < newsChannels.size(); i++) {
            String item = newsChannels.get(i);
            submenu.add(CHANNEL, mNewsChannelMap.get(item), i, item);
        }
        mNavigationView.invalidate();
    }

}
