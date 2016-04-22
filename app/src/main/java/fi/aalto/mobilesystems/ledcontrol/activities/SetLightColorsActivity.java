package fi.aalto.mobilesystems.ledcontrol.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import fi.aalto.mobilesystems.ledcontrol.LedControl;
import fi.aalto.mobilesystems.ledcontrol.R;

public class SetLightColorsActivity extends AppCompatActivity {
    private PHHueSDK sdk;
    private static final int MAX_HUE=65535;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_light_colors);
        ViewGroup root = (ViewGroup)findViewById(android.R.id.content);
        final Button setLightButtonOn = (Button) findViewById(R.id.setButtonOn);
        this.sdk = PHHueSDK.getInstance();
        List<PHLight> lights = this.sdk.getSelectedBridge().getResourceCache().getAllLights();
        List<PHLightState> states = new LinkedList<>();
        for (PHLight light : lights) {
            states.add(light.getLastKnownLightState());
        }
        addLightViews(root, states);

        setLightButtonOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(LedControl.getContext(), "Bridge Connected", Toast.LENGTH_LONG).show();
                randomLights();
            }
        });
    }


    private void addLightViews(ViewGroup root, List<PHLightState> states) {
        /*for (PHLightState state : states) {
            SetLightColorView lightView = (SetLightColorView)findViewById(R.id.setLightColorView);
            lightView.setColor(state.getX(), state.getY());
            root.addView(lightView);
        }*/
    }

    public void randomLights() {
        this.sdk = PHHueSDK.getInstance();

        List<PHLight> allLights = this.sdk.getSelectedBridge().getResourceCache().getAllLights();
        Random rand = new Random();

        for (PHLight light : allLights) {
            PHLightState lightState = new PHLightState();
            lightState.setHue(rand.nextInt(MAX_HUE));
            // To validate your lightstate is valid (before sending to the bridge) you can use:
            // String validState = lightState.validateState();
            this.sdk.getSelectedBridge().updateLightState(light, lightState);
            //  bridge.updateLightState(light, lightState);   // If no bridge response is required then use this simpler form

        }
    }

}
