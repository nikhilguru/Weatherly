package com.androniks.weatherly.adapter;

/**
 * Created by AndroNiks on 10/03/18.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androniks.weatherly.R;
import com.androniks.weatherly.entity.WeatherData;
import com.androniks.weatherly.util.AndroidHelper;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

    private ArrayList<WeatherData> weatherDataArrayList;
    private Context mContext;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtYear, txtMon, txtMaxT, txtMinT, txtRain;

        public MyViewHolder(View view) {
            super(view);
            txtYear = view.findViewById(R.id.txtYear);
            txtMon = view.findViewById(R.id.txtMon);
            txtMaxT = view.findViewById(R.id.txtMaxT);
            txtMinT = view.findViewById(R.id.txtMinT);
            txtRain = view.findViewById(R.id.txtRain);

        }
    }


    public DataAdapter(ArrayList<WeatherData> weatherDataArrayList, Context mContext) {
        this.weatherDataArrayList = weatherDataArrayList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.data_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final WeatherData weatherData = weatherDataArrayList.get(holder.getAdapterPosition());

        holder.txtYear.setText(weatherData.getYear());
        holder.txtMon.setText(AndroidHelper.getMonth(weatherData.getMonth()));
        holder.txtMaxT.setText(TextUtils.isEmpty(weatherData.getTmax()) ? "N/A": weatherData.getTmax());
        holder.txtMinT.setText(TextUtils.isEmpty(weatherData.getTmin())? "N/A": weatherData.getTmin());
        holder.txtRain.setText(TextUtils.isEmpty(weatherData.getRain())? "N/A": weatherData.getRain());


    }

    @Override
    public int getItemCount() {
        return weatherDataArrayList.size();
    }


}
