package com.alexander.networking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WebService {

    @GET("/forecast/9f14af241f941fc2a4e672ac29970d3b/55.742793,37.615401?exclude=currently,minutely,daily,alerts,flags&lang=ru&units=si")
    Call<Forecast> getForecast();
}
