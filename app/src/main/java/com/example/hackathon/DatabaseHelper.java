package com.example.hackathon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ContactsDB";

    private static final String CONTACT_TABLE = "contacts";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE_NO = "phone_no";
    private static  final String OTHER_TABLE = "OtherDB";

    private static final String TIME_DURATION = "time_val";
    private static final String CHILD_MODE = "child_mode";
    private static final String BATTERY_SOS = "battery";
    private static final String ID = "id";
    private static final String AI_KEY = "ai";




    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + CONTACT_TABLE +
                "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME + " TEXT,"
        + KEY_EMAIL + " TEXT," + KEY_PHONE_NO + " TEXT" + ")"
        );

        sqLiteDatabase.execSQL("CREATE TABLE " + OTHER_TABLE +
                "(" + ID + " INTEGER, " + TIME_DURATION + " INTEGER ,"
                + CHILD_MODE + " INTEGER ," + BATTERY_SOS + " INTEGER, " + AI_KEY + " INTEGER )"
        );





    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CONTACT_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OTHER_TABLE);
        onCreate(sqLiteDatabase);


    }
    public void addContact(String name, String email, String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PHONE_NO, phone);
        db.insert(CONTACT_TABLE,null, values);
    }

    public ArrayList<DBContactModel> fetchContacts(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CONTACT_TABLE, null);
        ArrayList<DBContactModel> arr = new ArrayList<>();
        while(cursor.moveToNext()){
            DBContactModel model = new DBContactModel();
            model.id = cursor.getInt(0);
            model.name = cursor.getString(1);
            model.email = cursor.getString(2);
            model.phone = cursor.getString(3);
            arr.add(model);
        }
        cursor.close();
        return arr;
    }

    public void updateContact(DBContactModel model){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, model.name);
        cv.put(KEY_PHONE_NO, model.phone);
        cv.put(KEY_EMAIL, model.email);

        db.update(CONTACT_TABLE, cv,  KEY_ID + " = " + model.id, null);

    }
    public void deleteContact(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CONTACT_TABLE, KEY_ID + " = " + id, null);

    }
    public void addTimeChild(int time , int child, int battery, int ai){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, 1);
        values.put(TIME_DURATION, time);
        values.put(CHILD_MODE, child);
        values.put(BATTERY_SOS, battery);
        values.put(AI_KEY, ai);

        db.insert(OTHER_TABLE,null, values);
    }

    public void changeTimeChild(int time, int childMode, int battery, int ai){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID, 1);
        cv.put(TIME_DURATION, time);
        cv.put(CHILD_MODE, childMode);
        cv.put(BATTERY_SOS, battery);
        cv.put(AI_KEY, ai);
        db.update(OTHER_TABLE, cv,  ID + " = " + 1, null);
    }

    public ArrayList<Integer> fetchChildTime(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + OTHER_TABLE, null);
        ArrayList<Integer> arr = new ArrayList<>();
        if (cursor.moveToFirst()) {
            arr.add(cursor.getInt(1));
            arr.add(cursor.getInt(2));
            arr.add(cursor.getInt(3));
            arr.add(cursor.getInt(4));

        }

        cursor.close();
        return arr;
    }

}
