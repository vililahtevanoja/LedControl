package fi.aalto.mobilesystems.ledcontrol.ledcontrol.features.timeofday;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import fi.aalto.mobilesystems.ledcontrol.LedControl;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.PointF;

public class AutomaticTimeOfDay extends IntentService implements LocationListener {
    private static final String TAG = "AutomaticTimeOfDay";
    private static final float REFRESH_DISTANCE = 50000.0f;
    private static final long REFRESH_TIME_INTERVAL = 6 * (60 * 60 * 1000); // 6h
    private Location lastLocation;
    private GregorianCalendar sunrise;
    private GregorianCalendar sunset;
    private double currentDayFactor = 0.0f;
    private int transition = 2;
    private boolean gotLocation = false;
    private boolean canGetLocation = false;

    public AutomaticTimeOfDay() {
        super(TAG);
        this.lastLocation = new Location("");
        this.lastLocation.setLatitude(60.1841);
        this.lastLocation.setLongitude(24.8301);
        this.sunrise = new GregorianCalendar();
        this.sunrise.set(Calendar.HOUR_OF_DAY, 6);
        this.sunrise.set(Calendar.MINUTE, 0);
        this.sunset = new GregorianCalendar();
        this.sunset.set(Calendar.HOUR_OF_DAY, 18);
        this.sunset.set(Calendar.MINUTE, 0);
    }

    public static final class IntentActions {
        public static final String Start = "START_AUTOMATIC_TIME_OF_DAY";
        public static final String Update = "UPDATE_AUTOMATIC_TIME_OF_DAY";
        public static final String Stop = "STOP_AUTOMATIC_TIME_OF_DAY";
    }

    public void updateDayFactor() {
        if (this.lastLocation == null) {
            return;
        }
        double sunriseRatio = timeToDayRatio(this.sunrise);
        double sunsetRatio = timeToDayRatio(this.sunset);
        this.currentDayFactor = 0.0f;
    }

    public Location getLocation() {
        Location location = lastLocation;
        double latitude;
        double longitude;
        try {
            LocationManager locationManager = (LocationManager) LedControl.getContext()
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    try {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                REFRESH_TIME_INTERVAL,
                                REFRESH_DISTANCE,(android.location.LocationListener) this);
                        Log.d("Network", "Network Enabled");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    } catch (SecurityException ex) {
                        Log.e(TAG, "Error while requesting location", ex);
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        try {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                    REFRESH_TIME_INTERVAL,
                                    REFRESH_TIME_INTERVAL, (android.location.LocationListener) this);
                            Log.d("GPS", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        } catch (SecurityException ex) {
                            Log.e(TAG, "Error while requesting locations", ex);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    public void updateSunriseSundown() {
        this.lastLocation = getLocation();
        RequestQueue queue = Volley.newRequestQueue(LedControl.getContext());
        String url = "http://api.sunrise-sunset.org/json"
                + "?lat="+ String.format("%.7f",lastLocation.getLatitude())
                + "&lng=" + String.format("%.7f", lastLocation.getLongitude())
                + "&formatted=0";

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Received data: "+ response.toString());
                        JSONObject results;
                        try {
                            results = response.getJSONObject("results");
                            String sunriseStr = results.getString("sunrise");
                            String sunsetStr = results.getString("sunset");
                            sunrise = stringToGregorianCalendarTime(sunriseStr);
                            updateDayFactor();
                            sunset = stringToGregorianCalendarTime(sunsetStr);
                        } catch (JSONException e) {
                            Log.e(TAG, "Error while handling sunrise/sunset query results", e);
                            return;
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error on response to sunrise get: " + error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

    /**
     *
     * @param str Of form: "2015-05-21T05:05:35+00:00"
     * @return Gregorian Calenar representing the HH:MM values of the string
     */
    protected GregorianCalendar stringToGregorianCalendarTime(String str) {
        GregorianCalendar cal = new GregorianCalendar();
        String timeStr = str.split("T")[1];
        String hourStr = timeStr.split(":")[1];
        String minuteStr = timeStr.split(":")[1].substring(0, 2);
        int hour = Integer.parseInt(hourStr);
        int minute = Integer.parseInt(minuteStr);
        TimeZone currentTz = cal.getTimeZone();
        int currentTzOffsetHours = currentTz.getRawOffset() / 1000 / 60 / 60;
        cal.set(Calendar.HOUR_OF_DAY, hour + currentTzOffsetHours);
        cal.set(Calendar.MINUTE, minute);
        return cal;
    }

    protected double timeToDayRatio(GregorianCalendar cal) {
        return cal.get(Calendar.HOUR_OF_DAY) + (cal.get(Calendar.MINUTE) / 60.0) / 24.0;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            if (this.lastLocation.distanceTo(location) > REFRESH_DISTANCE) {
                this.lastLocation = location;
                updateSunriseSundown();
            }
        }
    }

    public PointF getCurrentColorPoint() {
        return new PointF();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        updateSunriseSundown();
    }
}
