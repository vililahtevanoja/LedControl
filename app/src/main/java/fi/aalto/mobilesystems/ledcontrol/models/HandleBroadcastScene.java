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

    public HandleBroadcastScene() {
        this.IncomingCallScene = new HashMap<PHLight, PHLightState>();
        this.IncomingSMSScene = new HashMap<>();

    }
    public void addIncomingCallScene(PHLight light, PHLightState state) {
        IncomingCallScene.put(light, state);
    }

    public void addIncomingSMSScene(PHLight light, PHLightState state) {
        IncomingSMSScene.put(light, state);
    }

    public Map<PHLight, PHLightState> getIncomingCallScene() {
        return IncomingCallScene;
    }

    public Map<PHLight, PHLightState> getIncomingSMSScene() {
        return IncomingSMSScene;
    }

    public void clearBroadcastScene() {
        IncomingSMSScene.clear();
        IncomingCallScene.clear();
    }
}
