package com.seksy.code.trackme;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends AbstractBoundActivity {

  private GoogleMap       mMap;
  // a polyline to draw on the map
  private PolylineOptions poly;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.activity_main, menu);
    return true;
  }

  private void updateCamera(LatLng latLang) {
    // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
    CameraPosition cameraPosition = new CameraPosition.Builder().target(latLang) // Sets the center of the map to Mountain View
                                                                .zoom(17)
                                                                // Sets the zoom
                                                                .build();
    // Creates a CameraPosition from the builder
    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
  }

  /**
   * Adds a marker to the map given a location
   * 
   * @param location
   */
  private void addMarker(Location location) {
    final LatLng test = new LatLng(location.getLatitude(), location.getLongitude());
    Marker melbourne = mMap.addMarker(new MarkerOptions().position(test).title("Test").snippet("snippet"));
  }

  /**
   * Adds a point to the polyline drawn on the map
   * 
   * @param location
   */
  private void addPointToPolyline(Location location) {
    //Create a new LatLng object from the given location
    LatLng newLoc = new LatLng(location.getLatitude(), location.getLongitude());
    // Instantiates a new Polyline object and adds points to define a rectangle
    if (poly == null) {
      poly = new PolylineOptions().width(40).color(Color.RED).geodesic(true);
    }
    poly.add(newLoc);
    mMap.clear();
    mMap.addPolyline(poly);
    updateCamera(newLoc);
  }

  @Override
  public void onNewTracking(Location loc) {
    //addMarker(loc);
    addPointToPolyline(loc);
  }
}
