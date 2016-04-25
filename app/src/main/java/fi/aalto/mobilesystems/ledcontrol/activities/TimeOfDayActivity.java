package fi.aalto.mobilesystems.ledcontrol.activities;

import android.content.SharedPreferences;
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
        mIsAutomatic.setSelected(isAutomatic);
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
                mCurrentToast.cancel();
                mCurrentToast = Toast.makeText(TimeOfDayActivity.this, "Time of day enabled", Toast.LENGTH_LONG);
                mCurrentToast.show();
            }
            else {
                mCurrentToast.cancel();
                mCurrentToast = Toast.makeText(TimeOfDayActivity.this, "Time of day disabled", Toast.LENGTH_LONG);
                mCurrentToast.show();
            }
        }
    };

    private View.OnClickListener saveButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean isEnabled = mIsEnabled.isSelected();
            boolean isAutomatic = mIsAutomatic.isSelected();
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
            mCurrentToast.cancel();
            mCurrentToast = Toast.makeText(TimeOfDayActivity.this, "Settings saved", Toast.LENGTH_LONG);
            mCurrentToast.show();
        }
    };
}
