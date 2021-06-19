package com.s.c.s.abyssinia.batterymonitorv1;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.MessageFormat;

public class MainActivity extends AppCompatActivity {
    private Handler handler;
    private Runnable runnable;
    private ImageView iconBtn;
    private TextView persent;
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AlarmRecevier.startAlarm(MainActivity.this.getApplicationContext());

        FloatingActionButton fabS = (FloatingActionButton) findViewById(R.id.fabStart);
        assert fabS!=null;
        fabS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this,ServiceMain.class));
            }
        });
        FloatingActionButton fabP = (FloatingActionButton) findViewById(R.id.fabstop);
        assert fabP!=null;
        fabP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopServiceTemp();
            }
        });
    }
    private void stopServiceTemp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Disable");
        builder.setIcon(R.drawable.icon);
        builder.setMessage("The Disable process is not permanent.");
        builder.setCancelable(true);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopService( new Intent(MainActivity.this,ServiceMain.class));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Thanks ........", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onStart() {

        persent = (TextView)findViewById(R.id.persent);
        iconBtn = (ImageView)findViewById(R.id.batteryicon);
        info = (TextView)findViewById(R.id.info);
       // startBtn = (Button)findViewById(R.id.startbtn);
        //stopBtn = (Button)findViewById(R.id.stopBtn);
        runnable = new Runnable() {
            @Override
            public void run() {
                boolean when = batteryPluged();
                if (when) {
                    int level = (int) batteryLevel();
                    String lev = String.valueOf(level);
                    persent.setText(MessageFormat.format("{0}%", lev));

                    if (level >= 95) {
                        iconBtn.setImageResource(R.drawable.ic_battery_charging_full_black_24dp);
                    }
                    if (level >= 90 && level < 95) {
                        iconBtn.setImageResource(R.drawable.ic_battery_charging_90_black_24dp);
                    }
                    if (level >= 80 && level < 90) {
                        iconBtn.setImageResource(R.drawable.ic_battery_charging_80_black_24dp);
                    }
                    if (level >= 60 && level < 80) {
                        iconBtn.setImageResource(R.drawable.ic_battery_charging_60_black_24dp);
                    }
                    if (level >= 50 && level < 60) {
                        iconBtn.setImageResource(R.drawable.ic_battery_charging_50_black_24dp);
                    }
                    if (level >= 30 && level < 50) {
                        iconBtn.setImageResource(R.drawable.ic_battery_charging_30_black_24dp);
                    }
                    if (level >= 15 && level < 30) {
                        iconBtn.setImageResource(R.drawable.ic_battery_charging_20_black_24dp);
                    }
                    if (level < 15) {
                        iconBtn.setImageResource(R.drawable.ic_battery_charging_20_under);
                    }
                } else {
                    int level = (int) batteryLevel();
                    String lev = String.valueOf(level);
                    persent.setText(MessageFormat.format("{0}%", lev));
                    if (level >= 95) {
                        iconBtn.setImageResource(R.drawable.ic_battery_full_black_24dp);
                    }
                    if (level >= 90 && level < 95) {
                        iconBtn.setImageResource(R.drawable.ic_battery_90_black_24dp);
                    }
                    if (level >= 80 && level < 90) {
                        iconBtn.setImageResource(R.drawable.ic_battery_80_black_24dp);
                    }
                    if (level >= 60 && level < 80) {
                        iconBtn.setImageResource(R.drawable.ic_battery_60_black_24dp);
                    }
                    if (level >= 50 && level < 60) {
                        iconBtn.setImageResource(R.drawable.ic_battery_50_black_24dp);
                    }
                    if (level >= 30 && level < 50) {
                        iconBtn.setImageResource(R.drawable.ic_battery_30dp);
                    }
                    if (level >= 15 && level < 30) {
                        iconBtn.setImageResource(R.drawable.ic_battery_low);
                    }
                    if (level < 15) {
                        iconBtn.setImageResource(R.drawable.ic_battery_under_low);

                    }

                }
                batteryInfo();
                handler.postDelayed(runnable, 100);
            }
        };
        handler = new Handler();
        handler.postDelayed(runnable, 0);
        
        super.onStart();
    }

    private float batteryLevel() {
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        assert batteryIntent != null;
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        if (level == -1 || scale == -1) {
            return 50.0f;
        }
        return ((float) level / (float) scale) * 100f;
    }

    private boolean batteryPluged() {
        Intent pluged = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        assert pluged != null;
        int charger = pluged.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean statuse;
        switch (charger) {
            case BatteryManager.BATTERY_PLUGGED_AC:
            case BatteryManager.BATTERY_PLUGGED_USB:
                statuse = true;
                break;
            default:
                statuse = false;
                break;
        }
        return statuse;
    }

    private void batteryInfo() {
        Intent btinfor = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        assert btinfor != null;
        int healthy = btinfor.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
        String collectinfo;
        switch (healthy){
            case BatteryManager.BATTERY_HEALTH_COLD:
                collectinfo =("Healthy : COLD");
                break;
            case BatteryManager.BATTERY_HEALTH_DEAD:
                collectinfo =("Healthy : DEAD");
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                collectinfo =("Healthy : GOOD");
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                collectinfo =("Healthy : OVER_VOLTAGE");
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                collectinfo =("Healthy : OVERHEAT");
                break;
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                collectinfo =("Healthy : UNKNOWN");
                break;
            default:
                collectinfo =("Healthy : UNSPECIFIED_FAILURE");
        }
        int plugged = btinfor.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        switch (plugged){
            case BatteryManager.BATTERY_PLUGGED_AC:
                collectinfo +=("\nPlugged : AC");
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                collectinfo +=("\nPlugged : USB");
                break;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                collectinfo +=("\nPlugged : WIRELESS");
                break;
            default:
                collectinfo +=("\nPlugged : UNPLUGGED");
        }
        int level = btinfor.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        if (level<=15){
            if (plugged == 1){
                collectinfo += "\nLevel :" + level;
            }else {
                collectinfo += "\nLevel :" + level+" (plug Charger)";
            }
        }else if (level == 100){
            if (plugged == 1){
                collectinfo += "\nLevel :"+level+" (unplugged Charger)";
            }
        }
        else {
            collectinfo += "\nLevel :" + level;
        }
        boolean present = btinfor.getBooleanExtra(BatteryManager.EXTRA_PRESENT,true);
        int scale = btinfor.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
        int status = btinfor.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
        switch (status){
            case BatteryManager.BATTERY_STATUS_CHARGING:
                collectinfo += ("\nStatus : CHARGING");
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                collectinfo += ("\nStatus : DISCHARGING");
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                collectinfo += ("\nStatus : FULL");
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                collectinfo += ("\nStatus : NOT CHARGING");
                break;
            default:
                collectinfo += ("\nStatus : UNKNOWN");
                break;
        }
        String techenology = btinfor.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
        int temp = btinfor.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
        int volt = btinfor.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);



        collectinfo += "\nPresent :" + present;
        collectinfo += "\nScale :" + scale;
        collectinfo += "\nTechnology :" + techenology;
        collectinfo += "\nTemperature :" + temp;
        collectinfo += "\nVoltage :" + volt;
        info.setText(collectinfo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         LayoutInflater inflet = getLayoutInflater();
        View view = inflet.inflate(R.layout.about, (ViewGroup) findViewById(R.id.aboutLayout));
        int id = item.getItemId();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setView(view);
        builder.create().show();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
