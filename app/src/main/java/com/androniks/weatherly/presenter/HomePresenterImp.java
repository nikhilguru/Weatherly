package com.androniks.weatherly.presenter;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.androniks.weatherly.App;
import com.androniks.weatherly.contract.HomeContract;
import com.androniks.weatherly.entity.WeatherData;
import com.androniks.weatherly.util.AndroidHelper;
import com.androniks.weatherly.util.Types;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomePresenterImp implements HomeContract.Presenter, HomeContract.DataIntractor.OnFinishedListener {

    private HomeContract.MainView mainView;
    private HomeContract.DataIntractor dataIntractor;
    private String city = "UK";
    private String type = "Tmax";
    private ArrayList<WeatherData> weatherDataArrayList = new ArrayList<>();


    public HomePresenterImp(HomeContract.MainView mainView, HomeContract.DataIntractor dataIntractor) {
        this.mainView = mainView;
        this.dataIntractor = dataIntractor;
    }

    @Override
    public void onDestroy() {
        mainView = null;
    }

    @Override
    public void onCityChanged(String city) {
        this.city = city;
    }

    @Override
    public void onTypeChanged(String type) {
        this.type = type;
    }

    @Override
    public void onGetTmaxData() {
        onTypeChanged(Types.TMAX);

        if (mainView == null || dataIntractor == null ||
                TextUtils.isEmpty(city) || TextUtils.isEmpty(type)) {
            return;
        }

        if (AndroidHelper.isNetworkAvailable(mainView.getmContext())){
            mainView.showProgress();
            dataIntractor.getTmaxData(city, type, this);
        }else {
            getDataList();
            mainView.showToast( "Connect to Internet");
        }
    }

    @Override
    public void onGetTminData() {
        onTypeChanged(Types.TMIN);

        if (mainView == null || dataIntractor == null ||
                TextUtils.isEmpty(city) || TextUtils.isEmpty(type)) {
            return;
        }

        if (AndroidHelper.isNetworkAvailable(mainView.getmContext())){
            mainView.showProgress();
            dataIntractor.getTminData(city, type, this);
        }else {
            mainView.hideProgress();
            mainView.showToast( "Connect to Internet");
        }

    }

    @Override
    public void onGetRainData() {
        onTypeChanged(Types.RAIN);

        if (mainView == null || dataIntractor == null ||
                TextUtils.isEmpty(city) || TextUtils.isEmpty(type)) {
            return;
        }

        if (AndroidHelper.isNetworkAvailable(mainView.getmContext())){
            mainView.showProgress();
            dataIntractor.getRainData(city, type, this);
        }else {
            mainView.hideProgress();
            mainView.showToast( "Connect to Internet");
        }

    }

    @Override
    public void onFinished(String city, String type, String response) {
        if (mainView != null) {
            mainView.onResponseSuccess(city, type, response);

            switch (type){

                case Types.TMAX:
                    onGetTminData();
                    break;

                case Types.TMIN:
                    onGetRainData();
                    break;

                case Types.RAIN:
                    mainView.hideProgress();
                    getDataList();
                    Log.d("e", "last");
                    break;
            }

        }
    }

    @Override
    public void onFailure(String error) {
        if (mainView != null) {
            mainView.hideProgress();
            mainView.onResponseFailure( error);
        }
    }



    private void getDataList() {
        if (mainView != null) {
            weatherDataArrayList.clear();

            try {
                String max = AndroidHelper.getSharedPreferenceString(App.APP_CONTEXT, Types.TMAX + "-" + city);
                if (!TextUtils.isEmpty(max)) {
                    JSONArray tmaxArray = new JSONArray(max);

                    for (int i = 0; i < tmaxArray.length(); i++) {
                        JSONObject jsonObject = tmaxArray.getJSONObject(i);
                        WeatherData weatherData = new WeatherData();
                        weatherData.setYear(jsonObject.getString("year"));
                        weatherData.setMonth(jsonObject.getString("month"));
                        weatherData.setTmax(jsonObject.getString("value"));
                        weatherDataArrayList.add(weatherData);
                    }

                }

                String min = AndroidHelper.getSharedPreferenceString(App.APP_CONTEXT, Types.TMIN + "-" + city);
                if (!TextUtils.isEmpty(min)) {
                    JSONArray tminArray = new JSONArray(min);

                    for (int i = 0; i < tminArray.length(); i++) {
                        JSONObject jsonObject = tminArray.getJSONObject(i);
                        WeatherData weatherData = new WeatherData();
                        weatherData.setYear(jsonObject.getString("year"));
                        weatherData.setMonth(jsonObject.getString("month"));
                        weatherData.setTmin(jsonObject.getString("value"));

                        boolean isUpdate = false;
                        for (int j = 0; j < weatherDataArrayList.size(); j++) {
                            if (weatherDataArrayList.get(i).getYear().equalsIgnoreCase(weatherData.getYear())
                                    && weatherDataArrayList.get(i).getMonth().equalsIgnoreCase(weatherData.getMonth())) {
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
                }

                String rain = AndroidHelper.getSharedPreferenceString(App.APP_CONTEXT, Types.RAIN + "-" + city);
                if (!TextUtils.isEmpty(rain)) {

                    JSONArray rainArray = new JSONArray(rain);

                    for (int i = 0; i < rainArray.length(); i++) {
                        JSONObject jsonObject = rainArray.getJSONObject(i);
                        WeatherData weatherData = new WeatherData();
                        weatherData.setYear(jsonObject.getString("year"));
                        weatherData.setMonth(jsonObject.getString("month"));
                        weatherData.setRain(jsonObject.getString("value"));
                        boolean isUpdate = false;
                        for (int j = 0; j < weatherDataArrayList.size(); j++) {
                            if (weatherDataArrayList.get(i).getYear().equalsIgnoreCase(weatherData.getYear())
                                    && weatherDataArrayList.get(i).getMonth().equalsIgnoreCase(weatherData.getMonth())) {
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
                }

                mainView.setAdapter(weatherDataArrayList);
                mainView.hideProgress();


            } catch (JSONException e) {
                e.printStackTrace();
                mainView.hideProgress();

            }
        }
    }



}
