package fi.aalto.mobilesystems.ledcontrol.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeConfiguration;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHHueParsingError;
import com.philips.lighting.model.PHLight;

import java.util.List;
import java.util.Map;
import java.util.Observable;

import fi.aalto.mobilesystems.ledcontrol.LedControl;
import fi.aalto.mobilesystems.ledcontrol.R;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.HueController;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.HueProperties;

public class LedControlScheduler extends IntentService implements PHSDKListener, PHLightListener {
    private final static String TAG = "LedControlScheduler";
    private String serviceName = "LedControlScheduler";
    private PHHueSDK sdk;

    public LedControlScheduler() {
        super(TAG);
        serviceName = TAG;
        HueProperties.loadProperties();
        this.sdk = PHHueSDK.getInstance();
        this.sdk.getNotificationManager().registerSDKListener(this);
        if (Build.PRODUCT.contains("sdk_google")) {
            Log.i(TAG, "Build.PRODUCT: " + Build.PRODUCT);
            Log.i(TAG, "Emulator detected, connecting to Hue emulator access point");
            this.sdk.setAppName("LedControl");
            this.sdk.setDeviceName(android.os.Build.MODEL);
            PHAccessPoint ap = new PHAccessPoint();
            ap.setIpAddress("10.0.2.2:8000");
            ap.setUsername(HueProperties.getUsername());
            this.sdk.connect(ap);
        }
        findBridges();
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LedControlScheduler(String name) {
        super(name);
        serviceName = name;
        HueProperties.loadProperties();
        this.sdk = PHHueSDK.getInstance();
        this.sdk.getNotificationManager().registerSDKListener(this);
        this.sdk.setAppName("LedControl");
        this.sdk.setDeviceName(android.os.Build.MODEL);
        findBridges();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HueProperties.loadProperties();
        this.sdk = PHHueSDK.getInstance();
        findBridges();
    }

    public void findBridges() {
        PHBridgeSearchManager pm = (PHBridgeSearchManager) this.sdk.getSDKService(PHHueSDK.SEARCH_BRIDGE);
        pm.search(true, true);
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
        HueProperties.saveProperties();
        Log.d(TAG, "Bridge connected with username: " + username + ", IP: " + ipAddress);
        phBridge.findNewLights(this);
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
        this.sdk.getAccessPointsFound().clear();
        this.sdk.getAccessPointsFound().addAll(accessPointList);
        this.sdk.connect(this.sdk.getAccessPointsFound().get(0));
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(int i, String s) {
        Log.e(TAG, "ERROR " +  Integer.toString(i) + ": " + HueController.hueErrorToString(i) + " - " + s);
    }

    @Override
    public void onStateUpdate(Map<String, String> map, List<PHHueError> list) {

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

    @Override
    public void onDestroy() {
        this.sdk.getNotificationManager().unregisterSDKListener(this);
        PHBridge selectedBridge = this.sdk.getSelectedBridge();
        if (selectedBridge != null) {

            if (this.sdk.isHeartbeatEnabled(selectedBridge)) {
                this.sdk.disableHeartbeat(selectedBridge);
            }

            this.sdk.disconnect(selectedBridge);
        }
        super.onDestroy();
    }

    @Override
    public void onReceivingLightDetails(PHLight phLight) {

    }

    @Override
    public void onReceivingLights(List<PHBridgeResource> list) {

    }

    @Override
    public void onSearchComplete() {
        Log.i(TAG, "Search for lights complete");
    }
}
