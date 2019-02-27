package com.example.david.mobinfra;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    //region Fields
    private TextView xText, yText, zText;

    SensorManager mSensorManager;
    Sensor mySensor;

    private DatabaseReference myRef, myDataRef;
    private int userID = 1;
    private  String valueId;
    private List<RotationData> my_rDataList = new ArrayList<>();

    private Boolean enableListener = false;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get all layout content
        // Assign TextView
        xText = findViewById(R.id.xText);
        yText = findViewById(R.id.yText);
        zText = findViewById(R.id.zText);
        Button compasButton = findViewById(R.id.compasButton);
        Button buttonStart = findViewById(R.id.buttonStart);
        Button buttonStop = findViewById(R.id.buttonStop);
        Button buttonReset = findViewById(R.id.buttonReset);

        compasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableListener = false;
                onPause();
                startActivity(new Intent(MainActivity.this, ProximityActivity.class));
            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableListener = true;
                onResume();
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableListener = false;
                onPause();

            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableListener = false;
                userID = 1;
                ResetValuesOnScreen();
                onPause();
                myRef.child("Data").removeValue();
            }
        });
        // Create our Sensor
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mySensor = null;
        if (mSensorManager != null) {
            mySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        //myDatabaseReference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //prevents OnDataChange for asking for non existend data. So we call for the previous data-change
                    int x = userID - 1;
                    //value cannot be null cause data starts at 1
                    //Only retrieve data when the listener is running. To avoid data-requests after resetting the database.
                    if(x > 0 && enableListener) {
                        RotationData rData = new RotationData();
                        rData.setX(ds.child(String.valueOf(x)).getValue(RotationData.class).getX());
                        rData.setY(ds.child(String.valueOf(x)).getValue(RotationData.class).getY());
                        rData.setZ(ds.child(String.valueOf(x)).getValue(RotationData.class).getZ());

                        my_rDataList.add(rData);

                        xText.setText(("x: " + Float.toString(rData.getX())));
                        yText.setText(("y: " + Float.toString(rData.getY())));
                        zText.setText(("z: " + Float.toString(rData.getZ())));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ResetValuesOnScreen() {
        xText.setText(R.string.x);
        yText.setText(R.string.y);
        zText.setText(R.string.z);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mySensor != null && enableListener)
            mSensorManager.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    //region Accelerometer
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not in use
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //get and pass values(from Accelerometer
        WriteNewRotation(event.values[0],  event.values[1],  event.values[2]);
    }

    //Increase Id counter
    private String GetID() {
        valueId = String.valueOf(userID);
        userID ++;
        return valueId;
    }

    private void WriteNewRotation(float x, float y, float z){
        RotationData rData = new RotationData();
        rData.setX(x);
        rData.setY(y);
        rData.setZ(z);
        GetID();
        myRef.child("Data").child(valueId).setValue(rData);
    }
    //endregion


}