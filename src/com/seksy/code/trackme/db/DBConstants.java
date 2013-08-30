package com.seksy.code.trackme.db;

public class DBConstants {

  // The index key column name
  public static final String   ID             = "_id";
  // The latitude column name
  public static final String   LAT            = "LATITUDE";
  // The longitude column name
  public static final String   LONG           = "LONGITUTE";
  // The timestamp column name
  public static final String   TIME           = "TIMESTAMP";
  // The database table
  static final String          DATABASE_TABLE = "LocationPoints";

  public final static String[] PROJECTION     = new String[] {
      DBConstants.ID, DBConstants.LAT, DBConstants.LONG, DBConstants.TIME
                                              };
}
