package fi.aalto.mobilesystems.ledcontrol.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.larswerkman.lobsterpicker.LobsterPicker;
import com.larswerkman.lobsterpicker.OnColorListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHLight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import fi.aalto.mobilesystems.ledcontrol.LedControl;
import fi.aalto.mobilesystems.ledcontrol.R;
import fi.aalto.mobilesystems.ledcontrol.models.HandleBroadcastScene;

/**
 * Created by ZSY on 4/24/16.
 */
public class SelectLightColorActivity extends AppCompatActivity {
    private static final String TAG = "LightColorActivity";
    private ImageButton mButtonConfirm;
    private ImageButton mButtonCancel;
    private Spinner mSpinner;
    private LobsterPicker mLobsterPicker;
    private int mColor;
    private int HistoryColor;
    private PHHueSDK sdk;
    private List<PHLight> lights;
    private HandleBroadcastScene mBroadcastScene;
    private HashMap<String, String> mNameIdentifierMap;
    private SharedPreferences msharedPrefs;

    public static class Actions {
        public final static String PhoneCall = "Incoming Phone Call";
        public final static String SMS = "Incoming SMS";
        public final static String Alarm = "Alarm clock";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_light_color);


        mButtonConfirm = (ImageButton) findViewById(R.id.confirmButton);
        mButtonCancel = (ImageButton) findViewById(R.id.cancelButton);
        mLobsterPicker = (LobsterPicker) findViewById(R.id.lobsterpicker);
        mSpinner = (Spinner) findViewById(R.id.spinnerLight);
        initialize();

        this.sdk = PHHueSDK.getInstance();
        lights = this.sdk.getSelectedBridge().getResourceCache().getAllLights();
        mBroadcastScene = ((LedControl) this.getApplication()).getBroadcastScene();
        mNameIdentifierMap = new HashMap<>();

        for (PHLight light : lights) {
            mNameIdentifierMap.put(light.getName(), light.getIdentifier());
        }

        addListenerOnButton();
        addItemsOnSpinnerLight(lights);

    }

    private void addListenerOnButton() {

        mLobsterPicker.addOnColorListener(new OnColorListener() {
            @Override
            public void onColorChanged(@ColorInt int color) {

            }

            @Override
            public void onColorSelected(@ColorInt int color) {
                    mColor = color;
            }
        });

        mButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (getIntent().getAction()) {
                    case Actions.PhoneCall:
                        mBroadcastScene.addIncomingCallScene(
                                mNameIdentifierMap.get(mSpinner.getSelectedItem()), mColor);
                        Log.d(TAG, "Incoming Phone call: add to scene");
                        break;

                    case Actions.SMS:
                        mBroadcastScene.addIncomingSMSScene(
                                mNameIdentifierMap.get(mSpinner.getSelectedItem()), mColor);
                        Log.d(TAG, "Incoming SMS: add to scene");
                        break;

                    case Actions.Alarm:
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("lightIdentifier",
                                mNameIdentifierMap.get(mSpinner.getSelectedItem()));
                        returnIntent.putExtra("color", mColor);
                        setResult(Activity.RESULT_OK, returnIntent);
                        mBroadcastScene.addAlarmAlertScene(
                                mNameIdentifierMap.get(mSpinner.getSelectedItem()), mColor);
                        Log.d(TAG, "Alarm clock: add to scene");
                        finish();
                        break;
                }
                mLobsterPicker.setHistory(mColor);
                //  finish();
            }
        });

        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (getIntent().getAction()) {
                    case Actions.PhoneCall:
                        mBroadcastScene.removeIncomingCallScene(
                                mNameIdentifierMap.get(mSpinner.getSelectedItem()));
                        Log.d(TAG, "Incoming Phone call: remove light");
                        break;

                    case Actions.SMS:
                        mBroadcastScene.removeIncomingSMSScene(
                                mNameIdentifierMap.get(mSpinner.getSelectedItem()));
                        Log.d(TAG, "Incoming SMS: remove light");
                        break;

                    case Actions.Alarm:
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("lightIdentifier",
                                mNameIdentifierMap.get(mSpinner.getSelectedItem()));
                        returnIntent.putExtra("color", mColor);
                        setResult(Activity.RESULT_OK, returnIntent);
                        mBroadcastScene.removeAlarmAltertScene(
                                mNameIdentifierMap.get(mSpinner.getSelectedItem()));
                        Log.d(TAG, "Alarm clock: remove light");
                        finish();
                        break;
                }
                mLobsterPicker.setHistory(Color.WHITE);
            }
        });
    }

    private void addItemsOnSpinnerLight(List<PHLight> lights) {

        List<String> lightList = new ArrayList<>();

        for (PHLight light : lights) {
            lightList.add(light.getName());
            Log.d(TAG, "Light Name:" + light.getName() + " Identifier:" + light.getIdentifier());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.my_spinner_style,lightList) {

            @Override
            public View getView(int position, View convertView,ViewGroup parent) {

                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTextSize(20);
                ((TextView) v).setGravity(Gravity.CENTER);
                updateHistoryColor(getItem(position));
                return v;

            }

            @Override
            public View getDropDownView(int position, View convertView,ViewGroup parent) {

                View v = super.getDropDownView(position, convertView,parent);

                ((TextView) v).setGravity(Gravity.CENTER);

                return v;
            }

        };

        mSpinner.setAdapter(adapter);
    }

    private void updateHistoryColor(String lightName){
        switch(getIntent().getAction()) {
            case Actions.PhoneCall:
                if(mBroadcastScene.getIncomingCallScene().
                        containsKey(mNameIdentifierMap.get(lightName)))
                    HistoryColor = mBroadcastScene.getIncomingCallScene().
                            get(mNameIdentifierMap.get(lightName));
                else
                    HistoryColor = Color.WHITE;

                break;

            case Actions.SMS:
                if(mBroadcastScene.getIncomingSMSScene().
                        containsKey(mNameIdentifierMap.get(lightName)))
                    HistoryColor = mBroadcastScene.getIncomingSMSScene().
                            get(mNameIdentifierMap.get(lightName));
                else
                    HistoryColor = Color.WHITE;
                break;
            case Actions.Alarm:
                if(mBroadcastScene.getAlarmAlert().
                        containsKey(mNameIdentifierMap.get(lightName)))
                    HistoryColor = mBroadcastScene.getAlarmAlert().
                            get(mNameIdentifierMap.get(lightName));
                else
                    HistoryColor = Color.WHITE;
                break;
        }

        mLobsterPicker.setHistory(HistoryColor);

    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();  // Always call the superclass

        SharedPreferences.Editor prefsEditor = msharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mBroadcastScene);
        prefsEditor.putString("HandleBroadcastScene", json);
        prefsEditor.commit();
        Log.d(TAG, "toJson: " + json);

    }

    protected void initialize() {
       // msharedPrefs = getPreferences(MODE_PRIVATE);
        msharedPrefs = this.getSharedPreferences(getString(R.string.preference_file_key), this.MODE_PRIVATE);

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
