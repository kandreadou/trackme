package com.seksy.code.trackme.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

public class LocService extends Service implements LocationListener {

  private static final int MIN_TIME     = 5000; //two minutes
  private static final int MIN_DISTANCE = 500; // 500 meters
  private LocationManager  locationManager;
  private TrackingListener listener;

  public void setTrackingListener(TrackingListener listener) {
    this.listener = listener;
  }

  // Binder given to clients
  private final IBinder mBinder = new LocalBinder();

  @Override
  public IBinder onBind(Intent intent) {
    startService(intent);
    return mBinder;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    return super.onStartCommand(intent, flags, startId);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (locationManager != null) {
      locationManager.removeUpdates(this);
    }
  }

  ///////////////////////////////////////
  //////// LocationListener methods
  //////////////////////////////////////

  @Override
  public void onLocationChanged(Location location) {
    //TODO: Filter the location first
    if (listener != null) {
      listener.onNewTracking(location);
    }

  }

  @Override
  public void onProviderDisabled(String provider) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onProviderEnabled(String provider) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
    // TODO Auto-generated method stub

  }

  /////////////////////////////////////////////
  //////////// Inner classes 
  //////////////////////////////////////////

  public interface TrackingListener {
    public void onNewTracking(Location loc);
  }

  public class LocalBinder extends Binder {
    public LocService getService() {
      return LocService.this;
    }
  }
}
