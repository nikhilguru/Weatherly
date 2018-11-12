package com.androniks.weatherly.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androniks.weatherly.R;
import com.androniks.weatherly.adapter.DataAdapter;
import com.androniks.weatherly.contract.HomeContract;
import com.androniks.weatherly.entity.WeatherData;
import com.androniks.weatherly.model.HomeDataIntractorImp;
import com.androniks.weatherly.presenter.HomePresenterImp;
import com.androniks.weatherly.util.AndroidHelper;
import com.androniks.weatherly.util.Types;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, HomeContract.MainView {

    private TextView mTxtCity;
    private Context mContext;


    private DataAdapter adapter;
    private RecyclerView mRvData;
    private ProgressBar progressBar;
    private HomeContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext = this;
        initIds();
        presenter = new HomePresenterImp(this, new HomeDataIntractorImp());
        presenter.setCity(city);
        presenter.setType(Types.TMAX);
        presenter.onGetTmaxData();


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
                presenter.setCity(mTxtCity.getText().toString());
                presenter.setType(Types.TMAX);
                presenter.onGetTmaxData();
                return false;
            }
        });
        popup.inflate(R.menu.menu_home);
        popup.show();
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
        presenter.setCity(city);
        presenter.setType(Types.TMAX);
        presenter.onGetTmaxData();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onResponseSuccess(String city, String type, String response) {
        AndroidHelper.addSharedPreference(mContext, type + "-" + city, response);

    }

    @Override
    public void onResponseFailure(String error) {
        showToast(error);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setAdapter(ArrayList<WeatherData> weatherDataArrayList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvData.setLayoutManager(linearLayoutManager);

        adapter = new DataAdapter(weatherDataArrayList, mContext);
        mRvData.setAdapter(adapter);
    }

    @Override
    public Context getmContext() {
        return mContext;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
