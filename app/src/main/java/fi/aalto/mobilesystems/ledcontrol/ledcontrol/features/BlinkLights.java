package fi.aalto.mobilesystems.ledcontrol.ledcontrol.features;

import android.util.Log;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.utilities.PHUtilities;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class BlinkLights {
    public final String TAG = "BlinkLights";
    private class RGB {
        public int R;
        public int G;
        public int B;
        public RGB(int R, int G, int B) {
            this.R = R;
            this.G = G;
            this.B = B;
        }
    }
    private PHHueSDK sdk;
    private int intervalSeconds;
    private List<RGB> rgbList;
    private Timer timer;
    public BlinkLights(int intervalSeconds) {
        this.sdk = PHHueSDK.getInstance();
        this.intervalSeconds = intervalSeconds * 1000;
        this.rgbList = new ArrayList<>();
        this.rgbList.add(new RGB(255, 0, 0));
        this.rgbList.add(new RGB(0, 255, 0));
        this.rgbList.add(new RGB(0, 0, 255));
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new BlinkLightTask(this.rgbList), intervalSeconds * 1000, intervalSeconds * 1000);
    }

    public class BlinkLightTask extends TimerTask {
        private List<RGB> rgbs;
        private Random rand;
        public BlinkLightTask(List<RGB> rgbs) {
            this.rgbs = rgbs;
            this.rand = new Random();
        }

        @Override
        public void run() {
            int rgbIdx = Math.abs(rand.nextInt() % 3);
            RGB rgb = rgbs.get(rgbIdx);
            Log.i(TAG, "rgbIdx: " + Integer.toString(rgbIdx));
            float[] xy = PHUtilities.calculateXYFromRGB(rgb.R, rgb.G, rgb.B, null);
            PHLightState state = new PHLightState();
            state.setX(xy[0]);
            state.setY(xy[1]);
            PHHueSDK sdk = PHHueSDK.getInstance();
            for (PHBridge bridge : sdk.getAllBridges()) {
                for (PHLight light : bridge.getResourceCache().getAllLights()) {
                    bridge.updateLightState(light, state);
                }
            }
        }
    }
}
