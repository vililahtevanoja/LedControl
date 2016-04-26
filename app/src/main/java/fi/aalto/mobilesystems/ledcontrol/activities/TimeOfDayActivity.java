package fi.aalto.mobilesystems.ledcontrol.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

import fi.aalto.mobilesystems.ledcontrol.LedControl;
import fi.aalto.mobilesystems.ledcontrol.R;
import fi.aalto.mobilesystems.ledcontrol.services.AutomaticTimeOfDay;
import fi.aalto.mobilesystems.ledcontrol.services.SimpleTimeOfDay;

public class TimeOfDayActivity extends AppCompatActivity {
    private final static String TAG = "TimeOfDayActivity";
    private CheckBox mIsEnabled;
    private CheckBox mIsAutomatic;
    private EditText mMorningHour;
    private EditText mNightHour;
    private EditText mTransitionHours;
    private Button mSaveButton;
    private SharedPreferences mSharedPreferences;
    private Toast mCurrentToast;
    private static final String CLASS_PREFKEY = "timeofday";
    private static final String ENABLED_PREF_KEY = CLASS_PREFKEY + ".enabled";
    private static final String IS_AUTOMATIC_PREFKEY = CLASS_PREFKEY + ".isAutomatic";
    private static final String MORNING_HOUR_PREFKEY = CLASS_PREFKEY + ".morningHour";
    private static final String NIGHT_HOUR_PREF_KEY = CLASS_PREFKEY + ".nightHour";
    private static final String TRANSITION_PREF_KEY = CLASS_PREFKEY + ".transitionHours";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_of_day);
        mSharedPreferences = LedControl.getSharedPreferences();
        mIsEnabled = (CheckBox) findViewById(R.id.isEnabled);
        mIsAutomatic = (CheckBox) findViewById(R.id.isAutomatic);
        mMorningHour = (EditText) findViewById(R.id.morningHour);
        mNightHour = (EditText) findViewById((R.id.nightHour));
        mTransitionHours = (EditText) findViewById(R.id.transitionHours);
        mSaveButton = (Button) findViewById(R.id.save);
        mCurrentToast = new Toast(LedControl.getContext());
        initializeValues();
        setListeners();
    }

    private void initializeValues() {
        boolean isEnabled = mSharedPreferences.getBoolean(ENABLED_PREF_KEY, false);
        boolean isAutomatic = mSharedPreferences.getBoolean(IS_AUTOMATIC_PREFKEY, false);
        int morningHour = mSharedPreferences.getInt(MORNING_HOUR_PREFKEY, 6);
        int nightHour = mSharedPreferences.getInt(NIGHT_HOUR_PREF_KEY, 22);
        int transition = mSharedPreferences.getInt(TRANSITION_PREF_KEY, 1);
        mIsAutomatic.setChecked(isAutomatic);
        mIsEnabled.setSelected(isEnabled);
        mMorningHour.setText(String.format(Locale.getDefault(), "%d", morningHour));
        mNightHour.setText(String.format(Locale.getDefault(), "%d", nightHour));
        mTransitionHours.setText(String.format(Locale.getDefault(), "%d", transition));
        setAutomatic(isAutomatic);
    }

    private void setAutomatic(boolean isAutomatic) {
        if (isAutomatic) {
            findViewById(R.id.wakeUpAtTextView).setVisibility(View.GONE);
            findViewById(R.id.morningHour).setVisibility(View.GONE);
            findViewById(R.id.goToSleepAtTextView).setVisibility(View.GONE);
            findViewById(R.id.nightHour).setVisibility(View.GONE);
            findViewById(R.id.transitionTimeHoursTextView).setVisibility(View.GONE);
            findViewById(R.id.transitionHours).setVisibility(View.GONE);
//            mMorningHour.setEnabled(false);
//            mNightHour.setEnabled(false);
//            mTransitionHours.setEnabled(false);
        }
        else {
            findViewById(R.id.wakeUpAtTextView).setVisibility(View.VISIBLE);
            findViewById(R.id.morningHour).setVisibility(View.VISIBLE);
            findViewById(R.id.goToSleepAtTextView).setVisibility(View.VISIBLE);
            findViewById(R.id.nightHour).setVisibility(View.VISIBLE);
            findViewById(R.id.transitionTimeHoursTextView).setVisibility(View.VISIBLE);
            findViewById(R.id.transitionHours).setVisibility(View.VISIBLE);
//            mMorningHour.setEnabled(true);
//            mNightHour.setEnabled(true);
//            mTransitionHours.setEnabled(true);
        }
    }

    private void setListeners() {
        mIsEnabled.setOnCheckedChangeListener(enabledCheckBoxCheckedChangeListener);
        mIsAutomatic.setOnCheckedChangeListener(isAutomaticCheckBoCheckedChangexListener);
        mSaveButton.setOnClickListener(saveButtonListener);
    }

    private CheckBox.OnCheckedChangeListener isAutomaticCheckBoCheckedChangexListener = new CheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            setAutomatic(isChecked);
            if (isChecked) {
                mCurrentToast.cancel();
                mCurrentToast = Toast.makeText(TimeOfDayActivity.this, "Mode set to automatic", Toast.LENGTH_LONG);
                mCurrentToast.show();
            }
            else {
                mCurrentToast.cancel();
                mCurrentToast = Toast.makeText(TimeOfDayActivity.this, "Mode set to manual", Toast.LENGTH_LONG);
                mCurrentToast.show();
            }
        }
    };

    private CheckBox.OnCheckedChangeListener enabledCheckBoxCheckedChangeListener = new CheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                startTimeOfDay();
                mCurrentToast.cancel();
                mCurrentToast = Toast.makeText(TimeOfDayActivity.this, "Time of day enabled", Toast.LENGTH_LONG);
                mCurrentToast.show();
            }
            else {
                stopTimeOfDay();
                mCurrentToast.cancel();
                mCurrentToast = Toast.makeText(TimeOfDayActivity.this, "Time of day disabled", Toast.LENGTH_LONG);
                mCurrentToast.show();
            }
        }
    };

    private View.OnClickListener saveButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean isEnabled = mIsEnabled.isChecked();
            boolean isAutomatic = mIsAutomatic.isChecked();
            int morningHour = Integer.parseInt(mMorningHour.getText().toString());
            int nightHour = Integer.parseInt(mNightHour.getText().toString());
            int transitionHours = Integer.parseInt(mTransitionHours.getText().toString());
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(ENABLED_PREF_KEY, isEnabled);
            editor.putBoolean(IS_AUTOMATIC_PREFKEY, isAutomatic);
            editor.putInt(MORNING_HOUR_PREFKEY, morningHour);
            editor.putInt(NIGHT_HOUR_PREF_KEY, nightHour);
            editor.putInt(TRANSITION_PREF_KEY, transitionHours);
            editor.apply();
            if (isEnabled) {
                startTimeOfDay();
            }
            else {
                stopTimeOfDay();
            }
            mCurrentToast.cancel();
            mCurrentToast = Toast.makeText(TimeOfDayActivity.this, "Settings saved", Toast.LENGTH_LONG);
            mCurrentToast.show();
        }
    };

    private void startTimeOfDay() {
        AlarmManager am = (AlarmManager)LedControl.getContext().getSystemService(ALARM_SERVICE);
        if (mIsAutomatic.isChecked()) {
            Intent startIntent = new Intent(getString(R.string.automatictimeofday_action_start),
                    Uri.EMPTY, LedControl.getContext(), AutomaticTimeOfDay.class);
            this.startService(startIntent);
            Intent updateLightingIntent = new Intent(getString(R.string.automatictimeofday_action_update_lighting),
                    Uri.EMPTY, LedControl.getContext(), AutomaticTimeOfDay.class);
            Intent updateDataIntent = new Intent(getString(R.string.automatictimeofday_action_update_data),
                    Uri.EMPTY, LedControl.getContext(), AutomaticTimeOfDay.class);
            PendingIntent pendingUpdateLightingIntent = PendingIntent.getService(LedControl.getContext(),
                    1, updateLightingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent pendingUpdateDataIntent = PendingIntent.getService(LedControl.getContext(),
                    2, updateDataIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            am.setRepeating(AlarmManager.ELAPSED_REALTIME, 5000L, 60000L, pendingUpdateLightingIntent);
            am.setRepeating(AlarmManager.ELAPSED_REALTIME, 5000L, 17280000L, pendingUpdateDataIntent);
        }
        else {
            Intent startIntent = new Intent(getString(R.string.simpletimeofday_action_start),
                    Uri.EMPTY, LedControl.getContext(), SimpleTimeOfDay.class);
            this.startService(startIntent);
            Intent updateIntent = new Intent(getString(R.string.simpletimeofday_action_update),
                    Uri.EMPTY, LedControl.getContext(), SimpleTimeOfDay.class);
            PendingIntent pendingUpdateIntent = PendingIntent.getService(LedControl.getContext(),
                    1, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            am.setRepeating(AlarmManager.ELAPSED_REALTIME, 5000L, 60000L, pendingUpdateIntent);
        }
    }

    private void stopTimeOfDay() {
        AlarmManager am = (AlarmManager)LedControl.getContext().getSystemService(ALARM_SERVICE);
        boolean isAutomatic = mIsAutomatic.isChecked();
        if (isAutomatic) {
            Intent updateLightingIntent = new Intent(getString(R.string.automatictimeofday_action_update_lighting),
                    Uri.EMPTY, LedControl.getContext(), AutomaticTimeOfDay.class);
            Intent updateDataIntent = new Intent(getString(R.string.automatictimeofday_action_update_data),
                    Uri.EMPTY, LedControl.getContext(), AutomaticTimeOfDay.class);
            PendingIntent pendingUpdateLightingIntent = PendingIntent.getService(LedControl.getContext(),
                    1, updateLightingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent pendingUpdateDataIntent = PendingIntent.getService(LedControl.getContext(),
                    2, updateDataIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            am.cancel(pendingUpdateLightingIntent);
            am.cancel(pendingUpdateDataIntent);
            Intent stopIntent = new Intent(getString(R.string.automatictimeofday_action_stop),
                    Uri.EMPTY, LedControl.getContext(), AutomaticTimeOfDay.class);
            this.startService(stopIntent);
        }
        else {
            Intent updateIntent = new Intent(getString(R.string.simpletimeofday_action_update),
                    Uri.EMPTY, LedControl.getContext(), SimpleTimeOfDay.class);
            PendingIntent pendingUpdateIntent = PendingIntent.getService(LedControl.getContext(),
                    1, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            am.cancel(pendingUpdateIntent);
            Intent stopIntent = new Intent(getString(R.string.simpletimeofday_action_stop), Uri.EMPTY,
                    LedControl.getContext(), SimpleTimeOfDay.class);
            LedControl.getContext().startService(stopIntent);
        }
    }
}
