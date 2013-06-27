package com.seksy.code.trackme;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.seksy.code.trackme.services.LocService;
import com.seksy.code.trackme.services.LocService.LocalBinder;
import com.seksy.code.trackme.services.LocService.TrackingListener;

/**
 * An abstract activity bound to the location service in order to get location updates
 * 
 * @author katerina
 * 
 */
public abstract class AbstractBoundActivity extends Activity implements TrackingListener {

  private boolean mBound = false;

  @Override
  public void onStart() {
    super.onStart();
    // Bind to LocService
    final Intent intent = new Intent(this, LocService.class);
    bindService(intent, this.mConnection, Context.BIND_AUTO_CREATE);
  }

  @Override
  public void onStop() {

    // Unbind from the service
    if (this.mBound) {
      unbindService(this.mConnection);
      this.mBound = false;
    }
    super.onStop();
  }

  /** Defines callbacks for service binding, passed to bindService() */
  private final ServiceConnection mConnection = new ServiceConnection() {

                                                LocalBinder binder;

                                                @Override
                                                public void onServiceConnected(final ComponentName className,
                                                                               final IBinder service) {
                                                  // We've bound to LocService, cast the IBinder and get
                                                  // P4SPushService instance
                                                  binder = (LocalBinder) service;
                                                  binder.getService()
                                                        .setTrackingListener(AbstractBoundActivity.this);
                                                  mBound = true;
                                                }

                                                @Override
                                                public void onServiceDisconnected(final ComponentName arg0) {
                                                  mBound = false;
                                                  binder.getService().setTrackingListener(null);
                                                }
                                              };

}
