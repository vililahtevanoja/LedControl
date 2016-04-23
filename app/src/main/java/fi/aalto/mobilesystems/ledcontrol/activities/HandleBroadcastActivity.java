package fi.aalto.mobilesystems.ledcontrol.activities;

import android.content.SharedPreferences;
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

import com.google.gson.Gson;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHLight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fi.aalto.mobilesystems.ledcontrol.LedControl;
import fi.aalto.mobilesystems.ledcontrol.R;
import fi.aalto.mobilesystems.ledcontrol.models.Alarm;
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
    private HashMap<String, String> mNameIdentifierMap;
    private SharedPreferences msharedPrefs;

    public static class Actions {
        public final static String PhoneCall = "Incoming Phone Call";
        public final static String SMS = "Incoming SMS";
        public final static String Alarm = "Alarm alert";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_broadcast);
        initialize();
        this.sdk = PHHueSDK.getInstance();
        lights = this.sdk.getSelectedBridge().getResourceCache().getAllLights();
        mBroadcastScene = ((LedControl) this.getApplication()).getBroadcastScene();
        mNameIdentifierMap = new HashMap<>();

        for (PHLight light : lights) {
            mNameIdentifierMap.put(light.getName(), light.getIdentifier());
        }

        addItemsOnSpinnerLight(lights);
        addItemsOnSpinnerSource();
        addListenerOnButton();

        Alarm alarm = new Alarm();
        alarm.SetAlarm(this);


    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy");
        super.onDestroy();  // Always call the superclass

        SharedPreferences.Editor prefsEditor = msharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mBroadcastScene);
        prefsEditor.putString("HandleBroadcastScene", json);
        prefsEditor.commit();
        Log.d(TAG,"toJson: " + json);

    }

    private void addItemsOnSpinnerLight(List<PHLight> lights) {

        spinnerLight = (Spinner) findViewById(R.id.spinner_light);
        List<String> lightList = new ArrayList<>();

        for (PHLight light : lights) {
            lightList.add(light.getName());
            Log.d(TAG, "Light Name:" + light.getName() + " Identifier:" + light.getIdentifier());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, lightList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLight.setAdapter(dataAdapter);
    }

    private void addItemsOnSpinnerSource() {

        spinnerSource = (Spinner) findViewById(R.id.spinner_source);
        List<String> sourceList = new ArrayList<>();

        sourceList.add(Actions.SMS);
        sourceList.add(Actions.PhoneCall);
        sourceList.add(Actions.Alarm);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, sourceList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSource.setAdapter(dataAdapter);
    }

    public void addListenerOnButton() {
        spinnerLight = (Spinner) findViewById(R.id.spinner_light);
        spinnerSource = (Spinner) findViewById(R.id.spinner_source);
        buttonSubmit = (Button) findViewById(R.id.btnSubmit);
        buttonSelectColor = (Button) findViewById(R.id.select_color_button);
        mColorTextView = (TextView) findViewById(R.id.color_text_view);

        buttonSelectColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HandleBroadcastActivity.this,
                        "OnClickListener : " +
                                "\nLight: " + String.valueOf(spinnerLight.getSelectedItem()) +
                                "\nSource: " + String.valueOf(spinnerSource.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();

                switch(spinnerSource.getSelectedItem().toString()) {
                    case Actions.PhoneCall:
                        mBroadcastScene.addIncomingCallScene(
                                mNameIdentifierMap.get(spinnerLight.getSelectedItem()), mColor);
                        Log.d(TAG, "Incoming Phone call: add to scene");
                        break;

                    case Actions.SMS:
                        mBroadcastScene.addIncomingSMSScene(
                                mNameIdentifierMap.get(spinnerLight.getSelectedItem()), mColor);
                        Log.d(TAG, "Incoming SMS: add to scene");
                        break;

                    case Actions.Alarm:
                        mBroadcastScene.addAlarmAlertScene(
                                mNameIdentifierMap.get(spinnerLight.getSelectedItem()), mColor);
                        Log.d(TAG, "Alarm alert: add to scene");
                        break;

                }
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

    protected void initialize() {
        msharedPrefs = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        String json = msharedPrefs.getString("HandleBroadcastScene","");
        if(json.equals("")){
            Log.d(TAG,"getJson: "+json);
        } else {
            HandleBroadcastScene obj = gson.fromJson(json, HandleBroadcastScene.class);
            ((LedControl) this.getApplication()).setBroadcastScene(obj);
            Log.d(TAG, "getJson: " + json);
        }
    }

}
