package com.example.socialmapapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.SyncStatusObserver;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.System.exit;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    final HashMap<LatLng,String> map = new HashMap<LatLng,String>();
    final HashMap<LatLng,Marker> markerMap = new HashMap<LatLng,Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));

            if (!success) {
                Log.e("MapsActivity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivity", "Can't find style. Error: ", e);
        }
//////////////////////////////////////////////////////////////////////////////////////////////



        // Read from the database


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("/");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                mMap.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {

                   //LatLng location = new LatLng(ds.child("latitude").getValue(double.class), ds.child("longitude").getValue(double.class));
                    LocationMarker location;
                    location = ds.getValue(LocationMarker.class);
                    LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
                    map.put(position,ds.getKey());

                   Marker markerName = mMap.addMarker(new MarkerOptions().position(position).title(myRef.getKey()));
                   markerMap.put(position,markerName);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("data", "Failed to read value.", error.toException());
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////

        // Add a marker in Merced and move the camera
        LatLng merced = new LatLng(37.3, -120.48333);
        mMap.addMarker(new MarkerOptions().position(merced).title("Marker in Merced"));
        CameraUpdate center = CameraUpdateFactory.newLatLng( new LatLng (37.3,-120.48333));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

        /////////////////////////////////////////////////////////////////////////////////
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                Intent intent = new Intent(MapsActivity.this, MarkerPage.class);
                intent.putExtra("latitude",point.latitude);
                intent.putExtra("longitude", point.longitude);
                startActivityForResult(intent, 1);

            }

        });
        /////////////////////////////////////////////////////////////////////////////////////

       googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker){
                String fbKey = map.get(marker.getPosition());

                Intent intent = new Intent(MapsActivity.this, markerClick.class);
                intent.putExtra("fbKey", fbKey);
                intent.putExtra("latitude",marker.getPosition().latitude);
                intent.putExtra("longitude", marker.getPosition().longitude);

                startActivityForResult(intent, 2);

                /*
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference("/" + fbKey);


                myRef.removeValue();
                markerMap.remove(marker.getPosition());
                map.remove(marker.getPosition());
                marker.remove();
                */

                return true;

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==1)
        {
            if(resultCode == RESULT_OK) {
                String title = data.getStringExtra("title");
                String description = data.getStringExtra("description");

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("/");
                double latitude = data.getDoubleExtra("latitude",0);
                double longitude = data.getDoubleExtra("longitude", 0);
                LatLng point = new LatLng(latitude, longitude);
                LocationMarker location = new LocationMarker(latitude, longitude,title, description, "temp 3");
                myRef.child("/").push().setValue(location);
                mMap.addMarker(new MarkerOptions().position(point).title("temp"));
                map.put(point, myRef.getKey());
                myRef.child("/" + database.getReference().getKey() + "/dbKey").setValue(database.getReference().getKey());

            }

        }
        if(requestCode == 2){
            if(resultCode == RESULT_OK){
                String fbKey = data.getStringExtra("fbKey");
                LatLng position = new LatLng(data.getDoubleExtra("latitude",0), data.getDoubleExtra("longitude",0));
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference("/" + fbKey);


                myRef.removeValue();
                markerMap.remove(position);
                map.remove(position);
                
            }
        }
    }
}
