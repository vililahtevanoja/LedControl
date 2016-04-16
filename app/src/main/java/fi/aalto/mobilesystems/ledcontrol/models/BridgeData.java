package fi.aalto.mobilesystems.ledcontrol.models;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;

import java.util.LinkedList;
import java.util.List;

public class BridgeData {
    private PHHueSDK sdk;
    private List<PHBridge> bridges;
    private List<BridgeDataEventListener> listeners;
    public BridgeData() {
        this.sdk = PHHueSDK.getInstance();
        this.bridges = this.sdk.getAllBridges();
        this.listeners = new LinkedList<>();
    }

    public List<PHBridge> getBridges() {
        this.bridges = this.sdk.getAllBridges();
        return new LinkedList<>(this.bridges);
    }

    public List<PHLight> getAllLights() {
        LinkedList<PHLight> lights = new LinkedList<>();
        for (PHBridge bridge : this.bridges) {
            lights.addAll(bridge.getResourceCache().getAllLights());
        }
        return lights;
    }

    public void registerListener(BridgeDataEventListener listener) {
        this.listeners.add(listener);
    }

    public void updated() {
        for (BridgeDataEventListener listener : this.listeners) {
            listener.onBridgeDataUpdated();
        }
    }
}
