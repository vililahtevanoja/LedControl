package fi.aalto.mobilesystems.ledcontrol;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import fi.aalto.mobilesystems.ledcontrol.models.HandleBroadcastScene;

public class LedControl extends Application {
    private static final String TAG = "LedControl";

    private static Application sApplication;

    public static HandleBroadcastScene mBroadcastScene;

    public static HandleBroadcastScene getBroadcastScene() {return mBroadcastScene;}

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Created");
        sApplication = this;

    }

    public static SharedPreferences getSharedPreferences() {
        Context ctx = LedControl.getContext();
        String sharedPreferencesKey = ctx.getString(R.string.shared_preferences_key);
        return ctx.getSharedPreferences(sharedPreferencesKey ,Context.MODE_PRIVATE);
    }

    public static SharedPreferences getSharedPreferences(String name) {
        Context ctx = LedControl.getContext();
        return ctx.getSharedPreferences(name ,Context.MODE_PRIVATE);
    }

    public static String getStringResource(int resId) {
        return LedControl.getContext().getString(resId);
    }
}


