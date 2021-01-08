package com.group02.application;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RatingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        EditText editTextComment = (EditText)findViewById(R.id.rating_comment);
        RatingBar ratingBar = (RatingBar)findViewById(R.id.rating_bar);
        Button btNotRate = (Button)findViewById(R.id.rating_bt_not_rate);
        Button btRate = (Button)findViewById(R.id.rating_bt_rate);

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
                    /*addRatedDialog();*/

                    //Start of merge backend
                    mergeBackend();
                    //End of merge backend
                }
            }
        });
    }

    private void mergeBackend() {
        String server = SERVER.get_server() + "api/passenger/rate_driver/";

        Intent intent = getIntent();
        String tripID = intent.getStringExtra("trip_id");

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                server,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RATINGDEBUG", "Success!");
                        addRatedDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("SIGNINDEBUG", error.getMessage());
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