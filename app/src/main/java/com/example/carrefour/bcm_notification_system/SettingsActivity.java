package com.example.carrefour.bcm_notification_system;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.*;
import android.provider.ContactsContract.CommonDataKinds.*;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBarActivity;
import android.app.ActionBar;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by kyle.mari.torralba on 9/20/2016.
 */
public class SettingsActivity extends ActionBarActivity {

    Spinner roleSpinner;
    Spinner contactGroupSpinner;
    View linearLayout[];
    EditText emailBcmNumField;
    EditText drillMsgField;
    TextView emailBcmNumLabel;
    NumberPicker timeLimitPicker;
    Configuration config;

    public static final String MY_PREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private String userMode = "";
    ArrayList<ContactItem> groupList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        setTitle(R.string.settings_btn_label);


        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        //checks for extras from previous intent
        /*config = new Configuration();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();*/



        //initializes the view variables
        roleSpinner = (Spinner) findViewById(R.id.role_spinner);
        contactGroupSpinner = (Spinner) findViewById(R.id.contact_group_spinner);
        emailBcmNumField = (EditText) findViewById(R.id.email_bcm_num_field);
        emailBcmNumLabel = (TextView) findViewById(R.id.email_bcm_num_label);
        drillMsgField = (EditText) findViewById(R.id.drill_message_field);
        linearLayout = new View[2];
        linearLayout[0] = findViewById(R.id.label_ll);
        linearLayout[1] = findViewById(R.id.view_ll);

        //defining values for time limit picker
        timeLimitPicker = (NumberPicker) findViewById(R.id.time_limit_picker);
        String[] nums = new String[60];
        for(int i=0; i<nums.length; i++)
            nums[i] = Integer.toString(i+1);

        timeLimitPicker.setMinValue(1);
        timeLimitPicker.setMaxValue(60);
        timeLimitPicker.setWrapSelectorWheel(false);
        timeLimitPicker.setDisplayedValues(nums);
        timeLimitPicker.setValue(15);


        final float bcmFieldDp = this.getResources().getDimension(R.dimen.bcm_number_field_width);
        //sets the role spinner to team lead by default
        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            TextView label;
            View viewHolder;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                editor.putString("userMode", roleSpinner.getSelectedItem().toString().trim());
                //clears the field
                if((!roleSpinner.getSelectedItem().toString().equals(sharedPreferences.getString("userMode", "Team Lead"))))
                    emailBcmNumField.setText("");

                if(roleSpinner.getSelectedItem().toString().equals("Team Lead")){


                    //hides time limit picker
                    timeLimitPicker.setVisibility(View.GONE);
                    label = (TextView) findViewById(R.id.time_limit_label);
                    label.setVisibility(View.GONE);
                    label = (TextView) findViewById(R.id.length_time_label);
                    label.setVisibility(View.GONE);


                    //changes the field to BCM Number
                    emailBcmNumLabel.setText(R.string.bcm_number_label);
                    emailBcmNumField.setInputType(InputType.TYPE_CLASS_NUMBER);

                }
                if(roleSpinner.getSelectedItem().toString().equals("BCM")){


                    //shows time limit picker
                    timeLimitPicker.setVisibility(View.VISIBLE);
                    label = (TextView) findViewById(R.id.time_limit_label);
                    label.setVisibility(View.VISIBLE);
                    label = (TextView) findViewById(R.id.length_time_label);
                    label.setVisibility(View.VISIBLE);


                    //changes the field to EMAIL
                    emailBcmNumLabel.setText(R.string.email_label);
                    emailBcmNumField.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //puts the values to their respective views
        try {
            //states the currently selected user mode and its respective fields
            if(sharedPreferences.getString("userMode", "Team Lead").equals("BCM")) {
                roleSpinner.setSelection(0);
                emailBcmNumField.setText(sharedPreferences.getString("email", ""));
                timeLimitPicker.setValue(sharedPreferences.getInt("timeLimit", 15));
            }
            if(sharedPreferences.getString("userMode", "Team Lead").equals("Team Lead")) {
                roleSpinner.setSelection(1);
                emailBcmNumField.setText(sharedPreferences.getString("bcmNum", ""));
            }

            if(sharedPreferences.getBoolean("isFirstRun", true)){
                insertContactToGroup("BCM"); // programmatically inserts contact to groups
                createGroups(); // programmatically creates contact groups
                editor.putBoolean("isFirstRun", false);
                editor.commit();
            }


            //stores existing contact group names to an array
            ArrayList<ContactItem> contactGroupsList = fetchGroups();
            String[] groupNames = new String[contactGroupsList.size()];
            for(int i = 0; i < contactGroupsList.size(); i++){
                groupNames[i] = contactGroupsList.get(i).name;
            }

            //prepares the contact group spinner items
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, groupNames);
            contactGroupSpinner.setAdapter(adapter); //  populates existing group names to the contact group spinner

            //bases on the shared preference to state the currently selected contact group
            if(sharedPreferences.getString("contactGroup", "").equals("")){
                contactGroupSpinner.setSelection(0);
            }else{
                for(int i = 0; i < contactGroupSpinner.getCount(); i++){
                    if(sharedPreferences.getString("contactGroup", "").equals(contactGroupSpinner.getItemAtPosition(i))){
                        contactGroupSpinner.setSelection(i);
                    }
                }
            }


            //states the drill message
            drillMsgField.setText(sharedPreferences.getString("drillMsg", ""));
        }catch(NullPointerException ne){
            ne.printStackTrace();
        }

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
        //gets the value of the attributes
        //config.setContactGroup(contactGroupSpinner.getSelectedItem().toString().trim());
        //config.setRole(roleSpinner.getSelectedItem().toString().trim());

