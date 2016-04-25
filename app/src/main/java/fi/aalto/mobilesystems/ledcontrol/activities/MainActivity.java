package fi.aalto.mobilesystems.ledcontrol.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        final Button setLightColoursButton = (Button) findViewById(R.id.setColorsButton);
        setLightColoursButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LedControl.getContext(), RandomLightActivity.class);
                startActivity(intent);
            }
        });

//        final Button handleBroadcastButton = (Button) findViewById(R.id.handleBroadcastButton);
//        handleBroadcastButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LedControl.getContext(), HandleBroadcastActivity.class);
//                startActivity(intent);
//            }
//        });
//
//
//        final Button featureButton = (Button) findViewById(R.id.featureButton);
//        featureButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(LedControl.getContext(), Broadcast.class);
//                startActivity(intent);
//            }
//        });


        final ImageButton mail = (ImageButton) findViewById(R.id.mail);
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LedControl.getContext(), mailColorActivity.class);
                startActivity(intent);
                //startActivityForResult(intent, 1);
            }
        });

        final ImageButton phone = (ImageButton) findViewById(R.id.phone);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LedControl.getContext(), phoneColorActivity.class);
                startActivity(intent);
                //startActivityForResult(intent, 1);
            }
        });

        final ImageButton music = (ImageButton) findViewById(R.id.music);
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LedControl.getContext(), musicColorActivity.class);
                startActivity(intent);
            }
        });

        final ImageButton setAlarmButton = (ImageButton) findViewById(R.id.alarm);
        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LedControl.getContext(), SetAlarmActivity.class);
                startActivity(intent);
                //startActivityForResult(intent, 1);
            }
        });

        final ImageButton timeOfDayButton = (ImageButton) findViewById(R.id.timeofday);
        timeOfDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LedControl.getContext(), TimeOfDayActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_main,
                    container, false);
            return rootView;
        }
    }

}

