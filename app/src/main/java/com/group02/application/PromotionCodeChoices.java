package com.group02.application;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PromotionCodeChoices extends AppCompatActivity {
    int promotionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_code_choices);

        setTitle("Promotion Code");


        Intent intent = new Intent();
        intent.putExtra("promotionCode", promotionCode);

        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}