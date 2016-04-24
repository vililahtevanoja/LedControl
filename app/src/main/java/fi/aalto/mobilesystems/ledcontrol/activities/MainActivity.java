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

       /* final Button setLightColoursButton = (Button) findViewById(R.id.setColorsButton);
        setLightColoursButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LedControl.getContext(), SetLightColorsActivity.class);
                startActivity(intent);
            }
        });*/

        final ImageButton setPhoneButton = (ImageButton) findViewById(R.id.phoneButton);
        setPhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LedControl.getContext(), SelectLightColorActivity.class);
                intent.setAction(SelectLightColorActivity.Actions.PhoneCall);
                startActivity(intent);
            }
        });

        final ImageButton setSMSButton = (ImageButton) findViewById(R.id.smsButton);
        setSMSButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LedControl.getContext(),SelectLightColorActivity.class);
                intent.setAction(SelectLightColorActivity.Actions.SMS);
                startActivity(intent);
            }
        });

        final ImageButton setAlarmButton = (ImageButton) findViewById(R.id.setAlarmButton);
        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LedControl.getContext(),SetAlarmActivity.class);
                startActivity(intent);
            }
        });


    }
}
