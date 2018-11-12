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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    //region Fields
    private TextView xText, yText, zText;

    private DatabaseReference myRef;
    private int userID = 1;

    private ArrayList<RotationData> rD = new ArrayList<RotationData>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @NonNull
        @Override
        public Iterator<RotationData> iterator() {
            return null;
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @Override
        public <T> T[] toArray(@NonNull T[] a) {
            return null;
        }

        @Override
        public boolean add(RotationData rotationData) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(@NonNull Collection<? extends RotationData> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, @NonNull Collection<? extends RotationData> c) {
            return false;
        }

        @Override
        public boolean removeAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public RotationData get(int index) {
            return null;
        }

        @Override
        public RotationData set(int index, RotationData element) {
            return null;
        }

        @Override
        public void add(int index, RotationData element) {

        }

        @Override
        public RotationData remove(int index) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @NonNull
        @Override
        public ListIterator<RotationData> listIterator() {
            return null;
        }

        @NonNull
        @Override
        public ListIterator<RotationData> listIterator(int index) {
            return null;
        }

        @NonNull
        @Override
        public List<RotationData> subList(int fromIndex, int toIndex) {
            return null;
        }
    };

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
            mSensorManager.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }


        // Assign TextView
        xText = findViewById(R.id.xText);
        yText = findViewById(R.id.yText);
        zText = findViewById(R.id.zText);

        //myDatabaseReference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.addValueEventListener(myListener);


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
                xText.setText(("x: " + Double.toString(rData.x)));
                yText.setText(("y: " + Double.toString(rData.y)));
                zText.setText(("z: " + Double.toString(rData.z)));
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

    private void WriteNewRotation(String id, double x, double y, double z){
        RotationData rData = new RotationData(x, y, z);
        myRef.child("Data").child(id).setValue(rData);
    }
    //endregion
}