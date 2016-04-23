package fi.aalto.mobilesystems.ledcontrol.ledcontrol;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHHueError;

import fi.aalto.mobilesystems.ledcontrol.LedControl;

public class HueController {
    private static final String TAG = "HueController";
    private PHHueSDK sdk;
    private HueController instance;
    private PHSDKListener listener;

    public HueController() {
        this.sdk  = PHHueSDK.create();
        this.listener = new HueListener(sdk);
        this.sdk.getNotificationManager().registerSDKListener(this.listener);
        this.instance = this;
        Log.d(TAG, "HueController created");
        testRequestToApi();
    }

    public void connectToEmulatorAccessPoint() {
        PHAccessPoint ap = new PHAccessPoint();
        ap.setIpAddress("130.233.85.37:8000");   // Philips Hue Emulator address
        ap.setUsername("newdeveloper");
        this.sdk.connect(ap);
    }

    public static void testRequestToApi() {
        RequestQueue queue = Volley.newRequestQueue(LedControl.getContext());
        String url = "http://130.233.84.56:8000/api/newdeveloper/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "Response is: "+ response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error on response: " + error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

    public static String hueErrorToString(int code) {
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

    public void startBridgeSearch() {
        PHBridgeSearchManager sm = (PHBridgeSearchManager) this.sdk.getSDKService(PHHueSDK.SEARCH_BRIDGE);
        sm.search(true, true);
    }

    public PHSDKListener getHueListener() {
        return listener;
    }

    public HueController getInstance() {
        return instance;
    }
}