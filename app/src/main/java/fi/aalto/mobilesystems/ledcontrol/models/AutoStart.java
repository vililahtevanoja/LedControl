package fi.aalto.mobilesystems.ledcontrol.models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ZSY on 4/21/16.
 */
public class AutoStart extends BroadcastReceiver
{
    private static String TAG = "AutoStart";
    Alarm alarm = new Alarm();
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            alarm.SetAlarm(context);
            Log.d(TAG,"Auto start");
        }
    }
}
