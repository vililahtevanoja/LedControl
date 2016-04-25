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

    }

    public static SharedPreferences getSharedPreferences() {
        Context ctx = LedControl.getContext();
        String sharedPreferencesKey = ctx.getString(R.string.shared_preferences_key);
        return ctx.getSharedPreferences(sharedPreferencesKey ,Context.MODE_PRIVATE);

       // BlinkLights blink = new BlinkLights(2);
       // mBroadcastScene = new HandleBroadcastScene();

    }

    public static String getStringResource(int resId) {
        return LedControl.getContext().getString(resId);
    }
}


