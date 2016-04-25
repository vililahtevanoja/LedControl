package fi.aalto.mobilesystems.ledcontrol.services;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.Switch;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.utilities.PHUtilities;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fi.aalto.mobilesystems.ledcontrol.LedControl;
import fi.aalto.mobilesystems.ledcontrol.models.HandleBroadcastScene;

/**
 * Created by ZSY on 4/21/16.
 */
public class ChangeLightService extends IntentService {
    private static final String TAG = "ChangeLightService";
    private PHHueSDK sdk;
    private  Map<String,PHLight> mLightsMap;
    private PHLight mPHLight;
    private PHBridge bridge;
    private int mColor;

    public ChangeLightService() {
        super("ChangeLightService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.sdk = PHHueSDK.getInstance();
        mLightsMap = this.sdk.getSelectedBridge().getResourceCache().getLights();

        LedControl mApplication = ((LedControl)getApplicationContext());
        HandleBroadcastScene BroadcastScene = mApplication.getBroadcastScene();
        Map<String, Integer> IncomingCallScene = BroadcastScene.getIncomingCallScene();
        Map<String, Integer> IncomingSMSScene = BroadcastScene.getIncomingSMSScene();
        Map<String, Integer> AlarmAlert = BroadcastScene.getAlarmAlert();


        PHHueSDK sdk= PHHueSDK.getInstance();
        bridge = sdk.getSelectedBridge();

        Log.d(TAG, "ChangeLightService Started");

        Log.d(TAG, "intent.getAction:" + intent.getAction());

        switch (intent.getAction()) {
            case "android.intent.action.PHONE_STATE":
                updateLightWithScene(IncomingCallScene);
                Log.d(TAG, "PHONE_STATE Intent Detected.");
                break;

            case "android.provider.Telephony.SMS_RECEIVED":
                updateLightWithScene(IncomingSMSScene);
                Log.d(TAG, "PHONE_STATE Intent Detected.");
                break;

            case "com.android.deskclock.ALARM_ALERT":
                updateLightWithScene(AlarmAlert);
                Log.d(TAG, "ALARM_ALERT Intent Detected.");
                break;

            case "Alarm":
                updateLight(intent.getStringExtra("lightIdentifier"), intent.getIntExtra("color",0));
                Log.d(TAG, "Alarm Intent Detected.");
                break;

            case "RestoreAlarm":
                break;

        }

    }

    private PHLightState getPHLightStateWithRGB(PHLight light, int mColor) {
        PHLightState lightState = new PHLightState();
        float xy[] = PHUtilities.calculateXYFromRGB(
                Color.red(mColor), Color.green(mColor), Color.blue(mColor), light.getModelNumber());
        lightState.setX(xy[0]);
        lightState.setY(xy[1]);

        return lightState;
    }

    private void updateLightWithScene(Map<String,Integer> Scene) {
        Iterator it = Scene.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            mPHLight = mLightsMap.get(pair.getKey());
            mColor = (int)pair.getValue();
            bridge.updateLightState(mPHLight,getPHLightStateWithRGB(mPHLight, mColor));
            // it.remove(); // avoids a ConcurrentModificationException
            Log.d(TAG, "updateLightState with Scene");
        }
    }

    private void updateLight(String lightIdentifier, int color){

        mPHLight = mLightsMap.get(lightIdentifier);
        bridge.updateLightState(mPHLight,getPHLightStateWithRGB(mPHLight, color));
        Log.d(TAG, "updateLight with color");
    }

    private void updateLightWithState(String lightIdentifier, String lightState){
        mPHLight = mLightsMap.get(lightIdentifier);
        Log.d(TAG, "updateLight with lightState");
    }
}
