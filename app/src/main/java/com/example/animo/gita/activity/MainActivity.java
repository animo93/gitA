package com.example.animo.gita.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.animo.gita.CircleTransform;
import com.example.animo.gita.FetchActivityService;
import com.example.animo.gita.R;
import com.example.animo.gita.fragments.MainActivityFragment;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private View navHeader;
    private ImageView imgNavHeaderBg,imgProfile;
    private TextView txtName,txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";

    public static int navItemIndex = 0;

    private static final String TAG_HOME = "home";
    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_MOVIES = "movies";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;

    private String[] activityTitles;

    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(MainActivity.class.getSimpleName(),"inside onCreate");
        //setContentView(R.layout.navigation_drawer);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //scheduleJob();

       /* mHandler = new Handler();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        Log.e(MainActivity.class.getSimpleName(),activityTitles[0]);*/

        /*fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        /*loadNavHeader();

        setupNavigationView();

        if(savedInstanceState == null){
            navItemIndex =0 ;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }*/
    }

    private void scheduleJob() {
        //Every 12 hours periodically expressed as seconds
        final int periodicity = (int) java.util.concurrent.TimeUnit.HOURS.toSeconds(1);
        // A small windows of time when triggering is OK
        final int toleranceLevel = (int) TimeUnit.HOURS.toSeconds(1);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));
        Bundle bundle = new Bundle();
        Job myJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(FetchActivityService.class)
                // uniquely identifies the job
                .setTag("my-unique-tag")
                // one-off job
                .setRecurring(false)
                // don't persist past a device reboot
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(0, 80))
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(false)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                // constraints that need to be satisfied for the job to run
                .setConstraints(
                        // only run on an unmetered network
                        Constraint.ON_ANY_NETWORK

                )
                .setExtras(bundle)
                .build();

        dispatcher.mustSchedule(myJob);
    }

    private void setupNavigationView() {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.nav_home:
                                navItemIndex=0;
                                CURRENT_TAG = TAG_HOME;
                                break;
                            case R.id.nav_photos:
                                navItemIndex = 1;
                                CURRENT_TAG = TAG_PHOTOS;
                                break;
                            case R.id.nav_movies:
                                navItemIndex = 2;
                                CURRENT_TAG = TAG_MOVIES;
                                break;
                            case R.id.nav_notifications:
                                navItemIndex = 3;
                                CURRENT_TAG = TAG_NOTIFICATIONS;
                                break;
                            case R.id.nav_settings:
                                navItemIndex = 4;
                                CURRENT_TAG = TAG_SETTINGS;
                                break;
                            /*case R.id.nav_about_us:
                                // launch new intent instead of loading fragment
                                startActivity(new Intent(MainActivity.this, null));
                                drawerLayout.closeDrawers();
                                return true;*/
                           /* case R.id.nav_log_out:
                                // launch new intent instead of loading fragment
                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                if(auth!=null){
                                    auth.signOut();
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                }
                                drawerLayout.closeDrawers();
                                return true;*/
                            default:
                                navItemIndex = 0;
                        }

                        if(item.isChecked())
                            item.setChecked(false);
                        else
                            item.setChecked(true);
                        item.setChecked(true);

                        loadHomeFragment();
                        return true;
                    }
                }
        );

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.openDrawer,
                R.string.closeDrawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
    }



    private void loadNavHeader() {
        txtName.setText("Animo");
        txtWebsite.setText("www.semiti.com");

        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        Glide.with(this).load(R.drawable.github_logo)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);
    }

    private void loadHomeFragment() {

        selectNavMenu();

        setToolbarTitle();

        if(getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawerLayout.closeDrawers();

            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.content_frame,fragment,CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        toggleFab();

        drawerLayout.closeDrawers();

        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                //home
                MainActivityFragment mainActivityFragment = new MainActivityFragment();
                return mainActivityFragment;
            case 1:
                // photos
                /*PhotosFragment photosFragment = new PhotosFragment();
                return photosFragment;*/
            case 2:
                // movies fragment
                /*MoviesFragment moviesFragment = new MoviesFragment();
                return moviesFragment;*/
            case 3:
                // notifications fragment
                /*NotificationsFragment notificationsFragment = new NotificationsFragment();
                return notificationsFragment;*/

            case 4:
                // settings fragment
                /*SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;*/
            default:
                return new MainActivityFragment();
        }
    }

    private void toggleFab() {
        if(navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }

    private void setToolbarTitle() {
        Log.e(MainActivity.class.getSimpleName(), "setToolbarTitle "+activityTitles[navItemIndex]);
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(LOG_TAG,"inside on Destroy");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if(auth!=null){
                auth.signOut();
                finish();
                //startActivity(new Intent(MainActivity.this, LoginActivity.class));
                Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(
                        getBaseContext().getPackageName()
                );
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
