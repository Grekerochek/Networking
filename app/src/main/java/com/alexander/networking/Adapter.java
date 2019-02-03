package com.alexander.networking;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>{

    private List<Weather> forecasts;
    private Context context;


    public Adapter(Context context, List<Weather> forecasts){
        this.context = context;
        this.forecasts = forecasts;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items, parent, false);
        return new MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault());
        final String date = dateFormat.format(new Date(forecasts.get(position).getTime()*1000L));
        holder.time.setText(date);
        holder.time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                context.startActivity(DetailsActivity.newIntent(context, date, forecasts.get(position).getTime()));
            }
        });
    }
    public void setData(List<Weather> forecasts){
        this.forecasts = forecasts;
        notifyDataSetChanged();
    }


public static class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView time;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        time = itemView.findViewById(R.id.time);
    }
}

    @Override
    public int getItemCount() {
        return forecasts.size();
    }

}
