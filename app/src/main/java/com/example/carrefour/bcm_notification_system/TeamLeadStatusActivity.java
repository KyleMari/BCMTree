package com.example.carrefour.bcm_notification_system;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class TeamLeadStatusActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_lead_status);
        setTitle(R.string.status_title_bar);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                returnToMainScreen();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        returnToMainScreen();
    }

    private void returnToMainScreen(){
        //passes values to intent and starts the next class
        Intent i = new Intent(TeamLeadStatusActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void sendReportOnClick(View v){
        Toast.makeText(this, "Report successfully sent to BCM Manager", Toast.LENGTH_LONG).show();
        Intent i = new Intent(TeamLeadStatusActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

}
