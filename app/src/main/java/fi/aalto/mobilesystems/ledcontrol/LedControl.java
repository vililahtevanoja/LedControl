package fi.aalto.mobilesystems.ledcontrol;
import android.app.Application;
import android.content.Context;

import fi.aalto.mobilesystems.ledcontrol.ledcontrol.features.BlinkLights;

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
        BlinkLights blink = new BlinkLights(2);
    }
}
