package com.androniks.weatherly.contract;

import android.content.Context;

import com.androniks.weatherly.entity.WeatherData;

import java.util.ArrayList;

public interface HomeContract {

    /**
     * Call when user interact with the view and other when view OnDestroy()
     * */
    interface Presenter {

        void onDestroy();

        void onCityChanged(String city);

        void onTypeChanged(String type);

        void onGetTmaxData();

        void onGetTminData();

        void onGetRainData();



    }

        /**
         * showProgress() and hideProgress() would be used for displaying and hiding the progressBar
         * while the setDataToRecyclerView and onResponseFailure is fetched from the HomeDataIntractorImp class
         **/
        interface MainView {

            void showProgress();

            void hideProgress();

            void onResponseSuccess(String city, String type, String response);

            void onResponseFailure( String error);

            void showToast(String msg);

            void setAdapter(ArrayList<WeatherData> weatherDataArrayList);

            Context getmContext();
        }



        /**
         * Intractors are classes built for fetching data from your database, web services, or any other data source.
         **/
        interface DataIntractor {

            interface OnFinishedListener {

                void onFinished(String city, String type, String response);

                void onFailure(String error);

            }

            void getTmaxData(String city, String type, OnFinishedListener onFinishedListener);

            void getTminData(String city, String type, OnFinishedListener onFinishedListener);

            void getRainData(String city, String type, OnFinishedListener onFinishedListener);


        }


}
