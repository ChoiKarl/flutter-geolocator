package com.baseflow.geolocator.location;

import android.location.Location;
import android.os.Build;

import java.util.HashMap;
import java.util.Map;

public class LocationMapper {
  public static Map<String, Object> toHashMap(Location location) {
    if (location == null) {
      return null;
    }

    Map<String, Object> position = new HashMap<>();

    position.put("latitude", location.getLatitude());
    position.put("longitude", location.getLongitude());
    position.put("timestamp", location.getTime());
    position.put("is_mocked", isMocked(location));

    if (location.hasAltitude()) position.put("altitude", location.getAltitude());
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && location.hasVerticalAccuracy())
        position.put("altitude_accuracy", location.getVerticalAccuracyMeters());
    if (location.hasAccuracy()) position.put("accuracy", (double) location.getAccuracy());
    if (location.hasBearing()) position.put("heading", (double) location.getBearing());
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && location.hasBearingAccuracy())
        position.put("heading_accuracy", location.getBearingAccuracyDegrees());
    if (location.hasSpeed()) position.put("speed", (double) location.getSpeed());
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && location.hasSpeedAccuracy())
      position.put("speed_accuracy", (double) location.getSpeedAccuracyMetersPerSecond());

    if (location.getExtras() != null) {
      if (location.getExtras().containsKey(NmeaClient.NMEA_ALTITUDE_EXTRA)) {
        Double mslAltitude = location.getExtras().getDouble(NmeaClient.NMEA_ALTITUDE_EXTRA);
        position.put("altitude", mslAltitude);
      }
    }
    return position;
  }

  @SuppressWarnings("deprecation")
  private static boolean isMocked(Location location) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      return location.isMock();
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      return location.isFromMockProvider();
    } else {
      return false;
    }
  }
}
