package com.example.appAnime.activities;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.appAnime.R;
import com.example.appAnime.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        // Add a marker in Sydney and move the camera
        LatLng mappa = new LatLng(35.89029608052928, 139.5651337204019);
        mMap.addMarker(new MarkerOptions().position(mappa).title("Mappa Studio"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mappa));

        LatLng wit = new LatLng(35.9240462201997, 139.54776342826023);
        mMap.addMarker(new MarkerOptions().position(wit).title("Wit Studio"));

        LatLng key = new LatLng(38.47477669845244, 140.80859725210402);
        mMap.addMarker(new MarkerOptions().position(key).title("Key Studio"));

        LatLng madhouse = new LatLng(35.69635302944392, 139.67424754016554);
        mMap.addMarker(new MarkerOptions().position(madhouse).title("MadHouse Studio"));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(mappa).zoom(10).build();

        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate);


    }
}