package com.example.hackathon;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        // Get the selected contact from the intent extras
        TextView nameTextView = findViewById(R.id.textView_name);
        TextView emailTextView = findViewById(R.id.textView_email);
        TextView phone = findViewById(R.id.textView_phone);
        int contact = getIntent().getIntExtra("key", -1);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        ArrayList<DBContactModel> contactList = databaseHelper.fetchContacts();
        for(DBContactModel i: contactList){
            if (i.id == contact){
                nameTextView.setText(i.name);
                emailTextView.setText(i.email);
                phone.setText(i.phone);

            }
        }

        // Set the contact name and email in the activity's views



        Button edit = findViewById(R.id.button_edit);
        Button delete = findViewById(R.id.button_delete);


        // Set the contact image in the activity's view
//        @SuppressLint("WrongViewCast") ImageView imageView = findViewById(R.id.textView_name);
//        imageView.setImageResource(contact.getImageResId());

        // TODO: Add click listeners for edit and delete buttons
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( v.getContext(), EditContact.class);
                intent.putExtra("key", Integer.valueOf(contact));
                startActivity(intent);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                AlertDialog.Builder dialog = new AlertDialog.Builder(ContactDetails.this);
                dialog.setTitle("Delete Contact?");

                dialog.setMessage("Are you sure you want to delete this emergency contact?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseHelper.deleteContact(contact);
                        Toast.makeText(getApplicationContext(), "Contact deleted Successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent( view.getContext(), ContactList.class);
                        startActivity(intent);
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent( view.getContext(), ContactList.class);
                        startActivity(intent);
                    }
                });
                dialog.setIcon(R.drawable.delete);
               dialog.show();

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