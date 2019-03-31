package com.example.socialmapapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MarkerPage extends AppCompatActivity {

    private EditText mEditTextDescription;
    private EditText mEditTextTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_page);

        setTitle("Add Marker");

        Intent intent = getIntent();

        final double latitude = intent.getDoubleExtra("latitude", 0);
        final double longitude = intent.getDoubleExtra("longitude", 0);

        Button buttonSaveMarker = findViewById(R.id.button_save_marker);
        buttonSaveMarker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mEditTextDescription = findViewById(R.id.edit_description);
                final String title = mEditTextTitle.getText().toString();

                mEditTextTitle = findViewById(R.id.edit_title);
                final String description = mEditTextDescription.getText().toString();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("/");
                LocationMarker location = new LocationMarker(latitude, longitude,title, description, "temp 3");
                myRef.child("/").push().setValue(location);
                Intent intent = new Intent(MarkerPage.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }
}
