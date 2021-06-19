package com.s.c.s.abyssinia.batterymonitorv1;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by ${yohannes} on 25/11/2017.
 */
public class ServiceMain extends Service {
    public static final String BOOT = "boot complete";
    public static final String BATTERY_CURRENT = "battery update";
    private String levinfo;
    public void onCreate() {

        Toast.makeText(this, "...Start...", Toast.LENGTH_SHORT).show();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent!=null && intent.hasExtra(BOOT)){
            AlarmRecevier.startAlarm(this.getApplicationContext());
            notifay("Boot Complete");
        } if (intent!=null&& intent.hasExtra(BATTERY_CURRENT)){
            level();
            //AlarmRecevier.startAlarm(ServiceMain.this.getApplicationContext());
        }
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(ServiceMain.this, "...Disable...", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
    private void level(){
        IntentFilter iFlert = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent status = registerReceiver(null,iFlert);
        assert status != null;
        int stat = status.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
        int level = status.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
        switch (stat){
            case BatteryManager.BATTERY_STATUS_CHARGING:
                if (level == 99||level == 100){
                    MediaPlayer mp = MediaPlayer.create(this,R.raw.fulls);
                    mp.start();
                    levinfo="(Unplug Charger)";
                    notifay(level + "% "+levinfo);
                }else if (level <98){
                    levinfo = "(Charging)";
                    notifay(level+"% "+levinfo);
                }
                break;
            default:
                if(level <15 ){
                    levinfo = "(Plug Charger)";
                    notifay(level + "% "+ levinfo);
                }else {
                    levinfo = "(Not Charging)";
                    notifay(level + "% " + levinfo);
                }
        }

    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void notifay(String con){
        NotificationCompat.Builder Nbuilder = new NotificationCompat.Builder(ServiceMain.this);
        Nbuilder.setContentTitle("Battery Monitor");
        Nbuilder.setContentText(con);
        Nbuilder.setContentInfo("Monitoring..");
        Nbuilder.setSmallIcon(R.drawable.icon);
        Nbuilder.setAutoCancel(false);
        Nbuilder.setColor(Color.parseColor("#112233"));
        Nbuilder.setDefaults(Notification.DEFAULT_LIGHTS);

        Intent resultIntent = new Intent(this,MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent ResultpendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        Nbuilder.setContentIntent(ResultpendingIntent);
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationID = 100;
        mNotificationManager.notify(notificationID, Nbuilder.build());
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
