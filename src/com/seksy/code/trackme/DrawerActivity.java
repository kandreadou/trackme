package com.seksy.code.trackme;

import android.app.FragmentManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.MapFragment;

public class DrawerActivity extends AbstractBoundActivity {

  private DrawerClickListener drawerClickListener = new DrawerClickListener();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_drawer);
    /* register the drawer items to receive click events */
    findViewById(R.id.map).setOnClickListener(drawerClickListener);
    findViewById(R.id.list).setOnClickListener(drawerClickListener);
  }

  @Override
  public void onNewTracking(Location loc) {
    // TODO Auto-generated method stub

  }

  private void showMap() {
    MapFragment fragment = new MapFragment();
    FragmentManager fragmentManager = getFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
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
          break;
        default:
          break;
      }
    }

  }

}
