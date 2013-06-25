package com.seksy.code.trackme.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class LocationDBHelper extends SQLiteOpenHelper {

  private final static String DATABASE_NAME    = "location.db";
  private final static String DATABASE_TABLE   = "LocationPoints";
  private final static int    DATABASE_VERSION = 1;
  private final static String DATABASE_CREATE  = "create table " + DATABASE_TABLE
                                                 + " ("
                                                 + DBConstants.ID
                                                 + " integer primary key autoincrement, "
                                                 + DBConstants.LAT
                                                 + " real not null, "
                                                 + DBConstants.LONG
                                                 + " real not null, "
                                                 + DBConstants.TIME
                                                 + " int);";

  public LocationDBHelper(Context context, CursorFactory factory) {
    super(context, DATABASE_NAME, factory, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // drop the old table
    db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
    // recreate it
    onCreate(db);
  }

}
