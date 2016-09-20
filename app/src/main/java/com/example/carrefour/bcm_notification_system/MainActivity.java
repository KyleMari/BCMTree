package com.example.carrefour.bcm_notification_system;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    private boolean isFirstRun = true;
    private Configuration config;
    private String userMode = ""; // refers to the user mode of this system

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        config = new Configuration();

        //checks if the user mode has already been switched from team lead to bcm
        Intent i = getIntent();
        Bundle extras = i.getExtras();

        try {
            if (extras != null) {
                isFirstRun = false;
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
        }


    }

    public void settingsOnClick(View v){
        Intent i = new Intent(MainActivity.this, SettingsActivity.class);
        if(!isFirstRun) {
            i.putExtras(config.getAttribBundle());
        }
        startActivity(i);
    }


}
