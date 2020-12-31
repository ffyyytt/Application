package com.group02.application;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class RatingActivity extends AppCompatActivity {

    EditText editTextComment;
    RatingBar ratingBar;
    Button btNotRate;
    Button btRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        editTextComment = (EditText)findViewById(R.id.rating_comment);
        ratingBar = (RatingBar)findViewById(R.id.rating_bar);
        btNotRate = (Button)findViewById(R.id.rating_bt_not_rate);
        btRate = (Button)findViewById(R.id.rating_bt_rate);

        btNotRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double stars = Double.parseDouble(String.valueOf(ratingBar.getRating()));
                String comment = editTextComment.getText().toString();
                Log.d("RatingDebug", "Stars = " + stars);
                Log.d("RatingDebug", "Comment = " + comment);
                if (stars == 0.0)
                    addNotRateYetDialog();
                else {
                    addRatedDialog();
                }
            }
        });
    }

    private void addRatedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.rated_title));
        builder.setMessage(getResources().getString(R.string.rated_message));
        builder.setCancelable(false);
        builder.setPositiveButton(
                getResources().getString(R.string.accept),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void addNotRateYetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.not_rate_yet_title));
        builder.setMessage(getResources().getString(R.string.not_rate_yet_message));
        builder.setCancelable(true);
        builder.setPositiveButton(
                getResources().getString(R.string.accept),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}