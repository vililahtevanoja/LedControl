package fi.aalto.mobilesystems.ledcontrol.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.larswerkman.lobsterpicker.LobsterPicker;
import com.larswerkman.lobsterpicker.OnColorListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.utilities.PHUtilities;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.List;

import fi.aalto.mobilesystems.ledcontrol.LedControl;
import fi.aalto.mobilesystems.ledcontrol.R;
import fi.aalto.mobilesystems.ledcontrol.models.HandleBroadcastScene;

/**
 * Created by Zhengwu on 21/04/2016.
 */
public class phoneColorActivity extends AppCompatActivity {

    private static final String TAG = "Phone Activity";
    private ImageButton submitButton;
    private static CheckBox light1;
    private static CheckBox light2;
    private static CheckBox light3;
    private static LobsterPicker lobsterPicker;
    private int mColor;
    private PHHueSDK sdk;
    private List<PHLight> lights;
    private HandleBroadcastScene mBroadcastScene;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_color);

        this.sdk = PHHueSDK.getInstance();
        lights = this.sdk.getSelectedBridge().getResourceCache().getAllLights();
        mBroadcastScene = ((LedControl) this.getApplication()).getBroadcastScene();

        light1 = (CheckBox) findViewById(R.id.light1);
        light2 = (CheckBox) findViewById(R.id.light2);
        light3 = (CheckBox) findViewById(R.id.light3);
        lobsterPicker = (LobsterPicker) findViewById(R.id.lobsterpicker);
        lobsterPicker.setColorHistoryEnabled(true);

        addListenerOnButton();
        //lobsterPicker.getColor();

        /*
        lobsterPicker.addOnColorListener(new OnColorListener() {
            @Override
            public void onColorChanged(@ColorInt int color) {

            }

            @Override
            public void onColorSelected(@ColorInt int color) {

            }
        });
        */
    }
    /*
    private void addLightName(List<PHLight> lights){


    }
    */

    public void addListenerOnButton(){

        submitButton = (ImageButton) findViewById(R.id.OK);

        submitButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                mColor = lobsterPicker.getColor();
                PHLightState lightState = new PHLightState();
                PHLight light;

                if(light1.isChecked()){
                    light = lights.get(0);
                    float xy[] = PHUtilities.calculateXYFromRGB(
                            Color.red(mColor), Color.green(mColor), Color.blue(mColor), light.getModelNumber());
                    lightState.setX(xy[0]);
                    lightState.setY(xy[1]);
                    mBroadcastScene.addIncomingCallScene(light, lightState);
                    Log.d(TAG, "Incoming Phone call: add to scene");
                }

                if(light2.isChecked()){
                    light = lights.get(1);
                    float xy[] = PHUtilities.calculateXYFromRGB(
                            Color.red(mColor), Color.green(mColor), Color.blue(mColor), light.getModelNumber());
                    lightState.setX(xy[0]);
                    lightState.setY(xy[1]);
                    mBroadcastScene.addIncomingCallScene(light, lightState);
                    Log.d(TAG, "Incoming Phone call: add to scene");
                }

                if (light3.isChecked()) {
                    light = lights.get(2);
                    float xy[] = PHUtilities.calculateXYFromRGB(
                            Color.red(mColor), Color.green(mColor), Color.blue(mColor), light.getModelNumber());
                    lightState.setX(xy[0]);
                    lightState.setY(xy[1]);
                    mBroadcastScene.addIncomingCallScene(light, lightState);
                    Log.d(TAG, "Incoming Phone call: add to scene");
                }

                if(!light1.isChecked() && !light2.isChecked() && !light3.isChecked()){
                    AlertDialog alertDialog = new AlertDialog.Builder(phoneColorActivity.this).create();
                    alertDialog.setTitle("Oops");
                    alertDialog.setMessage("Please selecte at least one light");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        });
    }

}
