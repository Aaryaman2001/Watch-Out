package com.example.hackathon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditContact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        int contact = getIntent().getIntExtra("key", -1);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        ArrayList<DBContactModel> contactList = databaseHelper.fetchContacts();


        // Set the contact name and email in the activity's views
        TextView nameTextView = findViewById(R.id.nameEditText);
        TextView emailTextView = findViewById(R.id.emailEditText);
        TextView phone = findViewById(R.id.phoneEditText);

        Button edit = findViewById(R.id.EditContactButton);
        for(DBContactModel i: contactList){
            if (i.id == contact){
                nameTextView.setText(i.name);
                emailTextView.setText(i.email);
                phone.setText(i.phone);

            }
        }



        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get updated contact information from the EditText views
                String updatedName = nameTextView.getText().toString();
                String updatedEmail = emailTextView.getText().toString();
                String updatedPhone = phone.getText().toString();
                DBContactModel model = new DBContactModel();
                model.id = contact;
                model.email = updatedEmail;
                model.phone = updatedPhone;
                model.name = updatedName;

                // Update the contact in the database
                databaseHelper.updateContact(model);
                Toast.makeText(EditContact.this, "Contact updated successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent( v.getContext(), ContactList.class);
                startActivity(intent);

            }
        });


    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ContactDetails.class);
        startActivity(intent);
        finish();
    }
}