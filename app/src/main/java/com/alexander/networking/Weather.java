package com.alexander.networking;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "forecast")
public class Weather {

    @PrimaryKey
    private long time;
    private String summary;

    @SerializedName("temperature")
    private float temp;
    private float pressure;
    private float humidity;
    private float windSpeed;
    private float visibility;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public float getVisibility() {
        return visibility;
    }

    public void setVisibility(float visibility) {
        this.visibility = visibility;
    }

    @Override
    public String toString() {
        return  summary + "\nTемпература воздуха = " + temp + "C\n" +
                "Давление = " + pressure + " ммРтСт\n" +
                "Влажность = " + humidity + " %\n" +
                "Скорость ветра = " + windSpeed + " м/c\n" +
                "Видимость = " + visibility + " км";
    }
}
