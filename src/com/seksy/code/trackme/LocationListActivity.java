package com.seksy.code.trackme;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.seksy.code.trackme.db.DBConstants;
import com.seksy.code.trackme.db.LocationProvider;

public class LocationListActivity extends AbstractBoundActivity implements LoaderCallbacks<Cursor> {

  private CursorAdapter         adapter;
  private ListView              list;

  private final static String[] projection = new String[] {
      DBConstants.ID, DBConstants.LAT, DBConstants.LONG, DBConstants.TIME
                                           };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);
    list = (ListView) findViewById(android.R.id.list);
    getLoaderManager().initLoader(0, null, this);
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
  private void saveLocation(Location loc) {
    ContentValues values = new ContentValues();
    values.put(DBConstants.LAT, loc.getLatitude());
    values.put(DBConstants.LONG, loc.getLongitude());
    values.put(DBConstants.TIME, System.currentTimeMillis());
    getContentResolver().insert(LocationProvider.CONTENT_URI, values);
  }

  //////////////////////////////////////////////
  /////////////// Cursor adapter     ///////////
  /////////////////////////////////////////////

  class LocationAdapter extends CursorAdapter {

    public LocationAdapter(Context context, Cursor c) {
      super(context, c, true);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
      TextView latitude = (TextView) view.findViewById(R.id.latitude);
      TextView longitude = (TextView) view.findViewById(R.id.longitude);
      TextView date = (TextView) view.findViewById(R.id.date);
      float lat = cursor.getFloat(cursor.getColumnIndexOrThrow(DBConstants.LAT));
      float lng = cursor.getFloat(cursor.getColumnIndexOrThrow(DBConstants.LONG));
      double time = cursor.getDouble(cursor.getColumnIndexOrThrow(DBConstants.TIME));
      latitude.setText(String.valueOf(lat));
      longitude.setText(String.valueOf(lng));
      date.setText(String.valueOf(time));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
      LayoutInflater inflater = LayoutInflater.from(context);
      View v = inflater.inflate(R.layout.list_item, parent, false);
      bindView(v, context, cursor);
      return v;
    }

  }

  //////////////////////////////////////////////
  /////////////// Loader Calllbacks ///////////
  /////////////////////////////////////////////

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new CursorLoader(this, LocationProvider.CONTENT_URI, projection, null, null, null);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    adapter.swapCursor(null);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    // This seems to be necessary to get notifications from the ContentProvider
    cursor.setNotificationUri(getContentResolver(), LocationProvider.CONTENT_URI);
    if (adapter == null) {
      adapter = new LocationAdapter(this, cursor);
      list.setAdapter(adapter);
    }
    adapter.swapCursor(cursor);
  }
}
