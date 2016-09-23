package com.example.carrefour.bcm_notification_system;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class TriggerActivity extends ActionBarActivity {

    public static final String MY_PREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger);
        setTitle(R.string.trigger_title_bar);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        editor = sharedPreferences.edit();
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
        sendDrillMessage();
        if(sharedPreferences.getString("userMode", "Team Lead").equals("BCM")) {
            Intent i = new Intent(TriggerActivity.this, BCMStatusActivity.class);
            startActivity(i);
            finish();
        }
        if(sharedPreferences.getString("userMode", "Team Lead").equals("Team Lead")){
            Intent i = new Intent(TriggerActivity.this, TeamLeadStatusActivity.class);
            startActivity(i);
            finish();
        }
    }

    //this method retrieves the phone numbers of all the members of a chosen group
    public ArrayList<String> getPhoneNumFromGroup(String groupID){
        ArrayList<String> phoneNums = new ArrayList<String>();
        Uri groupURI = ContactsContract.Data.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID
        };
        Cursor c = getContentResolver().query(
                groupURI, projection,
                ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + "=" + groupID, null, null);
        while(c.moveToNext()){
            String id = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID));
            Cursor pCur = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[] { id }, null);
            while(pCur.moveToNext()){
                //ContactItem data = new ContactItem();
                //data.phDisplayName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                phoneNums.add(pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                //contactList.add(data);
            }
            pCur.close();
        }
        return phoneNums;
    }

    //this method begins the execution of sending non-drill message to members of the selected group
    public void sendDrillMessage(){

        EditText messageBody = (EditText) findViewById(R.id.non_drill_msg_body);
        String messageString = messageBody.getText().toString();
        ArrayList<String> targetPhoneNums = new ArrayList<String>();
        SmsManager smsManager = SmsManager.getDefault();

        //divides the entire message string into several parts (to avoid exceeding the maximum length)
        ArrayList<String> messageParts = new ArrayList<String>();
        int index = 0;
        while(index < messageString.length()){
            messageParts.add(messageString.substring(index, Math.min(index + 120, messageString.length())));
            index += 120;
        }

        //obtains the phone numbers of the members and assign the list to the variable
        targetPhoneNums = getPhoneNumFromGroup(sharedPreferences.getString("contactGroupID", ""));

        //sends the message to each of the selected recipients
        boolean isSuccessful = false;
        for(String number : targetPhoneNums){
            try{
                for(int i = 0; i < messageParts.size(); i++) {
                    smsManager.sendTextMessage(number, null, messageParts.get(i), null, null);
                }
                isSuccessful = true;
            }catch(Exception e){
                isSuccessful = false;
            }
        }
        if(isSuccessful){
            Toast.makeText(this, "Message successfully sent", Toast.LENGTH_LONG).show();
        }else
            Toast.makeText(this, "Message sending failed to some or all recipients", Toast.LENGTH_LONG).show();
    }
}
