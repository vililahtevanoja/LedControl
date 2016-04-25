package fi.aalto.mobilesystems.ledcontrol.ledcontrol;

import android.util.Log;

import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeConfiguration;
import com.philips.lighting.model.PHHueParsingError;

import java.util.List;

public class HueListener implements PHSDKListener {
    private static final String TAG = "HueListener";
    private PHHueSDK sdk;

    public HueListener(PHHueSDK sdk) {
        this.sdk = sdk;
        Log.i(TAG, "HueListener created");
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
        if (accessPointList != null && accessPointList.size() > 0) {
            this.sdk.getAccessPointsFound().clear();
            this.sdk.getAccessPointsFound().addAll(accessPointList);
        }
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
}
