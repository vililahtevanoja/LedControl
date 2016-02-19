package fi.aalto.mobilesystems.ledcontrol.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fi.aalto.mobilesystems.ledcontrol.R;
import fi.aalto.mobilesystems.ledcontrol.hue.HueController;
import fi.aalto.mobilesystems.ledcontrol.hue.HueProperties;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private HueController handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HueProperties.loadProperties();
        this.handler = new HueController();
    }
}
