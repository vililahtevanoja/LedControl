package fi.aalto.mobilesystems.ledcontrol.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;

import fi.aalto.mobilesystems.ledcontrol.R;
import fi.aalto.mobilesystems.ledcontrol.models.Alarm;

/**
 * Created by lenovo on 2016/4/20.
 */
public class SetAlarmActivity extends AppCompatActivity {
    private static final String TAG = "SetAlarmActivity";
    private TimePicker mTimePicker;
    private int mHour;
    private int mMin;
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_alert);

        mTimePicker=(TimePicker)findViewById(R.id.alarmTime);



    }
    public void setTime(View view){
        mHour = mTimePicker.getCurrentHour();
        mMin = mTimePicker.getCurrentMinute();

        mCalendar =Calendar.getInstance();

        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMin);

        Alarm alarm = new Alarm();
        alarm.SetAlarm(this, mCalendar, null, 0);

        Log.d(TAG,"mHour:" + mHour + " mMin:" + mMin);
    }

}
