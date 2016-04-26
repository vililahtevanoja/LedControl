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
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.GamutArea;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.GamutTypes;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.Line;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.PointF;

/**
 * Created by haris on 4/26/2016.
 */
public class DiskoLight implements Runnable {
    private PHHueSDK sdk;
    private static final int MAX_HUE = 65535;
    private static final String TAG = "RandomLightActivity";

    private volatile boolean stopRequested;
    private Thread runThread;

    public void run() {
        runThread = Thread.currentThread();
        stopRequested = false;
        int count = 0;
        while (!stopRequested) {
            soundTrack();
           // System.out.println("count: " + count);
            // count++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException x) {
                Thread.currentThread().interrupt();
            }
        }
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
        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult result, AudioEvent e) {
                final float pitchInHz = result.getPitch();
                randomLights(pitchInHz);
                System.out.println("pitchInHz: " + pitchInHz);
            }
        };
        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(p);
        dispatcher.run();
    }

    /**
     * Diskolight features that blinks the lights based on Sound Hertz
     */
    public void randomLights(float pitchInHz) {
        this.sdk = PHHueSDK.getInstance();
        PHLight light1, light2, light3;
        if (this.sdk.getSelectedBridge() == null) {
            Log.e(TAG, "Bridge is null for diskolights");
            return;
        }
        List<PHLight> allLights = this.sdk.getSelectedBridge().getResourceCache().getAllLights();

        if (pitchInHz > 10 || pitchInHz < 100) {
            for (PHLight light : allLights) {
                PHLightState lightState = new PHLightState();
                PointF pf = generateRandomLightValues(1).get(0);
                lightState.setX(pf.x);
                lightState.setY(pf.y);
                this.sdk.getSelectedBridge().updateLightState(light, lightState);
            }
        } else if (pitchInHz >= 100 || pitchInHz < 200) {
            for (PHLight light : allLights) {
                PHLightState lightState = new PHLightState();
                PointF pf = generateRandomLightValues(2).get(0);
                lightState.setX(pf.x);
                lightState.setY(pf.y);
                this.sdk.getSelectedBridge().updateLightState(light, lightState);
            }
        }
//        } else if (pitchInHz >= 200) {
//            for (PHLight light : allLights) {
//                PHLightState lightState = new PHLightState();
//                PointF pf = generateRandomLightValues(1).get(0);
//                lightState.setX(pf.x);
//                lightState.setY(pf.y);
//                this.sdk.getSelectedBridge().updateLightState(light, lightState);
//            }
//        }
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
            Line l = gamutLines.get(rand.nextInt(gamutLines.size()-1));
            double ratio = rand.nextDouble() % 1.0;
            lightValues.add(l.getPointOnLine(ratio));
        }
        return lightValues;
    }
}
