package com.group02.application;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class SingletonRequestQueue {
    private static SingletonRequestQueue instance;
    private RequestQueue requestQueue;
    private static Context mContext;

    private SingletonRequestQueue(Context context){
        mContext = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized SingletonRequestQueue getInstance(Context context){
        if (instance==null){
            Log.d("NewSingletonRQ", "New!");
            instance = new SingletonRequestQueue(context);
        }
        else {
            Log.d("NewSingletonRQ", "Old!");
        }
        return instance;
    }

    public RequestQueue getRequestQueue(){
        if (requestQueue==null){
            requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return requestQueue;
    }

    public<T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }
}
