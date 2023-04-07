package com.example.hackathon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddContact extends AppCompatActivity {

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private Button addContactButton;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addContactButton = findViewById(R.id.addContactButton);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Add contact button click listener
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get contact details from EditTexts
                String name = nameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();

                // Validate contact details
                if (name.isEmpty()) {
                    nameEditText.setError("Name is required");
                    nameEditText.requestFocus();
                    return;
                }

                // Add contact to database
                databaseHelper.addContact(name, email, phone);

                // Show success message
                Toast.makeText(AddContact.this, "Contact added successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent( view.getContext(), ContactList.class);
                startActivity(intent);
                // Clear EditTexts
                nameEditText.setText("");
                emailEditText.setText("");
                phoneEditText.setText("");
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
