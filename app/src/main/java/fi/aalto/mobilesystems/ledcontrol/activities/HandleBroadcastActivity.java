package fi.aalto.mobilesystems.ledcontrol.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.utilities.PHUtilities;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.ArrayList;
import java.util.List;

import fi.aalto.mobilesystems.ledcontrol.LedControl;
import fi.aalto.mobilesystems.ledcontrol.R;
import fi.aalto.mobilesystems.ledcontrol.models.HandleBroadcastScene;
import me.priyesh.chroma.ChromaDialog;
import me.priyesh.chroma.ColorMode;
import me.priyesh.chroma.ColorSelectListener;

/**
 * Created by ZSY on 4/17/16.
 */
public class HandleBroadcastActivity extends AppCompatActivity {
    private static final String TAG = "HandleBroadcastActivity";
    private Spinner spinnerLight, spinnerSource;
    private Button buttonSubmit;
    private Button buttonSelectColor;
    private TextView mColorTextView;
    private int mColor;
    private PHHueSDK sdk;
    private List<PHLight> lights;
    private HandleBroadcastScene mBroadcastScene;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_broadcast);

        this.sdk = PHHueSDK.getInstance();
        lights = this.sdk.getSelectedBridge().getResourceCache().getAllLights();
        mBroadcastScene = ((LedControl) this.getApplication()).getBroadcastScene();

        addItemsOnSpinnerLight(lights);
        addListenerOnButton();

    }

    private void addItemsOnSpinnerLight(List<PHLight> lights) {

        spinnerLight = (Spinner) findViewById(R.id.spinner_light);
        List<String> lightList = new ArrayList<String>();

        for (PHLight light : lights) {
            lightList.add(light.getName());
            Log.d(TAG, "Light Name:" + light.getName() + " Identifier:" + light.getIdentifier());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lightList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLight.setAdapter(dataAdapter);
    }

    public void addListenerOnButton() {
        spinnerLight = (Spinner) findViewById(R.id.spinner_light);
        spinnerSource = (Spinner) findViewById(R.id.spinner_source);
        buttonSubmit = (Button) findViewById(R.id.btnSubmit);
        buttonSelectColor = (Button) findViewById(R.id.select_color_button);
        mColorTextView = (TextView) findViewById(R.id.color_text_view);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HandleBroadcastActivity.this,
                        "OnClickListener : " +
                                "\nLight: " + String.valueOf(spinnerLight.getSelectedItem()) +
                                "\nSource: " + String.valueOf(spinnerSource.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();

                if(spinnerSource.getSelectedItem().equals("Incoming Phone Call")) {
                    Log.d(TAG, "Incoming Phone Call: spinnerSource");
                    PHLightState lightState = new PHLightState();
                    for (PHLight light : lights) {
                        if(light.getName().equals(String.valueOf(spinnerLight.getSelectedItem()))) {
                            float xy[] = PHUtilities.calculateXYFromRGB(
                                    Color.red(mColor), Color.green(mColor), Color.blue(mColor), light.getModelNumber());
                            lightState.setX(xy[0]);
                            lightState.setY(xy[1]);
                            mBroadcastScene.addIncomingCallScene(light, lightState);
                            Log.d(TAG, "Incoming Phone call: add to scene");
                        }
                    }

                }

                if(spinnerSource.getSelectedItem().equals("Incoming SMS")) {
                    Log.d(TAG, "Incoming SMS: spinnerSource");
                    PHLightState lightState = new PHLightState();
                    for (PHLight light : lights) {
                        if(light.getName().equals(String.valueOf(spinnerLight.getSelectedItem()))) {
                            float xy[] = PHUtilities.calculateXYFromRGB(
                                    Color.red(mColor), Color.green(mColor), Color.blue(mColor), light.getModelNumber());
                            lightState.setX(xy[0]);
                            lightState.setY(xy[1]);
                            mBroadcastScene.addIncomingSMSScene(light, lightState);
                            Log.d(TAG, "Incoming SMS: add to scene");
                        }
                    }

                }

                if(spinnerSource.getSelectedItem().equals("Alarm alert")) {
                    Log.d(TAG, "Alarm alert: spinnerSource");
                    PHLightState lightState = new PHLightState();
                    for (PHLight light : lights) {
                        if(light.getName().equals(String.valueOf(spinnerLight.getSelectedItem()))) {
                            float xy[] = PHUtilities.calculateXYFromRGB(
                                    Color.red(mColor), Color.green(mColor), Color.blue(mColor), light.getModelNumber());
                            lightState.setX(xy[0]);
                            lightState.setY(xy[1]);
                            mBroadcastScene.addAlarmAlertScene(light, lightState);
                            Log.d(TAG, "Alarm alert: add to scene");
                        }
                    }

                }
            }
        });

        buttonSelectColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog();
            }
        });
    }

    private void showColorPickerDialog() {
        new ChromaDialog.Builder()
                .initialColor(Color.GREEN)
                .colorMode(ColorMode.RGB)
                .onColorSelected(new ColorSelectListener() {
                    @Override
                    public void onColorSelected(int color) {
                        mColor = color;
                        updateTextView(color);
                    }
                })
                .create()
                .show(getSupportFragmentManager(), "dialog");
    }

    private void updateTextView(int color) {
        mColorTextView.setText(String.format("#%06X", 0xFFFFFF & color));
        mColorTextView.setTextColor(color);
    }


}
