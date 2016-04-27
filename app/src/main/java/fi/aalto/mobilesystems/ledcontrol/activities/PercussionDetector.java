package fi.aalto.mobilesystems.ledcontrol.activities;

import android.util.Log;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.onsets.OnsetHandler;
import be.tarsos.dsp.onsets.PercussionOnsetDetector;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.GamutArea;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.GamutTypes;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.Line;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.PointF;

public class PercussionDetector implements Runnable {
    private PHHueSDK sdk;
    private static final int MAX_HUE = 65535;
    private static final String TAG = "RandomLightActivity";
    private PercussionOnsetDetector percussionOnsetDetector;

    private volatile boolean stopRequested;
    private Thread runThread;

    public void run() {
        this.sdk = PHHueSDK.getInstance();
        runThread = Thread.currentThread();
        stopRequested = false;
        System.out.println("STARTING");
        soundTrack();
    }

    public void stopRequest() {
        stopRequested = true;
        if (runThread != null) {
            dispatcher.stop();
            runThread.interrupt();

        }
    }

    AudioDispatcher dispatcher;

    public void soundTrack() {
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);
        OnsetHandler osh = new OnsetHandler() {
            @Override
            public void handleOnset(double v, double v1) {
                Log.i(TAG, "ONSET DETECTED!");
                setLightsToRandom();
            }
        };
        percussionOnsetDetector = new PercussionOnsetDetector(22050, 1024, osh, 50.0, 1.0);
        synchronized(this) {
            dispatcher.addAudioProcessor(percussionOnsetDetector);
            dispatcher.run();
        }
    }

    public void setLightsToRandom() {
        if (this.sdk.getSelectedBridge() == null) {
            Log.e(TAG, "Bridge is null");
            return;
        }
        List<PHLight> allLights = this.sdk.getSelectedBridge().getResourceCache().getAllLights();
        List<PointF> points = generateRandomLightValues(allLights.size());
        List<PHLightState> states = new LinkedList<>();
        for (PointF point : points) {
            PHLightState state = new PHLightState();
            state.setX(point.x);
            state.setY(point.y);
            state.setTransitionTime(0);
            states.add(state);
        }
        for (int i = 0; i < allLights.size() && i < states.size(); i++) {
            this.sdk.getSelectedBridge().updateLightState(allLights.get(i), states.get(i));
        }
    }

    public List<PointF> generateRandomLightValues(int n) {
        GamutArea gamutArea = new GamutArea(GamutTypes.B);
        Line rgLine = new Line(gamutArea.getRed(), gamutArea.getGreen());
        Line gbLine = new Line(gamutArea.getGreen(), gamutArea.getBlue());
        Line brLine = new Line(gamutArea.getBlue(), gamutArea.getRed());
        List<Line> gamutLines = new ArrayList<>();
        gamutLines.add(rgLine);
        gamutLines.add(gbLine);
        gamutLines.add(brLine);
        Random rand = new Random();
        List<PointF> lightValues = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            Line l = gamutLines.get(rand.nextInt(gamutLines.size() - 1));
            double ratio = rand.nextDouble() % 1.0;
            lightValues.add(l.getPointOnLine(ratio));
        }
        return lightValues;
    }
}
