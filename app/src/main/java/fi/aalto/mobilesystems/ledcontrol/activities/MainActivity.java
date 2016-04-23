package fi.aalto.mobilesystems.ledcontrol.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import fi.aalto.mobilesystems.ledcontrol.LedControl;
import fi.aalto.mobilesystems.ledcontrol.R;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.HueController;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.HueProperties;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private HueController handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HueProperties.loadProperties();
        this.handler = new HueController();
        this.handler.startBridgeSearch();
        this.handler.connectToEmulatorAccessPoint();

        /*
        final Button setLightColoursButton = (Button) findViewById(R.id.setColorsButton);
        setLightColoursButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LedControl.getContext(), SetLightColorsActivity.class);
                startActivity(intent);
            }
        });

        final Button handleBroadcastButton = (Button) findViewById(R.id.handleBroadcastButton);
        handleBroadcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LedControl.getContext(), HandleBroadcastActivity.class);
                startActivity(intent);
            }
        });


        final Button featureButton = (Button) findViewById(R.id.featureButton);
        featureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(LedControl.getContext(),Broadcast.class);
                startActivity(intent);
            }
        });
        */

        final ImageButton mail = (ImageButton) findViewById(R.id.mail);
        mail.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(LedControl.getContext(), mailColorActivity.class);
                startActivity(intent);
            }
        });

        final ImageButton phone = (ImageButton) findViewById(R.id.phone);
        phone.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(LedControl.getContext(),phoneColorActivity.class);
                startActivity(intent);
            }
        });

        final ImageButton music = (ImageButton) findViewById(R.id.music);
        music.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(LedControl.getContext(), musicColorActivity.class);
                startActivity(intent);
            }
        });

        final ImageButton setAlarmButton = (ImageButton) findViewById(R.id.alarm);
        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LedControl.getContext(),SetAlarmActivity.class);
                startActivity(intent);
            }
        });
    }
}
