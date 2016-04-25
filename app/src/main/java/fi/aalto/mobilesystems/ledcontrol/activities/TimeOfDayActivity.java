package fi.aalto.mobilesystems.ledcontrol.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;

import fi.aalto.mobilesystems.ledcontrol.LedControl;
import fi.aalto.mobilesystems.ledcontrol.R;

public class TimeOfDayActivity extends AppCompatActivity {
    private final static String TAG = "TimeOfDayActivity";
    private CheckBox mIsAutomatic;
    private EditText mMorningHour;
    private EditText mNightHour;
    private EditText mTransitionHours;
    private SharedPreferences mSharedPreferences;
    private static final String CLASS_PREFKEY = "timeofday";
    private static final String IS_AUTOMATIC_PREFKEY = CLASS_PREFKEY + ".isAutomatic";
    private static final String MORNING_HOUR_PREFKEY = CLASS_PREFKEY + ".morningHour";
    private static final String NIGHT_HOUR_PREF_KEY = CLASS_PREFKEY + ".nightHour";
    private static final String TRANSITION_PREF_KEY = CLASS_PREFKEY + ".transitionHours";
    private static final String ENABLED_PREF_KEY = CLASS_PREFKEY + ".enabled";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_of_day);
        mSharedPreferences = LedControl.getSharedPreferences();
        mIsAutomatic = (CheckBox) findViewById(R.id.isAutomatic);
        mMorningHour = (EditText) findViewById(R.id.morningHour);
        mNightHour = (EditText) findViewById((R.id.nightHour));
        mTransitionHours = (EditText) findViewById(R.id.transitionHours);
    }

    private void initializeValues() {
        int morningHour = mSharedPreferences.getInt(MORNING_HOUR_PREFKEY, 6);
        int nightHour = mSharedPreferences.getInt(NIGHT_HOUR_PREF_KEY, 22);
        int transition = mSharedPreferences.getInt(TRANSITION_PREF_KEY, 1);
    }

    private void setAutomatic(boolean isAutomatic) {
        if (isAutomatic) {
            mMorningHour.setEnabled(false);
            mNightHour.setEnabled(false);
            mTransitionHours.setEnabled(false);
        }
        else {
            mMorningHour.setEnabled(true);
            mNightHour.setEnabled(true);
            mTransitionHours.setEnabled(true);
        }
    }
}
