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

public class SignInActivity extends AppCompatActivity {

    private Button btn_signup;
    private Button btn_cancel;
    private EditText edt_phone, edt_password;
    String str_phone, str_password;

    String server = SERVER.get_server() + "api/passenger/login/";

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

                if (edt_phone.getText().toString().equals("") || edt_password.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.not_null),Toast.LENGTH_SHORT).show();
                }
                else if (edt_phone.getText().toString().length() != 11 || !edt_phone.getText().toString().replaceAll("0", "").replaceAll("1","").replaceAll("2","").replaceAll("3","").replaceAll("4","").replaceAll("5","").replaceAll("6","").replaceAll("7","").replaceAll("8","").replaceAll("9","").equals(""))
                {
                    edt_phone.setBackgroundColor(Color.RED);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.phone_wrong),Toast.LENGTH_SHORT).show();
                }
                else
                {

                    str_phone = edt_phone.getText().toString();
                    str_password = edt_password.getText().toString(); //getMD5(edt_password.getText().toString() + getResources().getString(R.string.SALT));

                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            server,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("SIGNINDEBUG", "onResponse: " + response);
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.SignInCompleted), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), SignInCompletedActivity.class);
                                    intent.putExtra("phone_no", str_phone);
                                    startActivity(intent);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
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
                    SingletonRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                }
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
