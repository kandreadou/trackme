package com.seksy.code.trackme;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.seksy.code.trackme.db.DBConstants;
import com.seksy.code.trackme.db.LocationProvider;

public class DrawerActivity extends AbstractBoundActivity {

    private final static String FRAGMENT_TAG_MAP    = "TAG_MAP";
    private final static String FRAGMENT_TAG_LIST   = "TAG_LIST";

    private DrawerClickListener drawerClickListener = new DrawerClickListener();
    private DrawerLayout        mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout        mLeftDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftDrawer = (LinearLayout) findViewById(R.id.left_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(R.string.app_name);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(R.string.app_name);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

    /* register the drawer items to receive click events */
        findViewById(R.id.map).setOnClickListener(drawerClickListener);
        findViewById(R.id.list).setOnClickListener(drawerClickListener);
    /* show map by default at the beginning */
        showMap();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNewTracking(Location loc) {
        saveLocation(loc);
    }

    /**
     * Saves a location to the DB
     *
     * @param loc
     */
    public void saveLocation(Location loc) {
        ContentValues values = new ContentValues();
        values.put(DBConstants.LAT, loc.getLatitude());
        values.put(DBConstants.LONG, loc.getLongitude());
        values.put(DBConstants.TIME, System.currentTimeMillis());
        getContentResolver().insert(LocationProvider.CONTENT_URI, values);
    }

    private void showMap() {
        FragmentManager fm = getFragmentManager();
        Fragment current = fm.findFragmentByTag(FRAGMENT_TAG_MAP);
        if (current == null) {
            current = new MyMapFragment();
        }
        fm.beginTransaction().replace(R.id.content_frame, current, FRAGMENT_TAG_MAP).commit();
    }

    private void showList() {
        FragmentManager fm = getFragmentManager();
        Fragment current = fm.findFragmentByTag(FRAGMENT_TAG_LIST);
        if (current == null) {
            current = new MyListFragment();
        }
        fm.beginTransaction().replace(R.id.content_frame, current, FRAGMENT_TAG_LIST).commit();
    }

    /**
     * A ClickListener for the Drawer TextViews
     *
     * @author katerina
     *
     */
    private class DrawerClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.map:
                    showMap();
                    break;
                case R.id.list:
                    showList();
                    break;
                default:
                    break;
            }
            mDrawerLayout.closeDrawer(mLeftDrawer);
        }
    }

}
