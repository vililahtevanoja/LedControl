package fi.aalto.mobilesystems.ledcontrol;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import fi.aalto.mobilesystems.ledcontrol.ledcontrol.features.BlinkLights;

public class LedControl extends Application {
    private static final String TAG = "LedControl";

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
        Log.i(TAG, "Created");
        sApplication = this;
    }
}
