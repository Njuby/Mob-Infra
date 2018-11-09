package com.example.david.mobinfra;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener{

    private TextView xText, yText, zText;
    private String x, y, z;
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
            mSensorManager.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }


        // Assign TextView
        xText = findViewById(R.id.xText);
        yText = findViewById(R.id.yText);
        zText = findViewById(R.id.zText);
        x = getString(R.string.x);
        y = getString(R.string.y);
        z = getString(R.string.z);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not in use
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        xText.setText(x +  event.values[0]);
        yText.setText(y +  event.values[1]);
        zText.setText(z +  event.values[2]);
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
}