package com.androniks.weatherly.util;

public class AppUrls {

    public static final String BASE_URL = "https://s3.eu-west-2.amazonaws.com/interview-question-data/metoffice/";


    public static String getAPI(String type, String city){
        return BASE_URL.concat(type + "-" + city + ".json");
    }
}
