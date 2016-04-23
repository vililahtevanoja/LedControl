package fi.aalto.mobilesystems.ledcontrol.models;

import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZSY on 4/18/16.
 */
public class HandleBroadcastScene {
    private Map<String, Integer> IncomingCallScene;
    private Map<String, Integer> IncomingSMSScene;
    private Map<String, Integer> AlarmAlert;


    public HandleBroadcastScene() {
        this.IncomingCallScene = new HashMap<>();
        this.IncomingSMSScene = new HashMap<>();
        this.AlarmAlert = new HashMap<>();

    }
    public void addIncomingCallScene(String lightIdentifier, int color) {
        IncomingCallScene.put(lightIdentifier, color);
    }

    public void addIncomingSMSScene(String lightIdentifier, int color) {
        IncomingSMSScene.put(lightIdentifier, color);
    }

    public void addAlarmAlertScene(String lightIdentifier, int color) {
        AlarmAlert.put(lightIdentifier, color);
    }

    public Map<String, Integer> getIncomingCallScene() {
        return IncomingCallScene;
    }

    public Map<String, Integer> getIncomingSMSScene() {
        return IncomingSMSScene;
    }

    public Map<String, Integer> getAlarmAlert() { return AlarmAlert;};

    public void clearBroadcastScene() {
        IncomingSMSScene.clear();
        IncomingCallScene.clear();
        AlarmAlert.clear();
    }
}
