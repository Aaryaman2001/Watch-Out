package com.example.hackathon;

import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable {
    private String name;
    private String email;
    private int photoResId;
    private String phone;

    public Contact(String name, String email, int photoResId, String phone) {
        this.name = name;
        this.email = email;
        this.photoResId = photoResId;
        this.phone = phone;
    }

    protected Contact(Parcel in) {
        name = in.readString();
        email = in.readString();
        photoResId = in.readInt();
        phone = in.readString();
    }

    public static final Parcelable.Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeInt(photoResId);
        dest.writeString(phone);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPhotoResId(int photoResId) {
        this.photoResId = photoResId;
    }

    public int getPhotoResId() {
        return photoResId;
    }
}
