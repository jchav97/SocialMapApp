package com.example.socialmapapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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
import java.util.List;

import static java.lang.System.exit;

class Marker{

    public LatLng location;
    public double latitude;
    public double longitude;

    public Marker(){
    }
    public Marker(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = new LatLng(latitude,longitude);
    }

}

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

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
        DatabaseReference myRef = database.getReference("/");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    double latitude = ds.child("latitude").getValue(double.class);
                    System.out.println(latitude);
                    List<Double> list = new ArrayList<>();
                    list.add(latitude);
                    Double longitude = ds.child("longitude").getValue(Double.class);
                    System.out.println(longitude);
                    list.add(longitude);

                   LatLng location = new LatLng(list.get(0), list.get(1));
                   MarkerOptions marker = new MarkerOptions().position(
                          new LatLng(location.latitude, location.longitude)).title("New Marker");
                   mMap.addMarker(marker);
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

                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(point.latitude, point.longitude)).title("New Marker");
                mMap.addMarker(marker);

                // Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("/");

                myRef.child("/").push().setValue(point);
            }
            
        });
        /////////////////////////////////////////////////////////////////////////////////////
    }
}
