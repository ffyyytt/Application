package com.group02.application;

import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ChooseOptionsRoute extends FragmentActivity implements OnMapReadyCallback {
    int REQUEST_CODE_INTENT_PAY = 10;
    int REQUEST_CODE_INTENT_PROMOTIONCODE = 20;

    int typeOfPayValue;
    int promotionCodeValue;


    private GoogleMap mMap;
    LatLng startLocation;
    LatLng destinationLocation;
    LinearLayout[] btnVehicle;
    Button btnTypePay;
    Button btnPromotionCode;
    Button btnHelp;
    Button btnDetailStep;
    Button btnBook;
    int curVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_options_route);

        setTitle("Choose Options Route");

        Intent intent = getIntent();
        startLocation = intent.getParcelableExtra("startLocation");
        destinationLocation = intent.getParcelableExtra("destinationLocation");

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
                startActivity(intent);
            }
        });
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
            } else {
                // DetailActivity không thành công, không có data trả về.
            }
        }

        if (requestCode == REQUEST_CODE_INTENT_PROMOTIONCODE) {
            if (resultCode == Activity.RESULT_OK) {
                promotionCodeValue = data.getIntExtra("promotionCode", 0);
            } else {
                // DetailActivity không thành công, không có data trả về.
            }
        }
    }
}