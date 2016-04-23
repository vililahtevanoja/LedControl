package fi.aalto.mobilesystems.ledcontrol.models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResourcesCache;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fi.aalto.mobilesystems.ledcontrol.LedControl;
import fi.aalto.mobilesystems.ledcontrol.services.ChangeLightService;

/**
 * Created by ZSY on 4/17/16.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "intent.getAction:"+intent.getAction());

        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            Intent i = new Intent(context, ChangeLightService.class);
            i.setAction("android.intent.action.PHONE_STATE");
            context.startService(i);

        }

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Intent i = new Intent(context, ChangeLightService.class);
            i.setAction("android.provider.Telephony.SMS_RECEIVED");
            context.startService(i);

        }

        if (intent.getAction().equals("com.android.deskclock.ALARM_ALERT")) {
            Intent i = new Intent(context, ChangeLightService.class);
            i.setAction("com.android.deskclock.ALARM_ALERT");
        }


    }
}
