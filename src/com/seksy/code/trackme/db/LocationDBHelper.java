package com.seksy.code.trackme.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocationDBHelper extends SQLiteOpenHelper {

  private final static String DATABASE_NAME    = "location.db";

  private final static int    DATABASE_VERSION = 1;
  private final static String DATABASE_CREATE  = "create table " + DBConstants.DATABASE_TABLE
                                                 + " ("
                                                 + DBConstants.ID
                                                 + " integer primary key autoincrement, "
                                                 + DBConstants.LAT
                                                 + " real not null, "
                                                 + DBConstants.LONG
                                                 + " real not null, "
                                                 + DBConstants.TIME
                                                 + " int);";

  public LocationDBHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // drop the old table
    db.execSQL("DROP TABLE IF IT EXISTS " + DBConstants.DATABASE_TABLE);
    // recreate it
    onCreate(db);
  }

}
