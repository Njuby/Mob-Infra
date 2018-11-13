package com.example.david.mobinfra;

import android.content.Intent;
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
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
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

    SensorManager mSensorManager;
    Sensor mySensor;

    private DatabaseReference myRef, myDataRef;
    private int userID = 1;
    private List<RotationData> my_rDataList = new List<RotationData>() {
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
        onPause();
        //Get all layout content
        // Assign TextView
        xText = findViewById(R.id.xText);
        yText = findViewById(R.id.yText);
        zText = findViewById(R.id.zText);
        Button compasButton = findViewById(R.id.compasButton);
        Button buttonStart = findViewById(R.id.buttonStart);
        Button buttonStop = findViewById(R.id.buttonStop);

        compasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();
                startActivity(new Intent(MainActivity.this, ProximityActivity.class));
            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume();
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();

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
                //showData(dataSnapshot);
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    RotationData rData = ds.child("Data").getValue(RotationData.class);
                    my_rDataList.add(rData);

                    //.setX(ds.child("Data").getValue(RotationData.class).getX());
                    //rData.setY(ds.child("Data").getValue(RotationData.class).getY());
                    //rData.setZ(ds.child("Data").getValue(RotationData.class).getZ());

                    xText.setText(("x: " + Float.toString(rData.getX())));
                    yText.setText(("y: " + Float.toString(rData.getY())));
                    zText.setText(("z: " + Float.toString(rData.getZ())));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mySensor != null)
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
        WriteNewRotation(GetID(), event.values[0],  event.values[1],  event.values[2]);
    }

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