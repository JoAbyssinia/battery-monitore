package com.s.c.s.abyssinia.batterymonitorv1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ${yohannes} on 25/11/2017.
 */
public class Boot_Resiver extends BroadcastReceiver {
    private static final String BOOT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(BOOT)){
            Intent monitortIntent = new Intent(context,ServiceMain.class);
            monitortIntent.putExtra(ServiceMain.BOOT,true);
            context.startService(monitortIntent);
        }
    }
}
