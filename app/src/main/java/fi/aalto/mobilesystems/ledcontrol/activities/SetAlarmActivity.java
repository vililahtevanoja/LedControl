package fi.aalto.mobilesystems.ledcontrol.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.philips.lighting.hue.sdk.PHHueSDK;

import java.util.Calendar;

import fi.aalto.mobilesystems.ledcontrol.LedControl;
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
    private int mColor;
    private PHHueSDK sdk;
    private Button mSelectBulbButton;
    private String lightIdentifier;
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_alert);

        this.sdk = PHHueSDK.getInstance();
        mTimePicker=(TimePicker)findViewById(R.id.alarmTime);
        mSelectBulbButton = (Button) findViewById(R.id.light_button);


    }

    public void setTime(View view){

        if(lightIdentifier == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please select at least one light")
                    .setTitle("Oops");
            AlertDialog dialog = builder.create();
            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog.show();
            return;
        }
        mHour = mTimePicker.getCurrentHour();
        mMin = mTimePicker.getCurrentMinute();

        mCalendar =Calendar.getInstance();

        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMin);
        mCalendar.set(Calendar.SECOND, 0);

        Alarm alarm = new Alarm();
        alarm.SetAlarm(this, mCalendar, lightIdentifier, mColor);

        Toast.makeText(SetAlarmActivity.this,
                        "\nlight: " + lightIdentifier +
                        "\ncolor: " + mColor +
                        "\n" + mHour + ":" + mMin,
                Toast.LENGTH_SHORT).show();

        Log.d(TAG,"mHour:" + mHour + " mMin:" + mMin);
    }

    public void selectLight(View view) {

        if(this.sdk.getSelectedBridge() == null)
        {
            Log.d(TAG, "No bridge found");
            Toast.makeText(SetAlarmActivity.this,
                    "No bridge found",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(LedControl.getContext(),SelectLightColorActivity.class);
        intent.setAction(SelectLightColorActivity.Actions.Alarm);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                lightIdentifier = data.getStringExtra("lightIdentifier");
                mColor = data.getIntExtra("color", 0);

                Toast.makeText(SetAlarmActivity.this,
                                "\nlight: " + lightIdentifier +
                                "\ncolor: " + mColor,
                        Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                lightIdentifier = data.getStringExtra("lightIdentifier");
                Log.d(TAG, "alarm canceled light identifier:" + lightIdentifier);
                lightIdentifier = null;
                //Write your code if there's no result
            }
        }
    }//onActivityResult

}
