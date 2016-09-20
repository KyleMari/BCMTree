package com.example.carrefour.bcm_notification_system;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    private String userMode = "Team Lead"; // refers to the user mode of this system

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //checks if the user mode has already been switched from team lead to bcm manager
        Intent i = getIntent();
        if(i.hasExtra("User Mode")){
            String modeLabel = i.getStringExtra("User Mode");
            if(modeLabel.equals("BCM Manager")) {
                userMode = modeLabel;
                setTitle(R.string.home_title_bar_bcm_manager_mode);
            }else{
                setTitle(R.string.home_title_bar_team_lead_mode);
            }
        }else {
            setTitle(R.string.home_title_bar_team_lead_mode);
        }


    }


}
