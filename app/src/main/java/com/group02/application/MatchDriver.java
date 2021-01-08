package com.group02.application;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MatchDriver extends FragmentActivity implements OnMapReadyCallback, RoutingListener {

    private GoogleMap mMap;
    int idDriver;
    int price;
    int vehicle;
    String startLocationName;
    String destinationLocationName;
    LatLng startLocation;
    LatLng destinationLocation;

    ImageView imgDriver;
    TextView infoDriver;
    TextView textViewPrice;
    TextView textViewStartLocation;
    TextView textViewDestinationLocation;
    TextView textDistance;
    TextView textDuration;
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
        textDistance = findViewById(R.id.textDistance);
        textDuration = findViewById(R.id.textDuration);
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

        vehicle = intent.getIntExtra("vehicle", 0);
        price = intent.getIntExtra("priceRoute", 0);
        startLocation = intent.getParcelableExtra("startLocation");
        destinationLocation = intent.getParcelableExtra("destinationLocation");
        startLocationName = intent.getStringExtra("startLocationName");
        destinationLocationName = intent.getStringExtra("destinationLocationName");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //set image for driver from idDriver
        //set info for driver from idDriver
        if (vehicle==1) {
            textViewPrice.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_baseline_four_seat_24, 0, 0);
            textViewPrice.setText(price+"k\n"+"Car 4-seat");
        }
        else if (vehicle==2) {
            textViewPrice.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_baseline_seven_seat_24, 0, 0);
            textViewPrice.setText(price+"k\n"+"Car 7-seat");
        }
        else{
            textViewPrice.setText(price+"k\n"+"Bike");
        }
        textViewStartLocation.setText(startLocationName);
        textViewDestinationLocation.setText(destinationLocationName);


        detailSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),DetailStepOfRoute.class);
                startActivity(intent);
            }
        });

        cancelRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCancelRouteDialog();
            }
        });

        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(startLocation,destinationLocation)
                .key(getResources().getString(R.string.google_maps_key))
                .build();

        routing.execute();
    }

    private void addCancelRouteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.cancel_route_title));
        builder.setMessage(getResources().getString(R.string.cancel_route_message));
        builder.setCancelable(true);
        builder.setPositiveButton(
                getResources().getString(R.string.accept),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Start of merge backend
                        //mergeBackendCancel(tripID);
                        //End of merge backend

                        addSuccessfulCancelRouteDialog();
                    }
                });
        builder.setNegativeButton(
                getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    
    private void mergeBackendCancel(String tripID) {
        String server = SERVER.get_server() + "api/passenger/cancel_trip/";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                server,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("CANCELDEBUG", "Success!");
                        addSuccessfulCancelRouteDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("CANCELDEBUG", error.getMessage());
                Toast.makeText(getApplicationContext(),"FAILED",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()
                    throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("trip_id", tripID);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void addSuccessfulCancelRouteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.successful_cancel_route_title));
        builder.setMessage(getResources().getString(R.string.successful_cancel_route_message));
        builder.setCancelable(false);
        builder.setPositiveButton(
                getResources().getString(R.string.accept),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), ChooseLocationActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(Color.argb(255,0,0,255));
        polyOptions.width(7);
        polyOptions.addAll(arrayList.get(i).getPoints());
        mMap.addPolyline(polyOptions);

        mMap.addMarker(new MarkerOptions().position(startLocation)
                .title(startLocationName)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))).showInfoWindow();
        mMap.addMarker(new MarkerOptions().position(destinationLocation)
                .title(destinationLocationName)).showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLocation,15));

        textDistance.setText(textDistance.getText().toString()+arrayList.get(i).getDistanceValue()*1.0/1000+"km");
        textDuration.setText(textDuration.getText().toString() + arrayList.get(i).getDurationText());
    }

    @Override
    public void onRoutingCancelled() {

    }
}