package com.example.carrefour.bcm_notification_system;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class BCMEmailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email);
        setTitle(R.string.email_title_bar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }

    public void sendEmailOnClick(View v){
        Toast.makeText(this, "Report in Excel file successfully sent", Toast.LENGTH_LONG).show();
        Intent i = new Intent(BCMEmailActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

}
