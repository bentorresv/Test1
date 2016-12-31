package com.matias.core;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.os.SystemClock.elapsedRealtime;

/*
 * Created by BenTorres on 01/11/2015.
 */
public class Check extends Activity {

    final int target_adjustment = 5;

    private int target_hours = 9;
    private int target_minutes = target_hours * 60;
    private int target_seconds = target_hours * 3600;
    private int target_ms = target_seconds * 1000;

    private boolean start = false;
    private boolean settings = true;

    private TextView tv_finish_hour;
    private Chronometer cr_work_time;
    private TextView tv_elapsed_time;
    private TextView tv_remaining_time;
    private TextView tv_target;
    private LinearLayout layout_target_hours;

    private ProgressBar pb_time_elapsed;
    private long time_elapsed_ms = 0;
    private long time_remaining_ms = target_ms;
    private long time_at_stop = 0;
    private long pb_time_progress = 0;
    private long finish_time_min = 0;
    private long stop_time = 0;

    private List<CheckLog> checkLogList;
    private CheckLog_BaseAdapter checkLog_baseAdapter;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        LinearLayout layout_action_bar = (LinearLayout) findViewById(R.id.layout_action_bar);
        layout_action_bar.setBackgroundColor(My_Colors.PRIMARY_COLOR);
        layout_target_hours = (LinearLayout) findViewById(R.id.layout_target_hours);
        TextView tv_bottom_line = (TextView) findViewById(R.id.tv_bottom_line);
        tv_bottom_line.setBackgroundColor(My_Colors.SECONDARY_COLOR);

        checkLogList = new ArrayList<>();
        ListView lv_checklog = (ListView) findViewById(R.id.lv_checklog);
        checkLog_baseAdapter = new CheckLog_BaseAdapter(checkLogList, getLayoutInflater());
        lv_checklog.setAdapter(checkLog_baseAdapter);

        tv_finish_hour = (TextView) findViewById(R.id.tv_hour);
        cr_work_time = (Chronometer) findViewById(R.id.cr_work_time);

        tv_elapsed_time = (TextView) findViewById(R.id.tv_elapsed_time);
        tv_remaining_time = (TextView) findViewById(R.id.tv_remaining_time);
        tv_target = (TextView) findViewById(R.id.tv_target);

        pb_time_elapsed = (ProgressBar) findViewById(R.id.pb_time_elasped);

        final ImageButton bt_play = (ImageButton) findViewById(R.id.bt_play);
        final ImageButton bt_more_target = (ImageButton) findViewById(R.id.iv_more_target);
        final ImageButton bt_less_target = (ImageButton) findViewById(R.id.iv_less_target);
        final ImageButton bt_settings = (ImageButton) findViewById(R.id.iv_settings);

        tv_elapsed_time.setText(R.string.time_default);
        tv_remaining_time.setText(R.string.time_default);

        tv_target.setText(String.format(Locale.getDefault(), "Target: %02d:%02d", target_hours, 0));
        tv_target.setTextColor(Color.WHITE);
        tv_finish_hour.setText(R.string.app_name);
        tv_finish_hour.setTextColor(Color.WHITE);
        pb_time_elapsed.setMax(1000);
        pb_time_elapsed.setProgress(0);

