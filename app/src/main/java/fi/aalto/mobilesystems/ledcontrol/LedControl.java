package fi.aalto.mobilesystems.ledcontrol;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.content.SharedPreferences;

<<<<<<< HEAD
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.features.BlinkLights;
<<<<<<< HEAD
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.features.timeofday.AutomaticTimeOfDay;
=======
import fi.aalto.mobilesystems.ledcontrol.models.HandleBroadcastScene;
>>>>>>> Shudaa

=======
>>>>>>> 9a6683c7c3c81494693edbb541f2ec07e1cef376
public class LedControl extends Application {
    private static final String TAG = "LedControl";

    private static Application sApplication;

    private static HandleBroadcastScene mBroadcastScene;

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
<<<<<<< HEAD
    }

    public static SharedPreferences getSharedPreferences() {
        Context ctx = LedControl.getContext();
        String sharedPreferencesKey = ctx.getString(R.string.shared_preferences_key);
        return ctx.getSharedPreferences(sharedPreferencesKey ,Context.MODE_PRIVATE);
=======
        //BlinkLights blink = new BlinkLights(2);
        mBroadcastScene = new HandleBroadcastScene();
>>>>>>> Shudaa
    }

    public static String getStringResource(int resId) {
        return LedControl.getContext().getString(resId);
    }
}


