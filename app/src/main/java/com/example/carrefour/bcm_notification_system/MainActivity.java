package com.example.carrefour.bcm_notification_system;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    private boolean isFirstRun;
    private Configuration config;
    private String userMode = ""; // refers to the user mode of this system

    public static final String MY_PREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        config = new Configuration();
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);

        //sets the title bar label
        userMode = sharedPreferences.getString("userMode", "Team Lead");
        if(userMode.equals("Team Lead"))
            setTitle(R.string.home_title_bar_team_lead_mode);
        if(userMode.equals("BCM"))
            setTitle(R.string.home_title_bar_bcm_mode);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(sharedPreferences.getBoolean("isFirstRun", true)){

            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            editor.putBoolean("isFirstRun", false);
            editor.commit();
        }


        //checks if the user mode has already been switched from team lead to bcm
        /*
        Intent i = getIntent();
        Bundle extras = i.getExtras();

        try {
            if (extras != null) {
                config.setAttribFromBundle(extras);
                if (config.getRole().equals("BCM")) {
                    setTitle(R.string.home_title_bar_bcm_manager_mode);
                } else {
                    setTitle(R.string.home_title_bar_team_lead_mode);
                }
            } else {
                setTitle(R.string.home_title_bar_team_lead_mode);
            }
        }catch(NullPointerException ne){
            ne.printStackTrace();
            setTitle(R.string.home_title_bar_team_lead_mode);
            isFirstRun = true;
        }

        //directs user to settings activity if the app is opened for the first time
        if(isFirstRun){
            isFirstRun = false;
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }*/

    }

    public void settingsOnClick(View v){
        Intent i = new Intent(MainActivity.this, SettingsActivity.class);
/*        if(!isFirstRun) {
            i.putExtras(config.getAttribBundle());
        }*/
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void triggerNonDrillOnClick(View v){
        Intent i = new Intent(MainActivity.this, TriggerActivity.class);
        /*if(!isFirstRun) {
            i.putExtras(config.getAttribBundle());
            startActivity(i);
        }*/
        startActivity(i);
    }
}
