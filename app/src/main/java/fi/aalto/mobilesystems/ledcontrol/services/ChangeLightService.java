package fi.aalto.mobilesystems.ledcontrol.services;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.Switch;

import com.google.gson.Gson;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.utilities.PHUtilities;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fi.aalto.mobilesystems.ledcontrol.LedControl;
import fi.aalto.mobilesystems.ledcontrol.models.Alarm;
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
    private Map<String, Integer> AlarmAlert;

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
       AlarmAlert = BroadcastScene.getAlarmAlert();


        PHHueSDK sdk= PHHueSDK.getInstance();
        bridge = sdk.getSelectedBridge();

        Log.d(TAG, "ChangeLightService Started");

        Log.d(TAG, "intent.getAction:" + intent.getAction());

        switch (intent.getAction()) {
            case "android.intent.action.PHONE_STATE":
                Log.d(TAG, "PHONE_STATE Intent Detected.");
                updateLightWithScene(IncomingCallScene);
                break;

            case "android.provider.Telephony.SMS_RECEIVED":
                Log.d(TAG, "SMS_RECEIVED Intent Detected.");
                updateLightWithScene(IncomingSMSScene);
                break;

            case "com.android.deskclock.ALARM_ALERT":
                Log.d(TAG, "ALARM_ALERT Intent Detected.");
                updateLightWithScene(AlarmAlert);
                break;

            case "Alarm":
                Log.d(TAG, "Alarm Intent Detected.");
                String strColor = intent.getStringExtra("newColor");
                Log.d(TAG, "get string color from intent:" + strColor);
                updateLight(intent.getStringExtra("lightIdentifier"), Integer.parseInt(strColor));
                break;

            case "RestoreAlarm":
                Log.d(TAG, "RestoreAlarm Intent Detected.");
                updateLightWithState(intent.getStringExtra("lightIdentifier"),
                        intent.getStringExtra("lightState"));
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
            setRestore((String)pair.getKey(), mPHLight.getLastKnownLightState());

            Log.d(TAG, "updateLightState with Scene");
        }
    }

    private void updateLight(String lightIdentifier, int color){

        mPHLight = mLightsMap.get(lightIdentifier);
        color =  AlarmAlert.get(lightIdentifier);
        bridge.updateLightState(mPHLight,getPHLightStateWithRGB(mPHLight, color));
        Log.d(TAG, "updateLight with color" + "Light:" + lightIdentifier + " color:" + color);
    }

    private void updateLightWithState(String lightIdentifier, String lightState)
    {

        Gson gson = new Gson();
        if(lightState.equals(""))
        {
            Log.d(TAG,"lightState getJson: "+ lightState);
        } else {

            PHLightState state = gson.fromJson(lightState, PHLightState.class);
            Log.d(TAG, "lightState getJson: " + lightState);

            mPHLight = mLightsMap.get(lightIdentifier);

            PHLightState lightState1 = new PHLightState();

            lightState1.setX(state.getX());
            lightState1.setY(state.getY());

            bridge.updateLightState(mPHLight, lightState1);

            Log.d(TAG, "updateLight with lightState");
        }

    }

    private void setRestore(String lightIdentifier, PHLightState state)
    {
        Alarm alarm = new Alarm();
        alarm.setRestoreAlarm(this, 60*1000, lightIdentifier, state);
    }

}
