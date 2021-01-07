package com.group02.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private Button btn_signup;
    private Button btn_cancel;
    private EditText edt_phone, edt_password, edt_password_con, edt_name, edt_email;
    RadioGroup radioGroup;
    String str_phone="", str_password="", str_password_con="", str_name="", str_email="", str_gender="Nam";

    String server ="http://192.168.100.7:8000/api/passenger/register/";
    String result="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btn_signup = (Button) findViewById(R.id.signup_btn_signup);
        btn_cancel = (Button) findViewById(R.id.signup_btn_cancel);

        edt_phone = (EditText) findViewById(R.id.signup_edt_phone);
        edt_password = (EditText) findViewById(R.id.signup_edt_password);
        edt_password_con = (EditText) findViewById(R.id.signup_edt_password_confirm);
        edt_name = (EditText) findViewById(R.id.signup_edt_name);
        edt_email = (EditText) findViewById(R.id.signup_edt_email);

        radioGroup = (RadioGroup)findViewById(R.id.signup_radio);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                str_gender = radioButton.getText().toString();
            }
        });

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
                str_password_con = getMD5(edt_password_con.getText().toString()+getResources().getString(R.string.SALT));
                str_name = edt_name.getText().toString();
                str_email = edt_email.getText().toString();

                if (!str_password.equals(str_password_con))
                {
                    edt_password_con.setText("");
                    edt_password_con.setHint(getResources().getString(R.string.password_not_match));
                    edt_password_con.setBackgroundColor(Color.RED);
                }
                else
                {
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            server,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.SignUpCompleted),Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), SignUpCompletedActivity.class);
                                    startActivity(intent);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),"FAILED",Toast.LENGTH_SHORT).show();
                        }

                    }) {
                        @Override
                        protected Map<String, String> getParams()
                                throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("name", str_name);
                            params.put("phone_no", str_phone);
                            params.put("email", str_email);
                            params.put("password", str_password);
                            params.put("gender", str_gender);
                            params.put("point", "0");
                            return params;
                        }
                    };
                    queue.add(stringRequest);
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

    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            return convertByteToHex(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String convertByteToHex(byte[] data) {
        BigInteger number = new BigInteger(1, data);
        String hashtext = number.toString(16);
        // Now we need to zero pad it if you actually want the full 32 chars.
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }
}
