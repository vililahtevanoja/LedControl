package fi.aalto.mobilesystems.ledcontrol.models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import fi.aalto.mobilesystems.ledcontrol.LedControl;

/**
 * Created by ZSY on 4/21/16.
 */
public class AutoStart extends BroadcastReceiver
{
    private static String TAG = "AutoStart";
    private SharedPreferences msharedPrefs;
    Alarm alarm = new Alarm();

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            msharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            Gson gson = new Gson();
            String json = msharedPrefs.getString("HandleBroadcastScene","");
            if(json.equals("")){
                Log.d(TAG,"getJson: "+ json);
            } else {
                HandleBroadcastScene obj = gson.fromJson(json, HandleBroadcastScene.class);
                Log.d(TAG, "getJson: " + json);
            }
            alarm.SetAlarm(context, null, null, 0);
            Log.d(TAG,"Auto start");
        }
    }
}
