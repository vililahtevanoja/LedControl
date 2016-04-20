package fi.aalto.mobilesystems.ledcontrol.ledcontrol.features.timeofday;

import android.content.SharedPreferences;
import android.util.Pair;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import fi.aalto.mobilesystems.ledcontrol.LedControl;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.Curve;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.PointF;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.SimpleColorTemperatureCurve;

public class SimpleTimeOfDay implements TimeOfDay {
    private static final int DEFAULT_MORNING = 6;
    private static final int DEFAULT_NIGHT = 22;
    private static final int DEFAULT_TRANSITION = 1;
    private static final int CURVE_REFINE_STEPS = 5;
    private int morningHour;
    private int nightHour;
    private int transition;
    private Curve colourTemperatureCurve;
    private SharedPreferences sharedPrefs;

    public SimpleTimeOfDay() {
        this.sharedPrefs = LedControl.getSharedPreferences();
        List<Pair<String, Integer>> toAdd = new LinkedList<>();
        if (!this.sharedPrefs.contains("time_of_day.morninghour")) {
            toAdd.add(new Pair<>("time_of_day.morninghour", DEFAULT_MORNING));
        }
        if (!this.sharedPrefs.contains("time_of_day.nighthour")) {
            toAdd.add(new Pair<>("time_of_day.nighthour", DEFAULT_NIGHT));
        }
        if (!this.sharedPrefs.contains("time_of_day.transitionhours")) {
            toAdd.add(new Pair<>("time_of_day.transitionhours", DEFAULT_TRANSITION));
        }
        SharedPreferences.Editor sharedPrefsEditor = this.sharedPrefs.edit();
        for (Pair<String, Integer> kv : toAdd) {
            sharedPrefsEditor.putInt(kv.first, kv.second);
        }
        sharedPrefsEditor.apply();
        this.colourTemperatureCurve = SimpleColorTemperatureCurve.getRefinedCurve(CURVE_REFINE_STEPS);
    }

    public SimpleTimeOfDay(int morningHour, int nightHour) {
        this.sharedPrefs = LedControl.getSharedPreferences();
        SharedPreferences.Editor sharedPrefsEditor = this.sharedPrefs.edit();
        sharedPrefsEditor.putInt("time_of_day.morninghour", DEFAULT_MORNING);
        sharedPrefsEditor.putInt("time_of_day.nighthour", DEFAULT_NIGHT);
        sharedPrefsEditor.putInt("time_of_day.transitionhours", DEFAULT_TRANSITION);
        sharedPrefsEditor.apply();
        this.colourTemperatureCurve = SimpleColorTemperatureCurve.getRefinedCurve(CURVE_REFINE_STEPS);

    }

    public SimpleTimeOfDay(int morningHour, int nightHour, int transition) {
        this.morningHour = morningHour;
        this.nightHour = nightHour;
        this.transition = transition;
        this.colourTemperatureCurve = SimpleColorTemperatureCurve.getRefinedCurve(CURVE_REFINE_STEPS);
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

    @Override
    public PointF getCurrentColorPoint() {
        GregorianCalendar cal = new GregorianCalendar();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        double transitionValue = this.getTransitionValue(hour, minute);
        return this.colourTemperatureCurve.getPointOnCurve(transitionValue);
    }
}
