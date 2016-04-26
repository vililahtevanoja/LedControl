package fi.aalto.mobilesystems.ledcontrol.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import fi.aalto.mobilesystems.ledcontrol.LedControl;
import fi.aalto.mobilesystems.ledcontrol.R;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.HueController;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.HueProperties;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HueProperties.loadProperties();

        final ImageButton setLightColoursButton = (ImageButton) findViewById(R.id.discoButton);
        setLightColoursButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LedControl.getContext(), RandomLightActivity.class);
                startActivity(intent);
            }
        });


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
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LedControl.getContext(), SetAlarmActivity.class);
                startActivity(intent);
            }
        });

        final ImageButton timeOfDayButton = (ImageButton) findViewById(R.id.setSunButton);
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

