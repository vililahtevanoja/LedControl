package fi.aalto.mobilesystems.ledcontrol.services;

import android.app.IntentService;
import android.content.Intent;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.aalto.mobilesystems.ledcontrol.ledcontrol.HueProperties;

public class LedControlScheduler extends IntentService implements PHSDKListener, PHLightListener {
    private final static String TAG = "LedControlScheduler";
    private String serviceName = "LedControlScheduler";
    private PHHueSDK sdk;
    private HashMap<Integer, String> errorMap;

    public LedControlScheduler() {
        super(TAG);
        serviceName = TAG;
        HueProperties.loadProperties();
        this.sdk = PHHueSDK.getInstance();
        this.sdk.getNotificationManager().registerSDKListener(this);
        findBridges();
        this.errorMap = getErrorMap();
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
        Log.e(TAG, "ERROR " +  Integer.toString(i) + ": " + this.hueErrorToString(i) + " - " + s);
        if (i == 1158) {
            findBridges();
        }
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

    public String hueErrorToString(int errno) {
        if (this.errorMap.containsKey(errno)) {
            return this.errorMap.get(errno);
        }
        else {
            return "UNKNOWN_ERROR";
        }
    }

    private HashMap<Integer, String> getErrorMap() {
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(PHHueError.NO_CONNECTION, "NO_CONNECTION");
        errorMap.put(PHHueError.INVALID_PARAMETERS, "INVALID_PARAMETERS");
        errorMap.put(PHHueError.INVALID_PARAMETERS_MISSING_URL, "INVALID_PARAMETERS_MISSING_URL");
        errorMap.put(PHHueError.INVALID_PARAMETERS_INVALID_METHOD, "INVALID_PARAMETERS_INVALID_METHOD");
        errorMap.put(PHHueError.BRIDGE_ALREADY_CONNECTED, "BRIDGE_ALREADY_CONNECTED");
        errorMap.put(PHHueError.LIGHT_ID_NOT_FOUND, "LIGHT_ID_NOT_FOUND");
        errorMap.put(PHHueError.UNABLE_TO_PROCESS_REQUEST, "UNABLE_TO_PROCESS_REQUEST");
        errorMap.put(PHHueError.GROUP_ID_NOT_FOUND, "GROUP_ID_NOT_FOUND");
        errorMap.put(PHHueError.INVALID_DATA, "INVALID_DATA");
        errorMap.put(PHHueError.UNSUPPORTED_BRIDGE_RESPONSE, "UNSUPPORTED_BRIDGE_RESPONSE");
        errorMap.put(PHHueError.BRIDGE_NOT_RESPONDING, "BRIDGE_NOT_RESPONDING");
        errorMap.put(PHHueError.FIND_LIGHT_ERROR, "FIND_LIGHT_ERROR");
        errorMap.put(PHHueError.DISABLED_PORTAL_SERVICE, "DISABLED_PORTAL_SERVICE");
        errorMap.put(PHHueError.SOFTWARE_UPDATE_NOT_AVAILABLE, "SOFTWARE_UPDATE_NOT_AVAILABLE");
        errorMap.put(PHHueError.UNSUPPORTED_BRIDGE_VERSION, "UNSUPPORTED_BRIDGE_VERSION");
        errorMap.put(PHHueError.INVALID_OBJECT_PARAMETER, "INVALID_OBJECT_PARAMETER");
        errorMap.put(PHHueError.INVALID_JSON, "INVALID_JSON");
        errorMap.put(PHHueError.NO_DATA_FOUND, "NO_DATA_FOUND");
        errorMap.put(PHHueError.CLIP_ERROR, "CLIP_ERROR");
        errorMap.put(PHHueError.INVALID_API_CALL, "INVALID_API_CALL");
        errorMap.put(PHHueError.PORTAL_NOT_RESPONDING, "PORTAL_NOT_RESPONDING");
        errorMap.put(PHHueError.SOFTWARE_UPDATE_DOWNLOADING, "SOFTWARE_UPDATE_DOWNLOADING");
        errorMap.put(PHHueError.RESOURCE_UNPARSABLE_CONFIG, "RESOURCE_UNPARSABLE_CONFIG");
        errorMap.put(PHHueError.RESOURCE_UNPARSABLE_LIGHT, "RESOURCE_UNPARSABLE_LIGHT");
        errorMap.put(PHHueError.RESOURCE_UNPARSABLE_GROUP, "RESOURCE_UNPARSABLE_GROUP");
        errorMap.put(PHHueError.RESOURCE_UNPARSABLE_SCENE, "RESOURCE_UNPARSABLE_SCENE");
        errorMap.put(PHHueError.RESOURCE_UNPARSABLE_SCHEDULE, "RESOURCE_UNPARSABLE_SCHEDULE");
        errorMap.put(PHHueError.RESOURCE_UNPARSABLE_SENSOR, "RESOURCE_UNPARSABLE_SENSOR");
        errorMap.put(PHHueError.RESOURCE_UNPARSABLE_RULE, "RESOURCE_UNPARSABLE_RULE");
        errorMap.put(PHHueError.RESOURCE_UNPARSABLE_MULTI_LIGHT, "RESOURCE_UNPARSABLE_MULTI_LIGHT");
        errorMap.put(PHHueError.AUTHENTICATION_FAILED, "AUTHENTICATION_FAILED");
        return errorMap;
    }
}
