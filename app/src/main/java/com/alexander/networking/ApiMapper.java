package com.alexander.networking;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class ApiMapper {

    private RetrofitHelper helper;

    public ApiMapper(RetrofitHelper helper) {
        this.helper = helper;
    }

    public List<Weather> getWeather() throws IOException {
        Response<Forecast> response = helper.getService().getForecast().execute();
        return response.body().getHourly().getData();
    }
}
