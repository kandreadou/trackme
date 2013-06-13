package com.seksy.code.trackme.utils;

import android.location.Location;

public class Utils {

  private final static int VELOCITY_THRESHOLD = 200;
  private final static int ACCURARY_PERCENT   = 10;
  private final static int TIME_THRESHOLD     = 30000;

  public static boolean isNewLocationAcceptable(Location previous, Location current) {
    float velocity = calcVelocity(previous, current);
    return velocity <= VELOCITY_THRESHOLD && isAccuracyDiffAcceptable(previous, current)
           && (current.getTime() - previous.getTime() <= TIME_THRESHOLD);
  }

  /** Calculates the velocity between two locations */
  private static float calcVelocity(Location previous, Location current) {
    float[] results = new float[1];
    Location.distanceBetween(previous.getLatitude(),
                             previous.getLongitude(),
                             current.getLatitude(),
                             current.getLongitude(),
                             results);
    float velocity = results[0] / ((current.getTime() - previous.getTime()) / 1000);
    return velocity;
  }

  /** Checks if the difference in accuracy between two points is acceptable */
  private static boolean isAccuracyDiffAcceptable(Location previous, Location current) {
    float previousAccur = previous.getAccuracy();
    float currentAccur = current.getAccuracy();
    float accurDifference = Math.abs(previousAccur - currentAccur);
    return isSameProvider(previous.getProvider(), current.getProvider()) && accurDifference <= previousAccur / ACCURARY_PERCENT;
  }

  /** Checks whether two providers are the same */
  private static boolean isSameProvider(String provider1, String provider2) {
    if (provider1 == null) {
      return provider2 == null;
    }
    return provider1.equals(provider2);
  }
}
