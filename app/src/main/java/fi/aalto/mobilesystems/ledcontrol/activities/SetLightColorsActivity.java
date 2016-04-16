package fi.aalto.mobilesystems.ledcontrol.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.LinkedList;
import java.util.List;

import fi.aalto.mobilesystems.ledcontrol.R;
import fi.aalto.mobilesystems.ledcontrol.views.SetLightColorView;

public class SetLightColorsActivity extends AppCompatActivity {
    private PHHueSDK sdk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_light_colors);
        ViewGroup root = (ViewGroup)findViewById(android.R.id.content);
        this.sdk = PHHueSDK.getInstance();
        List<PHLight> lights = this.sdk.getSelectedBridge().getResourceCache().getAllLights();
        List<PHLightState> states = new LinkedList<>();
        for (PHLight light : lights) {
            states.add(light.getLastKnownLightState());
        }
        addLightViews(root, states);
    }

    private void addLightViews(ViewGroup root, List<PHLightState> states) {
        /*for (PHLightState state : states) {
            SetLightColorView lightView = (SetLightColorView)findViewById(R.id.setLightColorView);
            lightView.setColor(state.getX(), state.getY());
            root.addView(lightView);
        }*/
    }
}
