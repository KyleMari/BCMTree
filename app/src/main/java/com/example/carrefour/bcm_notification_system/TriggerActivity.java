package com.example.carrefour.bcm_notification_system;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;

public class TriggerActivity extends ActionBarActivity {

    private Configuration config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger);
        setTitle(R.string.trigger_title_bar);
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
        Intent i = new Intent(TriggerActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void sendMessageOnClick(View v){
        Intent i = new Intent(TriggerActivity.this, BCMStatusActivity.class);
        startActivity(i);
        finish();
    }
}
