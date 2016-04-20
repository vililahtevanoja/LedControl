package fi.aalto.mobilesystems.ledcontrol.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeConfiguration;
import com.philips.lighting.model.PHHueParsingError;

import java.util.List;
import java.util.Observable;

import fi.aalto.mobilesystems.ledcontrol.LedControl;
import fi.aalto.mobilesystems.ledcontrol.R;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.HueController;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.HueProperties;

public class LedControlService  extends IntentService implements PHSDKListener {
    private final static String TAG = "LedControlService";
    private String serviceName = "LedControlService";
    private PHHueSDK sdk;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LedControlService(String name) {
        super(name);
        serviceName = name;
    }

    public void onCreate() {
        this.sdk = PHHueSDK.getInstance();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCacheUpdated(List<Integer> list, PHBridge phBridge) {
        Log.i(TAG, "Cache updated");
    }

    @Override
    public void onBridgeConnected(PHBridge phBridge, String username) {
        Log.d(TAG, "Bridge connected");
        sdk.setSelectedBridge(phBridge);
        sdk.enableHeartbeat(phBridge, PHHueSDK.HB_INTERVAL);
        String ipAddress = phBridge.getResourceCache().getBridgeConfiguration().getIpAddress();
        HueProperties.setUsername(username);
        HueProperties.setIpAddress(ipAddress);
        Log.d(TAG, "Bridge connected with username: " + username + ", IP: " + ipAddress);
    }

    @Override
    public void onAuthenticationRequired(PHAccessPoint phAccessPoint) {
        sdk.startPushlinkAuthentication(phAccessPoint);
    }

    @Override
    public void onAccessPointsFound(List<PHAccessPoint> accessPointList) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Access points found: \n");
        Log.i(TAG, "Access points found");
        for (PHAccessPoint ap: accessPointList) {
            stringBuilder.append("ID: " + ap.getBridgeId());
            stringBuilder.append(", Username: " + ap.getUsername());
            stringBuilder.append(", IP: " + ap.getIpAddress());
            stringBuilder.append(", MAC: " + ap.getMacAddress());
        }
        Log.i(TAG, stringBuilder.toString());
    }

    @Override
    public void onError(int i, String s) {
        Log.e(TAG, "ERROR " +  Integer.toString(i) + ": " + HueController.hueErrorToString(i) + " - " + s);
    }

    @Override
    public void onConnectionResumed(PHBridge phBridge) {
        PHBridgeConfiguration bridgeConf = phBridge.getResourceCache().getBridgeConfiguration();
        Log.d(TAG, "Connection resumed to " + bridgeConf.getBridgeID() + "@" + bridgeConf.getIpAddress());
    }

    @Override
    public void onConnectionLost(PHAccessPoint phAccessPoint) {
        Log.d(TAG, "Connection lost");
    }

    @Override
    public void onParsingErrors(List<PHHueParsingError> parsingErrorList) {
        Log.e(TAG, "Parsing error(s) occurred");
        for (PHHueParsingError error : parsingErrorList) {
            Log.d(TAG, "Parsing error: " + error.getMessage());
        }
    }

    public void onHandleIntent(Intent intent) {

    }
}
