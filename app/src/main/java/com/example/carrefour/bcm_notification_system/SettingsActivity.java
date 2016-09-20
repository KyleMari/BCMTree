package com.example.carrefour.bcm_notification_system;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        setTitle(R.string.settings_btn_label);

        //checks for extras from previous intent
        config = new Configuration();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();



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

                //clears the field
                if((!roleSpinner.getSelectedItem().toString().equals(config.getRole())))
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
        roleSpinner.setSelection(1);


        //puts the values to their respective views
        try {
            if (extras != null) {
                //sets the attributes from passed bundle
                config.setAttribFromBundle(extras);

                //states the currently selected user mode and its respective fields
                if(config.getRole().equals("BCM")) {
                    roleSpinner.setSelection(0);
                    emailBcmNumField.setText(config.getEmail());
                    timeLimitPicker.setValue(config.getTimeLimitLength());
                }
                else {
                    roleSpinner.setSelection(1);
                    emailBcmNumField.setText(config.getBcmNumber());
                }

                //states the drill message
                drillMsgField.setText(config.getDrillMsg());



            }
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
        config.setRole(roleSpinner.getSelectedItem().toString().trim());

        if(config.getRole().equals("BCM")){
            config.setEmail(emailBcmNumField.getText().toString().trim());
            config.setTimeLimitLength(timeLimitPicker.getValue());
        }else
            config.setBcmNumber(emailBcmNumField.getText().toString().trim());

        config.setDrillMsg(drillMsgField.getText().toString().trim());

        //passes values to intent and starts the next class
        Intent i = new Intent(SettingsActivity.this, MainActivity.class);
        i.putExtras(config.getAttribBundle());
        startActivity(i);
        finish();
    }
}
