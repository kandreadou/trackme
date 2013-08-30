package com.seksy.code.trackme.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class LocationProvider extends ContentProvider {

  // The datapath to the primary content
  public final static Uri         CONTENT_URI = Uri.parse("content://com.seksy.code.trackme/elements");
  // The constants used to differentiate between the different URI requests
  private static final int        ALLROWS     = 1;
  private static final int        SINGLE_ROW  = 2;
  // The UriMatcher
  private static final UriMatcher uriMatcher;
  static {
    uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    uriMatcher.addURI("com.seksy.code.trackme", "elements", ALLROWS);
    uriMatcher.addURI("com.seksy.code.trackme", "elements/#", SINGLE_ROW);
  }
  // The SQLiteOpenHelper
  private LocationDBHelper        dbHelper;

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    switch (uriMatcher.match(uri)) {
      case SINGLE_ROW:
        String rowID = uri.getPathSegments().get(1);
        selection = DBConstants.ID + "="
                    + rowID
                    + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
      default:
        break;
    }
    // If there is no selection string, delete all rows
    if (selection == null) {
      selection = String.valueOf(ALLROWS);
    }
    // Do it
    int deleteCount = db.delete(DBConstants.DATABASE_TABLE, selection, selectionArgs);
    // Notify the observers of the change in the dataset
    getContext().getContentResolver().notifyChange(uri, null);

    return deleteCount;
  }

  @Override
  public String getType(Uri uri) {
    switch (uriMatcher.match(uri)) {
      case ALLROWS:
        return "vnd.android.cursor.dir/vnd.seksycode.elemental";
      case SINGLE_ROW:
        return "vnd.android.cursor.item/vnd.seksycode.elemental";
      default:
        throw new IllegalArgumentException("Unsupported URI: " + uri);
    }
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    SQLiteDatabase db = dbHelper.getWritableDatabase();

    long id = db.insert(DBConstants.DATABASE_TABLE, null, values);

    if (id > -1) {
      Uri insertedId = ContentUris.withAppendedId(CONTENT_URI, id);
      // Notify the observers of the change in the dataset
      getContext().getContentResolver().notifyChange(insertedId, null);
      return insertedId;
    }
    return null;

  }

  @Override
  public boolean onCreate() {
    // Construct the underlying database
    dbHelper = new LocationDBHelper(getContext());
    return true;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    //Open the db
    SQLiteDatabase db;
    try {
      db = dbHelper.getWritableDatabase();
    } catch (SQLiteException ex) {
      db = dbHelper.getReadableDatabase();
    }
    SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

    switch (uriMatcher.match(uri)) {
      case SINGLE_ROW:
        String rowID = uri.getPathSegments().get(1);
        builder.appendWhere(DBConstants.ID + "=" + rowID);
      default:
        break;
    }
    builder.setTables(DBConstants.DATABASE_TABLE);

    return builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    switch (uriMatcher.match(uri)) {
      case SINGLE_ROW:
        String rowID = uri.getPathSegments().get(1);
        selection = DBConstants.ID + "="
                    + rowID
                    + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
      default:
        break;
    }
    // If there is no selection string, delete all rows
    if (selection == null) {
      selection = String.valueOf(ALLROWS);
    }
    // Do it
    int updateCount = db.update(DBConstants.DATABASE_TABLE, values, selection, selectionArgs);
    // Notify the observers of the change in the dataset
    getContext().getContentResolver().notifyChange(uri, null);

    return updateCount;
  }

}
