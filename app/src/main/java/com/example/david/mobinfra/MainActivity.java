package com.example.david.mobinfra;

import android.os.Bundle;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private TextView xText, yText, zText;
    private String x, y, z;
    private DatabaseReference myRef;

    //private Sensor mySensor;
    //private SensorManager mSensorManager;


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
        x = getString(R.string.x);
        y = getString(R.string.y);
        z = getString(R.string.z);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        //myRef.setValue("Hello, World!");


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not in use
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        WriteNewRotation( event.values[0],  event.values[1],  event.values[2]);
        //xText.setText(x +  event.values[0]);
        //yText.setText(y +  event.values[1]);
        //zText.setText(z +  event.values[2]);
    }

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

    private void WriteNewRotation(float x, float y, float z){

        myRef.child("Data").child("X").setValue(x);
        myRef.child("Data").child("Y").setValue(y);
        myRef.child("Data").child("Z").setValue(z);
    }
}