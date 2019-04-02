package com.example.socialmapapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MarkerPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_page);

        setTitle("Add Marker");

        Intent intent = getIntent();

        final double latitude = intent.getDoubleExtra("latitude", 0);
        final double longitude = intent.getDoubleExtra("longitude", 0);

        Button buttonSaveMarker = (Button)findViewById(R.id.button_save_marker);
        buttonSaveMarker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                EditText mEditTextTitle = (EditText)findViewById(R.id.edit_title);
                String title = mEditTextTitle.getText().toString();

                EditText mEditTextDescription = findViewById(R.id.edit_description);
                String description = mEditTextDescription.getText().toString();


                Intent resultIntent = new Intent();
                resultIntent.putExtra("title", title);
                resultIntent.putExtra("description", description);
                resultIntent.putExtra("latitude", latitude);
                resultIntent.putExtra("longitude", longitude);

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