        //passes values to intent and starts the next class
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        //i.putExtras(config.getAttribBundle());
        startActivity(intent);

        editor.putString("userMode", roleSpinner.getSelectedItem().toString().trim());
        editor.putString("contactGroup", contactGroupSpinner.getSelectedItem().toString().trim());
        for(int i = 0; i < groupList.size(); i++){
            if(groupList.get(i).name.equals(contactGroupSpinner.getSelectedItem().toString().trim())){
                Toast.makeText(this, groupList.get(i).id + " - " + groupList.get(i).name, Toast.LENGTH_LONG).show();
                editor.putString("contactGroupID", groupList.get(i).id);
            }
        }

        if(roleSpinner.getSelectedItem().toString().trim().equals("BCM")){
            //config.setEmail(emailBcmNumField.getText().toString().trim());
            //config.setTimeLimitLength(timeLimitPicker.getValue());
            editor.putString("email", emailBcmNumField.getText().toString().trim());
            editor.putInt("timeLimit", timeLimitPicker.getValue());
        }
        if(roleSpinner.getSelectedItem().toString().trim().equals("Team Lead")) {
            //config.setBcmNumber(emailBcmNumField.getText().toString().trim());
            editor.putString("bcmNum", emailBcmNumField.getText().toString().trim());
        }

        //config.setDrillMsg(drillMsgField.getText().toString().trim());
        editor.putString("drillMsg", drillMsgField.getText().toString().trim());
        editor.commit();

        finish();
    }

    private ArrayList<ContactItem> fetchGroups(){
        groupList = new ArrayList<ContactItem>();
        String[] projection = new String[]{ContactsContract.Groups._ID, ContactsContract.Groups.TITLE};
        Cursor cursor = getContentResolver().query(ContactsContract.Groups.CONTENT_URI, projection,
                null, null, null);
        ArrayList<String> groupTitle = new ArrayList<String>();
        while(cursor.moveToNext()){
            ContactItem item = new ContactItem();
            item.id = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID));

            String groupName = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));

            if(groupName.contains("Group: "))
                groupName = groupName.substring(groupName.indexOf("Group:")+"Group:".length()).trim();
            if(groupName.contains("Favorite_"))
                groupName = "Favorite";

            if(groupName.contains("Starred in Android") || groupName.contains("My Contacts"))
                continue;

            /*
            if(groupTitle.contains(groupName)){
                for(ContactItem group : groupList){
                    if(group.name.equals(groupName)){
                        group.id += "," + item.id;
                        break;
                    }
                }
            }else{
                groupTitle.add(groupName);
                item.name = groupName;
                groupList.add(item);
            }*/
            groupTitle.add(groupName);
            item.name = groupName;
            groupList.add(item);

        }

        cursor.close();
        Collections.sort(groupList, new Comparator<ContactItem>(){
            public int compare(ContactItem item1, ContactItem item2){
                return item2.name.compareTo(item1.name) < 0? 0:-1;
            }
        });
        return groupList;
    }

    private ArrayList<String> retrieveContactNames(){
        ArrayList<String> names = new ArrayList<String>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if(cur.getCount() > 0){
            while(cur.moveToNext()){
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                names.add(name);
            }
        }
        return names;
    }

    private void insertContactToGroup(String role){
        ContentValues values = new ContentValues();

        if(role.equals("BCM")) {

            values.put(Data.RAW_CONTACT_ID, 1);
            values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
            values.put(Phone.NUMBER, "09339329234");
            values.put(Phone.TYPE, Phone.TYPE_CUSTOM);
            values.put(Data.DISPLAY_NAME, "Aileen Allattica");
            values.put(Phone.LABEL, "Project Lead");
            values.put(GroupMembership.RAW_CONTACT_ID, 1);
            values.put(GroupMembership.GROUP_ROW_ID, 1);
            values.put(ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE,
                    ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE);
            Uri dataUri = getContentResolver().insert(Data.CONTENT_URI, values);

        }
    }

    private void createGroups(){
        ContentValues groupValues;
        ContentResolver cr = getContentResolver();
        groupValues = new ContentValues();
        groupValues.put(ContactsContract.Groups._ID, 1);
        groupValues.put(ContactsContract.Groups.TITLE, "Carrefour");
        cr.insert(ContactsContract.Groups.CONTENT_URI, groupValues);

        groupValues = new ContentValues();
        groupValues.put(ContactsContract.Groups._ID, 2);
        groupValues.put(ContactsContract.Groups.TITLE, "Coles");
        cr.insert(ContactsContract.Groups.CONTENT_URI, groupValues);
    }

}