        //Object used to initialize stop_time
        calendar = Calendar.getInstance();
        CheckLog checkLog = new CheckLog(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        stop_time = checkLog.get_time_in_minutes();

        bt_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(start){
                    start = false;
                    cr_work_time.stop();
                    time_at_stop = (elapsedRealtime() - cr_work_time.getBase());
                    bt_play.setImageResource(R.drawable.ic_action_play);
                    tv_finish_hour.setText(R.string.app_name);

                    calendar = Calendar.getInstance();
                    CheckLog checkLog = new CheckLog(calendar.get(Calendar.HOUR_OF_DAY),
                                                     calendar.get(Calendar.MINUTE),
                                                     calendar.get(Calendar.SECOND),
                                                     calendar.get(Calendar.AM_PM),
                                                     false);

                    stop_time = checkLog.get_time_in_minutes();

                    checkLogList.add(checkLog);
                    checkLog_baseAdapter.notifyDataSetChanged();
                }
                else{
                    start = true;
                    cr_work_time.setBase(elapsedRealtime() - time_at_stop);
                    cr_work_time.start();
                    bt_play.setImageResource(R.drawable.ic_action_pause);

                    calendar = Calendar.getInstance();
                    CheckLog checkLog = new CheckLog(calendar.get(Calendar.HOUR_OF_DAY),
                                                     calendar.get(Calendar.MINUTE),
                                                     calendar.get(Calendar.SECOND),
                                                     calendar.get(Calendar.AM_PM),
                                                     true);
                    if(finish_time_min == 0) {
                        finish_time_min = target_minutes + checkLog.get_time_in_minutes();
                    }
                    else{
                        finish_time_min += (checkLog.get_time_in_minutes() - stop_time);
                    }

                    update_finish_time_min();
                    checkLogList.add(checkLog);
                    checkLog_baseAdapter.notifyDataSetChanged();
                }
            }
        });

        cr_work_time.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {

                time_elapsed_ms = elapsedRealtime() - chronometer.getBase();
                time_remaining_ms = target_ms - time_elapsed_ms;
                if(time_remaining_ms < 0) time_remaining_ms = 0;

                pb_time_progress = time_elapsed_ms / target_seconds;

                pb_time_elapsed.setProgress((int)pb_time_progress);

                //tv_finish_hour.setText("Time elapsed: " + time_elapsed_ms/1000 + " s\nProgress: " + pb_time_progress);

                String hours_minutes_seconds = String.format(Locale.getDefault(), "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(time_elapsed_ms),
                        TimeUnit.MILLISECONDS.toMinutes(time_elapsed_ms) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time_elapsed_ms)),
                        TimeUnit.MILLISECONDS.toSeconds(time_elapsed_ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time_elapsed_ms)));

                tv_elapsed_time.setText(hours_minutes_seconds);

                hours_minutes_seconds = String.format(Locale.getDefault(), "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(time_remaining_ms),
                    TimeUnit.MILLISECONDS.toMinutes(time_remaining_ms) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time_remaining_ms)),
                    TimeUnit.MILLISECONDS.toSeconds(time_remaining_ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time_remaining_ms)));

                tv_remaining_time.setText(hours_minutes_seconds);
            }
        });

        bt_more_target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(target_hours < 12){
                    if(start){
                        finish_time_min += target_adjustment;
                        Check.this.update_finish_time_min();
                    }
                    target_minutes += target_adjustment;
                    Check.this.update_target_time();
                    tv_target.setText(String.format(Locale.getDefault(),"Target: %02d:%02d", target_hours, target_minutes - TimeUnit.HOURS.toMinutes(target_hours)));
                }
            }
        });

        bt_less_target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(target_minutes > 15){
                    if(start){
                        finish_time_min -= target_adjustment;
                        Check.this.update_finish_time_min();
                    }
                    target_minutes -= target_adjustment;
                    Check.this.update_target_time();
                    tv_target.setText(String.format(Locale.getDefault(),"Target: %02d:%02d", target_hours, target_minutes - TimeUnit.HOURS.toMinutes(target_hours)));
                }
            }
        });

        bt_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(settings){
                    settings = false;
                    layout_target_hours.setVisibility(View.GONE);
                }
                else{
                    settings = true;
                    layout_target_hours.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_check, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return (keyCode != KeyEvent.KEYCODE_BACK && super.onKeyDown(keyCode, event));
    }


    private String identify_am_pm(int total_hours){
        String am_pm_string;
        if (total_hours > 23) am_pm_string = "am";
        else if (total_hours > 11) am_pm_string = "pm";
        else am_pm_string = "am";
        return am_pm_string;
    }

    private void update_target_time(){

        target_hours = (int) TimeUnit.MINUTES.toHours(target_minutes);
        target_seconds = target_minutes * 60;
        target_ms = target_seconds * 1000;
    }

    private void update_finish_time_min(){

        String finish_time_min_string = String.format(Locale.getDefault(), "Finish at: %02d:%02d %s",
                convert_hours_to_12format((int) TimeUnit.MINUTES.toHours(finish_time_min)),
                finish_time_min - TimeUnit.HOURS.toMinutes(TimeUnit.MINUTES.toHours(finish_time_min)),
                identify_am_pm((int) TimeUnit.MINUTES.toHours(finish_time_min)));

        tv_finish_hour.setText(finish_time_min_string);
    }

    private int convert_hours_to_12format(int hours12format){
        if (hours12format > 24) hours12format -= 24;
        else if (hours12format > 12) hours12format -= 12;
        return hours12format;
    }
}
