package com.example.hackathon;

import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.RECORD_AUDIO;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.app.Application;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.Manifest;


import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE_PERMISSION = 123;

    private FusedLocationProviderClient fusedLocationClient;

    private Button loginButton;
    private EditText username;
    private EditText password;
    boolean isChildModeOn = false;
    int time = 2;
    String[] bluetooth = new String[1];
    HashMap<String, String> users = new HashMap<>();
    boolean battery = false;
    private static final String speechSubscriptionKey = "3a407ec65c66462f9d1c3c6083e2ebd1";
    private static final String serviceRegion = "canadacentral";
    private String audioString ="";
    boolean yesSpeech = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.username);
        loginButton = findViewById(R.id.login_button);
        Button sos = findViewById(R.id.sos);
        password = findViewById(R.id.password);

//        int time = findViewById(R.id.time_option_label);
//        Button child = findViewById(R.id.child_mode_label);
        bluetooth[0] = "";
        bluetoothstring();
        users.put("Aarya", "1234");







        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        ArrayList<Integer> timeChild = databaseHelper.fetchChildTime();

        if (timeChild.get(1) == 0) {
            isChildModeOn = false;
        } else {
            isChildModeOn = true;

        }

        if (timeChild.get(0) == 2) {
            time = 2;
        } else {
            time = 4;
        }
        if (timeChild.get(2) == 0) {
            battery = false;
        } else {
            battery = true;
        }
        if (timeChild.get(3) == 0) {
            yesSpeech = false;
        } else {
            yesSpeech = true;
        }



        SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor mysensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        ArrayList<DBContactModel> contactList = databaseHelper.fetchContacts();
