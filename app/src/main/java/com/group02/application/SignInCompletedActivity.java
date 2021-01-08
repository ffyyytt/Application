package com.group02.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SignInCompletedActivity extends AppCompatActivity {

    Button button;
    String phone_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_completed);

        phone_no = getIntent().getStringExtra("phone_no");


        button = (Button) findViewById(R.id.signin_completed_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChooseLocationActivity.class);
                intent.putExtra("phone_no",phone_no);
                startActivity(intent);
            }
        });
    }
}