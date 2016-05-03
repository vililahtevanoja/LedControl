package fi.aalto.mobilesystems.ledcontrol.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import fi.aalto.mobilesystems.ledcontrol.R;


public class RandomLightActivity extends AppCompatActivity {
    private PHHueSDK sdk;
    private static final int MAX_HUE = 65535;
    private static final String TAG = "RandomLightActivity";
    int DELAY;
    CheckBox ShotCheckBox;
    ImageButton StartBtn, CancelBtn, StartDisko, StopDisko, StartPrecussion, StopPrecussion;
    EditText EtTime;
    Timer timer;
    RandomBlinkLight task;
    Boolean diskoOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_light);
        ShotCheckBox = (CheckBox) findViewById(R.id.shot);
        EtTime = (EditText) findViewById(R.id.edt_time);
        StartBtn = (ImageButton) findViewById(R.id.start);
        StartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (timer != null) {
                    timer.cancel();
                }
                String mEditText = EtTime.getText().toString();
                if (mEditText.matches("[0-9]+")) {
                    DELAY = Integer.parseInt(EtTime.getText().toString());
                } else {
                    DELAY = 1000;
                }


                timer = new Timer();
                task = new RandomBlinkLight();

                if (ShotCheckBox.isChecked()) {
                    //single call
                    timer.schedule(task, DELAY);
                } else {
                    //delay 1000ms
                    timer.schedule(task, DELAY, DELAY);
                    Toast.makeText(RandomLightActivity.this, "task repeated with delay " + DELAY, Toast.LENGTH_LONG).show();
                }
            }
        });
        CancelBtn = (ImageButton) findViewById(R.id.cancel);
        CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
            }
        });

        StartDisko = (ImageButton) findViewById(R.id.btnDiskoOn);
        final DiskoLight as = new DiskoLight();
        final PercussionDetector pd = new PercussionDetector();

        StartDisko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Thread disko = new Thread(as);
                disko.start();
            }
        });
        StopDisko = (ImageButton) findViewById(R.id.btnDiskoOff);
        StopDisko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                as.stopRequest();
               try {
                   pd.stopRequest();
                   if (timer != null) {
                       timer.cancel();
                       timer = null;
                   }
               }catch(Exception e){}

            }
        });


        StartPrecussion = (ImageButton) findViewById(R.id.btnPrecussionOn);
        //final PercussionDetector pd = new PercussionDetector();

        StartPrecussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Thread perc = new Thread(pd);
                perc.start();
            }
        });

        StopPrecussion = (ImageButton) findViewById(R.id.btnPrecussionOff);
        StopPrecussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.stopRequest();
                try {
                    pd.stopRequest();
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                }catch(Exception e){}
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
        if (this.sdk.getSelectedBridge() == null) {
            Log.e(TAG, "Bridge is null");
            return;
        }
        List<PHLight> allLights = this.sdk.getSelectedBridge().getResourceCache().getAllLights();
        Random rand = new Random();
        for (PHLight light : allLights) {
            PHLightState lightState = new PHLightState();
            lightState.setHue(rand.nextInt(MAX_HUE));
            lightState.setTransitionTime(0);
            this.sdk.getSelectedBridge().updateLightState(light, lightState);
        }
    }
}

