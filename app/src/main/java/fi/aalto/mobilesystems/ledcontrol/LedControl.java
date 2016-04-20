package fi.aalto.mobilesystems.ledcontrol;
import android.app.Application;
import android.content.Context;

import fi.aalto.mobilesystems.ledcontrol.ledcontrol.features.BlinkLights;
import fi.aalto.mobilesystems.ledcontrol.models.HandleBroadcastScene;

public class LedControl extends Application {

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
        sApplication = this;
        //BlinkLights blink = new BlinkLights(2);
        mBroadcastScene = new HandleBroadcastScene();
    }
}
