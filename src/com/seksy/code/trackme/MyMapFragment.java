package com.seksy.code.trackme;

import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

public class MyMapFragment extends MapFragment {

  // a polyline to draw on the map
  private PolylineOptions poly;

  private void updateCamera(LatLng latLang) {
    // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
    CameraPosition cameraPosition = new CameraPosition.Builder().target(latLang) // Sets the center of the map to Mountain View
                                                                .zoom(17)
                                                                // Sets the zoom
                                                                .build();
    // Creates a CameraPosition from the builder
    getMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
  }

  /**
   * Adds a point to the polyline drawn on the map
   * 
   * @param location
   */
  public void addPointToPolyline(Location location) {
    //Create a new LatLng object from the given location
    LatLng newLoc = new LatLng(location.getLatitude(), location.getLongitude());
    // Instantiates a new Polyline object and adds points to define a rectangle
    if (poly == null) {
      poly = new PolylineOptions().width(40).color(Color.RED).geodesic(true);
    }
    poly.add(newLoc);
    getMap().clear();
    getMap().addPolyline(poly);
    updateCamera(newLoc);
  }

}
