package com.group02.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static com.group02.application.SignUpActivity.getMD5;

public class SignInActivity extends AppCompatActivity {

    private Button btn_signup;
    private Button btn_cancel;
    private EditText edt_phone, edt_password;
    String str_phone, str_password;

    String server ="http://192.168.5.10:5555";
    String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btn_signup = (Button) findViewById(R.id.signin_btn_signin);
        btn_cancel = (Button) findViewById(R.id.signin_btn_cancel);

        edt_phone = (EditText) findViewById(R.id.signin_edt_phone);
        edt_password = (EditText) findViewById(R.id.signin_edt_password);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                byte[] hashedData= md.digest();

                str_phone = edt_phone.getText().toString();
                str_password = getMD5(edt_password.getText().toString()+"ffyytt");

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        server,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                result = response.toString();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams()
                            throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("phone", str_phone);
                        params.put("password", str_password);
                        return params;
                    }
                };
                queue.add(stringRequest);
                new CountDownTimer(1500, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                    public void onFinish() {
                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                        if  (result.equals("False"))
                        {
                            edt_password.setText("");
                            edt_password.setHint(getResources().getString(R.string.password_not_match));
                            edt_password.setBackgroundColor(Color.RED);
                        }
                        else
                        {
                            Intent intent = new Intent(getApplicationContext(), SignInCompletedActivity.class);
                            startActivity(intent);
                        }
                    }
                }.start();

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
