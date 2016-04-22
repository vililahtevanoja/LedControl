package fi.aalto.mobilesystems.ledcontrol.services;

import android.content.SharedPreferences;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.Calendar;
import java.util.GregorianCalendar;

import fi.aalto.mobilesystems.ledcontrol.LedControl;
import fi.aalto.mobilesystems.ledcontrol.R;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.Curve;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.PointF;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.SimpleColorTemperatureCurve;

public class SimpleTimeOfDay extends IntentService implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "SimpleTimeOfDay";
    private static final int DEFAULT_MORNING = 6;
    private static final int DEFAULT_NIGHT = 22;
    private static final int DEFAULT_TRANSITION = 1;
    private static final int CURVE_REFINE_STEPS = 5;
    private static final String classPrefkey = "timeofday";
    private static final String morningHourPrefkey = classPrefkey + ".morningHour";
    private static final String nightHourPrefKey = classPrefkey + ".nightHour";
    private static final String transitionPrefKey = classPrefkey + ".transitionHours";
    private static final String enabledPrefKey = classPrefkey + ".enabled";
    private int morningHour;
    private int nightHour;
    private int transition;
    private boolean enabled;
    private Curve colourTemperatureCurve;
    private SharedPreferences sharedPrefs;

    public static class IntentActions {
        private final static String prefix = SimpleTimeOfDay.class.getName();
        public final static String Start = prefix + ".START";
        public final static String Stop = prefix + ".STOP";
        public final static String Update = prefix + ".UPDATE";
    }

    public SimpleTimeOfDay() {
        super(TAG);
        this.initialize();
    }

    public SimpleTimeOfDay(String name) {
        super(name);
        this.initialize();
    }

    protected SimpleTimeOfDay(String name, int morningHour, int nightHour, int transition) {
        super(name);
        this.morningHour = morningHour;
        this.nightHour = nightHour;
        this.transition = transition;
        this.colourTemperatureCurve = SimpleColorTemperatureCurve.getRefinedCurve(CURVE_REFINE_STEPS);
    }

    protected void initialize() {
        Context ctx = LedControl.getContext();
        String preferencesKey = ctx.getString(R.string.shared_preferences_key);
        this.sharedPrefs = LedControl.getContext().getSharedPreferences(preferencesKey, MODE_PRIVATE);
        this.updateTimes();
        if (this.sharedPrefs.getBoolean(enabledPrefKey, false)) {
            this.enabled = true;
        }
        this.colourTemperatureCurve = SimpleColorTemperatureCurve.getRefinedCurve(5);
    }

    protected void updateTimes() {
        Log.i(TAG, "Updating configured times");
        SharedPreferences.Editor editor = this.sharedPrefs.edit();
        int morning = this.sharedPrefs.getInt(morningHourPrefkey, -1);
        int night = this.sharedPrefs.getInt(nightHourPrefKey, -1);
        int transition = this.sharedPrefs.getInt(transitionPrefKey, -1);
        if (morning < 0) {
            editor.putInt(morningHourPrefkey, DEFAULT_MORNING);
        }
        if (night < 0) {
            editor.putInt(nightHourPrefKey, DEFAULT_NIGHT);
        }
        if (transition < 0) {
            editor.putInt(transitionPrefKey, DEFAULT_TRANSITION);
        }
        editor.apply();
        this.morningHour = morning;
        this.nightHour = night;
        this.transition = transition;
        Log.i(TAG, "Morning: " + this.morningHour
                + ", night: " + this.nightHour
                + ", transition: " + this.transition);

    }

    protected double getTransitionValue(int hour, int minute) {
        double k;
        double x;
        double y;
        if (hour >= this.morningHour && hour < this.nightHour - this.transition) {
            return 1.0;
        }
        else if (hour < this.morningHour - transition || hour >= this.nightHour) {
            return 0.0;
        }
        else if (hour >= this.morningHour - transition && hour < this.nightHour - this.transition) {
            double time = hour + (minute / 60.0);
            double transitionStart = this.morningHour - this.transition;
            x = time - transitionStart;
            k = 1.0 / this.transition;
            y = 0.0;
        }
        else {
            double time = hour + (minute / 60.0);
            double transitionStart = this.nightHour - this.transition;
            x = time - transitionStart;
            k = -(1.0 / this.transition);
            y = 1.0;
        }
        return (k*x+y);
    }

    public PointF getCurrentColorPoint() {
        GregorianCalendar cal = new GregorianCalendar();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        double transitionValue = this.getTransitionValue(hour, minute);
        return this.colourTemperatureCurve.getPointOnCurve(transitionValue);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        PHHueSDK sdk = PHHueSDK.getInstance();
        String action = intent.getAction();
        Log.i(TAG, "Intent received with action " + action);
        if (action == null)
            return;
        if (action.equals(IntentActions.Stop)) {
            this.enabled = false;
        }
        else if (action.equals(IntentActions.Start)) {
            this.enabled = true;
            updateTimes();
        }
        else if (action.equals(IntentActions.Update)) {
            if (!this.enabled) {
                return;
            }
        }
        PHLightState state = new PHLightState();
        PointF p = getCurrentColorPoint();
        state.setX(p.x);
        state.setY(p.y);
        Log.i(TAG, "Updating light color to " + p.toString());
        for (PHBridge bridge : sdk.getAllBridges()) {
            for (PHLight light : bridge.getResourceCache().getAllLights()) {
                bridge.updateLightState(light, state);
            }
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.i(TAG, "Shared preference cache changed");
        if (key.equals(morningHourPrefkey) || key.equals(nightHourPrefKey) && key.equals(transitionPrefKey)) {
            updateTimes();
        }
    }
}
