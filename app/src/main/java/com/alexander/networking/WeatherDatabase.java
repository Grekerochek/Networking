package com.alexander.networking;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = Weather.class, version = 1)
public abstract class WeatherDatabase extends RoomDatabase {

    public abstract WeatherDAO getWeatherDAO();
}