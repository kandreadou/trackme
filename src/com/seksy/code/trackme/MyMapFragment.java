package com.seksy.code.trackme;

import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.seksy.code.trackme.db.DBConstants;
import com.seksy.code.trackme.db.LocationProvider;

public class MyMapFragment extends MapFragment {

  // a polyline to draw on the map
  private PolylineOptions    poly;
  // the content observer
  private LocationDBObserver observer;
  // the handler for the content observer
  private Handler            handler = new Handler();

  @Override
  public void onResume() {
    super.onResume();
    if (observer == null) {
      observer = new LocationDBObserver(handler);
      getActivity().getContentResolver()
                   .registerContentObserver(LocationProvider.CONTENT_URI, true, observer);
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if (observer != null) {
      getActivity().getContentResolver().unregisterContentObserver(observer);
      observer = null;
    }
  }

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
  public void addPointToPolyline(LatLng latLng) {

    // Instantiates a new Polyline object and adds points to define a rectangle
    if (poly == null) {
      poly = new PolylineOptions().width(40).color(Color.RED).geodesic(true);
    }
    poly.add(latLng);
    getMap().clear();
    getMap().addPolyline(poly);
    updateCamera(latLng);
  }

  private class LocationDBObserver extends ContentObserver {

    public LocationDBObserver(Handler handler) {
      super(handler);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
      super.onChange(selfChange, uri);
      Cursor cursor = getActivity().getContentResolver().query(uri, DBConstants.PROJECTION, null, null, null);

      if (!cursor.moveToFirst()) {
        cursor.close();
        return;
      }
      float lat = cursor.getFloat(cursor.getColumnIndexOrThrow(DBConstants.LAT));
      float lng = cursor.getFloat(cursor.getColumnIndexOrThrow(DBConstants.LONG));
      cursor.close();
      LatLng newLoc = new LatLng(lat, lng);
      addPointToPolyline(newLoc);
    }

  }

}
