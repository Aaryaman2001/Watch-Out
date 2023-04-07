package com.example.hackathon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ContactList extends AppCompatActivity {
    private ArrayList<Contact> contactList;
    private ListView listView;
    private static final int SETTINGS_REQUEST_CODE = 1;
    private static final int SETTINGS_RESULT_CODE = RESULT_OK;
    int[] selectedTimeOption =new int[1];
    boolean[] isChildModeOn = new boolean[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactlist);


        // Get the contact list from the previous activity
//        isChildModeOn[0] = getIntent().getBooleanExtra("child_mode", false);
//        selectedTimeOption[0] = getIntent().getIntExtra("time_option",2);

//        ArrayList<Contact> contactList = getIntent().getParcelableArrayListExtra("contacts");

        // Initialize the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve the contacts from the database
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        ArrayList<DBContactModel> contactList = databaseHelper.fetchContacts();

        // Initialize the RecyclerView adapter
        ContactAdapter adapter = new ContactAdapter(this, contactList);

        // Set the adapter for the RecyclerView
        recyclerView.setAdapter(adapter);

        Button btnAddContact = findViewById(R.id.btn_add_contact);
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactList.this, AddContact.class);
                startActivity(intent);
            }
        });

    }

    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsPanel.class);
//        intent.putExtra("child_mode", isChildModeOn);
//        intent.putExtra("time_option", selectedTimeOption);
        startActivityForResult(intent, SETTINGS_REQUEST_CODE);
    }

//    // Override the onActivityResult method to retrieve the updated values from the SettingsPanel activity
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == SETTINGS_REQUEST_CODE && resultCode == SETTINGS_RESULT_CODE && data != null) {
//            // Retrieve the updated values from the returned Intent extras
//            boolean updatedChildModeOn = data.getBooleanExtra("child_mode", false);
//            int updatedSelectedTimeOption = data.getIntExtra("time_option", 2);
//
//            // Update the values in the MainActivity
//            MainActivity mainActivity = (MainActivity) getParent();
//            mainActivity.isChildModeOn[0] = updatedChildModeOn;
//            mainActivity.selectedTimeOption[0] = updatedSelectedTimeOption;
//        }
//    }

    public void onContactItemClick(Integer contact) {
        Intent intent = new Intent(this, ContactDetails.class);

        // Pass the selected contact to the details activity
        intent.putExtra("key",contact);

        // Start the details activity
        startActivity(intent);

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
