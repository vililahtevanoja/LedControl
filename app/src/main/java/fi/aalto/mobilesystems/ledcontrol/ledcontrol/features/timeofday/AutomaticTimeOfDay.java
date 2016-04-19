package fi.aalto.mobilesystems.ledcontrol.ledcontrol.features.timeofday;

import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

public class AutomaticTimeOfDay implements LocationListener, TimeOfDay {
    private static final String TAG = "AutomaticTimeOfDay";
    private Location lastLocation;
    private double currentDayFactor = 0.0f;

    public AutomaticTimeOfDay() {}

    public void updateDayFactor() {
        if (this.lastLocation == null) {
            return;
        }
        this.currentDayFactor = 0.0f;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            this.lastLocation = location;
            this.updateDayFactor();
        }
    }

    @Override
    public float[] fetchPHueXYColor() {
        float[] xy = new float[]{0.0f, 0.0f};
        return xy;
    }
}
