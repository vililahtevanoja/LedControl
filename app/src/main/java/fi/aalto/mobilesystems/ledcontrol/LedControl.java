package fi.aalto.mobilesystems.ledcontrol;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import fi.aalto.mobilesystems.ledcontrol.models.HandleBroadcastScene;
import fi.aalto.mobilesystems.ledcontrol.services.LedControlScheduler;

public class LedControl extends Application {
    private static final String TAG = "LedControl";

    private static Application sApplication;

    public static HandleBroadcastScene mBroadcastScene;

    private static LedControlScheduler mScheduler;

    public static HandleBroadcastScene getBroadcastScene() {return mBroadcastScene;}
    public static void setBroadcastScene(HandleBroadcastScene scene){mBroadcastScene = scene;}

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
        mBroadcastScene = new HandleBroadcastScene();
        sApplication = this;
        mScheduler = new LedControlScheduler();
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


