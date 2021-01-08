package com.group02.application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;

import static com.group02.application.PromotionCodeChoices.dataList;

public class DetailPromotionCode extends AppCompatActivity {
    String promo_code;

    TextView textViewId;
    TextView textViewExpiredDate;
    TextView textViewCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_promotion_code);

        promo_code = getIntent().getStringExtra("promo_code");
        setTitle(promo_code);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewId = findViewById(R.id.textViewId);
        textViewExpiredDate = findViewById(R.id.textViewExpiredDate);
        textViewCondition = findViewById(R.id.textViewCondition);

        for (int i=0;i<dataList.size();i++){
            HashMap<String,String> data = dataList.get(i);
            if (promo_code.equals(data.get("promo_code"))){
                textViewId.setText(textViewId.getText().toString()+data.get("id"));
                textViewExpiredDate.setText(textViewExpiredDate.getText().toString()+data.get("expired_date"));
                textViewCondition.setText(textViewCondition.getText().toString()+data.get("condition"));
            }
        }
    }
}