package com.seksy.code.trackme;

import android.app.Fragment;
import android.app.FragmentManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.maps.MapFragment;

public class DrawerActivity extends AbstractBoundActivity {

  private DrawerClickListener drawerClickListener = new DrawerClickListener();
  private Fragment            currentFragment;
  private DrawerLayout        mDrawerLayout;
  private LinearLayout        mLeftDrawer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_drawer);
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mLeftDrawer = (LinearLayout) findViewById(R.id.left_drawer);
    /* register the drawer items to receive click events */
    findViewById(R.id.map).setOnClickListener(drawerClickListener);
    findViewById(R.id.list).setOnClickListener(drawerClickListener);
  }

  @Override
  public void onNewTracking(Location loc) {
    if (currentFragment == null) {
      currentFragment = getFragmentManager().findFragmentById(R.id.content_frame);
    }
    if (currentFragment != null) {
      if (currentFragment instanceof MapFragment) {
        ((MyMapFragment) currentFragment).addPointToPolyline(loc);
      } else {
        ((MyListFragment) currentFragment).saveLocation(loc);
      }
    }

  }

  private void showMap() {
    currentFragment = new MyMapFragment();
    FragmentManager fragmentManager = getFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.content_frame, currentFragment).commit();
  }

  private void showList() {
    currentFragment = new MyListFragment();
    FragmentManager fragmentManager = getFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.content_frame, currentFragment).commit();
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
