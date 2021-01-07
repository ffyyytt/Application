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

public class PromotionCodeChoices extends AppCompatActivity {
    double promotionCode;
    String[] listItem;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_code_choices);

        setTitle(R.string.title_promotion_code);
        listItem = getResources().getStringArray(R.array.list_view);
        listView = findViewById(R.id.listView);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listItem);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // TODO Auto-generated method stub
                String value=adapter.getItem(position);
                Log.d("TAG", "onItemClick: code string promotion: "+value);

                promotionCode = Double.parseDouble(value);

                Intent intent = new Intent();
                intent.putExtra("promotionCode", promotionCode);

                setResult(Activity.RESULT_OK, intent);
                finish();

            }
        });


    }
}