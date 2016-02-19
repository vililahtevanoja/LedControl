package fi.aalto.mobilesystems.ledcontrol.hue;

import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHHueParsingError;

import java.util.List;

public class HueController {
    private PHHueSDK sdk;
    private HueController instance;

    public HueController() {
        this.sdk  = PHHueSDK.create();
        this.sdk.getNotificationManager().registerSDKListener(this.hueListener);
        this.instance = this;
    }

    private PHSDKListener hueListener = new PHSDKListener() {
        @Override
        public void onCacheUpdated(List<Integer> list, PHBridge phBridge) {

        }

        @Override
        public void onBridgeConnected(PHBridge phBridge, String username) {
            sdk.setSelectedBridge(phBridge);
            sdk.enableHeartbeat(phBridge, PHHueSDK.HB_INTERVAL);
            HueProperties.setUsername(username);
            HueProperties.setIpAddress(phBridge.getResourceCache().getBridgeConfiguration().getIpAddress());
        }

        @Override
        public void onAuthenticationRequired(PHAccessPoint phAccessPoint) {
            sdk.startPushlinkAuthentication(phAccessPoint);
        }

        @Override
        public void onAccessPointsFound(List<PHAccessPoint> accessPointList) {

        }

        @Override
        public void onError(int i, String s) {
            System.out.println(Integer.toString(i) + ": " + hueErrorToString(i) + " - " + s);
        }

        @Override
        public void onConnectionResumed(PHBridge phBridge) {

        }

        @Override
        public void onConnectionLost(PHAccessPoint phAccessPoint) {

        }

        @Override
        public void onParsingErrors(List<PHHueParsingError> parsingErrorList) {
            for (PHHueParsingError error : parsingErrorList) {
                System.out.println("Parsing error: " + error.getMessage());
            }
        }
    };

    public String hueErrorToString(int code) {
        String errorStr = "";
        switch (code) {
            case PHHueError.NO_CONNECTION:
                errorStr = "NO_CONNECTION";
                break;
            case PHHueError.INVALID_PARAMETERS:
                errorStr = "INVALID_PARAMETERS";
                break;
            case PHHueError.INVALID_PARAMETERS_MISSING_URL:
                errorStr = "INVALID_PARAMETERS_MISSING_URL";
                break;
            case PHHueError.INVALID_PARAMETERS_INVALID_METHOD:
                errorStr = "INVALID_PARAMETERS_INVALID_METHOD";
                break;
            case PHHueError.BRIDGE_ALREADY_CONNECTED:
                errorStr = "BRIDGE_ALREADY_CONNECTED";
                break;
            case PHHueError.LIGHT_ID_NOT_FOUND:
                errorStr = "LIGHT_ID_NOT_FOUND";
                break;
            case PHHueError.UNABLE_TO_PROCESS_REQUEST:
                errorStr = "UNABLE_TO_PROCESS_REQUEST";
                break;
            case PHHueError.GROUP_ID_NOT_FOUND:
                errorStr = "GROUP_ID_NOT_FOUND";
                break;
            case PHHueError.INVALID_DATA:
                errorStr = "INVALID_DATA";
                break;
            case PHHueError.UNSUPPORTED_BRIDGE_RESPONSE:
                errorStr = "UNSUPPORTED_BRIDGE_RESPONSE";
                break;
            case PHHueError.BRIDGE_NOT_RESPONDING:
                errorStr = "BRIDGE_NOT_RESPONDING";
                break;
            case PHHueError.FIND_LIGHT_ERROR:
                errorStr = "FIND_LIGHT_ERROR";
                break;
            case PHHueError.DISABLED_PORTAL_SERVICE:
                errorStr = "DISABLED_PORTAL_SERVICE";
                break;
            case PHHueError.SOFTWARE_UPDATE_NOT_AVAILABLE:
                errorStr = "SOFTWARE_UPDATE_NOT_AVAILABLE";
                break;
            case PHHueError.UNSUPPORTED_BRIDGE_VERSION:
                errorStr = "UNSUPPORTED_BRIDGE_VERSION";
                break;
            case PHHueError.INVALID_OBJECT_PARAMETER:
                errorStr = "INVALID_OBJECT_PARAMETER";
                break;
            case PHHueError.INVALID_JSON:
                errorStr = "INVALID_JSON";
                break;
            case PHHueError.NO_DATA_FOUND:
                errorStr = "NO_DATA_FOUND";
                break;
            case PHHueError.CLIP_ERROR:
                errorStr = "CLIP_ERROR";
                break;
            case PHHueError.INVALID_API_CALL:
                errorStr = "INVALID_API_CALL";
                break;
            case PHHueError.PORTAL_NOT_RESPONDING:
                errorStr = "PORTAL_NOT_RESPONDING";
                break;
            case PHHueError.SOFTWARE_UPDATE_DOWNLOADING:
                errorStr = "SOFTWARE_UPDATE_DOWNLOADING";
                break;
            case PHHueError.RESOURCE_UNPARSABLE_CONFIG:
                errorStr = "RESOURCE_UNPARSABLE_CONFIG";
                break;
            case PHHueError.RESOURCE_UNPARSABLE_LIGHT:
                errorStr = "RESOURCE_UNPARSABLE_LIGHT";
                break;
            case PHHueError.RESOURCE_UNPARSABLE_GROUP:
                errorStr = "RESOURCE_UNPARSABLE_GROUP";
                break;
            case PHHueError.RESOURCE_UNPARSABLE_SCENE:
                errorStr = "RESOURCE_UNPARSABLE_SCENE";
                break;
            case PHHueError.RESOURCE_UNPARSABLE_SCHEDULE:
                errorStr = "RESOURCE_UNPARSABLE_SCHEDULE";
                break;
            case PHHueError.RESOURCE_UNPARSABLE_SENSOR:
                errorStr = "RESOURCE_UNPARSABLE_SENSOR";
                break;
            case PHHueError.RESOURCE_UNPARSABLE_RULE:
                errorStr = "RESOURCE_UNPARSABLE_RULE";
                break;
            case PHHueError.RESOURCE_UNPARSABLE_MULTI_LIGHT:
                errorStr = "RESOURCE_UNPARSABLE_MULTI_LIGHT";
                break;
            case PHHueError.AUTHENTICATION_FAILED:
                errorStr = "AUTHENTICATION_FAILED";
                break;
        }
        return errorStr;
    }

    public PHSDKListener getHueListener() {
        return hueListener;
    }

    public HueController getInstance() {
        return instance;
    }
}