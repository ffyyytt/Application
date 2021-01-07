package com.group02.application;

import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class ChooseOptionsRoute extends FragmentActivity implements OnMapReadyCallback, RoutingListener {
    String TAG = "ChooseOptionsRoute";
    int REQUEST_CODE_INTENT_PAY = 10;
    int REQUEST_CODE_INTENT_PROMOTIONCODE = 20;

    int typeOfPayValue;
    int promotionCodeValue;
    double distanceRoute;


    private GoogleMap mMap;
    LatLng startLocation;
    LatLng destinationLocation;
    String startLocationName;
    String destinationLocationName;


    LinearLayout[] btnVehicle;
    Button btnTypePay;
    Button btnPromotionCode;
    Button btnHelp;
    Button btnDetailStep;
    Button btnBook;

    int curVehicle = 0;
    int priceRoute=0;//calculate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_options_route);

        setTitle("Choose Options Route");

        Intent intent = getIntent();
        startLocation = intent.getParcelableExtra("startLocation");
        destinationLocation = intent.getParcelableExtra("destinationLocation");
        startLocationName = intent.getStringExtra("startLocationName");
        destinationLocationName = intent.getStringExtra("destinationLocationName");

        btnVehicle = new LinearLayout[3];
        btnVehicle[0] = findViewById(R.id.btnBike);
        btnVehicle[1] = findViewById(R.id.btnCar4);
        btnVehicle[2] = findViewById(R.id.btnCar7);
        btnTypePay = findViewById(R.id.btnTypePay);
        btnPromotionCode = findViewById(R.id.btnPromotionCode);
        btnHelp = findViewById(R.id.btnHelp);
        btnDetailStep = findViewById(R.id.btnDetailSteps);
        btnBook = findViewById(R.id.btnBookNow);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //setOnClick for vehicle
        for (int i=0;i<3;i++)
            setOnClickButtonVehicle(btnVehicle,i);

        btnTypePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseOptionsRoute.this,TypeOfPay.class);
                startActivityForResult(intent, REQUEST_CODE_INTENT_PAY);
            }
        });

        btnPromotionCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseOptionsRoute.this,PromotionCodeChoices.class);
                startActivityForResult(intent,REQUEST_CODE_INTENT_PROMOTIONCODE);
            }
        });

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseOptionsRoute.this,HelpActivity.class);
                startActivity(intent);
            }
        });

        btnDetailStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseOptionsRoute.this,DetailStepOfRoute.class);
                startActivity(intent);
            }
        });

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseOptionsRoute.this, MatchDriver.class);
                intent.putExtra("vehicle", curVehicle);
                intent.putExtra("priceRoute", priceRoute);
                intent.putExtra("startLocation",startLocation);
                intent.putExtra("destinationLocation",destinationLocation);
                intent.putExtra("startLocationName",startLocationName);
                intent.putExtra("destinationLocationName",destinationLocationName);
                startActivity(intent);
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

    private void setOnClickButtonVehicle(LinearLayout[] btnVehicle,int pos){
        btnVehicle[pos].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=0;i<3;i++){
                    if (i==pos)
                        btnVehicle[i].setBackgroundColor(getResources().getColor(R.color.quantum_yellow200));
                    else
                        btnVehicle[i].setBackgroundColor(getResources().getColor(R.color.white));
                }
                curVehicle = pos;

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_INTENT_PAY) {
            if (resultCode == Activity.RESULT_OK) {
                typeOfPayValue = data.getIntExtra("typePay", 0);
                btnTypePay.setText("Cash");
            } else {
                // DetailActivity không thành công, không có data trả về.
            }
        }

        if (requestCode == REQUEST_CODE_INTENT_PROMOTIONCODE) {
            if (resultCode == Activity.RESULT_OK) {
                promotionCodeValue = data.getIntExtra("promotionCode", 0);
                btnTypePay.setText("Credit Card");
            } else {
                // DetailActivity không thành công, không có data trả về.
            }
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        Toast.makeText(getApplicationContext(),"Finding Route failed!", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onRoutingFailure: ");
    }

    @Override
    public void onRoutingStart() {
        Toast.makeText(getApplicationContext(),"Finding Route started!", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onRoutingStart: ");
    }

    @Override
    public void onRoutingCancelled() {
        Toast.makeText(getApplicationContext(),"Finding Route cancelled!", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onRoutingCancelled: ");
    }


    //If Route finding success..
    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        mMap.clear();

        distanceRoute = route.get(shortestRouteIndex).getDistanceValue();
        showRoute(route.get(shortestRouteIndex),mMap);
    }

    public void showRoute(Route r, GoogleMap mMap){
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(Color.argb(255,0,0,255));
        polyOptions.width(7);
        polyOptions.addAll(r.getPoints());
        mMap.addPolyline(polyOptions);

        mMap.addMarker(new MarkerOptions().position(startLocation)
                .title(startLocationName)
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))).showInfoWindow();
        mMap.addMarker(new MarkerOptions().position(destinationLocation)
                .title(destinationLocationName)).showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLocation,15));
    }
}