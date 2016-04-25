package fi.aalto.mobilesystems.ledcontrol.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import fi.aalto.mobilesystems.ledcontrol.LedControl;
import fi.aalto.mobilesystems.ledcontrol.R;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.Curve;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.PointF;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.SimpleColorTemperatureCurve;

/**
 * Implements automatic time-of-day feature.
 *
 * The class uses location providers to get device location and uses a public API to get the
 * sunrise/sunset-times which it uses to direct the Hue light states.
 */
public class AutomaticTimeOfDay extends IntentService implements LocationListener {
    private static final String TAG = "AutomaticTimeOfDay";
    private static final float REFRESH_DISTANCE = 50000.0f;
    private static final long REFRESH_TIME_INTERVAL = 6 * (60 * 60 * 1000); // 6h
    private static final int TEMPERATURE_CURVE_REFINE_STEPS = 5;
    private static final String CLASS_PREFERENCE_KEY = "timeofday";
    private static final String IS_AUTOMATIC_PREFERENCE_KEY = CLASS_PREFERENCE_KEY + ".isAutomatic";
    private Location lastLocation;
    private Calendar sunrise;
    private Calendar sunset;
    private Curve colorTemperatureCurve;
    private double transitionHours;
    private boolean canGetLocation;
    private boolean enabled;
    private boolean halted;

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
        this.transitionHours = 2.0;
        this.colorTemperatureCurve = SimpleColorTemperatureCurve.getRefinedCurve(TEMPERATURE_CURVE_REFINE_STEPS);
    }

    /**
     * Contains Intent action string for this service
     */
    public static final class IntentActions {
        public static final String Start = LedControl.getStringResource(R.string.automatictimeofday_action_start);
        public static final String UpdateData = LedControl.getStringResource(R.string.automatictimeofday_action_update_data);
        public static final String UpdateLighting = LedControl.getStringResource(R.string.automatictimeofday_action_update_lighting);
        public static final String Stop = LedControl.getStringResource(R.string.automatictimeofday_action_stop);
        public static final String Halt = LedControl.getStringResource(R.string.automatictimeofday_action_halt);
        public static final String Resume = LedControl.getStringResource(R.string.automatictimeofday_action_resume);
    }
    /**
     * Gets current location.
     *
     * The method tries both network and GPS location providers and if neither of those works the
     * location is left unchanged.
     * @return Updated or current this.lastLocation.
     */
    public Location getLocation() {
        Location location = null;
        try {
            LocationManager locationManager = (LocationManager) LedControl.getContext()
                    .getSystemService(LOCATION_SERVICE);

            if (locationManager == null) {
                this.canGetLocation = false;
                return this.lastLocation;
            }
            location = lastLocation;
            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                this.canGetLocation = false;
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    try {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                REFRESH_TIME_INTERVAL,
                                REFRESH_DISTANCE,(android.location.LocationListener) this);
                        Log.d("Network", "Network Enabled");
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null && this.lastLocation.distanceTo(location) > REFRESH_DISTANCE) {
                            this.lastLocation = location;
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
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null && this.lastLocation.distanceTo(location) > REFRESH_DISTANCE) {
                                this.lastLocation = location;
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

    /**
     * Updates sunrise/sundown data by querying a public API at api.sunrise-sunset.org
     */
    public void updateSunriseSundown() {
        this.lastLocation = getLocation();
        Log.i(TAG, "Setting up sunrise/sundown time update query");
        RequestQueue queue = Volley.newRequestQueue(LedControl.getContext());
        String url = String.format(Locale.US, "http://api.sunrise-sunset.org/json"
                + "?lat=%.7f&lng=%.7f&formatted=0",
                lastLocation.getLatitude(), lastLocation.getLongitude());

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
                            sunrise = stringToCalendar(sunriseStr);
                            sunset = stringToCalendar(sunsetStr);
                            Log.i(TAG, "Sunrise/sunset data updated");
                        } catch (JSONException e) {
                            Log.e(TAG, "Error while handling sunrise/sunset query results", e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error on response to sunrise get: " + error.getMessage());
            }
        });
        Log.i(TAG, "Sunrise/sunset time update query sent to queue");
        queue.add(stringRequest);
    }

    /**
     * Converts datetime string to a Calendar-object
     *
     * @param dateStr Of form: "2015-05-21T05:05:35+00:00"
     * @return Calendar-object representing the HH:MM values of the string
     */
    protected Calendar stringToCalendar(String dateStr) {
        String dateFormatString = "yyyy-MM-dd'T'HH:mm:ssZZZZZ";
        DateFormat format = new SimpleDateFormat(dateFormatString);
        Date date;
        try {
            date = format.parse(dateStr);
        } catch (ParseException ex) {
            Log.e(TAG, "Error while parsing " + dateStr
                    + " against format string " + dateFormatString);
            return null;
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal;
    }

    /**
     * Calculates given time to ratio of day
     * e.g. 12:00 -> 0.5, 18:00 -> 0.75
     * @param cal Current time as a Calendar object
     * @return
     */
    protected double timeToDayRatio(Calendar cal) {
        return (cal.get(Calendar.HOUR_OF_DAY) + (cal.get(Calendar.MINUTE) / 60.0)) / 24.0;
    }

    /**
     * Implements LocationListener. Is called when the current device location has changed
     * @param location New location
     */
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            if (this.lastLocation.distanceTo(location) > REFRESH_DISTANCE) {
                this.lastLocation = location;
                updateSunriseSundown();
            }
        }
    }

    /**
     * Converts given time to hours.
     * e.g. 17:15 -> 17.25, 12:20 -> 12.33333
     * @param cal
     * @return
     */
    protected double timeToHours(Calendar cal) {
        return (cal.get(Calendar.HOUR_OF_DAY) + (cal.get(Calendar.MINUTE) / 60.0));
    }

    public double getTransitionValue(double currentDayRatio) {
        double x;
        double y;
        double k;

        double sunriseDayRatio = timeToDayRatio(this.sunrise);
        double sunsetDayRatio = timeToDayRatio(this.sunset);
        double transitionDayRatio = this.transitionHours / 24;

        if (currentDayRatio > sunriseDayRatio
                && currentDayRatio < sunsetDayRatio - transitionDayRatio) {
            return 1.0;
        }
        else if (currentDayRatio < sunriseDayRatio - transitionDayRatio || currentDayRatio > sunriseDayRatio) {
            return 0.0;
        }
        else if (currentDayRatio >= sunriseDayRatio - transitionDayRatio
                && currentDayRatio < sunsetDayRatio - transitionDayRatio) {
            double transitionStart = sunriseDayRatio - transitionDayRatio;
            x = currentDayRatio - transitionStart;
            k = 1.0 / transitionDayRatio;
            y = 0.0;
        }
        else {
            double transitionStart = sunsetDayRatio - transitionDayRatio;
            x = currentDayRatio - transitionStart;
            k = -(1.0 / transitionDayRatio);
            y = 1.0;
        }
        return (k*y*x);
    }

    /**
     * Calculates current color point in the Hue XY-colorspace
     * @return
     */
    public PointF getCurrentColorPoint() {
        GregorianCalendar cal = new GregorianCalendar();
        double currentDayRatio = timeToDayRatio(cal);
        double transitionValue = this.getTransitionValue(currentDayRatio);
        return this.colorTemperatureCurve.getPointOnCurve(transitionValue);
    }

    /**
     * Updates Hue light states according to the time of day, last known location and sunrise/sunset
     * data for that location.
     */
    protected void updateLighting() {
        PHHueSDK sdk = PHHueSDK.getInstance();
        PHLightState state = new PHLightState();
        PointF p = this.getCurrentColorPoint();
        state.setX(p.x);
        state.setY(p.y);
        Log.i(TAG, "Updating light color to " + p.toString());
        for (PHBridge bridge : sdk.getAllBridges()) {
            for (PHLight light : bridge.getResourceCache().getAllLights()) {
                bridge.updateLightState(light, state);
            }
        }
    }

    /**
     * Implements IntentService. Is used to receive commands for the feature.s
     * @param intent Intent for this service
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        String intentAction = intent.getAction();
        Log.i(TAG, "Intent received with action " + intentAction);
        if (intent.getAction().equals(IntentActions.Start)) {
            this.enabled = true;
            this.halted = false;
            updateSunriseSundown();
            updateLighting();
        }
        else if (intent.getAction().equals(IntentActions.Stop)) {
            this.enabled = false;
        }
        else if (intent.getAction().equals(IntentActions.Halt)) {
            this.halted = true;
        }
        else if (intent.getAction().equals(IntentActions.Resume)) {
            this.halted = false;
        }
        else if (intent.getAction().equals(IntentActions.UpdateData)) {
            if (this.enabled) {
                Log.i(TAG, "Updating sunrise/sundown data");
                updateSunriseSundown();
            }
        }
        else if (intent.getAction().equals(IntentActions.UpdateLighting)) {
            if (this.enabled && !this.halted) {
                updateLighting();
            }
        }
    }
}
