package com.example.hackathon;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private ArrayList<DBContactModel> contactList;
    private Context context;

    public ContactAdapter(Context context, ArrayList<DBContactModel> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        DBContactModel contact = contactList.get(position);

        holder.textViewContactEmail.setText(contact.email);
        holder.textViewContactName.setText(contact.name);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the onContactItemClick() method on the activity

                ((ContactList) context).onContactItemClick(Integer.valueOf(contact.id));

            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageViewContactPhoto;
        public TextView textViewContactName;
        public TextView textViewContactEmail;


        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewContactPhoto = itemView.findViewById(R.id.image_view_contact_photo);
            textViewContactName = itemView.findViewById(R.id.text_view_contact_name);
            textViewContactEmail = itemView.findViewById(R.id.text_view_contact_email);

        }
    }
}


