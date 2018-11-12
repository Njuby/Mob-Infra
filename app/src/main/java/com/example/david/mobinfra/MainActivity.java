package com.example.david.mobinfra;

import android.graphics.Region;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    //region Fields
    private TextView xText, yText, zText;

    private DatabaseReference myRef;
    private int userID = 1;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create our Sensor Manager
        SensorManager mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        // Accelerometer Sensor

        Sensor mySensor = null;
        if (mSensorManager != null) {
            mySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        // Register sensor Listener
        if(mySensor != null){
            mSensorManager.registerListener(this, mySensor, 50000000, 50000000);
        }


        // Assign TextView
        xText = findViewById(R.id.xText);
        yText = findViewById(R.id.yText);
        zText = findViewById(R.id.zText);

        //myDatabaseReference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Not in Use
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                //Not in Use
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Not in Use
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Not in Use
            }
        });

    }

    //region Accelerometer
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not in use
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //get and pass values(from Accelerometer
        WriteNewRotation(GetID(), event.values[0],  event.values[1],  event.values[2]);
    }



    ValueEventListener myListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            RotationData rData = dataSnapshot.getValue(RotationData.class);
            if(rData != null) {
                xText.setText(("x: " + Float.toString(rData.x)));
                yText.setText(("y: " + Float.toString(rData.y)));
                zText.setText(("z: " + Float.toString(rData.z)));
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    //endregion


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //region Methods

    //Increase Id counter
    private String GetID() {
        String id = String.valueOf(userID);
        userID ++;
        return id;
    }

    private void WriteNewRotation(String id, float x, float y, float z){
        RotationData rData = new RotationData(x, y, z);
        myRef.child("Data").child(id).setValue(rData);
    }
    //endregion
}