package com.androniks.weatherly.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.androniks.weatherly.R;
import com.androniks.weatherly.adapter.DataAdapter;
import com.androniks.weatherly.entity.WeatherData;
import com.androniks.weatherly.util.AndroidHelper;
import com.androniks.weatherly.util.AppUrls;
import com.androniks.weatherly.util.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTxtCity;
    private Context mContext;
    private final String TMAX = "Tmax";
    private final String TMIN = "Tmin";
    private final String RAIN = "Rainfall";

    private ArrayList<WeatherData> weatherDataArrayList = new ArrayList<>();
    private DataAdapter adapter;
    private RecyclerView mRvData;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext = this;
        initIds();

        getDataList();
        callAPI();

    }

    private void callAPI() {
        weatherDataArrayList.clear();
        if (AndroidHelper.isNetworkAvailable(mContext)){
            progressBar.setVisibility(View.VISIBLE);
            callTmaxAPI();
        }else {
            progressBar.setVisibility(View.GONE);
            getDataList();
            Toast.makeText(getApplicationContext(), "Connect to Internet", Toast.LENGTH_SHORT).show();
        }
    }


    private void initIds() {
        mTxtCity = findViewById(R.id.txtCity);
        mRvData = findViewById(R.id.rvData);
        progressBar = findViewById(R.id.progressBar);

        //Default city
        mTxtCity.setText(getString(R.string.uk));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtChange:
                showMenu(view);
                break;

            case R.id.imgInfo:
                showAbout();
                break;

        }
    }

    public void showAbout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("About");
        builder.setMessage("Weatherly v1.0\nDesigned and Developed by:\nNikhil Shete\nshete.nikhil@gmail.com\n+918888705573");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mTxtCity.setText(item.getTitle());
                callAPI();
                return false;
            }
        });
        popup.inflate(R.menu.menu_home);
        popup.show();
    }


    private void callTmaxAPI() {

        String url = AppUrls.getAPI(TMAX, mTxtCity.getText().toString());
        Log.e("url", url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                AndroidHelper.addSharedPreference(mContext, TMAX + "-" + mTxtCity.getText().toString(), response);
                callTminAPI();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);

            }
        });

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
    }


    private void callTminAPI() {

        String url = AppUrls.getAPI(TMIN, mTxtCity.getText().toString());
        Log.e("url", url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                AndroidHelper.addSharedPreference(mContext, TMIN + "-" + mTxtCity.getText().toString(), response);
                callRainAPI();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);

            }
        });

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
    }


    private void callRainAPI() {

        String url = AppUrls.getAPI(RAIN, mTxtCity.getText().toString());
        Log.e("url", url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                AndroidHelper.addSharedPreference(mContext, RAIN + "-" + mTxtCity.getText().toString(), response);
                getDataList();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);

            }
        });

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
    }


    private void getDataList() {
        try {
            JSONArray tmaxArray = new JSONArray(AndroidHelper.getSharedPreferenceString(mContext, TMAX + "-" + mTxtCity.getText().toString()));

            for (int i = 0; i < tmaxArray.length(); i++) {
                JSONObject jsonObject = tmaxArray.getJSONObject(i);
                WeatherData weatherData = new WeatherData();
                weatherData.setYear(jsonObject.getString("year"));
                weatherData.setMonth(jsonObject.getString("month"));
                weatherData.setTmax(jsonObject.getString("value"));
                weatherDataArrayList.add(weatherData);
            }


            JSONArray tminArray = new JSONArray(AndroidHelper.getSharedPreferenceString(mContext, TMIN + "-" + mTxtCity.getText().toString()));

            for (int i = 0; i < tminArray.length(); i++) {
                JSONObject jsonObject = tminArray.getJSONObject(i);
                WeatherData weatherData = new WeatherData();
                weatherData.setYear(jsonObject.getString("year"));
                weatherData.setMonth(jsonObject.getString("month"));
                weatherData.setTmin(jsonObject.getString("value"));

                boolean isUpdate = false;
                for (int j = 0; j < weatherDataArrayList.size(); j++) {
                    if (weatherDataArrayList.get(i).getYear().equalsIgnoreCase( weatherData.getYear())
                            && weatherDataArrayList.get(i).getMonth().equalsIgnoreCase( weatherData.getMonth())) {
                        weatherData.setTmax(weatherDataArrayList.get(i).getTmax());
                        weatherDataArrayList.set(i, weatherData);
                        isUpdate = true;
                        break;
                    }
                }

                if (!isUpdate) {
                    weatherDataArrayList.add(weatherData);
                }
            }

            JSONArray rainArray = new JSONArray(AndroidHelper.getSharedPreferenceString(mContext, RAIN + "-" + mTxtCity.getText().toString()));

            for (int i = 0; i < rainArray.length(); i++) {
                JSONObject jsonObject = rainArray.getJSONObject(i);
                WeatherData weatherData = new WeatherData();
                weatherData.setYear(jsonObject.getString("year"));
                weatherData.setMonth(jsonObject.getString("month"));
                weatherData.setRain(jsonObject.getString("value"));
                boolean isUpdate = false;
                for (int j = 0; j < weatherDataArrayList.size(); j++) {
                    if (weatherDataArrayList.get(i).getYear().equalsIgnoreCase( weatherData.getYear())
                            && weatherDataArrayList.get(i).getMonth().equalsIgnoreCase( weatherData.getMonth())) {
                        weatherData.setTmax(weatherDataArrayList.get(i).getTmax());
                        weatherData.setTmin(weatherDataArrayList.get(i).getTmin());
                        weatherDataArrayList.set(i, weatherData);
                        isUpdate = true;
                        break;
                    }
                }

                if (!isUpdate) {
                    weatherDataArrayList.add(weatherData);
                }
            }

            setAdapter();
            progressBar.setVisibility(View.GONE);


        } catch (JSONException e) {
            e.printStackTrace();
            progressBar.setVisibility(View.GONE);

        }
    }




    private void setAdapter(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvData.setLayoutManager(linearLayoutManager);

        adapter = new DataAdapter(weatherDataArrayList, mContext);
        mRvData.setAdapter(adapter);

    }


    private static final String SELECTED_CITY = "selected_city";
    private String city = "UK";

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(SELECTED_CITY, mTxtCity.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        city = savedInstanceState.getString(SELECTED_CITY);
        mTxtCity.setText(city);
        getDataList();
    }
}
