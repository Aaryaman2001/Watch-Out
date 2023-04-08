package com.example.hackathon;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.MissingFormatArgumentException;

public class SettingsPanel extends Activity {

    private RadioGroup timeOptionGroup;
    private RadioButton timeOption2;
    private RadioButton timeOption4;
    private Switch childModeSwitch;
    private Switch battery_switch;
    private TextView childlabel;
    private Button applyButton;
    int time = 2;
    boolean isChildModeOn = false;
    boolean battery = false;

    private Switch ai_mode;

    boolean ai = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_panel);

        timeOptionGroup = findViewById(R.id.time_option_group);
        timeOption2 = findViewById(R.id.time_option_2);
        timeOption4 = findViewById(R.id.time_option_4);
        childModeSwitch = findViewById(R.id.child_mode_switch);
        applyButton = findViewById(R.id.apply_button);
        childlabel = findViewById(R.id.child_mode_label);
        battery_switch = findViewById(R.id.battery_switch);
        ai_mode = findViewById(R.id.ai);


        DatabaseHelper db = new DatabaseHelper(this);
        ArrayList<Integer> timeChild = db.fetchChildTime();
//        Button child = findViewById(R.id.child_mode_label);

        if(timeChild.get(1) == 0){
            isChildModeOn = false;
        }
        else{
            isChildModeOn = true;
        }

        if(timeChild.get(0)==2){
            time = 2;
        }
        else{
            time = 4;
        }
        if (timeChild.get(2)==0){
            battery = false;
        }
        else{
            battery = true;
        }

        if (timeChild.get(3)==0){
            ai = false;
        }
        else{
            ai = true;
        }




        // Retrieve initial values from the calling activity's Intent extras
//        isChildModeOn = getIntent().getBooleanArrayExtra("child_mode");
//        selectedTimeOption = getIntent().getIntArrayExtra("time_option");

        // Set the initial state of the UI based on the retrieved values
        if (time == 4) {
            timeOption4.setChecked(true);
        } else {
            timeOption2.setChecked(true);
        }
        childModeSwitch.setChecked(isChildModeOn);
        battery_switch.setChecked(battery);
        ai_mode.setChecked(ai);

        // Set up the click listener for the applyButton
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the values of the selected options based on the UI state
                if (timeOption2.isChecked()) {
                    time = 2;
                } else {
                    time = 4;
                }
                int child;
                int bat;
                int aint;
                if(isChildModeOn = childModeSwitch.isChecked()){
                    child = 1;

                }
                else{
                    child = 0;

                }
                if(battery = battery_switch.isChecked()){
                    bat = 1;

                }
                else{
                    bat = 0;

                }
                if(ai = ai_mode.isChecked()){
                    aint = 1;

                }
                else{
                    aint = 0;

                }

                db.changeTimeChild(time, child, bat, aint);


                Toast.makeText(SettingsPanel.this, "Settings updated successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent( v.getContext(), MainActivity.class);
                startActivity(intent);

            }

        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ContactList.class);
        startActivity(intent);
        finish();
    }
}
