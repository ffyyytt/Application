package com.group02.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list;
    private Activity activity;

    public CustomAdapter(ArrayList<String> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_layout, null);
        }

        //Handle TextView and display string from your list
        TextView promotionCodeString= (TextView)view.findViewById(R.id.promotionCodeString);
        promotionCodeString.setText(list.get(position));

        promotionCodeString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, DetailPromotionCode.class);
                intent.putExtra("promo_code", promotionCodeString.getText().toString());
                activity.startActivity(intent);
            }
        });

        //Handle buttons and add onClickListeners
        Button btnChoose= (Button)view.findViewById(R.id.btnChoose);

        btnChoose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("promotionCode", promotionCodeString.getText().toString());

                activity.setResult(Activity.RESULT_OK, intent);
                activity.finish();

            }
        });

        return view;
    }
}
