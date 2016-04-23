package fi.aalto.mobilesystems.ledcontrol.activities;

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
import android.widget.TimePicker;
import android.view.Menu;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.utilities.PHUtilities;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fi.aalto.mobilesystems.ledcontrol.LedControl;
import fi.aalto.mobilesystems.ledcontrol.R;
import fi.aalto.mobilesystems.ledcontrol.models.HandleBroadcastScene;
import me.priyesh.chroma.ChromaDialog;
import me.priyesh.chroma.ColorMode;
import me.priyesh.chroma.ColorSelectListener;

/**
 * Created by lenovo on 2016/4/20.
 */
public class SetAlarmActivity extends AppCompatActivity {
    private static final String TAG = "SetAlarmActivity";
    private TimePicker alarmTime;
    private Calendar calendar;
    private String format= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_alert);

        alarmTime=(TimePicker)findViewById(R.id.alarmTime);
        calendar=Calendar.getInstance();

        // int hour=calendar.get(Calendar.HOUR_OF_DAY);
        // int min=calendar.get(Calendar.MINUTE);
        // showTime(hour,min);

        //this.sdk = PHHueSDK.getInstance();

    }
    public void setTime(View view){
        int hour=alarmTime.getCurrentHour();
        int min=alarmTime.getCurrentMinute();
    }

}
