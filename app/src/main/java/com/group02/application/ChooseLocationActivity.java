package com.group02.application;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class ChooseLocationActivity extends FragmentActivity implements OnMapReadyCallback {
    String TAG = "MapsFragment";
    private GoogleMap mMap;
    EditText editTextStartSearch;
    EditText editTextDestinationSearch;
    LatLng[] location;

    String phone_no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);

        phone_no = getIntent().getStringExtra("phone_no");

        editTextStartSearch = findViewById(R.id.startSearch);
        editTextDestinationSearch = findViewById(R.id.destinationSearch);
        location = new LatLng[2];

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setTitle("Choose Location");

        Places.initialize(this, getResources().getString(R.string.google_maps_key));
        //set editText non focusable
        editTextStartSearch.setFocusable(false);
        editTextDestinationSearch.setFocusable(false);

        editTextStartSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(ChooseLocationActivity.this);
                startActivityForResult(intent, 100);
            }
        });
        editTextDestinationSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(ChooseLocationActivity.this);
                startActivityForResult(intent, 200);
            }
        });
    }

    private void intentToChooseOptionsRoute(){
        if (location[0]!=null && location[1]!=null){
            Intent intent = new Intent(getApplicationContext(),ChooseOptionsRoute.class);
            intent.putExtra("startLocation",location[0]);
            intent.putExtra("destinationLocation",location[1]);
            intent.putExtra("startLocationName",editTextStartSearch.getText().toString());
            intent.putExtra("destinationLocationName",editTextDestinationSearch.getText().toString());
            intent.putExtra("phone_no", phone_no);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getLatLng().toString());
                location[0] = place.getLatLng();
                editTextStartSearch.setText(place.getName());
                intentToChooseOptionsRoute();
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getLatLng().toString());
                location[1] = place.getLatLng();
                editTextDestinationSearch.setText(place.getName());
                intentToChooseOptionsRoute();
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}