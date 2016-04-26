package fi.aalto.mobilesystems.ledcontrol.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import fi.aalto.mobilesystems.ledcontrol.R;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.GamutArea;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.GamutTypes;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.Line;
import fi.aalto.mobilesystems.ledcontrol.ledcontrol.PointF;


public class DiskoLightActivity extends AppCompatActivity {
    int DELAY;
    Button StartBtn, CancelBtn;
    Timer timer;
    SoundTrack task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_light);
        StartBtn = (Button) findViewById(R.id.start);
        CancelBtn = (Button) findViewById(R.id.cancel);
        StartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (timer != null) {
                    timer.cancel();
                }

                timer = new Timer();
                task = new SoundTrack();
                    //delay 1000ms
                    timer.schedule(task, DELAY, DELAY);
                    Toast.makeText(DiskoLightActivity.this, "task repeated with delay " + DELAY, Toast.LENGTH_LONG).show();
            }
        });

        CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
            }
        });

    }

    class SoundTrack extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    trackSound();
                }
            });
        }

    }

    public void trackSound() {
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);

        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult result,AudioEvent e) {
                final float pitchInHz = result.getPitch();
                       TextView text = (TextView) findViewById(R.id.soundHertz);
                        text.setText("" + pitchInHz);

            }
        };
        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(p);
       // new Thread(dispatcher,"Audio Dispatcher").start();
    }

    public List<PointF> generateRandomLightValues(int n) {
        GamutArea gamutArea = new GamutArea(GamutTypes.B);
        Line rgLine = new Line(gamutArea.getRed(), gamutArea.getGreen());
        Line gbLine = new Line(gamutArea.getGreen(), gamutArea.getBlue());
        Line brLine = new Line(gamutArea.getBlue(), gamutArea.getRed());
        List<Line> gamutLines = new ArrayList<>();
        gamutLines.add(rgLine);
        gamutLines.add(gbLine);
        gamutLines.add(brLine);
        Random rand = new Random();
        List<PointF> lightValues = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            Line l = gamutLines.get(rand.nextInt(n));
            double ratio = rand.nextDouble() % 1.0;
            lightValues.add(l.getPointOnLine(ratio));
        }
        return lightValues;
    }
}

