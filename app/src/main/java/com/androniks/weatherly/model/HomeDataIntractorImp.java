package com.androniks.weatherly.model;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androniks.weatherly.contract.HomeContract;
import com.androniks.weatherly.util.AndroidHelper;
import com.androniks.weatherly.util.AppUrls;
import com.androniks.weatherly.util.MySingleton;

import static com.androniks.weatherly.App.APP_CONTEXT;

public class HomeDataIntractorImp implements HomeContract.DataIntractor {

    @Override
    public void getTmaxData(final String city, final String type, final OnFinishedListener onFinishedListener) {
        String url = AppUrls.getAPI(type, city);
        Log.e("url", url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                onFinishedListener.onFinished(city, type, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onFinishedListener.onFailure("getTmaxData error");

            }
        });

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(APP_CONTEXT).addToRequestQueue(stringRequest);
    }



    @Override
    public void getTminData(final String city, final String type, final OnFinishedListener onFinishedListener) {
        String url = AppUrls.getAPI(type, city);
        Log.e("url", url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                onFinishedListener.onFinished(city, type, response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onFinishedListener.onFailure("getTminData error");

            }
        });

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(APP_CONTEXT).addToRequestQueue(stringRequest);
    }



    @Override
    public void getRainData(final String city, final String type, final OnFinishedListener onFinishedListener) {
        String url = AppUrls.getAPI(type, city);
        Log.e("url", url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                onFinishedListener.onFinished(city, type, response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onFinishedListener.onFailure("getRainData error");

            }
        });

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(APP_CONTEXT).addToRequestQueue(stringRequest);
    }
}
