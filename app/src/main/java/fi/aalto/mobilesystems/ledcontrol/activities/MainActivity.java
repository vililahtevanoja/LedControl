package fi.aalto.mobilesystems.ledcontrol.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
<<<<<<< HEAD
import android.widget.TextView;
=======
import android.widget.ImageButton;
>>>>>>> Shudaa

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
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

<<<<<<< HEAD
      final Button setLightColoursButton = (Button) findViewById(R.id.setColorsButton);
=======
        /*
        final Button setLightColoursButton = (Button) findViewById(R.id.setColorsButton);
>>>>>>> Shudaa
        setLightColoursButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LedControl.getContext(), SetLightColorsActivity.class);
                startActivity(intent);
            }
        });
<<<<<<< HEAD

<<<<<<< HEAD
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment()).commit();
//        }
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);


        dispatcher.addAudioProcessor(new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, new PitchDetectionHandler() {

            @Override
            public void handlePitch(PitchDetectionResult pitchDetectionResult,
                                    AudioEvent audioEvent) {
                final float pitchInHz = pitchDetectionResult.getPitch();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView text = (TextView) findViewById(R.id.textView1);
                        text.setText("" + pitchInHz);
                    }
                });

            }
        }));
        new Thread(dispatcher,"Audio Dispatcher").start();

=======
>>>>>>> 9a6683c7c3c81494693edbb541f2ec07e1cef376
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
=======
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
                startActivityForResult(intent,1);
            }
        });

        final ImageButton phone = (ImageButton) findViewById(R.id.phone);
        phone.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(LedControl.getContext(),phoneColorActivity.class);
                startActivityForResult(intent,1);
            }
        });

        final ImageButton music = (ImageButton) findViewById(R.id.music);
        music.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(LedControl.getContext(), musicColorActivity.class);
                startActivityForResult(intent,1);
            }
        });

        final ImageButton setAlarmButton = (ImageButton) findViewById(R.id.alarm);
        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LedControl.getContext(),SetAlarmActivity.class);
                startActivityForResult(intent,1);
            }
        });
>>>>>>> Shudaa
    }
}