//        String loc = location();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        final String[] locationString = {""};

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    locationString[0] = "Latitude: " + location.getLatitude() + "\n" + "Longitude: " + location.getLongitude() + "\n";
                }
            }
        });

        BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
        int batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        if (batteryLevel < 90 && battery) {
            
            Toast.makeText(MainActivity.this, "Battery Level:" + batteryLevel + " , warning message sent.", Toast.LENGTH_SHORT).show();

            String message = "This message has been sent from Watch Out, it is NOT an SOS.\n\n"
                    + "The device's battery is low. \n\n";

            for (DBContactModel i : contactList) {
                try {
                    String phoneNumber = i.phone;
//                SmsManager smsManager = SmsManager.getDefault();

//                int id = getSubscriptionIdForSimSlot(1);
                    SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(1);
                    smsManager.sendMultipartTextMessage(phoneNumber, null, smsManager.divideMessage(message), null, null);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


//        Toast.makeText(MainActivity.this, loc, Toast.LENGTH_SHORT).show();
        SensorEventListener se = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float[] values = sensorEvent.values;
                Float x = values[0];
                Float y = values[1];
                Float z = values[2];
                ArrayList<Integer> timeChild = databaseHelper.fetchChildTime();


                double acc = (x * x) + (y * y) + (z * z) / (9.8 * 9.8);

                if (acc >= 500 & timeChild.get(1) == 1) {
//
//                    Log.d("Sensor", "X: " + values[0] + ", Y: " + values[1] + ", Z: " + values[2]);

                    Toast.makeText(getApplicationContext(), "Device found in motion; initiating SOS.", Toast.LENGTH_SHORT).show();

                    try {

                        sendSOS(locationString[0]);
                        isChildModeOn = false;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        sm.registerListener(se, mysensor, SensorManager.SENSOR_DELAY_NORMAL);


        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//
                try {
                    Toast.makeText(MainActivity.this, "SOS Clicked.", Toast.LENGTH_SHORT).show();

                    sendSOS(locationString[0]);


                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


//
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkUser(username, password, users)) {
                    Intent intent = new Intent(MainActivity.this, ContactList.class);
                    startActivity(intent);


                } else {

                }
            }
        });



        };




    private Boolean checkUser(EditText login, EditText password, HashMap<String, String> users) {
        for (String i : users.keySet()) {
            if (i.equals(login.getText().toString())) {
                if (users.get(i).equals(password.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Login Successful.", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else{
                    Toast.makeText(getApplicationContext(), "Password incorrect", Toast.LENGTH_SHORT).show();
                    return false;
                }

            }
        }
        Toast.makeText(getApplicationContext(), "User not registered.", Toast.LENGTH_SHORT).show();
        return false;

    }
    public String location(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        final String[] locationString = new String[1];

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            return "Could not get location.";
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    locationString[0] = "Latitude: " + location.getLatitude() + "\n" + "Longitude: " + location.getLongitude() + "\n";
                }
            }
        });
        return locationString[0];
    }


    public void sendSOS(String location) throws Exception {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        ArrayList<DBContactModel> contactList = db.fetchContacts();


//        // Compose the message with recording and location
        String message = "This SOS has been sent from Watch Out. \n\n" +
                location + "\n\n" +
                bluetooth[0];




        // Send the message with location to all contacts
        for (DBContactModel i : contactList) {
            try {
                String phoneNumber = i.phone;
//                SmsManager smsManager = SmsManager.getDefault();

//                int id = getSubscriptionIdForSimSlot(1);
                SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(1);
                smsManager.sendMultipartTextMessage(phoneNumber, null, smsManager.divideMessage(message), null, null);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(MainActivity.this, "Message sent to emergency contacts.", Toast.LENGTH_SHORT).show();
        start();

    }

    private void start() throws Exception {
        if (!hasWritePermission()) {
            // Request write permission from the user
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_PERMISSION);
            return;
        }
        Toast.makeText(getApplicationContext(), "Initiating Recording buffer.", Toast.LENGTH_SHORT).show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String current = sdf.format(new Date());

        MediaRecorder mr = new MediaRecorder();
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File music = wrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(music, "Emergency "+ current + ".wav");
        String path = file.getPath();
        mr.setAudioSource(MediaRecorder.AudioSource.MIC);
        mr.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        path = file.getPath().replace(".mp3", ".wav");
        mr.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mr.setOutputFile(path);
        mr.prepare();
        mr.start();
        Toast.makeText(getApplicationContext(), "Recording Started", Toast.LENGTH_SHORT).show();
        // Record for 10 seconds
        try {
            Thread.sleep(12000);
//            else Thread.sleep(240000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mr.stop();
        mr.release();
//        SpeechToText speech = new SpeechToText();
//        String res = speech.transcribeAudio("/storage/emulated/0/Android/data/com.example.hackathon/files/Music/Emergency " + current + ".wav");
        int requestCode = 5; // unique code for the permission request
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{RECORD_AUDIO, INTERNET}, requestCode);
        if (yesSpeech){
        try (SpeechConfig config = SpeechConfig.fromSubscription(speechSubscriptionKey, serviceRegion);
             SpeechRecognizer reco = new SpeechRecognizer(config);) {

            Future<SpeechRecognitionResult> task = reco.recognizeOnceAsync();

            // Note: this will block the UI thread, so eventually, you want to
            //       register for the event (see full samples)
            SpeechRecognitionResult result = task.get();

            if (result.getReason() == ResultReason.RecognizedSpeech) {
                audioString = result.toString();
                audioString.toLowerCase();
            }


        } catch (Exception ex) {

            assert(false);
        }
        String secondMessage = "";
        if(audioString.contains("firefighters") || audioString.contains("fire") || audioString.contains("burning")){
            secondMessage = secondMessage + "Fire Hazard \n";

        }
        if(audioString.contains("pedophile") || audioString.contains("child abuse") || audioString.contains("molest")){
            secondMessage = secondMessage + "Molestation \n";

        }
            if(audioString.contains("fuel") || audioString.contains("out of fuel") || audioString.contains("no gas")){
                secondMessage = secondMessage + "No Fuel in Vehicle. \n";

            }

        if(audioString.contains("crash") || audioString.contains("car") || audioString.contains("accident") || audioString.contains("hurt")){
            secondMessage = secondMessage + "Car Accident \n";

        }
            DatabaseHelper db = new DatabaseHelper(getApplicationContext());
            ArrayList<DBContactModel> contactList = db.fetchContacts();


//        // Compose the message with recording and location
            String message = "The AI voice detection was enabled, potential emergencies might include: \n\n" +
                    secondMessage;

            // Send the message with location to all contacts
            for (DBContactModel i : contactList) {
                try {
                    String phoneNumber = i.phone;
//                SmsManager smsManager = SmsManager.getDefault();

//                int id = getSubscriptionIdForSimSlot(1);
                    SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(1);
                    smsManager.sendMultipartTextMessage(phoneNumber, null, smsManager.divideMessage(message), null, null);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(MainActivity.this, "Potential hazards have been conveyed.", Toast.LENGTH_SHORT).show();


        }



        Toast.makeText(getApplicationContext(), "Recording stopped.", Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), "Recording saved.", Toast.LENGTH_SHORT).show();

    }




    private boolean hasWritePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }


    public void bluetoothstring() {

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Bluetooth not supported on this device

        }

        if (!bluetoothAdapter.isEnabled()) {
            // Bluetooth not enabled, request user to enable it
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
        }

//        final StringBuilder devicesStringBuilder = new StringBuilder();

        // Register the BroadcastReceiver
        HashMap<String,String> map = new HashMap<>();
        bluetooth[0] = "List of Bluetooth Devices Detected:" + "\n";


        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//
                    // Retrieve the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device != null) {
                        String deviceName = device.getName();
                        String deviceHardwareAddress = device.getAddress();
                        if (deviceName != null && deviceHardwareAddress != null && !bluetooth[0].contains(deviceName) && !bluetooth[0].contains(deviceHardwareAddress)) {
                            // Append the device name and MAC address to the StringBuilder object
                            bluetooth[0] += deviceName + "-" + deviceHardwareAddress + "\n";
                        }
                    }

                }
            }
        };

        // Unregister the BroadcastReceiver
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException e) {
            // Receiver was not registered, ignore exception
        }

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiver, filter);

        // Start the discovery process
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            // Bluetooth permissions not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                    1);
//            return "Bluetooth permissions not granted";
        }
        boolean discoveryStarted = bluetoothAdapter.startDiscovery();
        if (!discoveryStarted) {
//            return "Bluetooth discovery failed to start";
        }


    }
    private int getSubscriptionIdForSimSlot(int slotIndex) {
        SubscriptionManager subscriptionManager = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        if (subscriptionManager != null) {
            List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
            if (subscriptionInfoList != null && subscriptionInfoList.size() > slotIndex) {
                SubscriptionInfo subscriptionInfo = subscriptionInfoList.get(slotIndex);
                if (subscriptionInfo != null) {
                    return subscriptionInfo.getSubscriptionId();
                }
            }
        }
        return -1; // return -1 if the subscription ID cannot be retrieved
    }



}