package com.group02.application;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MatchDriver extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    int idDriver;
    int price;
    String startLocation;
    String destinationLocation;
    ImageView imgDriver;
    TextView infoDriver;
    TextView textViewPrice;
    TextView textViewStartLocation;
    TextView textViewDestinationLocation;
    Button detailSteps;
    Button cancelRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_driver);

        //retrieveIntentfromChooseOptionRoute
        retrieveIntentfromChooseOptionRoute();

        imgDriver = findViewById(R.id.imageDriver);
        infoDriver = findViewById(R.id.infoDriver);
        textViewPrice = findViewById(R.id.vehicle);
        textViewStartLocation = findViewById(R.id.startLocation);
        textViewDestinationLocation = findViewById(R.id.destinationLocation);
        detailSteps = findViewById(R.id.btnDetailSteps);
        cancelRoute = findViewById(R.id.btnCancel);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void retrieveIntentfromChooseOptionRoute() {
        Intent intent = getIntent();
        //set value for map,price, location, driver
        //ex: id driver
        idDriver = intent.getIntExtra("id_driver",0);
        price = intent.getIntExtra("price",0);
        startLocation = intent.getStringExtra("start");
        destinationLocation = intent.getStringExtra("destination");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //setup map fragment

        //set image for driver from idDriver
        //set info for driver from idDriver
        textViewPrice.setText(price+"\n"+textViewPrice.getText());
        textViewStartLocation.setText(startLocation);
        textViewDestinationLocation.setText(destinationLocation);

        detailSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //something to do
            }
        });

        cancelRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MatchDriver.this,ChooseOptionsRoute.class);
                startActivity(intent);
            }
        });
    }
}