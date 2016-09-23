package com.example.carrefour.bcm_notification_system;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private boolean isFirstRun;
    private String userMode = ""; // refers to the user mode of this system

    public static final String MY_PREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



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
        }
    }

    public void settingsOnClick(View v){
        Intent i = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void triggerNonDrillOnClick(View v){
        Intent i = new Intent(MainActivity.this, TriggerActivity.class);
        startActivity(i);
    }

    public void triggerDrillOnClick(View v){
        sendNonDrillMessage();
        Intent i;
        if(sharedPreferences.getString("userMode", "Team Lead").equals("BCM")){
            i = new Intent(MainActivity.this, BCMStatusActivity.class);
            startActivity(i);
        }
        if(sharedPreferences.getString("userMode", "Team Lead").equals("Team Lead")){
            i = new Intent(MainActivity.this, TeamLeadStatusActivity.class);
            startActivity(i);
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
    public void sendNonDrillMessage(){

        String messageString = sharedPreferences.getString("drillMsg", "Default");
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
                    Toast.makeText(this, messageParts.get(i), Toast.LENGTH_LONG).show();
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
