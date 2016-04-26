package fi.aalto.mobilesystems.ledcontrol.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import fi.aalto.mobilesystems.ledcontrol.R;


public class RandomLightActivity extends AppCompatActivity {
    private PHHueSDK sdk;
    private static final int MAX_HUE = 65535;

    int DELAY;
    CheckBox ShotCheckBox;
    Button StartBtn, CancelBtn, StartDisko, StopDisko;
    EditText EtTime;
    Timer timer;
    RandomBlinkLight task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_light);
        ShotCheckBox = (CheckBox) findViewById(R.id.shot);
        EtTime = (EditText) findViewById(R.id.edt_time);
        StartBtn = (Button) findViewById(R.id.start);
        StartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (timer != null) {
                    timer.cancel();
                }
                String mEditText = EtTime.getText().toString();
                if(mEditText.matches("[0-9]+")) {
                    DELAY = Integer.parseInt(EtTime.getText().toString());
                } else {
                    DELAY = 1000;
                }


                timer = new Timer();
                task = new RandomBlinkLight();

                if (ShotCheckBox.isChecked()){
                    //single call
                    timer.schedule(task, DELAY);
                } else {
                    //delay 1000ms
                    timer.schedule(task, DELAY, DELAY);
                    Toast.makeText(RandomLightActivity.this, "task repeated with delay " + DELAY, Toast.LENGTH_LONG).show();
                }
            }
        });
        CancelBtn = (Button) findViewById(R.id.cancel);
        CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
            }
        });

        StartDisko = (Button) findViewById(R.id.btnDiskoOn);
        StartDisko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);

                PitchDetectionHandler pdh = new PitchDetectionHandler() {
                    @Override
                    public void handlePitch(PitchDetectionResult result,AudioEvent e) {
                        final float pitchInHz = result.getPitch();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView text = (TextView) findViewById(R.id.soundHertz);
                                text.setText("" + pitchInHz);
                            }
                        });
                    }
                };
                AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
                dispatcher.addAudioProcessor(p);
                new Thread(dispatcher,"Audio Dispatcher").start();
            }
        });
        StopDisko = (Button) findViewById(R.id.btnDiskoOff);
        StopDisko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
            }
        });

    }

    class RandomBlinkLight extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    randomLights();
                }
            });
        }

    }

    public void randomLights() {
        this.sdk = PHHueSDK.getInstance();
        if(this.sdk.getSelectedBridge() == null) {
            Log.e("Random Light", "Bridge is null");
            return;
        }

        List<PHLight> allLights = this.sdk.getSelectedBridge().getResourceCache().getAllLights();
        Random rand = new Random();

        for (PHLight light : allLights) {
            PHLightState lightState = new PHLightState();
            lightState.setHue(rand.nextInt(MAX_HUE));
            this.sdk.getSelectedBridge().updateLightState(light, lightState);
        }
    }

    public void randomLights(float pitchInHz ) {
        this.sdk = PHHueSDK.getInstance();
        if(this.sdk.getSelectedBridge() == null) {
            Log.e("Random Light", "Bridge is null");
            return;
        }

        List<PHLight> allLights = this.sdk.getSelectedBridge().getResourceCache().getAllLights();
        Random rand = new Random();
        if(pitchInHz > 10 || pitchInHz < 100) {
            for (PHLight light : allLights) {
                PHLightState lightState = new PHLightState();
                lightState.setHue(rand.nextInt(MAX_HUE));
                this.sdk.getSelectedBridge().updateLightState(light, lightState);
            }
        }else if(pitchInHz > 100 || pitchInHz < 200) {
            for (PHLight light : allLights) {
                PHLightState lightState = new PHLightState();
                lightState.setHue(rand.nextInt(MAX_HUE));
                this.sdk.getSelectedBridge().updateLightState(light, lightState);
            }
        }
    }
}

