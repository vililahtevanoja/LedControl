package fi.aalto.mobilesystems.ledcontrol.models;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;
import com.philips.lighting.model.PHLightState;

import java.util.Calendar;

import fi.aalto.mobilesystems.ledcontrol.services.ChangeLightService;

/**
 * Created by ZSY on 4/21/16.
 */
public class Alarm {

    private static String TAG = "Alarm";

    public void SetAlarm(Context context, Calendar calendar, String lightIdentifier, int color)
    {
        if(calendar == null){
            // Set the alarm to start at approximately 2:00 p.m.
            return;
           /* calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 20);
            calendar.set(Calendar.MINUTE, 50);*/
        }

        if(lightIdentifier == null){
            return;
        }

        AlarmManager alarmMgr =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ChangeLightService.class);
        intent.putExtra("lightIdentifier", lightIdentifier);
        //intent.putExtra("color", color);
        String strColor = Integer.toString(color);
        intent.putExtra("newColor", strColor);
        intent.setAction("Alarm");
        PendingIntent alarmIntent = PendingIntent.getService(context, 0, intent, 0);


        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

        Log.d(TAG, "set alarm calendar:" + calendar.toString());
        Log.d(TAG,"set alarm light:" + lightIdentifier + " color:" + color + "color String:" + strColor);
    }

    public void setRestoreAlarm(Context context, int second, String lightIdentifier, PHLightState lightState)
    {
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;

        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ChangeLightService.class);
        intent.putExtra("lightIdentifier", lightIdentifier);

        Gson gson = new Gson();
        String json = gson.toJson(lightState);
        intent.putExtra("lightState", json);

        intent.setAction("RestoreAlarm");
        alarmIntent = PendingIntent.getService(context, 0, intent, 0);

        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() +
                        second, alarmIntent);
        Log.d(TAG, "set RestoreAlarm delay:" + second);
        Log.d(TAG, "set RestoreAlarm light:" + lightIdentifier + " lightState:" + json);
    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, ChangeLightService.class);
        PendingIntent sender = PendingIntent.getService(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);

        Log.d(TAG, "Alarm canceled" );
    }
}
