package com.group02.application;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TypeOfPay extends AppCompatActivity {
    int typePay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_of_pay);

        setTitle("Payment");

        Button btnCreditCard = findViewById(R.id.btnCreditCard);
        Button btnCash = findViewById(R.id.btnCash);

        btnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typePay = -1;
            }
        });

        btnCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typePay = 1;
            }
        });

        Intent intent = new Intent();
        intent.putExtra("typePay", typePay);

        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}