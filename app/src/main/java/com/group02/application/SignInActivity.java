package com.group02.application;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    String server = new SERVER().get_server() + "api/passenger/login/";
    String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btn_signup = findViewById(R.id.signin_btn_signin);
        btn_cancel = findViewById(R.id.signin_btn_cancel);

        edt_phone = findViewById(R.id.signin_edt_phone);
        edt_password = findViewById(R.id.signin_edt_password);

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
                str_password = getMD5(edt_password.getText().toString()+getResources().getString(R.string.SALT));

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        server,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("SIGNINDEBUG", "onResponse: " + response);
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.SignInCompleted), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), SignInCompletedActivity.class);
                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("SIGNINDEBUG", error.getMessage());
                        edt_password.setText("");
                        edt_password.setHint(getResources().getString(R.string.password_not_match));
                        edt_password.setBackgroundColor(Color.RED);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams()
                            throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("phone_no", str_phone);
                        params.put("password", str_password);
                        return params;
                    }
                };
                queue.add(stringRequest);
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
