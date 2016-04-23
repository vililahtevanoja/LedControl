package fi.aalto.mobilesystems.ledcontrol.models;

import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZSY on 4/18/16.
 */
public class HandleBroadcastScene {
    private Map<PHLight, PHLightState> IncomingCallScene;
    private Map<PHLight, PHLightState> IncomingSMSScene;
    private Map<PHLight, PHLightState> AlarmAlert;

    public HandleBroadcastScene() {
        this.IncomingCallScene = new HashMap<>();
        this.IncomingSMSScene = new HashMap<>();
        this.AlarmAlert = new HashMap<>();

    }
    public void addIncomingCallScene(PHLight light, PHLightState state) {
        IncomingCallScene.put(light, state);
    }

    public void addIncomingSMSScene(PHLight light, PHLightState state) {
        IncomingSMSScene.put(light, state);
    }

    public void addAlarmAlertScene(PHLight light, PHLightState state) {
        AlarmAlert.put(light, state);
    }

    public Map<PHLight, PHLightState> getIncomingCallScene() {
        return IncomingCallScene;
    }

    public Map<PHLight, PHLightState> getIncomingSMSScene() {
        return IncomingSMSScene;
    }

    public Map<PHLight, PHLightState> getAlarmAlert() { return AlarmAlert;};

    public void clearBroadcastScene() {
        IncomingSMSScene.clear();
        IncomingCallScene.clear();
        AlarmAlert.clear();
    }
}
