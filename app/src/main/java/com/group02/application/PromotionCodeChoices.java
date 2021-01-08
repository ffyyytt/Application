package com.group02.application;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class PromotionCodeChoices extends AppCompatActivity {
    String TAG = "PromotionCodeChoices";
    double promotionCode;
    ArrayList<String> listItem;
    ListView listView;

    public static List<HashMap<String,String>> dataList;

    String phone_no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_code_choices);

        setTitle(R.string.title_promotion_code);
        listItem = getListItemfromBackEnd();//new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.list_view)));//
        listView = findViewById(R.id.listView);

        phone_no = getIntent().getStringExtra("phone_no");

        CustomAdapter adapter = new CustomAdapter(listItem, this);
        listView.setAdapter(adapter);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                // TODO Auto-generated method stub
//                String value=adapter.getItem(position);
//                Log.d("TAG", "onItemClick: code string promotion: "+value);
//
//                promotionCode = Double.parseDouble(value);
//
//                Intent intent = new Intent();
//                intent.putExtra("promotionCode", promotionCode);
//
//                setResult(Activity.RESULT_OK, intent);
//                finish();
//
//            }
//        });
    }

    private ArrayList<String> getListItemfromBackEnd(){
        ArrayList<String> res = new ArrayList<>();
        String url = SERVER.get_server() + "api/passenger/get_promotion/";

        RequestQueue queue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "onResponse: " + response.toString());

                        dataList = parseJsonArray(response);
                        for (int i = 0; i < dataList.size(); i++){
                            HashMap<String, String> data = dataList.get(i);
                            if (phone_no.equals(data.get("phone_no"))){
                                res.add(data.get("promo_code"));
                            }
                        }
                        Log.d(TAG, "onResponse: "+res.toString());
                        Log.d(TAG, "onResponse: "+url);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: " + error.getMessage());
                        Log.d(TAG, "onErrorResponse: "+url);
                    }
                }
        );
        SingletonRequestQueue.getInstance().addToRequestQueue(jsonArrayRequest);
        return res;
    }

    private List<HashMap<String,String>> parseJsonArray(JSONArray jsonArray) {
        List<HashMap<String, String>> dataList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                HashMap<String, String> data = parseJsonObject((JSONObject) jsonArray.get(i));
                dataList.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return dataList;
    }

    private HashMap<String,String> parseJsonObject(JSONObject object){
        HashMap<String,String> dataList = new HashMap<>();
        try {
            Log.d(TAG, "parseJsonObject: promotionCode");

            String id = object.getString("id");
            String promo_code = object.getString("promo_code");
            String expired_date = object.getString("expired_date");
            String condition = object.getString("condition");
            String passenger_id = object.getString("passenger_id");
            dataList.put("id",id);
            dataList.put("promo_code",promo_code);
            dataList.put("expired_date",expired_date);
            dataList.put("condition",condition);
            dataList.put("passenger_id",passenger_id);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return dataList;
    }
}