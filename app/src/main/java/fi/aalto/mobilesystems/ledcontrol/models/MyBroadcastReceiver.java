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

/**
 * Created by ZSY on 4/17/16.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        LedControl mApplication = ((LedControl)context.getApplicationContext());
        HandleBroadcastScene BroadcastScene = mApplication.getBroadcastScene();
        Map<PHLight, PHLightState> IncomingCallScene = BroadcastScene.getIncomingCallScene();
        Map<PHLight, PHLightState> IncomingSMSScene = BroadcastScene.getIncomingSMSScene();
        Map<PHLight, PHLightState> AlarmAlert = BroadcastScene.getAlarmAlert();

        PHHueSDK sdk= PHHueSDK.getInstance();
        PHBridge bridge = sdk.getSelectedBridge();


        Log.d(TAG, "intent.getAction:"+intent.getAction());
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            Iterator it = IncomingCallScene.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                bridge.updateLightState((PHLight)pair.getKey(),(PHLightState)pair.getValue());
                Log.d(TAG,"PHONE_STATE updateLightState PHONE_STATE");
               // it.remove(); // avoids a ConcurrentModificationException
            }
            Toast.makeText(context, "PHONE_STATE Intent Detected.", Toast.LENGTH_LONG).show();

        }

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Iterator it = IncomingSMSScene.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                bridge.updateLightState((PHLight) pair.getKey(), (PHLightState) pair.getValue());
               // it.remove(); // avoids a ConcurrentModificationException
                Log.d(TAG, "SMS_RECEIVED updateLightState SMS_RECEIVED");
            }
            /*PHLightState lightState = new PHLightState();
            lightState.setHue(12345);
            for(PHLight light : bridge.getResourceCache().getAllLights()){
                bridge.updateLightState(light, lightState);
            }*/
            Toast.makeText(context, "SMS_RECEIVED Intent Detected.", Toast.LENGTH_LONG).show();
        }

        if (intent.getAction().equals("com.android.deskclock.ALARM_ALERT")) {
            Iterator it = AlarmAlert.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                bridge.updateLightState((PHLight)pair.getKey(),(PHLightState)pair.getValue());
                // it.remove(); // avoids a ConcurrentModificationException
                Log.d(TAG, "ALARM_ALERT updateLightState ALARM_ALERT");
            }
            Toast.makeText(context, "ALARM_ALERT Intent Detected.", Toast.LENGTH_LONG).show();
        }


    }
}
