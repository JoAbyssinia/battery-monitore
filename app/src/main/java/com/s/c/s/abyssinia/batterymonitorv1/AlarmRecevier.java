package com.s.c.s.abyssinia.batterymonitorv1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ${yohannes} on 25/11/2017.
 */
public class AlarmRecevier extends BroadcastReceiver {
    private static final int REQUEST_CODE = 1;
    private static final long ALAEM_INTERVAL = 20000;

    public static void startAlarm (final Context context){
        final AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,0,ALAEM_INTERVAL,getAlarmIntent(context));
    }

    private static PendingIntent getAlarmIntent(Context context) {
        return PendingIntent.getBroadcast(context,REQUEST_CODE,new Intent(context,AlarmRecevier.class),PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }if (context==null){
            return;
        }if (intent.getAction()==null){
            Intent monitorIntent = new Intent(context,ServiceMain.class);
            monitorIntent.putExtra(ServiceMain.BATTERY_CURRENT,true);
            context.startService(monitorIntent);
        }
    }
}
