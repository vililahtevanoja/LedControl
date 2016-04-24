package fi.aalto.mobilesystems.ledcontrol.models;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
        intent.putExtra("color", color);
        intent.setAction("Alarm");
        PendingIntent alarmIntent = PendingIntent.getService(context, 0, intent, 0);


        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

        Log.d(TAG, "set alarm calendar:" + calendar.toString());
        Log.d(TAG,"set alarm light:" + lightIdentifier + " color:" + color);
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
