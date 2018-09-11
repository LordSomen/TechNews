package lordsomen.android.com.technews.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import lordsomen.android.com.technews.R;
import lordsomen.android.com.technews.fragments.ChannelNewsFragment;
import lordsomen.android.com.technews.fragments.FavNewsFragment;
import lordsomen.android.com.technews.fragments.TopHeadlinesFragment;
import lordsomen.android.com.technews.utils.DataSource;
import lordsomen.android.com.technews.utils.GlideApp;
import lordsomen.android.com.technews.utils.ScheduledJobService;
import lordsomen.android.com.technews.widget.NewsAppWidget;

public class MainNewsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static final String TAG = MainNewsActivity.class.getSimpleName();
    public static final String JOB_TAG = "UniqueTagForYourJob";
    public int TRIGGER = 0;
    private static final int JOB_INTERVAL_HOURS = 12;
    private static final int JOB_INTERVAL_SECONDS = (int) (TimeUnit.HOURS
            .toSeconds(JOB_INTERVAL_HOURS));
    private static final int SYNC_FLEXTIME_SECONDS = (int) (TimeUnit.HOURS
            .toSeconds(2));

    public static final String PREF_NAME = "name-pref";
    public static final String PREF_EMAIL = "email-pref";
    public static final String PREF_IMAGE = "image-pref";


    private HashMap<String, Integer> mNewsChannelMap = new HashMap<>();
    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    TextView navHeaderName;
    TextView navHeaderEmail;
    ImageView navHeaderImage;
    private String name;
    private String email;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_news);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        View nav_header = mNavigationView.getHeaderView(0);
        navHeaderEmail = nav_header.findViewById(R.id.nav_email);
        navHeaderName = nav_header.findViewById(R.id.nav_name);
        navHeaderImage = nav_header.findViewById(R.id.nav_imageView);


        Intent intent = getIntent();
        if(null != intent) {
            Bundle intentData = intent.getExtras();
            setIdentityData(intentData);
            }

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

        //this lines set the initial nav fragment
        setFragment(R.id.nav_techcrunch);
        NewsAppWidget.sendRefreshBroadcast(getApplicationContext());
        scheduleJob(this);
    }
    
    private void setIdentityData(Bundle bundle){
        
        if (bundle != null) {
            name = bundle.getString(SignUpActivity.NAME_KEY);
            email = bundle.getString(SignUpActivity.EMAIL_KEY);
            imageUrl = bundle.getString(SignUpActivity.IMAGE_URL_KEY);
            String phoneNo = bundle.getString(SignUpActivity.PHONE_KEY);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(PREF_NAME,name);
            editor.putString(PREF_EMAIL,email);
            editor.putString(PREF_IMAGE,imageUrl);
            editor.apply();
            navHeaderName.setText(name);
            navHeaderEmail.setText(email);
            loadHeaderImage();
            Log.d("MainActivity","Uri "+imageUrl);

        }else {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            name = preferences.getString(PREF_NAME,"");
            email = preferences.getString(PREF_EMAIL,"");
            imageUrl = preferences.getString(PREF_IMAGE,"");
            Log.d(TAG,"Name "+name);
            navHeaderName.setText(name);
            navHeaderEmail.setText(email);
            loadHeaderImage();
        }
    }

    private void loadHeaderImage(){

        GlideApp.with(this)
                .load(imageUrl)
                .circleCrop()
                .into(navHeaderImage);
    }
    
    private void createMap(){
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
            case R.id.nav_cnn:
                fragment = ChannelNewsFragment.init("cnn");
                break;
            case R.id.nav_crypto_coin_news:
                fragment = ChannelNewsFragment.init("crypto_coin_news");
                break;
            case R.id.nav_engadget:
                fragment = ChannelNewsFragment.init("engadget");
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
            case R.id.nav_top_headlines:
                fragment = new TopHeadlinesFragment();
                break;
            case R.id.nav_log_out:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this,SignUpActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                Toast.makeText(this, "Can't find the nav item",Toast.LENGTH_SHORT).show();
        }
        constructFragment(fragment);
    }

    public void constructFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit();
    }

    public static void scheduleJob(Context context) {
        //creating new firebase job dispatcher
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        //creating new job and adding it with dispatcher
        Job job = createJob(dispatcher);
        dispatcher.mustSchedule(job);
    }

    public static Job createJob(FirebaseJobDispatcher dispatcher){

        Job job = dispatcher.newJobBuilder()
                //persist the task across boots
                .setLifetime(Lifetime.FOREVER)
                //.setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                //call this service when the criteria are met.
                .setService(ScheduledJobService.class)
                //unique id of the task
                .setTag(JOB_TAG)
                //don't overwrite an existing job with the same tag
                .setReplaceCurrent(false)
                // We are mentioning that the job is periodic.
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(10 ,
                        JOB_INTERVAL_SECONDS ))
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setReplaceCurrent(true)
                //Run this job only when the network is available.
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();

        return job;
    }

    public static Job updateJob(FirebaseJobDispatcher dispatcher) {
        Job newJob = dispatcher.newJobBuilder()
                //update if any task with the given tag exists.
                .setReplaceCurrent(true)
                //Integrate the job you want to start.
                .setService(ScheduledJobService.class)
                .setTag(JOB_TAG)
                // Run between 30 - 60 seconds from now.
                .setTrigger(Trigger.executionWindow(10 ,
                        JOB_INTERVAL_SECONDS ))
                .build();
        return newJob;
    }

    public void cancelJob(Context context){

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        //Cancel all the jobs for this package
        dispatcher.cancelAll();
        // Cancel the job for this tag
        dispatcher.cancel(JOB_TAG);

    }



    /**
     * this method adds menu item in the nav drawer dynamically
     */
//    private void addMenuItemInNavMenuDrawer() {
//
//        Menu menu = mNavigationView.getMenu();
//        Menu submenu = menu.addSubMenu(0, CHANNEL, 0, "Channels");
//
//        List<String> newsChannels = DataSource.NEWS_CHANNEL_LIST;
//        for (int i = 0; i < newsChannels.size(); i++) {
//            String item = newsChannels.get(i);
//            submenu.add(CHANNEL, mNewsChannelMap.get(item), i, item);
//        }
//        mNavigationView.invalidate();
//    }

}
