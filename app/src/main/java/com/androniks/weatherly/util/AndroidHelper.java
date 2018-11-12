package com.androniks.weatherly.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.androniks.weatherly.App;


public class AndroidHelper {

    /**
     * This function stores the data in the shared preferences based on the given Key and Value
     *
     * @param context
     * @param key
     * @param value
     * @return string which is a key
     */
    public static String addSharedPreference(Context context, String key, String value) {
        SharedPreferences sharedPref = context
                .getSharedPreferences("Location", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
        return key;
    }

    /**
     * This function fetch the value from the shared preferences based on the key
     *
     * @param context
     * @param key
     * @return string value for the key
     */
    public static String getSharedPreferenceString(Context context, String key) {
        SharedPreferences sharedPref = null;
        if (context != null) {
            sharedPref = context.getSharedPreferences("Location", Context.MODE_PRIVATE);
        }
        return sharedPref.getString(key, "");
    }


    /**
     * Function to check network is available or not
     *
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getMonth(String monNum) {
        if (TextUtils.isEmpty(monNum)){
            return "N/A";
        }
        switch (Integer.parseInt(monNum)){
            case 1: return "JAN";
            case 2: return "FEB";
            case 3: return "MAR";
            case 4: return "APR";
            case 5: return "MAY";
            case 6: return "JUN";
            case 7: return "JUL";
            case 8: return "AUG";
            case 9: return "SEP";
            case 10: return "OCT";
            case 11: return "NOV";
            case 12: return "DEC";
            default: return "N/A";
        }
    }




}
