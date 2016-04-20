package fi.aalto.mobilesystems.ledcontrol;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import fi.aalto.mobilesystems.ledcontrol.ledcontrol.features.BlinkLights;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.features.timeofday.AutomaticTimeOfDay;

public class LedControl extends Application {

    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    public static SharedPreferences getSharedPreferences() {
        Context ctx = LedControl.getContext();
        String sharedPreferencesKey = ctx.getString(R.string.shared_preferences_key);
        return ctx.getSharedPreferences(sharedPreferencesKey ,Context.MODE_PRIVATE);
    }
}


