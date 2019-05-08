package com.example.socialmapapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class markerClick extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_click);
        final TextView editTitle = (TextView) findViewById(R.id.stored_title);
        final TextView editDescription = (TextView) findViewById(R.id.stored_description);

        Intent intent = getIntent();
        final String fbKey = intent.getStringExtra("fbKey");
        final Double latitude = intent.getDoubleExtra("latitude", 0);
        final Double longitude = intent.getDoubleExtra("longitude", 0);


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("/" + fbKey + "/title");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title = dataSnapshot.getValue(String.class);
                editTitle.setText(title);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRef = database.getReference("/" + fbKey + "/description");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String description = dataSnapshot.getValue(String.class);
                editDescription.setText(description);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button buttonSaveMarker = (Button)findViewById(R.id.delete_marker_button);
        buttonSaveMarker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent resultIntent = new Intent();
                resultIntent.putExtra("fbKey", fbKey);
                resultIntent.putExtra("latitude",latitude);
                resultIntent.putExtra("longitude",longitude);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}

